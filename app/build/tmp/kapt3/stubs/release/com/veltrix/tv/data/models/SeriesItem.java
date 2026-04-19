package com.veltrix.tv.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\b.\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u00a7\u0001\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\u000e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0012\u0012\b\u0010\u0013\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0016J\u0010\u0010.\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010#J\u000b\u0010/\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0010\u00101\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003\u00a2\u0006\u0002\u0010(J\u0011\u00102\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0012H\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00108\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u00d0\u0001\u0010>\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010?J\u0013\u0010@\u001a\u00020A2\b\u0010B\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010C\u001a\u00020\u0003H\u00d6\u0001J\t\u0010D\u001a\u00020\u0005H\u00d6\u0001R\u001e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0018\u0010\t\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0018\u0010\u0007\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0018\u0010\n\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001aR\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001aR\u0018\u0010\r\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001aR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001aR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010$\u001a\u0004\b\"\u0010#R\u0018\u0010\b\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001aR\u001a\u0010\u000f\u001a\u0004\u0018\u00010\u00108\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b\'\u0010(R\u0018\u0010\f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001aR\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001a\u00a8\u0006E"}, d2 = {"Lcom/veltrix/tv/data/models/SeriesItem;", "", "num", "", "name", "", "seriesId", "cover", "plot", "cast", "director", "genre", "releaseDate", "lastModified", "rating", "rating5Based", "", "backdropPath", "", "youtubeTrailer", "episodeRunTime", "categoryId", "(Ljava/lang/Integer;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBackdropPath", "()Ljava/util/List;", "getCast", "()Ljava/lang/String;", "getCategoryId", "getCover", "getDirector", "getEpisodeRunTime", "getGenre", "getLastModified", "getName", "getNum", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getPlot", "getRating", "getRating5Based", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getReleaseDate", "getSeriesId", "()I", "getYoutubeTrailer", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/Integer;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/veltrix/tv/data/models/SeriesItem;", "equals", "", "other", "hashCode", "toString", "app_release"})
public final class SeriesItem {
    @com.google.gson.annotations.SerializedName(value = "num")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer num = null;
    @com.google.gson.annotations.SerializedName(value = "name")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @com.google.gson.annotations.SerializedName(value = "series_id")
    private final int seriesId = 0;
    @com.google.gson.annotations.SerializedName(value = "cover")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String cover = null;
    @com.google.gson.annotations.SerializedName(value = "plot")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String plot = null;
    @com.google.gson.annotations.SerializedName(value = "cast")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String cast = null;
    @com.google.gson.annotations.SerializedName(value = "director")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String director = null;
    @com.google.gson.annotations.SerializedName(value = "genre")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String genre = null;
    @com.google.gson.annotations.SerializedName(value = "releaseDate")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String releaseDate = null;
    @com.google.gson.annotations.SerializedName(value = "last_modified")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String lastModified = null;
    @com.google.gson.annotations.SerializedName(value = "rating")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String rating = null;
    @com.google.gson.annotations.SerializedName(value = "rating_5based")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Double rating5Based = null;
    @com.google.gson.annotations.SerializedName(value = "backdrop_path")
    @org.jetbrains.annotations.Nullable
    private final java.util.List<java.lang.String> backdropPath = null;
    @com.google.gson.annotations.SerializedName(value = "youtube_trailer")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String youtubeTrailer = null;
    @com.google.gson.annotations.SerializedName(value = "episode_run_time")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String episodeRunTime = null;
    @com.google.gson.annotations.SerializedName(value = "category_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String categoryId = null;
    
    public SeriesItem(@org.jetbrains.annotations.Nullable
    java.lang.Integer num, @org.jetbrains.annotations.NotNull
    java.lang.String name, int seriesId, @org.jetbrains.annotations.Nullable
    java.lang.String cover, @org.jetbrains.annotations.Nullable
    java.lang.String plot, @org.jetbrains.annotations.Nullable
    java.lang.String cast, @org.jetbrains.annotations.Nullable
    java.lang.String director, @org.jetbrains.annotations.Nullable
    java.lang.String genre, @org.jetbrains.annotations.Nullable
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable
    java.lang.String lastModified, @org.jetbrains.annotations.Nullable
    java.lang.String rating, @org.jetbrains.annotations.Nullable
    java.lang.Double rating5Based, @org.jetbrains.annotations.Nullable
    java.util.List<java.lang.String> backdropPath, @org.jetbrains.annotations.Nullable
    java.lang.String youtubeTrailer, @org.jetbrains.annotations.Nullable
    java.lang.String episodeRunTime, @org.jetbrains.annotations.Nullable
    java.lang.String categoryId) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getNum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    public final int getSeriesId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCover() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPlot() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCast() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getDirector() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getGenre() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getReleaseDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getLastModified() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getRating() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Double getRating5Based() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<java.lang.String> getBackdropPath() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getYoutubeTrailer() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getEpisodeRunTime() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCategoryId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Double component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<java.lang.String> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.veltrix.tv.data.models.SeriesItem copy(@org.jetbrains.annotations.Nullable
    java.lang.Integer num, @org.jetbrains.annotations.NotNull
    java.lang.String name, int seriesId, @org.jetbrains.annotations.Nullable
    java.lang.String cover, @org.jetbrains.annotations.Nullable
    java.lang.String plot, @org.jetbrains.annotations.Nullable
    java.lang.String cast, @org.jetbrains.annotations.Nullable
    java.lang.String director, @org.jetbrains.annotations.Nullable
    java.lang.String genre, @org.jetbrains.annotations.Nullable
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable
    java.lang.String lastModified, @org.jetbrains.annotations.Nullable
    java.lang.String rating, @org.jetbrains.annotations.Nullable
    java.lang.Double rating5Based, @org.jetbrains.annotations.Nullable
    java.util.List<java.lang.String> backdropPath, @org.jetbrains.annotations.Nullable
    java.lang.String youtubeTrailer, @org.jetbrains.annotations.Nullable
    java.lang.String episodeRunTime, @org.jetbrains.annotations.Nullable
    java.lang.String categoryId) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}