package ro.ubb.autoservice.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String marca;
    
    @Column(nullable = false, length = 50)
    private String model;
    
    @Column(nullable = false)
    private Integer anFabricatie;
    
    @Column(nullable = false, unique = true, length = 20)
    private String nrInmatriculare;
    
    @Column(nullable = false, unique = true, length = 17)
    private String vin;
    
    @Column(nullable = false)
    private Integer kilometraj;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    // Constructor gol pt JPA
    public Vehicle() {}
    
    // Constructor cu parametri
    public Vehicle(String marca, String model, Integer anFabricatie, 
                   String nrInmatriculare, String vin, Integer kilometraj) {
        this.marca = marca;
        this.model = model;
        this.anFabricatie = anFabricatie;
        this.nrInmatriculare = nrInmatriculare;
        this.vin = vin;
        this.kilometraj = kilometraj;
    }
    
    // Getters si Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Integer getAnFabricatie() {
        return anFabricatie;
    }
    
    public void setAnFabricatie(Integer anFabricatie) {
        this.anFabricatie = anFabricatie;
    }
    
    public String getNrInmatriculare() {
        return nrInmatriculare;
    }
    
    public void setNrInmatriculare(String nrInmatriculare) {
        this.nrInmatriculare = nrInmatriculare;
    }
    
    public String getVin() {
        return vin;
    }
    
    public void setVin(String vin) {
        this.vin = vin;
    }
    
    public Integer getKilometraj() {
        return kilometraj;
    }
    
    public void setKilometraj(Integer kilometraj) {
        this.kilometraj = kilometraj;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    // Helper method pt afisare
    public String getDescriere() {
        return marca + " " + model + " (" + anFabricatie + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                ", nrInmatriculare='" + nrInmatriculare + '\'' +
                '}';
    }
}
