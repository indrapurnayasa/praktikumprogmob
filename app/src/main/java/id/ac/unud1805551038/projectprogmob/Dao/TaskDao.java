package id.ac.unud1805551038.projectprogmob.Dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import id.ac.unud1805551038.projectprogmob.Models.Task;

@Dao
public interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY ID desc")
    List<Task> loadAllPosts();

    @Insert
    void insetTask(Task task);

    @Query("DELETE FROM tasks")
    void deleteAll();
}
