package com.example.tapwithit.UserDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> users;

    UserRepository(Application application) {
        UserDatabase database = UserDatabase.getDatabase(application);
        userDao = database.userDao();
        users = userDao.findAll();
    }

    LiveData<List<User>> findAllUsers() {
        return users;
    }

    void insert(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }

    void update(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
        });
    }

    void delete(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.delete(user);
        });
    }
}
