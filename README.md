# AutoService Manager - Iterația 1

Aplicație desktop Java pentru gestionarea unui service auto.  
**Proiect ISS - UBB Informatică - Anul II**

---

## 📋 Funcționalități (Iterația 1)

- ✅ **F1 - Gestionare Clienți**: CRUD complet (adăugare, editare, ștergere, căutare)
- ✅ **F2 - Gestionare Vehicule**: Asociere cu clienți, validare VIN și număr înmatriculare
- ✅ **F7 (parțial) - Gestionare Angajați**: CRUD de bază pentru mecanici

---

## 🛠️ Tehnologii

- **Java 17** (LTS)
- **Swing** (interfață grafică)
- **JPA / Hibernate 6.2** (ORM)
- **SQLite** (bază de date embedded)
- **Maven** (build tool)

---

## 📁 Structura Proiect

```
autoservice-manager/
├── src/main/java/ro/ubb/autoservice/
│   ├── model/              # Entități JPA (Client, Vehicle, Employee)
│   ├── repository/         # Acces bază de date
│   ├── service/            # Logică business
│   ├── view/               # Interfață Swing
│   ├── exception/          # Excepții custom
│   ├── util/               # JPAUtil
│   └── Main.java           # Entry point
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml # Configurare JPA
├── src/test/java/          # Teste JUnit
└── pom.xml                 # Dependențe Maven
```

---

## 🚀 Instalare și Rulare

### 1. Prerequisite

- **Java 17** instalat
- **Maven** instalat (sau folosește Maven Wrapper)
- **IntelliJ IDEA** (recomandat)

**NU** mai trebuie MySQL! SQLite e inclus automat. ✅

### 2. ~~Configurare Bază de Date~~ (SKIP - SQLite e automat!)

Baza de date `autoservice.db` se creează **automat** la prima rulare în folderul proiectului. Nu trebuie să faci nimic!

### 3. ~~Configurare `persistence.xml`~~ (SKIP - deja configurat!)

Configurarea SQLite e deja făcută. Nu trebuie să modifici nimic.

### 4. Import în IntelliJ IDEA

1. Deschide IntelliJ IDEA
2. `File → Open` → selectează folder-ul `autoservice-manager`
3. IntelliJ detectează automat proiectul Maven și descarcă dependențele (inclusiv SQLite)
4. Așteaptă finalizarea indexării

### 5. Rulare

**Variantă 1: Din IntelliJ**
- Click dreapta pe `Main.java` → `Run 'Main.main()'`

**Variantă 2: Din Terminal**
```bash
cd autoservice-manager
mvn clean compile
mvn exec:java -Dexec.mainClass="ro.ubb.autoservice.Main"
```

**Variantă 3: JAR executabil**
```bash
mvn clean package
java -jar target/autoservice-manager-1.0.0.jar
```

---

## 📊 Arhitectură (3 Straturi)

### 1. **Presentation Layer** (`view/`)
- `MainFrame.java` - fereastră principală cu navigare
- `ClientPanel.java` - tabel CRUD clienți
- `VehiclePanel.java` - tabel CRUD vehicule
- `EmployeePanel.java` - tabel CRUD angajați
- `DashboardPanel.java` - statistici

### 2. **Business Logic Layer** (`service/`)
- `ClientService` - validări, logică business clienți
- `VehicleService` - validări VIN, asociere cu client
- `EmployeeService` - gestionare angajați

### 3. **Data Access Layer** (`repository/`)
- `ClientRepository` - query-uri JPA pentru Client
- `VehicleRepository` - query-uri JPA pentru Vehicle
- `EmployeeRepository` - query-uri JPA pentru Employee

---

## 🧪 Testare

Rulează testele JUnit:

```bash
mvn test
```

Testele acoperă service-urile pentru validări și operații CRUD.

---

## 🔍 Exemplu de Utilizare

### 1. Adăugare Client
- Click **"Adauga Client"**
- Completează: Nume, CNP/CUI, Telefon, Email
- Alege tip: Persoană Fizică / Persoană Juridică
- Click **"Salveaza"**

### 2. Adăugare Vehicul
- Click **"Adauga Vehicul"**
- Selectează client din dropdown
- Completează: Marca, Model, An, Nr. Înmatriculare, VIN, Kilometraj
- Click **"Salveaza"**

### 3. Validări Automate
- CNP: exact 13 cifre (pentru PF)
- VIN: exact 17 caractere
- Email: format valid
- Duplicate: CNP/CUI, VIN, Nr. Înmatriculare

---

## 📝 Cerințe Nefuncționale (Implementate)

- ✅ **NF1 - Performanță**: operații CRUD sub 2 secunde
- ✅ **NF2 - Securitate**: validări robuste pe toate câmpurile
- ✅ **NF3 - Fiabilitate**: tranzacții atomice cu rollback
- ✅ **NF4 - Ușurință**: interfață intuitivă cu confirmări clare
- ✅ **NF6 - Mentenabilitate**: arhitectură 3-tier modulară

---

## 🐛 Debugging

Dacă aplicația nu pornește, verifică:

1. **Java 17 este setat?**
   ```bash
   java -version
   ```

2. **Dependențele Maven s-au descărcat?**
   ```bash
   mvn clean install
   ```

3. **Verifică consola** - erori JPA apar la pornire

4. **Fișierul `autoservice.db` apare în folderul proiectului?**
   - Dacă da → baza de date funcționează ✅
   - Dacă nu → verifică permisiunile folderului

---

## 📦 Următorii Pași (Iterația 2)

- Programări (calendar)
- Fișe de reparație
- Stoc piese (CRUD + alertă stoc minim)

---

## 👨‍💻 Dezvoltator

**Domy**  
UBB Informatică, Anul II  
Proiect ISS 2025

---

## 📄 Licență

Proiect educațional - Ingineria Sistemelor Software
