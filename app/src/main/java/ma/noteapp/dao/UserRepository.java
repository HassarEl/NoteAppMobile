package ma.noteapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ma.noteapp.entities.User;

public class UserRepository {
    private UserDao userDao;

    private Executor executor;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void register(User user, RegistrationCallback callback) {
        executor.execute(() -> {
            try {
                // Vérifier si l'email existe déjà
                if (userDao.checkEmailExists(user.email) != null) {
                    callback.onError("Email déjà utilisé");
                    return;
                }

                userDao.insert(user);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void login(String email, String password, LoginCallback callback) {
        executor.execute(() -> {
            try {
                User user = userDao.login(email, password);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Identifiants invalides");
                }
            } catch (SQLiteConstraintException e) {
                callback.onError("Database error: " + e.getMessage());
            }
        });
    }

    public interface RegistrationCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String error);
    }

}
