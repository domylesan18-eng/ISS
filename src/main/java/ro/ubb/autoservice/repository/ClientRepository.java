package ro.ubb.autoservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ubb.autoservice.model.Client;
import ro.ubb.autoservice.model.enums.ClientType;

import java.util.List;
import java.util.Optional;

/**
 * Repository pt operatii CRUD cu Client
 */
public class ClientRepository {
    
    private final EntityManager entityManager;
    
    public ClientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Salveaza sau actualizeaza un client
     */
    public Client save(Client client) {
        if (client.getId() == null) {
            entityManager.persist(client);
            return client;
        } else {
            return entityManager.merge(client);
        }
    }
    
    /**
     * Gaseste client dupa ID
     */
    public Optional<Client> findById(Long id) {
        Client client = entityManager.find(Client.class, id);
        return Optional.ofNullable(client);
    }
    
    /**
     * Gaseste toti clientii
     */
    public List<Client> findAll() {
        TypedQuery<Client> query = 
            entityManager.createQuery("SELECT c FROM Client c ORDER BY c.nume", Client.class);
        return query.getResultList();
    }
    
    /**
     * Verifica daca exista client cu CNP/CUI dat
     */
    public boolean existsByCnpCui(String cnpCui) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Client c WHERE c.cnpCui = :cnpCui", Long.class);
        query.setParameter("cnpCui", cnpCui);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Gaseste clienti dupa tip
     */
    public List<Client> findByTip(ClientType tip) {
        TypedQuery<Client> query = entityManager.createQuery(
            "SELECT c FROM Client c WHERE c.tip = :tip ORDER BY c.nume", Client.class);
        query.setParameter("tip", tip);
        return query.getResultList();
    }
    
    /**
     * Cauta clienti dupa nume sau prenume
     */
    public List<Client> searchByNume(String searchTerm) {
        TypedQuery<Client> query = entityManager.createQuery(
            "SELECT c FROM Client c WHERE LOWER(c.nume) LIKE LOWER(:search) " +
            "OR LOWER(c.prenume) LIKE LOWER(:search) ORDER BY c.nume", Client.class);
        query.setParameter("search", "%" + searchTerm + "%");
        return query.getResultList();
    }
    
    /**
     * Sterge un client
     */
    public void delete(Client client) {
        if (entityManager.contains(client)) {
            entityManager.remove(client);
        } else {
            entityManager.remove(entityManager.merge(client));
        }
    }
    
    /**
     * Numara toti clientii
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Client c", Long.class);
        return query.getSingleResult();
    }
}
