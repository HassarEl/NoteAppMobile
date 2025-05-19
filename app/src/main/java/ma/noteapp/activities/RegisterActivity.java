package ma.noteapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ma.noteapp.R;
import ma.noteapp.ViewModel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private EditText editTextName, editTextEmail, editTextPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonRegister.setOnClickListener(v -> registerUser());

        setupObservers();
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        userViewModel.register(name, email, password);
    }

    private void setupObservers() {
        userViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) { // Vérifier si l'erreur n'est pas vide
                Toast.makeText(this, error, Toast.LENGTH_LONG).show(); // Durée plus longue pour l'erreur
                userViewModel.clearErrorMessage(); // Optionnel: pour éviter de réafficher le même toast
            }
        });
        userViewModel.getRegistrationSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Inscription réussie ! Veuillez vous connecter.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Nettoie la pile d'activités
                startActivity(intent);
                finish(); // Termine RegisterActivity
                userViewModel.consumeRegistrationSuccess(); // Réinitialiser l'état
            }
        });
        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }


}
