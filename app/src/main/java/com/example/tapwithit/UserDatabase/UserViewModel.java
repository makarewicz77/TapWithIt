package com.example.tapwithit.UserDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tapwithit.UserDatabase.UserRepository;
import com.example.tapwithit.UserDatabase.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> users;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        users = userRepository.findAllUsers();
    }

    public LiveData<List<User>> findAll() {
        return users;
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
