package ro.ubb.autoservice.view;

import ro.ubb.autoservice.service.ClientService;
import ro.ubb.autoservice.service.VehicleService;
import ro.ubb.autoservice.service.EmployeeService;

import javax.swing.*;
import java.awt.*;

/**
 * Fereastra principala a aplicatiei
 */
public class MainFrame extends JFrame {
    
    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final EmployeeService employeeService;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panelurile principale
    private DashboardPanel dashboardPanel;
    private ClientPanel clientPanel;
    private VehiclePanel vehiclePanel;
    private EmployeePanel employeePanel;
    
    public MainFrame() {
        // Initializare servicii
        this.clientService = new ClientService();
        this.vehicleService = new VehicleService();
        this.employeeService = new EmployeeService();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("AutoService Manager");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Meniu bar
        createMenuBar();
        
        // Sidebar navigare
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content area cu CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Creez panelurile
        dashboardPanel = new DashboardPanel(clientService, vehicleService, employeeService);
        clientPanel = new ClientPanel(clientService);
        vehiclePanel = new VehiclePanel(vehicleService, clientService);
        employeePanel = new EmployeePanel(employeeService);
        
        // Adaug panelurile in CardLayout
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(clientPanel, "clients");
        contentPanel.add(vehiclePanel, "vehicles");
        contentPanel.add(employeePanel, "employees");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Afisez dashboard la start
        showPanel("dashboard");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Meniu Fisier
        JMenu fileMenu = new JMenu("Fisier");
        JMenuItem exitItem = new JMenuItem("Iesire");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Meniu Navigare
        JMenu navMenu = new JMenu("Navigare");
        
        JMenuItem dashItem = new JMenuItem("Dashboard");
        dashItem.addActionListener(e -> showPanel("dashboard"));
        navMenu.add(dashItem);
        
        JMenuItem clientsItem = new JMenuItem("Clienti");
        clientsItem.addActionListener(e -> showPanel("clients"));
        navMenu.add(clientsItem);
        
        JMenuItem vehiclesItem = new JMenuItem("Vehicule");
        vehiclesItem.addActionListener(e -> showPanel("vehicles"));
        navMenu.add(vehiclesItem);
        
        JMenuItem employeesItem = new JMenuItem("Angajati");
        employeesItem.addActionListener(e -> showPanel("employees"));
        navMenu.add(employeesItem);
        
        // Meniu Ajutor
        JMenu helpMenu = new JMenu("Ajutor");
        JMenuItem aboutItem = new JMenuItem("Despre");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(navMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Logo/titlu
        JLabel titleLabel = new JLabel("AutoService");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titleLabel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Butoane navigare
        addNavButton(sidebar, "Dashboard", "dashboard");
        addNavButton(sidebar, "Clienti", "clients");
        addNavButton(sidebar, "Vehicule", "vehicles");
        addNavButton(sidebar, "Angajati", "employees");
        
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private void addNavButton(JPanel sidebar, String text, String panelName) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Stilizare
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(116, 125, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(99, 110, 114));
            }
        });
        
        button.addActionListener(e -> showPanel(panelName));
        
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private void showPanel(String panelName) {
        // Refresh panel daca e necesar
        switch (panelName) {
            case "dashboard":
                dashboardPanel.refreshData();
                break;
            case "clients":
                clientPanel.refreshTable();
                break;
            case "vehicles":
                vehiclePanel.refreshTable();
                break;
            case "employees":
                employeePanel.refreshTable();
                break;
        }
        
        cardLayout.show(contentPanel, panelName);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "AutoService Manager v1.0\n\n" +
            "Aplicatie desktop pentru gestionare service auto\n" +
            "Dezvoltat cu Java 17 + Swing + JPA/Hibernate\n\n" +
            "© 2025 UBB Informatica",
            "Despre",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
