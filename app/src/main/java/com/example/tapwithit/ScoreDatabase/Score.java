package com.example.tapwithit.ScoreDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "score")
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username, mode;
    private float score;

    public Score() {}

    public String getUsername() {
        return this.username;
    }

    public float getScore() {
        return this.score;
    }

    public int getId() {
        return this.id;
    }

    public String getMode() {
        return this.mode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
