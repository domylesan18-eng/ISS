package ro.ubb.autoservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ubb.autoservice.model.Employee;
import ro.ubb.autoservice.model.enums.ExperienceLevel;

import java.util.List;
import java.util.Optional;

/**
 * Repository pt operatii CRUD cu Employee
 */
public class EmployeeRepository {
    
    private final EntityManager entityManager;
    
    public EmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Salveaza sau actualizeaza un angajat
     */
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            entityManager.persist(employee);
            return employee;
        } else {
            return entityManager.merge(employee);
        }
    }
    
    /**
     * Gaseste angajat dupa ID
     */
    public Optional<Employee> findById(Long id) {
        Employee employee = entityManager.find(Employee.class, id);
        return Optional.ofNullable(employee);
    }
    
    /**
     * Gaseste toti angajatii
     */
    public List<Employee> findAll() {
        TypedQuery<Employee> query = 
            entityManager.createQuery("SELECT e FROM Employee e ORDER BY e.nume", Employee.class);
        return query.getResultList();
    }
    
    /**
     * Gaseste angajati dupa specialitate
     */
    public List<Employee> findBySpecialitate(String specialitate) {
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e WHERE LOWER(e.specialitate) LIKE LOWER(:spec) ORDER BY e.nume", 
            Employee.class);
        query.setParameter("spec", "%" + specialitate + "%");
        return query.getResultList();
    }
    
    /**
     * Gaseste angajati dupa nivel experienta
     */
    public List<Employee> findByNivelExperienta(ExperienceLevel nivel) {
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e WHERE e.nivelExperienta = :nivel ORDER BY e.nume", 
            Employee.class);
        query.setParameter("nivel", nivel);
        return query.getResultList();
    }
    
    /**
     * Cauta angajati dupa nume
     */
    public List<Employee> searchByNume(String searchTerm) {
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e WHERE LOWER(e.nume) LIKE LOWER(:search) " +
            "OR LOWER(e.prenume) LIKE LOWER(:search) ORDER BY e.nume", Employee.class);
        query.setParameter("search", "%" + searchTerm + "%");
        return query.getResultList();
    }
    
    /**
     * Sterge un angajat
     */
    public void delete(Employee employee) {
        if (entityManager.contains(employee)) {
            entityManager.remove(employee);
        } else {
            entityManager.remove(entityManager.merge(employee));
        }
    }
    
    /**
     * Numara toti angajatii
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Employee e", Long.class);
        return query.getSingleResult();
    }
}
