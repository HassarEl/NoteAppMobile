package ma.noteapp.dao;


import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ma.noteapp.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password) throws SQLiteConstraintException;
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId LIMIT 1)")
    boolean userExists(int userId);
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User checkEmailExists(String email);
}
