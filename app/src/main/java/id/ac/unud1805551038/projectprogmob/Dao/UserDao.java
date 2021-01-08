package id.ac.unud1805551038.projectprogmob.Dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.Models.User;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM users ORDER BY idNya desc")
    List<User> loadAllUsers();

    @Insert
    void insertUser(User user);

    @Query("DELETE FROM users")
    void deleteAll();
}
