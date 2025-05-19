package ma.noteapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ma.noteapp.R;
import ma.noteapp.ViewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {


    private UserViewModel userViewModel;
    private EditText editTextEmail, editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewRegister = findViewById(R.id.textViewRegister);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonLogin.setOnClickListener(v -> loginUser());
        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        setupObservers();
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        userViewModel.login(email, password);
    }

    private void setupObservers() {
        userViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                // Redirection vers MainActivity avec l'ID utilisateur
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ID", user.id);
                startActivity(intent);

                // Fermeture de LoginActivity pour éviter de revenir en arrière
                finish();
            }
        });
    }

}
