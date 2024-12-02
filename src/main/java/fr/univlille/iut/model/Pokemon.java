package fr.univlille.iut.model;

import com.opencsv.bean.CsvBindByName;

import java.util.*;

@SuppressWarnings("ALL")
/**
 * Classe représentant un Pokémon avec ses attributs et ses fonctionnalités.
 */
public class Pokemon implements DistanceComparable<Pokemon> {

    @CsvBindByName(column = "name")
    String name;

    @CsvBindByName(column = "attack")
    int attack;

    @CsvBindByName(column = "base_egg_steps")
    int base_egg_steps ;

    @CsvBindByName(column = "capture_rate")
    double capture_rate;

    @CsvBindByName(column = "defense")
    int defense;

    @CsvBindByName(column = "experience_growth")
    int experience_growth;

    @CsvBindByName(column = "hp")
    int hp;

    @CsvBindByName(column = "sp_attack")
    int sp_attack;

    @CsvBindByName(column = "sp_defense")
    int sp_defense;

    @CsvBindByName(column = "type1")
    String type1String;

    @CsvBindByName(column = "type2")
    String type2String;

    @CsvBindByName(column = "speed")
    double speed;

    @CsvBindByName(column = "is_legendary")
    boolean is_legendary;

    Type type1;
    Type type2;

    private double x;
    private double y;
    private boolean isNew;

    /**
     * Constructeur par défaut.
     */
    public Pokemon(){}

    /**
     * Constructeur complet pour initialiser tous les attributs d'un Pokémon.
     */
    protected Pokemon(String name, int attack, int base_egg_steps, double capture_rate, int defense, int experience_growth, int hp, int sp_attaque, int sp_defense, String type1String, String type2String, Type type1, Type type2, double speed, boolean is_legendary) {
        this.name = name;
        this.attack = attack;
        this.base_egg_steps = base_egg_steps;
        this.capture_rate = capture_rate;
        this.defense = defense;
        this.experience_growth = experience_growth;
        this.hp = hp;
        this.sp_attack = sp_attack;
        this.sp_defense = sp_defense;
        this.type1String = type1String;
        this.type2String = type2String;
        this.type1 = type1;
        this.type2 = type2;
        this.speed = speed;
        this.is_legendary = is_legendary;
    }

    /**
     * Constructeur basé sur un autre Pokémon et des types spécifiques.
     */
    public Pokemon(Pokemon pokemon, String type1String, String type2String) {
        this(pokemon.name, pokemon.attack, pokemon.base_egg_steps, pokemon.capture_rate, pokemon.defense,
                pokemon.experience_growth, pokemon.hp, pokemon.sp_attack, pokemon.sp_defense, type2String, type1String, pokemon.type1,
                pokemon.type2, pokemon.speed, pokemon.is_legendary);
    }

    // Getters et setters pour chaque attribut.
    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getBase_egg_steps() {
        return base_egg_steps;
    }

    public double getCapture_rate() {
        return capture_rate;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience_growth() {
        return experience_growth;
    }

    public int getHp() {
        return hp;
    }

    public int getSp_attack() {
        return sp_attack;
    }

    public int getSp_defense() {
        return sp_defense;
    }

    public String getType1String() {return type1String;}

    public String getType2String() {return type2String;}

    public String getCategoryString() {
        return type1.toString();
    }

    public Type getType1() {
        return type1;
    }

    public Type getType2() {
        return type2;
    }

    /**
     * Détermine la catégorie du Pokémon à partir de ses voisins.
     * @param kNeighbors Liste des k voisins les plus proches.
     */
    @Override
    public void setBaseVariety() {
            this.type1 = Type.fromString(type1String);
            this.type2 = Type.fromString(type2String);
    }

    @Override
    public void initializeFromValues(double[] values) {
        this.attack = (int) values[0];
        this.base_egg_steps = (int) values[1];
        this.capture_rate = values[2];
        this.defense = (int) values[3];
        this.experience_growth = (int) values[4];
        this.hp = (int) values[5];
        this.sp_attack = (int) values[6];
        this.sp_defense = (int) values[7];
        this.speed = values[8];
        this.isNew=true;
    }

    public void setVariety(Type type1, Type type2) {
        this.type1 = type1;
        this.type2 = type2 == null ? null : type2;
    }

    public boolean isNew(){
        return this.isNew;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public String getCategory() {
        return type1String + " " + type2String;
    }

    public boolean isIs_legendary() {
        return is_legendary;
    }

    /**
     * Retourne les attributs numériques du Pokémon sous forme de liste.
     */
    public List<Double> getAllValues() {
        return List.of(
                (double) attack,
                (double) base_egg_steps,
                (double) capture_rate,
                (double) defense,
                (double) experience_growth,
                (double) hp,
                (double) sp_attack,
                (double) sp_defense,
                (double) speed
        );
    }

    /**
     * Convertit les attributs du Pokémon en chaîne de caractères.
     */
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", attack=" + attack +
                ", base_egg_steps=" + base_egg_steps +
                ", capture_rate=" + capture_rate +
                ", defense=" + defense +
                ", experience_growth=" + experience_growth +
                ", hp=" + hp +
                ", sp_attack=" + sp_attack +
                ", sp_defense=" + sp_defense +
                ", speed=" + speed +
                ", is_legendary=" + is_legendary +
                ", type1=" + type1 +
                ", type2=" + (type2 != null ? type2 : "None") +
                '}';
    }

    /**
     * Renvoie la liste de toutes les variétés possibles pour un Pokémon.
     *
     * @return une liste des types possibles.
     */
    public List<Enum<?>> getVariety() {
        return Arrays.asList(Type.values());
    }

    public Enum getVar(){
        return this.type1;
    }

    public String findMostCommonType1(List<Pokemon> kNeighbors) {
        Map<String, Integer> type1Count = new HashMap<>();
        for (Pokemon pokemon : kNeighbors) {
            String type1 = pokemon.getType1String();
            type1Count.put(type1, type1Count.getOrDefault(type1, 0) + 1);
        }
        return type1Count.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public String findMostCommonType2(List<Pokemon> kNeighbors) {
        Map<String, Integer> type2Count = new HashMap<>();
        for (Pokemon pokemon : kNeighbors) {
            String type2 = pokemon.getType2String();
            if (type2 != null && !type2.isEmpty()) {
                type2Count.put(type2, type2Count.getOrDefault(type2, 0) + 1);
            }
        }
        return type2Count.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Définit les types primaires et secondaires du Pokémon.
     *
     * @param type1 le type principal.
     * @param type2 le type secondaire (peut être null).
     */
    public void setVariety(String str){
        String[] types = str.split(",");
        this.type1 = Type.fromString(types[0]);
        this.type2 = types.length > 1 ? Type.fromString(types[1]) : null;

        type1String = types[0];
        type2String = types.length > 1 ? types[1] : null;
    }

    @Override
    public String determineCategory(List<Pokemon> kNeighbors) {
        String mostCommonType1 = findMostCommonType1(kNeighbors);
        String mostCommonType2 = findMostCommonType2(kNeighbors);
        return mostCommonType1 + (mostCommonType2 != null ? " " + mostCommonType2 : "");
    }



    @Override
    public int getNbAttributesComparable() {
        return 9;
    }

    /**
     * Renvoie la valeur de l'attribut correspondant à l'indice donné.
     *
     * @param i l'indice de l'attribut.
     * @return la valeur de l'attribut en tant qu'entier.
     */
    @Override
    public int getAttribute(int i) {
        return switch (i) {
            case 0 -> attack;
            case 1 -> base_egg_steps;
            case 2 -> (int) capture_rate;
            case 3 -> defense;
            case 4 -> experience_growth;
            case 5 -> hp;
            case 6 -> sp_attack;
            case 7 -> sp_defense;
            case 8 -> (int) speed;
            default -> 0;
        };
    }

    /**
     * Renvoie le nom de l'attribut correspondant à l'indice donné.
     *
     * @param i l'indice de l'attribut.
     * @return le nom de l'attribut.
     */
    @Override
    public String getAttributeName(int i) {
        return switch (i) {
            case 0 -> "attack";
            case 1 -> "base_egg_steps";
            case 2 -> "capture_rate";
            case 3 -> "defense";
            case 4 -> "experience_growth";
            case 5 -> "hp";
            case 6 -> "sp_attack";
            case 7 -> "sp_defense";
            case 8 -> "speed";
            default -> null;
        };
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Définit la coordonnée X en fonction d'un attribut donné.
     *
     * @param x le nom de l'attribut.
     */
    @Override
    public void setX(String x) {
        switch (x) {
            case "attack" -> this.x = attack;
            case "base_egg_steps" -> this.x = base_egg_steps;
            case "defense" -> this.x = defense;
            case "hp" -> this.x = hp;
            case "sp_attack" -> this.x = sp_attack;
            case "sp_defense" -> this.x = sp_defense;
            case "speed" -> this.x = speed;
            case "experience_growth" -> this.x = experience_growth;
            case "capture_rate" -> this.x = capture_rate;
        }
    }

    /**
     * Définit la coordonnée Y en fonction d'un attribut donné.
     *
     * @param y le nom de l'attribut.
     */
    @Override
    public void setY(String y) {
        switch (y) {
            case "attack" -> this.y = attack;
            case "base_egg_steps" -> this.y = base_egg_steps;
            case "defense" -> this.y = defense;
            case "hp" -> this.y = hp;
            case "sp_attack" -> this.y = sp_attack;
            case "sp_defense" -> this.y = sp_defense;
            case "speed" -> this.y = speed;
            case "experience_growth" -> this.y = experience_growth;
            case "capture_rate" -> this.y = capture_rate;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return attack == pokemon.attack &&
                base_egg_steps == pokemon.base_egg_steps &&
                Double.compare(pokemon.capture_rate, capture_rate) == 0 &&
                defense == pokemon.defense &&
                experience_growth == pokemon.experience_growth &&
                hp == pokemon.hp &&
                sp_attack == pokemon.sp_attack &&
                sp_defense == pokemon.sp_defense &&
                Double.compare(pokemon.speed, speed) == 0 &&
                is_legendary == pokemon.is_legendary &&
                Objects.equals(name, pokemon.name) &&
                Objects.equals(type1String, pokemon.type1String) &&
                Objects.equals(type2String, pokemon.type2String) &&
                type1 == pokemon.type1 &&
                type2 == pokemon.type2;
    }



}
