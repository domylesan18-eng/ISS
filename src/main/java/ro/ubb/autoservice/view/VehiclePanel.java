package ro.ubb.autoservice.view;

import ro.ubb.autoservice.model.Client;
import ro.ubb.autoservice.model.Vehicle;
import ro.ubb.autoservice.service.ClientService;
import ro.ubb.autoservice.service.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel pentru gestionare vehicule
 */
public class VehiclePanel extends JPanel {
    
    private final VehicleService vehicleService;
    private final ClientService clientService;
    
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public VehiclePanel(VehicleService vehicleService, ClientService clientService) {
        this.vehicleService = vehicleService;
        this.clientService = clientService;
        initializeUI();
        refreshTable();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Gestionare Vehicule");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Cauta");
        searchButton.addActionListener(e -> searchVehicles());
        
        searchPanel.add(new JLabel("Cauta:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columnNames = {"ID", "Marca", "Model", "An", "Nr. Inmatriculare", "VIN", "Kilometraj", "Client"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        vehicleTable = new JTable(tableModel);
        vehicleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        vehicleTable.setRowHeight(25);
        vehicleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Butoane
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Adauga Vehicul");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddVehicleDialog());
        
        JButton editButton = new JButton("Editeaza");
        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> showEditVehicleDialog());
        
        JButton deleteButton = new JButton("Sterge");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteVehicle());
        
        JButton refreshButton = new JButton("Reimprospatare");
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            tableModel.setRowCount(0);
            
            for (Vehicle v : vehicles) {
                Object[] row = {
                    v.getId(),
                    v.getMarca(),
                    v.getModel(),
                    v.getAnFabricatie(),
                    v.getNrInmatriculare(),
                    v.getVin(),
                    v.getKilometraj() + " km",
                    v.getClient().getNumeComplet()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare la incarcare vehicule: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchVehicles() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Vehicle> vehicles = vehicleService.searchVehicles(searchTerm);
            tableModel.setRowCount(0);
            
            for (Vehicle v : vehicles) {
                Object[] row = {
                    v.getId(),
                    v.getMarca(),
                    v.getModel(),
                    v.getAnFabricatie(),
                    v.getNrInmatriculare(),
                    v.getVin(),
                    v.getKilometraj() + " km",
                    v.getClient().getNumeComplet()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare la cautare: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddVehicleDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adauga Vehicul", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Combo cu clienti
        List<Client> clients = clientService.getAllClients();
        JComboBox<String> clientCombo = new JComboBox<>();
        for (Client c : clients) {
            clientCombo.addItem(c.getId() + " - " + c.getNumeComplet());
        }
        
        JTextField marcaField = new JTextField(20);
        JTextField modelField = new JTextField(20);
        JSpinner anSpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(2020), Integer.valueOf(1900), Integer.valueOf(2025), Integer.valueOf(1)));
        JTextField nrInmatriculareField = new JTextField(20);
        JTextField vinField = new JTextField(20);
        JSpinner kmSpinner = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(999999), Integer.valueOf(1000)));
        
        int row = 0;
        addFormField(panel, gbc, row++, "Client*:", clientCombo);
        addFormField(panel, gbc, row++, "Marca*:", marcaField);
        addFormField(panel, gbc, row++, "Model*:", modelField);
        addFormField(panel, gbc, row++, "An Fabricatie*:", anSpinner);
        addFormField(panel, gbc, row++, "Nr. Inmatriculare*:", nrInmatriculareField);
        addFormField(panel, gbc, row++, "VIN (17 caractere)*:", vinField);
        addFormField(panel, gbc, row++, "Kilometraj*:", kmSpinner);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salveaza");
        JButton cancelButton = new JButton("Anuleaza");
        
        saveButton.addActionListener(e -> {
            try {
                String selected = (String) clientCombo.getSelectedItem();
                Long clientId = Long.parseLong(selected.split(" - ")[0]);
                
                vehicleService.addVehicle(
                    clientId,
                    marcaField.getText().trim(),
                    modelField.getText().trim(),
                    (Integer) anSpinner.getValue(),
                    nrInmatriculareField.getText().trim(),
                    vinField.getText().trim(),
                    (Integer) kmSpinner.getValue()
                );
                
                JOptionPane.showMessageDialog(dialog,
                    "Vehicul adaugat cu succes!",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                refreshTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Eroare: " + ex.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditVehicleDialog() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un vehicul din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long vehicleId = (Long) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editeaza Vehicul", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JTextField marcaField = new JTextField(vehicle.getMarca(), 20);
            JTextField modelField = new JTextField(vehicle.getModel(), 20);
            JSpinner anSpinner = new JSpinner(new SpinnerNumberModel(vehicle.getAnFabricatie(), Integer.valueOf(1900), Integer.valueOf(2025), Integer.valueOf(1)));
            JTextField nrInmatriculareField = new JTextField(vehicle.getNrInmatriculare(), 20);
            JTextField vinField = new JTextField(vehicle.getVin(), 20);
            JSpinner kmSpinner = new JSpinner(new SpinnerNumberModel(vehicle.getKilometraj(), Integer.valueOf(0), Integer.valueOf(999999), Integer.valueOf(1000)));
            
            int row = 0;
            addFormField(panel, gbc, row++, "Marca*:", marcaField);
            addFormField(panel, gbc, row++, "Model*:", modelField);
            addFormField(panel, gbc, row++, "An Fabricatie*:", anSpinner);
            addFormField(panel, gbc, row++, "Nr. Inmatriculare*:", nrInmatriculareField);
            addFormField(panel, gbc, row++, "VIN*:", vinField);
            addFormField(panel, gbc, row++, "Kilometraj*:", kmSpinner);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Salveaza");
            JButton cancelButton = new JButton("Anuleaza");
            
            saveButton.addActionListener(e -> {
                try {
                    vehicleService.updateVehicle(
                        vehicleId,
                        marcaField.getText().trim(),
                        modelField.getText().trim(),
                        (Integer) anSpinner.getValue(),
                        nrInmatriculareField.getText().trim(),
                        vinField.getText().trim(),
                        (Integer) kmSpinner.getValue()
                    );
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Vehicul actualizat cu succes!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    refreshTable();
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Eroare: " + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            panel.add(buttonPanel, gbc);
            
            dialog.add(panel);
            dialog.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un vehicul din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long vehicleId = (Long) tableModel.getValueAt(selectedRow, 0);
        String descriere = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Sigur doresti sa stergi vehiculul '" + descriere + "'?",
            "Confirmare stergere",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehicleService.deleteVehicle(vehicleId);
                JOptionPane.showMessageDialog(this,
                    "Vehicul sters cu succes!",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Eroare la stergere: " + e.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
}
