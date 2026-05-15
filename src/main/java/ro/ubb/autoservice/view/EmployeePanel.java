package ro.ubb.autoservice.view;

import ro.ubb.autoservice.model.Employee;
import ro.ubb.autoservice.model.enums.ExperienceLevel;
import ro.ubb.autoservice.service.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel pentru gestionare angajati
 */
public class EmployeePanel extends JPanel {
    
    private final EmployeeService employeeService;
    
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public EmployeePanel(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
        
        JLabel titleLabel = new JLabel("Gestionare Angajati");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Cauta");
        searchButton.addActionListener(e -> searchEmployees());
        
        searchPanel.add(new JLabel("Cauta:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columnNames = {"ID", "Nume", "Prenume", "Specialitate", "Nivel Experienta"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Butoane
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Adauga Angajat");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddEmployeeDialog());
        
        JButton editButton = new JButton("Editeaza");
        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> showEditEmployeeDialog());
        
        JButton deleteButton = new JButton("Sterge");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteEmployee());
        
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
            List<Employee> employees = employeeService.getAllEmployees();
            tableModel.setRowCount(0);
            
            for (Employee emp : employees) {
                Object[] row = {
                    emp.getId(),
                    emp.getNume(),
                    emp.getPrenume(),
                    emp.getSpecialitate(),
                    emp.getNivelExperienta().getDisplayName()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Eroare la incarcare angajati: " + e.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Employee> employees = employeeService.searchEmployees(searchTerm);
            tableModel.setRowCount(0);
            
            for (Employee emp : employees) {
                Object[] row = {
                    emp.getId(),
                    emp.getNume(),
                    emp.getPrenume(),
                    emp.getSpecialitate(),
                    emp.getNivelExperienta().getDisplayName()
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
    
    private void showAddEmployeeDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adauga Angajat", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField numeField = new JTextField(20);
        JTextField prenumeField = new JTextField(20);
        JTextField specialitateField = new JTextField(20);
        JComboBox<ExperienceLevel> nivelCombo = new JComboBox<>(ExperienceLevel.values());
        
        int row = 0;
        addFormField(panel, gbc, row++, "Nume*:", numeField);
        addFormField(panel, gbc, row++, "Prenume*:", prenumeField);
        addFormField(panel, gbc, row++, "Specialitate*:", specialitateField);
        addFormField(panel, gbc, row++, "Nivel Experienta*:", nivelCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salveaza");
        JButton cancelButton = new JButton("Anuleaza");
        
        saveButton.addActionListener(e -> {
            try {
                employeeService.addEmployee(
                    numeField.getText().trim(),
                    prenumeField.getText().trim(),
                    specialitateField.getText().trim(),
                    (ExperienceLevel) nivelCombo.getSelectedItem()
                );
                
                JOptionPane.showMessageDialog(dialog,
                    "Angajat adaugat cu succes!",
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
    
    private void showEditEmployeeDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un angajat din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long employeeId = (Long) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editeaza Angajat", true);
            dialog.setSize(450, 350);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JTextField numeField = new JTextField(employee.getNume(), 20);
            JTextField prenumeField = new JTextField(employee.getPrenume(), 20);
            JTextField specialitateField = new JTextField(employee.getSpecialitate(), 20);
            JComboBox<ExperienceLevel> nivelCombo = new JComboBox<>(ExperienceLevel.values());
            nivelCombo.setSelectedItem(employee.getNivelExperienta());
            
            int row = 0;
            addFormField(panel, gbc, row++, "Nume*:", numeField);
            addFormField(panel, gbc, row++, "Prenume*:", prenumeField);
            addFormField(panel, gbc, row++, "Specialitate*:", specialitateField);
            addFormField(panel, gbc, row++, "Nivel Experienta*:", nivelCombo);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Salveaza");
            JButton cancelButton = new JButton("Anuleaza");
            
            saveButton.addActionListener(e -> {
                try {
                    employeeService.updateEmployee(
                        employeeId,
                        numeField.getText().trim(),
                        prenumeField.getText().trim(),
                        specialitateField.getText().trim(),
                        (ExperienceLevel) nivelCombo.getSelectedItem()
                    );
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Angajat actualizat cu succes!",
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
    
    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecteaza un angajat din tabel!",
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long employeeId = (Long) tableModel.getValueAt(selectedRow, 0);
        String numeAngajat = tableModel.getValueAt(selectedRow, 1) + " " + 
                            tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Sigur doresti sa stergi angajatul '" + numeAngajat + "'?",
            "Confirmare stergere",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeService.deleteEmployee(employeeId);
                JOptionPane.showMessageDialog(this,
                    "Angajat sters cu succes!",
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
