package it.rizzoli.eattogether.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.UserDbAdapter;
import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {

    private UserDbAdapter dbUser;
    EditText password;
    EditText username;
    Button login;
    TextView signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        File databaseFile = getDatabasePath("mydatabase.db");
        SQLiteDatabase.deleteDatabase(databaseFile);

        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        DatabaseHelper dbHelper =  new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbUser = new UserDbAdapter(this);


        login.setOnClickListener(view -> {
            if (isFill()) {
                String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
                String[] selectionArgs = new String[] { username.getText().toString(), password.getText().toString() };
                Cursor cursor = db.rawQuery(sql, selectionArgs);

                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "User with username = [" + username.getText().toString() +
                            "] and password = [" + password.getText().toString() + "] does not exists", Toast.LENGTH_LONG);
                    toast.show();
                }
                cursor.close();
            } else {
                Toast toast = Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

    }

    private Boolean isFill() {
        return !(username.getText().toString().equals("")
                || password.getText().toString().equals(""));
    }
}

