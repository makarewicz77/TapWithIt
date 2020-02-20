package com.example.tapwithit.ScoreDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tapwithit.ScoreDatabase.ScoreDatabase;
import com.example.tapwithit.ScoreDatabase.Score;
import com.example.tapwithit.ScoreDatabase.ScoreDao;

import java.util.List;

public class ScoreRepository {
    private ScoreDao scoreDao;
    private LiveData<List<Score>> scores;

    ScoreRepository(Application application) {
        ScoreDatabase database = ScoreDatabase.getDatabase(application);
        scoreDao = database.scoreDao();
        scores = scoreDao.findAll();
    }

    LiveData<List<Score>> findAllScores() {
        return scores;
    }

    void insert(Score score) {
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.insert(score);
        });
    }

    void update(Score score) {
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.update(score);
        });
    }

    void delete(Score score) {
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.delete(score);
        });
    }

    List<Score> findScoreWithUsername(String username) {
        return scoreDao.findScoreWithUsername(username);
    }
}
