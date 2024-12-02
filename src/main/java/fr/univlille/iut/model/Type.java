package fr.univlille.iut.model;

@SuppressWarnings("unused")
public enum Type {
    NORMAL("Normal"),
    FIRE("Fire"),
    WATER("Water"),
    ELECTRIC("Electric"),
    GRASS("Grass"),
    ICE("Ice"),
    FIGHTING("Fighting"),
    POISON("Poison"),
    GROUND("Ground"),
    FLYING("Flying"),
    PSYCHIC("Psychic"),
    BUG("Bug"),
    ROCK("Rock"),
    GHOST("Ghost"),
    DRAGON("Dragon"),
    DARK("Dark"),
    STEEL("Steel"),
    FAIRY("Fairy"),
    NONE("None");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
    
    public static Type fromString(String type) {
        if (type.isEmpty()) {
            return NONE;
        }
        for (Type t : Type.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
