package ro.ubb.autoservice.model;

import jakarta.persistence.*;
import ro.ubb.autoservice.model.enums.ClientType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nume;
    
    @Column(length = 100)
    private String prenume;  // null pt PJ
    
    @Column(nullable = false, unique = true, length = 13)
    private String cnpCui;
    
    @Column(nullable = false, length = 15)
    private String telefon;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @Column(length = 200)
    private String adresa;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientType tip;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicule = new ArrayList<>();
    
    // Constructor gol pt JPA
    public Client() {}
    
    // Constructor cu parametri
    public Client(String nume, String prenume, String cnpCui, String telefon, 
                  String email, String adresa, ClientType tip) {
        this.nume = nume;
        this.prenume = prenume;
        this.cnpCui = cnpCui;
        this.telefon = telefon;
        this.email = email;
        this.adresa = adresa;
        this.tip = tip;
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
    
    public String getCnpCui() {
        return cnpCui;
    }
    
    public void setCnpCui(String cnpCui) {
        this.cnpCui = cnpCui;
    }
    
    public String getTelefon() {
        return telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdresa() {
        return adresa;
    }
    
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    public ClientType getTip() {
        return tip;
    }
    
    public void setTip(ClientType tip) {
        this.tip = tip;
    }
    
    public List<Vehicle> getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(List<Vehicle> vehicule) {
        this.vehicule = vehicule;
    }
    
    // Helper method
    public void addVehicle(Vehicle vehicle) {
        vehicule.add(vehicle);
        vehicle.setClient(this);
    }
    
    public void removeVehicle(Vehicle vehicle) {
        vehicule.remove(vehicle);
        vehicle.setClient(null);
    }
    
    // Nume complet pt afisare
    public String getNumeComplet() {
        if (tip == ClientType.PERSOANA_FIZICA && prenume != null) {
            return nume + " " + prenume;
        }
        return nume;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", tip=" + tip +
                '}';
    }
}
