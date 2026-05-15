package ro.ubb.autoservice.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utilitar pt gestionarea EntityManagerFactory si EntityManager
 */
public class JPAUtil {
    
    private static final String PERSISTENCE_UNIT_NAME = "autoservice-pu";
    private static EntityManagerFactory emf;
    
    static {
        try {
            System.out.println("Initializare EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("EntityManagerFactory initializat cu succes!");
        } catch (Exception e) {
            System.err.println("Eroare la initializare EntityManagerFactory!");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Obtine un EntityManager nou
     */
    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory nu a fost initializat!");
        }
        return emf.createEntityManager();
    }
    
    /**
     * Inchide EntityManagerFactory
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            System.out.println("Inchidere EntityManagerFactory...");
            emf.close();
        }
    }
}
