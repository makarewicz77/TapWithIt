package com.example.tapwithit.ScoreDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.NonNull;

@Database(entities = {Score.class}, version = 1, exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {

    public abstract ScoreDao scoreDao();
    private static volatile ScoreDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ScoreDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ScoreDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ScoreDatabase.class, "score_db")
                            .addCallback(RoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback RoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                ScoreDao dao = INSTANCE.scoreDao();
                dao.deleteAll();
            });
        }
    };
}
