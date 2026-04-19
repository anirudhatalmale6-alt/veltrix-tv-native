package com.veltrix.tv.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WatchHistoryDao_Impl implements WatchHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WatchHistoryEntity> __insertionAdapterOfWatchHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdateProgress;

  public WatchHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWatchHistoryEntity = new EntityInsertionAdapter<WatchHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `watch_history` (`id`,`streamId`,`name`,`icon`,`type`,`categoryId`,`containerExtension`,`seriesId`,`seasonNumber`,`episodeNumber`,`episodeTitle`,`watchedAt`,`positionMs`,`durationMs`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WatchHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStreamId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getIcon());
        }
        if (entity.getType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getType());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCategoryId());
        }
        if (entity.getContainerExtension() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getContainerExtension());
        }
        if (entity.getSeriesId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getSeriesId());
        }
        if (entity.getSeasonNumber() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSeasonNumber());
        }
        if (entity.getEpisodeNumber() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getEpisodeNumber());
        }
        if (entity.getEpisodeTitle() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEpisodeTitle());
        }
        statement.bindLong(12, entity.getWatchedAt());
        statement.bindLong(13, entity.getPositionMs());
        statement.bindLong(14, entity.getDurationMs());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM watch_history WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM watch_history";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE watch_history SET positionMs = ?, durationMs = ?, watchedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final WatchHistoryEntity entry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWatchHistoryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgress(final long id, final long positionMs, final long durationMs,
      final long watchedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, positionMs);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, durationMs);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, watchedAt);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WatchHistoryEntity>> getRecentHistory() {
    final String _sql = "SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"watch_history"}, new Callable<List<WatchHistoryEntity>>() {
      @Override
      @NonNull
      public List<WatchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final List<WatchHistoryEntity> _result = new ArrayList<WatchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WatchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _item = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
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
  public Flow<List<WatchHistoryEntity>> getHistoryByType(final String type) {
    final String _sql = "SELECT * FROM watch_history WHERE type = ? ORDER BY watchedAt DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"watch_history"}, new Callable<List<WatchHistoryEntity>>() {
      @Override
      @NonNull
      public List<WatchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final List<WatchHistoryEntity> _result = new ArrayList<WatchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WatchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _item = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
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
  public Object getLastWatched(final int streamId, final String type,
      final Continuation<? super WatchHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM watch_history WHERE streamId = ? AND type = ? ORDER BY watchedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, streamId);
    _argIndex = 2;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WatchHistoryEntity>() {
      @Override
      @Nullable
      public WatchHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final WatchHistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _result = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastWatchedEpisode(final int seriesId,
      final Continuation<? super WatchHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM watch_history WHERE seriesId = ? ORDER BY watchedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, seriesId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WatchHistoryEntity>() {
      @Override
      @Nullable
      public WatchHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final WatchHistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _result = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEpisodeProgress(final int seriesId, final String season, final int episode,
      final Continuation<? super WatchHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM watch_history WHERE seriesId = ? AND seasonNumber = ? AND episodeNumber = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, seriesId);
    _argIndex = 2;
    if (season == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, season);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, episode);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WatchHistoryEntity>() {
      @Override
      @Nullable
      public WatchHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final WatchHistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _result = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSeriesHistory(final int seriesId,
      final Continuation<? super List<WatchHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM watch_history WHERE seriesId = ? ORDER BY watchedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, seriesId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WatchHistoryEntity>>() {
      @Override
      @NonNull
      public List<WatchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStreamId = CursorUtil.getColumnIndexOrThrow(_cursor, "streamId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfContainerExtension = CursorUtil.getColumnIndexOrThrow(_cursor, "containerExtension");
          final int _cursorIndexOfSeriesId = CursorUtil.getColumnIndexOrThrow(_cursor, "seriesId");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final List<WatchHistoryEntity> _result = new ArrayList<WatchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WatchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpStreamId;
            _tmpStreamId = _cursor.getInt(_cursorIndexOfStreamId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpContainerExtension;
            if (_cursor.isNull(_cursorIndexOfContainerExtension)) {
              _tmpContainerExtension = null;
            } else {
              _tmpContainerExtension = _cursor.getString(_cursorIndexOfContainerExtension);
            }
            final Integer _tmpSeriesId;
            if (_cursor.isNull(_cursorIndexOfSeriesId)) {
              _tmpSeriesId = null;
            } else {
              _tmpSeriesId = _cursor.getInt(_cursorIndexOfSeriesId);
            }
            final String _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getString(_cursorIndexOfSeasonNumber);
            }
            final Integer _tmpEpisodeNumber;
            if (_cursor.isNull(_cursorIndexOfEpisodeNumber)) {
              _tmpEpisodeNumber = null;
            } else {
              _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            }
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpWatchedAt;
            _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _item = new WatchHistoryEntity(_tmpId,_tmpStreamId,_tmpName,_tmpIcon,_tmpType,_tmpCategoryId,_tmpContainerExtension,_tmpSeriesId,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpWatchedAt,_tmpPositionMs,_tmpDurationMs);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
