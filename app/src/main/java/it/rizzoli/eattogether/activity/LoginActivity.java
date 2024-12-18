package it.rizzoli.eattogether.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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
                Intent intent = new Intent(LoginActivity.this, HomeFragment.class);
                startActivity(intent);
            }
            else {
                Toast toast = Toast.makeText(this, "Tutti i campi devono essere compilati", Toast.LENGTH_LONG);
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

