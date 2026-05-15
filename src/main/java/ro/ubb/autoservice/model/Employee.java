package ro.ubb.autoservice.model;

import jakarta.persistence.*;
import ro.ubb.autoservice.model.enums.ExperienceLevel;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nume;
    
    @Column(nullable = false, length = 100)
    private String prenume;
    
    @Column(nullable = false, length = 100)
    private String specialitate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExperienceLevel nivelExperienta;
    
    // Constructor gol pt JPA
    public Employee() {}
    
    // Constructor cu parametri
    public Employee(String nume, String prenume, String specialitate, 
                    ExperienceLevel nivelExperienta) {
        this.nume = nume;
        this.prenume = prenume;
        this.specialitate = specialitate;
        this.nivelExperienta = nivelExperienta;
    }
    
    // Getters si Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNume() {
        return nume;
    }
    
    public void setNume(String nume) {
        this.nume = nume;
    }
    
    public String getPrenume() {
        return prenume;
    }
    
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    
    public String getSpecialitate() {
        return specialitate;
    }
    
    public void setSpecialitate(String specialitate) {
        this.specialitate = specialitate;
    }
    
    public ExperienceLevel getNivelExperienta() {
        return nivelExperienta;
    }
    
    public void setNivelExperienta(ExperienceLevel nivelExperienta) {
        this.nivelExperienta = nivelExperienta;
    }
    
    // Helper method
    public String getNumeComplet() {
        return nume + " " + prenume;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", specialitate='" + specialitate + '\'' +
                ", nivelExperienta=" + nivelExperienta +
                '}';
    }
}
