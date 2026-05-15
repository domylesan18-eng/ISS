package ro.ubb.autoservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ro.ubb.autoservice.exception.EntityNotFoundException;
import ro.ubb.autoservice.exception.ValidationException;
import ro.ubb.autoservice.model.Employee;
import ro.ubb.autoservice.model.enums.ExperienceLevel;
import ro.ubb.autoservice.repository.EmployeeRepository;
import ro.ubb.autoservice.util.JPAUtil;

import java.util.List;

/**
 * Service pt gestionare angajati
 */
public class EmployeeService {
    
    /**
     * Adauga un angajat nou
     */
    public Employee addEmployee(String nume, String prenume, String specialitate, 
                               ExperienceLevel nivelExperienta) {
        validateEmployeeData(nume, prenume, specialitate, nivelExperienta);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            EmployeeRepository repo = new EmployeeRepository(em);
            
            Employee employee = new Employee(nume, prenume, specialitate, nivelExperienta);
            employee = repo.save(employee);
            
            tx.commit();
            System.out.println("Angajat adaugat cu succes: " + employee);
            
            return employee;
            
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
     * Actualizeaza un angajat
     */
    public Employee updateEmployee(Long id, String nume, String prenume, String specialitate, 
                                  ExperienceLevel nivelExperienta) {
        validateEmployeeData(nume, prenume, specialitate, nivelExperienta);
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            EmployeeRepository repo = new EmployeeRepository(em);
            
            Employee employee = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Angajat cu id " + id + " nu a fost gasit!"));
            
            // actualizez campurile
            employee.setNume(nume);
            employee.setPrenume(prenume);
            employee.setSpecialitate(specialitate);
            employee.setNivelExperienta(nivelExperienta);
            
            employee = repo.save(employee);
            
            tx.commit();
            System.out.println("Angajat actualizat cu succes: " + employee);
            
            return employee;
            
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
     * Sterge un angajat
     */
    public void deleteEmployee(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            EmployeeRepository repo = new EmployeeRepository(em);
            
            Employee employee = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Angajat cu id " + id + " nu a fost gasit!"));
            
            repo.delete(employee);
            
            tx.commit();
            System.out.println("Angajat sters cu succes: " + employee);
            
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
     * Gaseste angajat dupa ID
     */
    public Employee getEmployeeById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Angajat cu id " + id + " nu a fost gasit!"));
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste toti angajatii
     */
    public List<Employee> getAllEmployees() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.findAll();
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste angajati dupa specialitate
     */
    public List<Employee> getEmployeesBySpecialitate(String specialitate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.findBySpecialitate(specialitate);
        } finally {
            em.close();
        }
    }
    
    /**
     * Gaseste angajati dupa nivel experienta
     */
    public List<Employee> getEmployeesByNivel(ExperienceLevel nivel) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.findByNivelExperienta(nivel);
        } finally {
            em.close();
        }
    }
    
    /**
     * Cauta angajati dupa nume
     */
    public List<Employee> searchEmployees(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees();
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.searchByNume(searchTerm.trim());
        } finally {
            em.close();
        }
    }
    
    /**
     * Numara toti angajatii
     */
    public long countEmployees() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EmployeeRepository repo = new EmployeeRepository(em);
            return repo.count();
        } finally {
            em.close();
        }
    }
    
    /**
     * Valideaza datele angajatului
     */
    private void validateEmployeeData(String nume, String prenume, String specialitate, 
                                     ExperienceLevel nivelExperienta) {
        if (nume == null || nume.trim().isEmpty()) {
            throw new ValidationException("Numele este obligatoriu!");
        }
        
        if (prenume == null || prenume.trim().isEmpty()) {
            throw new ValidationException("Prenumele este obligatoriu!");
        }
        
        if (specialitate == null || specialitate.trim().isEmpty()) {
            throw new ValidationException("Specialitatea este obligatorie!");
        }
        
        if (nivelExperienta == null) {
            throw new ValidationException("Nivelul de experienta este obligatoriu!");
        }
    }
}
