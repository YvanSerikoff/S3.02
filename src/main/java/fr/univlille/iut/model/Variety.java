package fr.univlille.iut.model;

public enum Variety {
    SETOSA("Setosa"),
    VERSICOLOR("Versicolor"),
    VIRGINICA("Virginica");

    private final String name;

    Variety(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    public static Variety fromString(String str) {
        if (str == null) return null;
        for (Variety v : Variety.values()) {
            if (v.toString().equalsIgnoreCase(str)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown variety: " + str);
    }

}