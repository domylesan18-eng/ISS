package ro.ubb.autoservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ubb.autoservice.model.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * Repository pt operatii CRUD cu Vehicle
 */
public class VehicleRepository {
    
    private final EntityManager entityManager;
    
    public VehicleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Salveaza sau actualizeaza un vehicul
     */
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            entityManager.persist(vehicle);
            return vehicle;
        } else {
            return entityManager.merge(vehicle);
        }
    }
    
    /**
     * Gaseste vehicul dupa ID
     */
    public Optional<Vehicle> findById(Long id) {
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        return Optional.ofNullable(vehicle);
    }
    
    /**
     * Gaseste toate vehiculele
     */
    public List<Vehicle> findAll() {
        TypedQuery<Vehicle> query = 
            entityManager.createQuery("SELECT v FROM Vehicle v ORDER BY v.marca, v.model", Vehicle.class);
        return query.getResultList();
    }
    
    /**
     * Gaseste vehiculele unui client
     */
    public List<Vehicle> findByClientId(Long clientId) {
        TypedQuery<Vehicle> query = entityManager.createQuery(
            "SELECT v FROM Vehicle v WHERE v.client.id = :clientId ORDER BY v.marca", Vehicle.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
    
    /**
     * Gaseste vehicul dupa VIN
     */
    public Optional<Vehicle> findByVin(String vin) {
        TypedQuery<Vehicle> query = entityManager.createQuery(
            "SELECT v FROM Vehicle v WHERE v.vin = :vin", Vehicle.class);
        query.setParameter("vin", vin);
        List<Vehicle> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Gaseste vehicul dupa numar de inmatriculare
     */
    public Optional<Vehicle> findByNrInmatriculare(String nrInmatriculare) {
        TypedQuery<Vehicle> query = entityManager.createQuery(
            "SELECT v FROM Vehicle v WHERE v.nrInmatriculare = :nr", Vehicle.class);
        query.setParameter("nr", nrInmatriculare);
        List<Vehicle> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Cauta vehicule dupa marca sau model
     */
    public List<Vehicle> searchByMarcaModel(String searchTerm) {
        TypedQuery<Vehicle> query = entityManager.createQuery(
            "SELECT v FROM Vehicle v WHERE LOWER(v.marca) LIKE LOWER(:search) " +
            "OR LOWER(v.model) LIKE LOWER(:search) ORDER BY v.marca, v.model", Vehicle.class);
        query.setParameter("search", "%" + searchTerm + "%");
        return query.getResultList();
    }
    
    /**
     * Sterge un vehicul
     */
    public void delete(Vehicle vehicle) {
        if (entityManager.contains(vehicle)) {
            entityManager.remove(vehicle);
        } else {
            entityManager.remove(entityManager.merge(vehicle));
        }
    }
    
    /**
     * Numara toate vehiculele
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(v) FROM Vehicle v", Long.class);
        return query.getSingleResult();
    }
}
