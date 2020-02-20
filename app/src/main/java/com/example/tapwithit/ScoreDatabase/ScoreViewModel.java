package com.example.tapwithit.ScoreDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {

    private ScoreRepository scoreRepository;
    private LiveData<List<Score>> scores;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        scoreRepository = new ScoreRepository(application);
        scores = scoreRepository.findAllScores();
    }

    public LiveData<List<Score>> findAll() {
        return scores;
    }

    public void insert(Score score) {
        scoreRepository.insert(score);
    }

    public void update(Score score) {
        scoreRepository.update(score);
    }

    public void delete(Score score) {
        scoreRepository.delete(score);
    }

    public List<Score> findUserWithUsername(String username) {
        return scoreRepository.findScoreWithUsername(username);
    }
}
