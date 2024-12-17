package it.rizzoli.eattogether.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class UserDbAdapter {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = UserDbAdapter.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    // Database fields
    private static final String DATABASE_TABLE = "User";
    public static final String KEY_USERID = "_id";
    public static final String KEY_NAME = "nome";
    public static final String KEY_SURNAME = "cognome";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public UserDbAdapter(Context context) {
        this.context = context;
    }

    public UserDbAdapter open() throws SQLException {

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        return values;
    }

    public long createUser(String username, String password) {
        ContentValues initialValues = createContentValues(username, password);

        if (database == null || !database.isOpen()) {
            throw new IllegalStateException("Database is not open. Call open() before using createUser.");
        }

        return database.insertOrThrow("User", null, initialValues);
    }

    public Boolean existUser(String username) {
        String selection = "username = ?";

        String[] selectionArgs = { username };
        Cursor response = database.query(
                "user",
                new String[] { "username" },
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return (response != null && response.moveToFirst());
    }

    public Boolean existUser(String username, String password) {
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { username, password };

        Cursor response = database.query(
                "User",
                new String[] { "username" },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = (response != null && response.moveToFirst());
        if (response != null) {
            response.close();
        }

        return exists;
    }

}
 /*
        *
        *
        * //update a contact
        public boolean updateContact( long contactID, String name, String surname, String sex,
        String birth_date ) {
        ContentValues updateValues = createContentValues(name, surname, sex, birth_date);
        return database.update(DATABASE_TABLE, updateValues, KEY_CONTACTID + "=" + contactID,
        null) > 0;
        }

        Dott. Antonio Giovanni Lezzi

        //delete a contact
        public boolean deleteContact(long contactID) {
        return database.delete(DATABASE_TABLE, KEY_CONTACTID + "=" + contactID, null) > 0;
        }
        //fetch all contacts
        public Cursor fetchAllContacts() {
        return database.query(DATABASE_TABLE, new String[] { KEY_CONTACTID, KEY_SURNAME, KEY_SEX,
        KEY_BIRTH_DATE}, null, null, null, null, null);
        }
        //fetch contacts filter by a string
        public Cursor fetchContactsByFilter(String filter) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {

        KEY_CONTACTID, KEY_NAME, KEY_SURNAME, KEY_SEX,

        KEY_BIRTH_DATE },

        KEY_NAME + " like '%"+ filter + “%'", null, null, null,

        null, null);
        return mCursor;
        }
        *
        */