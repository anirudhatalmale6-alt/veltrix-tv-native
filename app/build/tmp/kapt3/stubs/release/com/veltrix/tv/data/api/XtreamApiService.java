package com.veltrix.tv.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J2\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ>\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\n\b\u0003\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0010J>\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\n\b\u0003\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0010J2\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ6\u0010\u0014\u001a\u00020\u00152\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\u0016\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J2\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ>\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\n\b\u0003\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lcom/veltrix/tv/data/api/XtreamApiService;", "", "authenticate", "Lcom/veltrix/tv/data/models/AuthResponse;", "username", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLiveCategories", "", "Lcom/veltrix/tv/data/models/Category;", "action", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLiveStreams", "Lcom/veltrix/tv/data/models/LiveStream;", "categoryId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSeries", "Lcom/veltrix/tv/data/models/SeriesItem;", "getSeriesCategories", "getSeriesInfo", "Lcom/veltrix/tv/data/models/SeriesInfo;", "seriesId", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVodCategories", "getVodStreams", "Lcom/veltrix/tv/data/models/VodStream;", "app_release"})
public abstract interface XtreamApiService {
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object authenticate(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.veltrix.tv.data.models.AuthResponse> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLiveCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.Category>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLiveStreams(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @retrofit2.http.Query(value = "category_id")
    @org.jetbrains.annotations.Nullable
    java.lang.String categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.LiveStream>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getVodCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.Category>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getVodStreams(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @retrofit2.http.Query(value = "category_id")
    @org.jetbrains.annotations.Nullable
    java.lang.String categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.VodStream>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSeriesCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.Category>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSeries(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @retrofit2.http.Query(value = "category_id")
    @org.jetbrains.annotations.Nullable
    java.lang.String categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.veltrix.tv.data.models.SeriesItem>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSeriesInfo(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull
    java.lang.String action, @retrofit2.http.Query(value = "series_id")
    int seriesId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.veltrix.tv.data.models.SeriesInfo> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}