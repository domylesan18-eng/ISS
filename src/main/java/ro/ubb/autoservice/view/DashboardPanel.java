package ro.ubb.autoservice.view;

import ro.ubb.autoservice.service.ClientService;
import ro.ubb.autoservice.service.EmployeeService;
import ro.ubb.autoservice.service.VehicleService;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard - ecran principal cu statistici
 */
public class DashboardPanel extends JPanel {
    
    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final EmployeeService employeeService;
    
    private JLabel clientCountLabel;
    private JLabel vehicleCountLabel;
    private JLabel employeeCountLabel;
    
    public DashboardPanel(ClientService clientService, VehicleService vehicleService, 
                         EmployeeService employeeService) {
        this.clientService = clientService;
        this.vehicleService = vehicleService;
        this.employeeService = employeeService;
        
        initializeUI();
        refreshData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Titlu
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel central cu statistici
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        
        // Card clienti
        JPanel clientCard = createStatCard("Clienti", "0", new Color(52, 152, 219));
        clientCountLabel = (JLabel) ((JPanel)clientCard.getComponent(1)).getComponent(0);
        statsPanel.add(clientCard);
        
        // Card vehicule
        JPanel vehicleCard = createStatCard("Vehicule", "0", new Color(46, 204, 113));
        vehicleCountLabel = (JLabel) ((JPanel)vehicleCard.getComponent(1)).getComponent(0);
        statsPanel.add(vehicleCard);
        
        // Card angajati
        JPanel employeeCard = createStatCard("Angajati", "0", new Color(155, 89, 182));
        employeeCountLabel = (JLabel) ((JPanel)employeeCard.getComponent(1)).getComponent(0);
        statsPanel.add(employeeCard);
        
        add(statsPanel, BorderLayout.CENTER);
        
        // Info panel jos
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Bine ai venit in AutoService Manager!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("Selecteaza o sectiune din meniul din stanga pentru a incepe.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(welcomeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createVerticalGlue());
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        // Titlu
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Valoare
        JPanel valuePanel = new JPanel();
        valuePanel.setBackground(color);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 48));
        valueLabel.setForeground(Color.WHITE);
        valuePanel.add(valueLabel);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refreshData() {
        try {
            long clientCount = clientService.countClients();
            long vehicleCount = vehicleService.countVehicles();
            long employeeCount = employeeService.countEmployees();
            
            clientCountLabel.setText(String.valueOf(clientCount));
            vehicleCountLabel.setText(String.valueOf(vehicleCount));
            employeeCountLabel.setText(String.valueOf(employeeCount));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare la incarcare statistici: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
