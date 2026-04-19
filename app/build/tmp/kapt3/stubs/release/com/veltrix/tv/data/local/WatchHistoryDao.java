package com.veltrix.tv.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0011\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ(\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00130\u00122\u0006\u0010\u0014\u001a\u00020\u000eH\'J \u0010\u0015\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0014\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00130\u0012H\'J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\n0\u00132\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u001eJ.\u0010\u001f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u00072\u0006\u0010!\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010#\u00a8\u0006$"}, d2 = {"Lcom/veltrix/tv/data/local/WatchHistoryDao;", "", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEpisodeProgress", "Lcom/veltrix/tv/data/local/WatchHistoryEntity;", "seriesId", "", "season", "", "episode", "(ILjava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHistoryByType", "Lkotlinx/coroutines/flow/Flow;", "", "type", "getLastWatched", "streamId", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLastWatchedEpisode", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentHistory", "getSeriesHistory", "insert", "entry", "(Lcom/veltrix/tv/data/local/WatchHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProgress", "positionMs", "durationMs", "watchedAt", "(JJJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
@androidx.room.Dao
public abstract interface WatchHistoryDao {
    
    @androidx.room.Query(value = "SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT 50")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity>> getRecentHistory();
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE type = :type ORDER BY watchedAt DESC LIMIT 50")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity>> getHistoryByType(@org.jetbrains.annotations.NotNull
    java.lang.String type);
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE streamId = :streamId AND type = :type ORDER BY watchedAt DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLastWatched(int streamId, @org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.veltrix.tv.data.local.WatchHistoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE seriesId = :seriesId ORDER BY watchedAt DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLastWatchedEpisode(int seriesId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.veltrix.tv.data.local.WatchHistoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE seriesId = :seriesId AND seasonNumber = :season AND episodeNumber = :episode LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getEpisodeProgress(int seriesId, @org.jetbrains.annotations.NotNull
    java.lang.String season, int episode, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.veltrix.tv.data.local.WatchHistoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE seriesId = :seriesId ORDER BY watchedAt DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSeriesHistory(int seriesId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.veltrix.tv.data.local.WatchHistoryEntity entry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "DELETE FROM watch_history WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM watch_history")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE watch_history SET positionMs = :positionMs, durationMs = :durationMs, watchedAt = :watchedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateProgress(long id, long positionMs, long durationMs, long watchedAt, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}