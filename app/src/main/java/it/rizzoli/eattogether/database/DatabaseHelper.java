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

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_FOODBOX_TABLE);
        db.execSQL(CREATE_FOOD_FOODBOX_TABLE);

        String[][] users = {
                {"admin", "admin"},
                {"guest", "guest"}
        };

        String insertUserQuery = "INSERT INTO Users (username, password) VALUES (?, ?);";

        for (String[] user : users) {
            db.execSQL(insertUserQuery, user);
        }

        String[][] events = {
                {"1", "Cena di Natale", "2024-12-20", "20:00:00", "Piazza del Duomo 1", "Milano", "Un incontro conviviale per celebrare il Natale con amici e famiglia."},
                {"1", "Festa di Capodanno", "2024-12-31", "22:00:00", "Viale dei Mille 10", "Roma", "Un'esplosiva festa per dare il benvenuto al nuovo anno!"},
                {"2", "Picnic al Parco", "2025-05-10", "12:00:00", "Parco Sempione", "Milano", "Una giornata di relax all'aria aperta, con picnic e giochi all'aperto."},
                {"2", "Concerto d'Estate", "2025-07-15", "21:00:00", "Arena di Verona", "Verona", "Un'esperienza musicale indimenticabile sotto le stelle."},
                {"2", "Festival del Cinema", "2025-09-10", "18:30:00", "Teatro La Fenice", "Venezia", "Proiezioni esclusive e incontri con celebrità del mondo del cinema."}
        };

        String INSERT_EVENT = "INSERT INTO Events (idUserCreator, nome, data, ora, indirizzo, citta, descrizione) VALUES (?, ?, ?, ?, ?, ?, ?)";
        for (String[] event : events) {
            db.execSQL(INSERT_EVENT, event);
        }

        String[] foodNames = {
                "Pizza", "Pasta", "Salad", "Burger", "Sushi", "Tacos",
                "Spaghetti", "Ice Cream", "Steak", "Fries", "Sandwich",
                "Soup", "Risotto", "Lasagna", "Paella"
        };

        for (String food : foodNames) {
            String query = "INSERT INTO Foods (nome) VALUES ('" + food + "');";
            db.execSQL(query);
        }

        String[][] foodBoxes = {
                {"1", "1", "Box 1", "Food box for event " + 1},
                {"2", "1", "Box 2", "Food box for event " + 1},
                {"1", "2", "Box 3", "Food box for event " + 2},
                {"2", "3", "Box 4", "Food box for event " + 3},
                {"2", "4", "Box 5", "Food box for event " + 4},
                {"2", "5", "Box 6", "Food box for event " + 5},
                {"2", "5", "Box 7", "Food box for event " + 5}

        };

        String insertFoodBox = "INSERT INTO Food_Boxes (idUserAdder, idEvent, nome, descrizione) VALUES (?, ?, ?, ?);";

        for (String[] foodBox : foodBoxes) {
            db.execSQL(insertFoodBox, foodBox);
        }

        String[][] eventUsers = {
                {"3", "1"},
                {"1", "2"},
                {"2", "2"}
        };

        String insertEventUserQuery = "INSERT INTO Event_User (idEvent, idUser) VALUES (?, ?);";

        for (String[] eventUser : eventUsers) {
            db.execSQL(insertEventUserQuery, eventUser);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @SuppressLint("Range")
    public List<Event> getEvents(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE idUserCreator = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        Cursor c = db.rawQuery(sql, selectionArgs);

        if (c != null && c.moveToFirst()) {
            do {
                Event event = Event.fromCursor(c);
                event.setImg(c.getBlob(c.getColumnIndex("image")));
                event.setRole("Organizer");
                events.add(event);
            } while (c.moveToNext());
            c.close();
        }

        sql = "SELECT Events.* FROM Events " +
                "INNER JOIN Event_User ON Events._id = Event_User.idEvent " +
                "INNER JOIN Users ON Event_User.idUser = Users._id " +
                "WHERE Users._id = ?";

        c = db.rawQuery(sql, new String[]{ "1" });

        if (c != null && c.moveToFirst()) {
            do {
                Event event = Event.fromCursor(c);
                event.setImg(c.getBlob(c.getColumnIndex("image")));
                event.setRole("Guest");
                events.add(event);
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

    public boolean checkInvitation(Event event, int userId) {
        boolean check = false;
        for (Event e : getEvents(userId)) {
            if(e.getId() == event.getId()) {
                check = true;
            }
        }
        return check;
    }

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
