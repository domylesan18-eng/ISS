package ro.ubb.autoservice.model.enums;

public enum ClientType {
    PERSOANA_FIZICA("Persoană Fizică"),
    PERSOANA_JURIDICA("Persoană Juridică");
    
    private final String displayName;
    
    ClientType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
