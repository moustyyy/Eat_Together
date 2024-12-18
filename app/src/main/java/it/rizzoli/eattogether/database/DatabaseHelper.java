package it.rizzoli.eattogether.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE Users (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL);";

    private static final String CREATE_USER_USER_TABLE =
            "CREATE TABLE User_User (" +
                    "idUser1 INTEGER NOT NULL, " +
                    "idUser2 INTEGER NOT NULL, " +
                    "PRIMARY KEY (idUser1, idUser2), " +
                    "FOREIGN KEY (idUser1) REFERENCES Users(id), " +
                    "FOREIGN KEY (idUser2) REFERENCES Users(id));";

    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE Events (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idUserCreator INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "data DATE NOT NULL, " +
                    "ora TIME NOT NULL, " +
                    "indirizzo TEXT, " +
                    "citta TEXT, " +
                    "descrizione TEXT, " +
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

    private static final String CREATE_FOOD_BOX_TABLE =
            "CREATE TABLE Food_Boxes (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idEvent INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "descrizione TEXT NOT NULL," +
                    "FOREIGN KEY (idEvent) REFERENCES Events(id));";

    private static final String CREATE_FOOD_FOODBOX_TABLE =
            "CREATE TABLE Food_FoodBox (" +
                    "idFoodBox INTEGER NOT NULL, " +
                    "idFood INTEGER NOT NULL, " +
                    "idUserAdder INTEGER NOT NULL, " +
                    "PRIMARY KEY (idFoodBox, idFood,idUserAdder), " +
                    "FOREIGN KEY (idFoodBox) REFERENCES Food_Boxes(id), " +
                    "FOREIGN KEY (idFood) REFERENCES Foods(id), " +
                    "FOREIGN KEY (idUserAdder) REFERENCES Users(id));";

    private static final String CREATE_CUSTOM_FOOD_TABLE =
            "CREATE TABLE Custom_Foods (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFoodBox INTEGER NOT NULL, " +
                    "idUserAdder INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "descrizione TEXT, " +
                    "FOREIGN KEY (idFoodBox) REFERENCES Food_Boxes(id), " +
                    "FOREIGN KEY (idUserAdder) REFERENCES Users(id));";

    private static final String CREATE_FOOD_DETAIL_TABLE =
            "CREATE TABLE Food_Details (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFood INTEGER NOT NULL, " +
                    "carboidrati REAL, " +
                    "proteine REAL, " +
                    "grassi REAL, " +
                    "FOREIGN KEY (idFood) REFERENCES Foods(id));";

    private static final String CREATE_COMPLEX_FOOD_TABLE =
            "CREATE TABLE Complex_Foods (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL);";

    private static final String CREATE_COMPLEXFOOD_FOODBOX_TABLE =
            "CREATE TABLE ComplexFood_FoodBox (" +
                    "idFoodBox INTEGER NOT NULL, " +
                    "idComplexFood INTEGER NOT NULL, " +
                    "idUserAdder INTEGER NOT NULL, " +
                    "PRIMARY KEY (idFoodBox, idComplexFood, idUserAdder), " +
                    "FOREIGN KEY (idFoodBox) REFERENCES Food_Boxex(id), " +
                    "FOREIGN KEY (idComplexFood) REFERENCES Complex_Foods(id), " +
                    "FOREIGN KEY (idUserAdder) REFERENCES Users(id));";

    private static final String CREATE_COMPLEXFOOD_FOOD_TABLE =
            "CREATE TABLE ComplexFood_Food (" +
                    "idFood INTEGER NOT NULL, " +
                    "idComplexFood INTEGER NOT NULL, " +
                    "PRIMARY KEY (idFood, idComplexFood), " +
                    "FOREIGN KEY (idFood) REFERENCES Foods(id), " +
                    "FOREIGN KEY (idComplexFood) REFERENCES Complex_Foods(id));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE", "onCreate chiamato");

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_USER_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_FOOD_BOX_TABLE);
        db.execSQL(CREATE_FOOD_FOODBOX_TABLE);
        db.execSQL(CREATE_CUSTOM_FOOD_TABLE);
        db.execSQL(CREATE_FOOD_DETAIL_TABLE);
        db.execSQL(CREATE_COMPLEX_FOOD_TABLE);
        db.execSQL(CREATE_COMPLEXFOOD_FOODBOX_TABLE);
        db.execSQL(CREATE_COMPLEXFOOD_FOOD_TABLE);

        String INSERT_USER = "INSERT INTO Users (username, password) VALUES (?, ?);";
        db.execSQL(INSERT_USER,  new Object[]{"admin", "admin"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
