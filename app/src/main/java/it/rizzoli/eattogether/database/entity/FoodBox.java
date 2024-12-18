package it.rizzoli.eattogether.database.entity;

public class FoodBox {
    private int id; // Corrisponde a _id
    private int idUserAdder;
    private int idEvent;
    private String nome;
    private String descrizione;

    // Costruttore vuoto
    public FoodBox() {
    }

    // Costruttore completo
    public FoodBox(int id, int idUserAdder, int idEvent, String nome, String descrizione) {
        this.id = id;
        this.idUserAdder = idUserAdder;
        this.idEvent = idEvent;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUserAdder() {
        return idUserAdder;
    }

    public void setIdUserAdder(int idUserAdder) {
        this.idUserAdder = idUserAdder;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}

