package id.ac.unud1805551038.projectprogmob.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.ac.unud1805551038.projectprogmob.Dao.TaskDao;
import id.ac.unud1805551038.projectprogmob.Dao.UserDao;
import id.ac.unud1805551038.projectprogmob.Models.Task;
import id.ac.unud1805551038.projectprogmob.Models.User;

@Database(entities = {Task.class, User.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static id.ac.unud1805551038.projectprogmob.Database.RoomDB database;
    private static String DATABASE_NAME = "database";

    public synchronized static id.ac.unud1805551038.projectprogmob.Database.RoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), id.ac.unud1805551038.projectprogmob.Database.RoomDB.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return database;
    }
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();
}
