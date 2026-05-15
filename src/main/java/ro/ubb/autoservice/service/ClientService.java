package ro.ubb.autoservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ro.ubb.autoservice.exception.DuplicateClientException;
import ro.ubb.autoservice.exception.EntityNotFoundException;
import ro.ubb.autoservice.exception.ValidationException;
import ro.ubb.autoservice.model.Client;
import ro.ubb.autoservice.model.enums.ClientType;
import ro.ubb.autoservice.repository.ClientRepository;
import ro.ubb.autoservice.util.JPAUtil;

import java.util.List;

/**
 * Service pt gestionare clienti - contine logica de business
 */
public class ClientService {
    
    /**
     * Adauga un client nou
     */
    public Client addClient(String nume, String prenume, String cnpCui, 
                           String telefon, String email, String adresa, ClientType tip) {
        validateClientData(nume, prenume, cnpCui, telefon, email, adresa, tip);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            ClientRepository repo = new ClientRepository(em);
            
            // verific duplicate
            if (repo.existsByCnpCui(cnpCui)) {
                throw new DuplicateClientException(
                    "Client cu CNP/CUI " + cnpCui + " exista deja in sistem!");
            }
            
            Client client = new Client(nume, prenume, cnpCui, telefon, email, adresa, tip);
            client = repo.save(client);
            
            tx.commit();
            System.out.println("Client adaugat cu succes: " + client);
            
            return client;
            
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
     * Actualizeaza un client existent
     */
    public Client updateClient(Long id, String nume, String prenume, String cnpCui, 
                              String telefon, String email, String adresa, ClientType tip) {
        validateClientData(nume, prenume, cnpCui, telefon, email, adresa, tip);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            ClientRepository repo = new ClientRepository(em);
            
            Client client = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Client cu id " + id + " nu a fost gasit!"));
            
            // verific daca CNP-ul modificat nu e luat de alt client
            if (!client.getCnpCui().equals(cnpCui) && repo.existsByCnpCui(cnpCui)) {
                throw new DuplicateClientException(
                    "CNP/CUI " + cnpCui + " este deja folosit de alt client!");
            }
            
            // actualizez campurile
            client.setNume(nume);
            client.setPrenume(prenume);
            client.setCnpCui(cnpCui);
            client.setTelefon(telefon);
            client.setEmail(email);
            client.setAdresa(adresa);
            client.setTip(tip);
            
            client = repo.save(client);
            
            tx.commit();
            System.out.println("Client actualizat cu succes: " + client);
            
            return client;
            
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
     * Sterge un client
     */
    public void deleteClient(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            ClientRepository repo = new ClientRepository(em);
            
            Client client = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Client cu id " + id + " nu a fost gasit!"));
            
            repo.delete(client);
            
            tx.commit();
            System.out.println("Client sters cu succes: " + client);
            
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
     * Gaseste client dupa ID
     */
    public Client getClientById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClientRepository repo = new ClientRepository(em);
            return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Client cu id " + id + " nu a fost gasit!"));
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste toti clientii
     */
    public List<Client> getAllClients() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClientRepository repo = new ClientRepository(em);
            return repo.findAll();
        } finally {
            em.close();
        }
    }
    
    /**
     * Cauta clienti dupa nume
     */
    public List<Client> searchClients(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllClients();
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClientRepository repo = new ClientRepository(em);
            return repo.searchByNume(searchTerm.trim());
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste clienti dupa tip
     */
    public List<Client> getClientsByType(ClientType tip) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClientRepository repo = new ClientRepository(em);
            return repo.findByTip(tip);
        } finally {
            em.close();
        }
    }
    
    /**
     * Numara toti clientii
     */
    public long countClients() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClientRepository repo = new ClientRepository(em);
            return repo.count();
        } finally {
            em.close();
        }
    }
    
    /**
     * Valideaza datele clientului
     */
    private void validateClientData(String nume, String prenume, String cnpCui, 
                                    String telefon, String email, String adresa, ClientType tip) {
        if (nume == null || nume.trim().isEmpty()) {
            throw new ValidationException("Numele este obligatoriu!");
        }
        
        if (cnpCui == null || cnpCui.trim().isEmpty()) {
            throw new ValidationException("CNP/CUI este obligatoriu!");
        }
        
        if (telefon == null || telefon.trim().isEmpty()) {
            throw new ValidationException("Telefonul este obligatoriu!");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email-ul este obligatoriu!");
        }
        
        if (tip == null) {
            throw new ValidationException("Tipul clientului este obligatoriu!");
        }
        
        // validez format CNP (13 cifre pt PF)
        if (tip == ClientType.PERSOANA_FIZICA) {
            if (!cnpCui.matches("\\d{13}")) {
                throw new ValidationException(
                    "CNP-ul trebuie sa contina exact 13 cifre!");
            }
            if (prenume == null || prenume.trim().isEmpty()) {
                throw new ValidationException(
                    "Prenumele este obligatoriu pentru persoana fizica!");
            }
        }
        
        // validez format email simplu
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Format email invalid!");
        }
    }
}
