package it.rizzoli.eattogether.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.database.DatabaseHelper;
import it.rizzoli.eattogether.database.UserDbAdapter;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    Button signup;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).hide();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        DatabaseHelper dbHelper =  new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        signup.setOnClickListener(View -> {
            if (isFill()) {
                if(!passMatch()) {
                    Toast toast = Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
                    String[] selectionArgs = new String[] { username.getText().toString(), password.getText().toString() };
                    Cursor cursor = db.rawQuery(sql, selectionArgs);

                    if (cursor.moveToFirst()) {
                        Toast toast = Toast.makeText(this, "User with username = [" + username.getText().toString() +
                                "] and password = [" + password.getText().toString() + "] already exists", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    cursor.close();
                }
            }
            else {
                Toast toast = Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_LONG);
                toast.show();
            }

        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private Boolean passMatch() {
        return (password.getText().toString().equals(passwordConfirm.getText().toString()));
    }


    private Boolean isFill() {
        return !(username.getText().toString().equals("")
                || password.getText().toString().equals("")
                || passwordConfirm.getText().toString().equals(""));
    }
}