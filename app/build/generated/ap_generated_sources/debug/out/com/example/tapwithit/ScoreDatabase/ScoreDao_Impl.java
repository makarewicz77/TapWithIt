package com.example.tapwithit.ScoreDatabase;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScoreDao_Impl implements ScoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Score> __insertionAdapterOfScore;

  private final EntityDeletionOrUpdateAdapter<Score> __deletionAdapterOfScore;

  private final EntityDeletionOrUpdateAdapter<Score> __updateAdapterOfScore;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ScoreDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScore = new EntityInsertionAdapter<Score>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `score` (`id`,`username`,`mode`,`score`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Score value) {
        stmt.bindLong(1, value.getId());
        if (value.getUsername() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUsername());
        }
        if (value.getMode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMode());
        }
        stmt.bindDouble(4, value.getScore());
      }
    };
    this.__deletionAdapterOfScore = new EntityDeletionOrUpdateAdapter<Score>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `score` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Score value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfScore = new EntityDeletionOrUpdateAdapter<Score>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `score` SET `id` = ?,`username` = ?,`mode` = ?,`score` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Score value) {
        stmt.bindLong(1, value.getId());
        if (value.getUsername() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUsername());
        }
        if (value.getMode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMode());
        }
        stmt.bindDouble(4, value.getScore());
        stmt.bindLong(5, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Score";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Score score) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfScore.insert(score);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Score score) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfScore.handle(score);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Score score) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfScore.handle(score);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Score>> findAll() {
    final String _sql = "SELECT * FROM Score ORDER BY score";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Score"}, false, new Callable<List<Score>>() {
      @Override
      public List<Score> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfMode = CursorUtil.getColumnIndexOrThrow(_cursor, "mode");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final List<Score> _result = new ArrayList<Score>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Score _item;
            _item = new Score();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            _item.setUsername(_tmpUsername);
            final String _tmpMode;
            _tmpMode = _cursor.getString(_cursorIndexOfMode);
            _item.setMode(_tmpMode);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            _item.setScore(_tmpScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<Score> findScoreWithUsername(final String username) {
    final String _sql = "SELECT * FROM Score WHERE username LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfMode = CursorUtil.getColumnIndexOrThrow(_cursor, "mode");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final List<Score> _result = new ArrayList<Score>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Score _item;
        _item = new Score();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpMode;
        _tmpMode = _cursor.getString(_cursorIndexOfMode);
        _item.setMode(_tmpMode);
        final float _tmpScore;
        _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
        _item.setScore(_tmpScore);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
