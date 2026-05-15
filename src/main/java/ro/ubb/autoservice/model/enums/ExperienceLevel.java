package ro.ubb.autoservice.model.enums;

public enum ExperienceLevel {
    JUNIOR("Junior"),
    MID("Mid"),
    SENIOR("Senior");
    
    private final String displayName;
    
    ExperienceLevel(String displayName) {
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
