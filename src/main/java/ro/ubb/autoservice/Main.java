package ro.ubb.autoservice;

import ro.ubb.autoservice.util.JPAUtil;
import ro.ubb.autoservice.view.MainFrame;

import javax.swing.*;

/**
 * Clasa principala - entry point al aplicatiei
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("   AutoService Manager - Pornire aplicatie");
        
        // Initializare JPA
        try {
            System.out.println("Verificare conexiune baza de date...");
            JPAUtil.getEntityManager().close();
            System.out.println("Conexiune la baza de date OK!\n");
        } catch (Exception e) {
            System.err.println("EROARE: Nu se poate conecta la baza de date!");
            System.err.println("Verificati ca MySQL ruleaza si ca datele din persistence.xml sunt corecte.");
            System.err.println("Eroare: " + e.getMessage());
            
            JOptionPane.showMessageDialog(null,
                "Nu se poate conecta la baza de date!\n" +
                "Verificati ca MySQL ruleaza si ca datele de conexiune sunt corecte.\n\n" +
                "Eroare: " + e.getMessage(),
                "Eroare conexiune",
                JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
        
        // Porneste interfata grafica pe Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel la sistemul de operare
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Nu se poate seta Look and Feel: " + e.getMessage());
            }
            
            // Creez si afisez fereastra principala
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            
            System.out.println("Aplicatie pornita cu succes!");
        });
        
        // Adaug shutdown hook pt inchidere corecta
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Inchidere aplicatie...");
            JPAUtil.close();
            System.out.println("Aplicatie inchisa cu succes!");
        }));
    }
}
