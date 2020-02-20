package com.example.tapwithit.UserDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int user_id;
    private String username;
    private String password;

    /*public User(String username, String password) {
        this.username = username;
        this.password = password;
    }*/

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public void setPassword(String password) { this.password = password; }

    public void setUsername(String username) { this.username = username; }

    public int getUser_id() { return user_id; }

    public String getPassword() { return password; }

    public String getUsername() { return username; }
}
