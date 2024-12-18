package it.rizzoli.eattogether.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.rizzoli.eattogether.database.entity.Event;
import it.rizzoli.eattogether.database.entity.FoodBox;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE Users (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL);";

    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE Events (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idUserCreator INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "data DATE, " +
                    "ora TIME, " +
                    "indirizzo TEXT, " +
                    "citta TEXT, " +
                    "descrizione TEXT, " +
                    "image BLOB, " +
                    "FOREIGN KEY (idUserCreator) REFERENCES Users(id));";

    private static final String CREATE_EVENT_USER_TABLE =
            "CREATE TABLE Event_User (" +
                    "idEvent INTEGER NOT NULL, " +
                    "idUser INTEGER NOT NULL, " +
                    "PRIMARY KEY (idEvent, idUser), " +
                    "FOREIGN KEY (idEvent) REFERENCES Events(id), " +
                    "FOREIGN KEY (idUser) REFERENCES Users(id));";

    private static final String CREATE_FOOD_TABLE =
            "CREATE TABLE Foods (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL);";

    private static final String CREATE_FOODBOX_TABLE =
            "CREATE TABLE Food_Boxes (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idUserAdder INTEGER NOT NULL, " +
                    "idEvent INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "descrizione TEXT NOT NULL," +
                    "FOREIGN KEY (idEvent) REFERENCES Events(id)," +
                    "FOREIGN KEY (idUserAdder) REFERENCES Users(id));";

    private static final String CREATE_FOOD_FOODBOX_TABLE =
            "CREATE TABLE Food_FoodBox (" +
                    "idFoodBox INTEGER NOT NULL, " +
                    "idFood INTEGER NOT NULL, " +
                    "PRIMARY KEY (idFoodBox, idFood), " +
                    "FOREIGN KEY (idFoodBox) REFERENCES Food_Boxes(id), " +
                    "FOREIGN KEY (idFood) REFERENCES Foods(id));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Event> getEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> eventNames = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE idUserCreator = ?";
        String[] selectionArgs = new String[]{"1"};
        Cursor c = db.rawQuery(sql, selectionArgs);

        if (c != null && c.moveToFirst()) {
            do {
                events.add(Event.fromCursor(c));
            } while (c.moveToNext());
            c.close();
        }

        return events;
    }

    public Event getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("events", null, "_id = ?", new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Event event = Event.fromCursor(cursor);
            cursor.close();
            return event;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE", "onCreate chiamato");

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_FOODBOX_TABLE);
        db.execSQL(CREATE_FOOD_FOODBOX_TABLE);

        String INSERT_USER = "INSERT INTO Users (username, password) VALUES (?, ?);";
        db.execSQL(INSERT_USER, new Object[]{"admin", "admin"});

        String INSERT_EVENT_1 = "INSERT INTO Events (idUserCreator, nome, data, ora, indirizzo, citta, descrizione) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(INSERT_EVENT_1, new Object[]{"1", "nome_evento_1", "2024-12-20", "18:00:00", "Via Roma 123",
                "Milano", "Descrizione dell'evento_1"});
        String INSERT_EVENT_2 = "INSERT INTO Events (idUserCreator, nome, data, ora, indirizzo, citta, descrizione) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(INSERT_EVENT_2, new Object[]{"1", "nome_evento_2", "2024-12-22", "20:00:00", "Via Roma 123",
                "Milano", "Descrizione dell'evento_2"});
        String INSERT_EVENT_3 = "INSERT INTO Events (idUserCreator, nome, data, ora, indirizzo, citta, descrizione) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(INSERT_EVENT_3, new Object[]{"1", "nome_evento_3", "2024-12-25", "21:00:00", "Via Roma 123",
                "Milano", "Descrizione dell'evento_3"});

        String insertFood1 = "INSERT INTO Foods (nome) VALUES ('Pizza');";
        String insertFood2 = "INSERT INTO Foods (nome) VALUES ('Pasta');";
        String insertFood3 = "INSERT INTO Foods (nome) VALUES ('Salad');";

        db.execSQL(insertFood1);
        db.execSQL(insertFood2);
        db.execSQL(insertFood3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public List<FoodBox> getFoodBoxesForEvent(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<FoodBox> foodBoxes = new ArrayList<>();

        String sql = "SELECT * FROM Food_Boxes WHERE idEvent = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(eventId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                FoodBox foodBox = new FoodBox(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("idUserAdder")),
                        cursor.getInt(cursor.getColumnIndex("idEvent")),
                        cursor.getString(cursor.getColumnIndex("nome")),
                        cursor.getString(cursor.getColumnIndex("descrizione")));
                foodBoxes.add(foodBox);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return foodBoxes;
    }

    public List<String> getFoodItemsForFoodBox(int foodBoxId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> foodItems = new ArrayList<>();

        String sql = "SELECT Foods.nome FROM Foods " +
                "INNER JOIN Food_FoodBox ON Foods._id = Food_FoodBox.idFood " +
                "WHERE Food_FoodBox.idFoodBox = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(foodBoxId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("nome"));
                foodItems.add(foodName);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return foodItems;
    }

    public void insertSampleFoodItemsForEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insertFoodBox = "INSERT INTO Food_Boxes (idEvent, idUserAdder, nome, descrizione) VALUES (?, ?, ?, ?);";
        db.execSQL(insertFoodBox, new Object[]{eventId, 1, "Box 1", "Food box for event " + eventId});

    }

    public List<String> getFoodNamesByFoodBox(int foodBoxId) {
        List<String> foodItems = getFoodItemsForFoodBox(foodBoxId);
        List<String> foodItemsToShow = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < foodItems.size(); i++) {
            placeholders.append("?");
            if (i < foodItems.size() - 1) {
                placeholders.append(", ");
            }
        }

        System.out.println("placeholder" + placeholders);

        String sql = "SELECT * FROM Foods WHERE nome NOT IN (" + placeholders + ")";

        Cursor cursor = db.rawQuery(sql, foodItems.toArray(new String[0]));

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("nome"));
                foodItemsToShow.add(foodName);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return foodItemsToShow;
    }

    @SuppressLint("Range")
    public int getFoodIdByFoodName(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Foods WHERE nome = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {foodName});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                return cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        };
        return -1;
    }

    public boolean addFoodToFoodBox(int foodBoxId, String foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertFoodFoodBox = "INSERT INTO Food_FoodBox (idFoodBox, idFood) VALUES (?, ?);";
        db.execSQL(insertFoodFoodBox, new Object[]{foodBoxId, foodId});

        return true;
    }
}
