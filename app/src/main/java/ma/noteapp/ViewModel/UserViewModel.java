package ma.noteapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ma.noteapp.entities.User;
import ma.noteapp.dao.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>(); // Nouveau

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void register(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Tous les champs sont obligatoires");
            registrationSuccess.setValue(false); // Échec de l'enregistrement
            return;
        }
        // TODO: Ajouter une validation plus poussée pour l'email et le mot de passe ici

        User user = new User(name, email, password); // Stockage en clair - NON RECOMMANDÉ pour la production
        userRepository.register(user, new UserRepository.RegistrationCallback() {
            @Override
            public void onSuccess() {
                errorMessage.postValue(null); // Pas d'erreur
                registrationSuccess.postValue(true); // Succès
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
                registrationSuccess.postValue(false); // Échec
            }
        });
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()){
            errorMessage.setValue("Email et mot de passe sont obligatoires.");
            return;
        }
        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                currentUser.postValue(user);
                errorMessage.postValue(null);
            }

            @Override
            public void onError(String error) {
                currentUser.postValue(null); // Assurer que currentUser est null en cas d'erreur de login
                errorMessage.postValue(error);
            }
        });
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() { // Optionnel
        errorMessage.setValue(null);
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getRegistrationSuccess() { // Nouveau getter
        return registrationSuccess;
    }

    public void consumeRegistrationSuccess() { // Pour réinitialiser après observation
        registrationSuccess.setValue(null);
    }

}
