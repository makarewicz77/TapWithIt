package com.example.tapwithit.ScoreDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tapwithit.ScoreDatabase.Score;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Score score);

    @Update
    void update(Score score);

    @Delete
    void delete(Score score);

    @Query("DELETE FROM Score")
    void deleteAll();

    @Query("SELECT * FROM Score ORDER BY score")
    LiveData<List<Score>> findAll();

    @Query("SELECT * FROM Score WHERE username LIKE :username")
    List<Score> findScoreWithUsername(String username);

}
