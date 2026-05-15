package ro.ubb.autoservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ro.ubb.autoservice.exception.EntityNotFoundException;
import ro.ubb.autoservice.exception.ValidationException;
import ro.ubb.autoservice.model.Client;
import ro.ubb.autoservice.model.Vehicle;
import ro.ubb.autoservice.repository.ClientRepository;
import ro.ubb.autoservice.repository.VehicleRepository;
import ro.ubb.autoservice.util.JPAUtil;

import java.util.List;

/**
 * Service pt gestionare vehicule
 */
public class VehicleService {
    
    /**
     * Adauga un vehicul nou
     */
    public Vehicle addVehicle(Long clientId, String marca, String model, Integer anFabricatie,
                             String nrInmatriculare, String vin, Integer kilometraj) {
        validateVehicleData(marca, model, anFabricatie, nrInmatriculare, vin, kilometraj);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            ClientRepository clientRepo = new ClientRepository(em);
            VehicleRepository vehicleRepo = new VehicleRepository(em);
            
            // verific ca exista clientul
            Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Client cu id " + clientId + " nu exista!"));
            
            // verific duplicate VIN
            if (vehicleRepo.findByVin(vin).isPresent()) {
                throw new ValidationException("VIN-ul " + vin + " este deja inregistrat!");
            }
            
            // verific duplicate numar inmatriculare
            if (vehicleRepo.findByNrInmatriculare(nrInmatriculare).isPresent()) {
                throw new ValidationException(
                    "Numarul de inmatriculare " + nrInmatriculare + " este deja inregistrat!");
            }
            
            Vehicle vehicle = new Vehicle(marca, model, anFabricatie, 
                                         nrInmatriculare, vin, kilometraj);
            vehicle.setClient(client);
            
            vehicle = vehicleRepo.save(vehicle);
            
            tx.commit();
            System.out.println("Vehicul adaugat cu succes: " + vehicle);
            
            return vehicle;
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualizeaza un vehicul
     */
    public Vehicle updateVehicle(Long id, String marca, String model, Integer anFabricatie,
                                String nrInmatriculare, String vin, Integer kilometraj) {
        validateVehicleData(marca, model, anFabricatie, nrInmatriculare, vin, kilometraj);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            VehicleRepository repo = new VehicleRepository(em);
            
            Vehicle vehicle = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Vehicul cu id " + id + " nu a fost gasit!"));
            
            // verific duplicate VIN (daca s-a modificat)
            if (!vehicle.getVin().equals(vin)) {
                if (repo.findByVin(vin).isPresent()) {
                    throw new ValidationException("VIN-ul " + vin + " este deja folosit!");
                }
            }
            
            // verific duplicate numar inmatriculare (daca s-a modificat)
            if (!vehicle.getNrInmatriculare().equals(nrInmatriculare)) {
                if (repo.findByNrInmatriculare(nrInmatriculare).isPresent()) {
                    throw new ValidationException(
                        "Numarul " + nrInmatriculare + " este deja folosit!");
                }
            }
            
            // actualizez campurile
            vehicle.setMarca(marca);
            vehicle.setModel(model);
            vehicle.setAnFabricatie(anFabricatie);
            vehicle.setNrInmatriculare(nrInmatriculare);
            vehicle.setVin(vin);
            vehicle.setKilometraj(kilometraj);
            
            vehicle = repo.save(vehicle);
            
            tx.commit();
            System.out.println("Vehicul actualizat cu succes: " + vehicle);
            
            return vehicle;
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Sterge un vehicul
     */
    public void deleteVehicle(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            VehicleRepository repo = new VehicleRepository(em);
            
            Vehicle vehicle = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Vehicul cu id " + id + " nu a fost gasit!"));
            
            repo.delete(vehicle);
            
            tx.commit();
            System.out.println("Vehicul sters cu succes: " + vehicle);
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste vehicul dupa ID
     */
    public Vehicle getVehicleById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehicleRepository repo = new VehicleRepository(em);
            return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Vehicul cu id " + id + " nu a fost gasit!"));
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste toate vehiculele
     */
    public List<Vehicle> getAllVehicles() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehicleRepository repo = new VehicleRepository(em);
            return repo.findAll();
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste vehiculele unui client
     */
    public List<Vehicle> getVehiclesByClient(Long clientId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehicleRepository repo = new VehicleRepository(em);
            return repo.findByClientId(clientId);
        } finally {
            em.close();
        }
    }
    
    /**
     * Cauta vehicule dupa marca/model
     */
    public List<Vehicle> searchVehicles(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllVehicles();
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehicleRepository repo = new VehicleRepository(em);
            return repo.searchByMarcaModel(searchTerm.trim());
        } finally {
            em.close();
        }
    }
    
    /**
     * Numara toate vehiculele
     */
    public long countVehicles() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehicleRepository repo = new VehicleRepository(em);
            return repo.count();
        } finally {
            em.close();
        }
    }
    
    /**
     * Valideaza datele vehiculului
     */
    private void validateVehicleData(String marca, String model, Integer anFabricatie,
                                    String nrInmatriculare, String vin, Integer kilometraj) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new ValidationException("Marca este obligatorie!");
        }
        
        if (model == null || model.trim().isEmpty()) {
            throw new ValidationException("Modelul este obligatoriu!");
        }
        
        if (anFabricatie == null) {
            throw new ValidationException("Anul de fabricatie este obligatoriu!");
        }
        
        if (anFabricatie < 1900 || anFabricatie > java.time.Year.now().getValue() + 1) {
            throw new ValidationException("An de fabricatie invalid!");
        }
        
        if (nrInmatriculare == null || nrInmatriculare.trim().isEmpty()) {
            throw new ValidationException("Numarul de inmatriculare este obligatoriu!");
        }
        
        if (vin == null || vin.trim().isEmpty()) {
            throw new ValidationException("VIN-ul este obligatoriu!");
        }
        
        if (vin.length() != 17) {
            throw new ValidationException("VIN-ul trebuie sa contina exact 17 caractere!");
        }
        
        if (kilometraj == null || kilometraj < 0) {
            throw new ValidationException("Kilometrajul trebuie sa fie un numar pozitiv!");
        }
    }
}
