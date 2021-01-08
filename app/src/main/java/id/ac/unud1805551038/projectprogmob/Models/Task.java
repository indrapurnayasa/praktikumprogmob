package id.ac.unud1805551038.projectprogmob.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int id;

    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "name_class")
    private String name_class;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "meeting")
    private String meeting;

    @ColumnInfo(name = "created_at")
    private String created_at;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_class() {
        return name_class;
    }

    public void setName_class(String name_class) {
        this.name_class = name_class;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
