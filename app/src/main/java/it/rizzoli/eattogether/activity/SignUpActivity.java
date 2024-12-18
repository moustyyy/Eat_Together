package it.rizzoli.eattogether.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.database.UserDbAdapter;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private UserDbAdapter db;
    private TextView errorUser;
    private TextView errorFill;
    private TextView errorPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new UserDbAdapter(this);

        Button login = findViewById(R.id.signin);
        Button signup = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        /*
        errorUser = findViewById(R.id.errorUser);
        errorFill = findViewById(R.id.errorFill);
        errorPass = findViewById(R.id.errorPass);
        errorFill.setVisibility(View.GONE);
        errorUser.setVisibility(View.GONE);
        errorPass.setVisibility(View.GONE);
        */

        signup.setOnClickListener(View -> {
            if (isFill()) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else {
                Toast toast = Toast.makeText(this, "Tutti i campi devono essere compilati", Toast.LENGTH_LONG);
                toast.show();
            }
            /*
            long checkCreateUser=-1;
            if (isFill()) {
                if (isPass()) {
                    if (!checkUser()) {
                        try {
                            db.open();
                            checkCreateUser = db.createUser(username.getText().toString(), password.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            db.close();
                        }
                        if (checkCreateUser != -1){
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }
                    } else {
                        errorUser.setVisibility(android.view.View.VISIBLE);
                        errorPass.setVisibility(android.view.View.GONE);
                        errorFill.setVisibility(android.view.View.GONE);
                    }
                } else {
                    errorPass.setVisibility(android.view.View.VISIBLE);
                }
            } else {
                errorFill.setVisibility(android.view.View.VISIBLE);
            }
            */

        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private Boolean checkUser() {
        Boolean response = false;
        try {
            db.open();
            response = db.existUser(username.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return response;
    }

    private Boolean isPass() {
        return (password.getText().toString().equals(passwordConfirm.getText().toString())
                && !password.getText().toString().equals("")
                && !passwordConfirm.getText().toString().equals(""));
    }


    private Boolean isFill() {
        return !(username.getText().toString().equals("")
                || password.getText().toString().equals("")
                || passwordConfirm.getText().toString().equals(""));
    }
}