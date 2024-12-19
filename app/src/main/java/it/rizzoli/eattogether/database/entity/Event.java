package it.rizzoli.eattogether.database.entity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    private int id;
    private int idUserCreator;
    private String nome;
    private String data;
    private String ora;
    private String indirizzo;
    private String citta;
    private String descrizione;
    private byte[] img;

    public Event() {
    }

    public Event(int idUserCreator, String nome, String data, String ora, String indirizzo, String citta, String descrizione, byte[] img) {
        this.idUserCreator = idUserCreator;
        this.nome = nome;
        this.data = data;
        this.ora = ora;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.descrizione = descrizione;
        this.img = img;
    }

    public Event(int idUserCreator, String nome, String data, String ora, String indirizzo, String citta, String descrizione) {
        this.idUserCreator = idUserCreator;
        this.nome = nome;
        this.data = data;
        this.ora = ora;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.descrizione = descrizione;
        this.img = null;
    }

    // Getters e setters per ciascun campo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUserCreator() {
        return idUserCreator;
    }

    public void setIdUserCreator(int idUserCreator) {
        this.idUserCreator = idUserCreator;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean hasImg() {
        return this.img != null;
    }
    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public ContentValues toContentValuesNoImg() {
        ContentValues values = new ContentValues();
        values.put("idUserCreator", idUserCreator);
        values.put("nome", nome);
        values.put("data", data);
        values.put("ora", ora);
        values.put("indirizzo", indirizzo);
        values.put("citta", citta);
        values.put("descrizione", descrizione);
        return values;
    }

    public ContentValues toContentValuesWithImg() {
        ContentValues values = new ContentValues();
        values.put("idUserCreator", idUserCreator);
        values.put("nome", nome);
        values.put("data", data);
        values.put("ora", ora);
        values.put("indirizzo", indirizzo);
        values.put("citta", citta);
        values.put("descrizione", descrizione);
        values.put("image", img);
        return values;
    }

    @SuppressLint("Range")
    public static Event fromCursor(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        event.setIdUserCreator(cursor.getInt(cursor.getColumnIndex("idUserCreator")));
        event.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        event.setData(cursor.getString(cursor.getColumnIndex("data")));
        event.setOra(cursor.getString(cursor.getColumnIndex("ora")));
        event.setIndirizzo(cursor.getString(cursor.getColumnIndex("indirizzo")));
        event.setCitta(cursor.getString(cursor.getColumnIndex("citta")));
        event.setDescrizione(cursor.getString(cursor.getColumnIndex("descrizione")));
        return event;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        idUserCreator = in.readInt();
        nome = in.readString();
        data = in.readString();
        ora = in.readString();
        indirizzo = in.readString();
        citta = in.readString();
        descrizione = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idUserCreator);
        dest.writeString(nome);
        dest.writeString(data);
        dest.writeString(ora);
        dest.writeString(indirizzo);
        dest.writeString(citta);
        dest.writeString(descrizione);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

