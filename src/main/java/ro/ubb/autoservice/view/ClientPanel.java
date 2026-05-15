package ro.ubb.autoservice.view;

import ro.ubb.autoservice.model.Client;
import ro.ubb.autoservice.model.enums.ClientType;
import ro.ubb.autoservice.service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel pentru gestionare clienti
 */
public class ClientPanel extends JPanel {
    
    private final ClientService clientService;
    
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public ClientPanel(ClientService clientService) {
        this.clientService = clientService;
        initializeUI();
        refreshTable();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Gestionare Clienti");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = new JButton("Cauta");
        searchButton.addActionListener(e -> searchClients());
        
        searchPanel.add(new JLabel("Cauta:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columnNames = {"ID", "Nume", "Prenume", "CNP/CUI", "Telefon", "Email", "Adresa", "Tip"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabel read-only
            }
        };
        
        clientTable = new JTable(tableModel);
        clientTable.setFont(new Font("Arial", Font.PLAIN, 14));
        clientTable.setRowHeight(25);
        clientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Butoane actiuni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Adauga Client");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddClientDialog());
        
        JButton editButton = new JButton("Editeaza");
        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> showEditClientDialog());
        
        JButton deleteButton = new JButton("Sterge");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteClient());
        
        JButton refreshButton = new JButton("Reimprospatare");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        try {
            List<Client> clients = clientService.getAllClients();
            tableModel.setRowCount(0); // clear table
            
            for (Client client : clients) {
                Object[] row = {
                    client.getId(),
                    client.getNume(),
                    client.getPrenume() != null ? client.getPrenume() : "-",
                    client.getCnpCui(),
                    client.getTelefon(),
                    client.getEmail(),
                    client.getAdresa() != null ? client.getAdresa() : "-",
                    client.getTip().getDisplayName()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare la incarcare clienti: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchClients() {
        String searchTerm = searchField.getText().trim();
        
        try {
            List<Client> clients = clientService.searchClients(searchTerm);
            tableModel.setRowCount(0);
            
            for (Client client : clients) {
                Object[] row = {
                    client.getId(),
                    client.getNume(),
                    client.getPrenume() != null ? client.getPrenume() : "-",
                    client.getCnpCui(),
                    client.getTelefon(),
                    client.getEmail(),
                    client.getAdresa() != null ? client.getAdresa() : "-",
                    client.getTip().getDisplayName()
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
    
    private void showAddClientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adauga Client", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fields
        JTextField numeField = new JTextField(20);
        JTextField prenumeField = new JTextField(20);
        JTextField cnpCuiField = new JTextField(20);
        JTextField telefonField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField adresaField = new JTextField(20);
        JComboBox<ClientType> tipCombo = new JComboBox<>(ClientType.values());
        
        // Layout
        int row = 0;
        addFormField(panel, gbc, row++, "Nume*:", numeField);
        addFormField(panel, gbc, row++, "Prenume:", prenumeField);
        addFormField(panel, gbc, row++, "CNP/CUI*:", cnpCuiField);
        addFormField(panel, gbc, row++, "Telefon*:", telefonField);
        addFormField(panel, gbc, row++, "Email*:", emailField);
        addFormField(panel, gbc, row++, "Adresa:", adresaField);
        addFormField(panel, gbc, row++, "Tip*:", tipCombo);
        
        // Butoane
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salveaza");
        JButton cancelButton = new JButton("Anuleaza");
        
        saveButton.addActionListener(e -> {
            try {
                clientService.addClient(
                    numeField.getText().trim(),
                    prenumeField.getText().trim(),
                    cnpCuiField.getText().trim(),
                    telefonField.getText().trim(),
                    emailField.getText().trim(),
                    adresaField.getText().trim(),
                    (ClientType) tipCombo.getSelectedItem()
                );
                
                JOptionPane.showMessageDialog(dialog,
                    "Client adaugat cu succes!",
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
    
    private void showEditClientDialog() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un client din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long clientId = (Long) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Client client = clientService.getClientById(clientId);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editeaza Client", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Fields pre-populate
            JTextField numeField = new JTextField(client.getNume(), 20);
            JTextField prenumeField = new JTextField(client.getPrenume() != null ? client.getPrenume() : "", 20);
            JTextField cnpCuiField = new JTextField(client.getCnpCui(), 20);
            JTextField telefonField = new JTextField(client.getTelefon(), 20);
            JTextField emailField = new JTextField(client.getEmail(), 20);
            JTextField adresaField = new JTextField(client.getAdresa() != null ? client.getAdresa() : "", 20);
            JComboBox<ClientType> tipCombo = new JComboBox<>(ClientType.values());
            tipCombo.setSelectedItem(client.getTip());
            
            int row = 0;
            addFormField(panel, gbc, row++, "Nume*:", numeField);
            addFormField(panel, gbc, row++, "Prenume:", prenumeField);
            addFormField(panel, gbc, row++, "CNP/CUI*:", cnpCuiField);
            addFormField(panel, gbc, row++, "Telefon*:", telefonField);
            addFormField(panel, gbc, row++, "Email*:", emailField);
            addFormField(panel, gbc, row++, "Adresa:", adresaField);
            addFormField(panel, gbc, row++, "Tip*:", tipCombo);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Salveaza");
            JButton cancelButton = new JButton("Anuleaza");
            
            saveButton.addActionListener(e -> {
                try {
                    clientService.updateClient(
                        clientId,
                        numeField.getText().trim(),
                        prenumeField.getText().trim(),
                        cnpCuiField.getText().trim(),
                        telefonField.getText().trim(),
                        emailField.getText().trim(),
                        adresaField.getText().trim(),
                        (ClientType) tipCombo.getSelectedItem()
                    );
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Client actualizat cu succes!",
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
                "Eroare la incarcare client: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un client din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long clientId = (Long) tableModel.getValueAt(selectedRow, 0);
        String numeClient = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Sigur doresti sa stergi clientul '" + numeClient + "'?\n" +
            "Toate vehiculele asociate vor fi sterse!",
            "Confirmare stergere",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                clientService.deleteClient(clientId);
                JOptionPane.showMessageDialog(this,
                    "Client sters cu succes!",
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
