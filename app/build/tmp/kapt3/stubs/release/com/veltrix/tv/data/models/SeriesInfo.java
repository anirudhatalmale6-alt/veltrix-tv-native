package com.veltrix.tv.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u001a\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\b\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u001d\u0010\u0014\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\bH\u00c6\u0003JE\u0010\u0015\u001a\u00020\u00002\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u001c\b\u0002\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\bH\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\tH\u00d6\u0001R*\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001c"}, d2 = {"Lcom/veltrix/tv/data/models/SeriesInfo;", "", "seasons", "", "Lcom/veltrix/tv/data/models/Season;", "info", "Lcom/veltrix/tv/data/models/SeriesInfoDetail;", "episodes", "", "", "Lcom/veltrix/tv/data/models/Episode;", "(Ljava/util/List;Lcom/veltrix/tv/data/models/SeriesInfoDetail;Ljava/util/Map;)V", "getEpisodes", "()Ljava/util/Map;", "getInfo", "()Lcom/veltrix/tv/data/models/SeriesInfoDetail;", "getSeasons", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_release"})
public final class SeriesInfo {
    @com.google.gson.annotations.SerializedName(value = "seasons")
    @org.jetbrains.annotations.Nullable
    private final java.util.List<com.veltrix.tv.data.models.Season> seasons = null;
    @com.google.gson.annotations.SerializedName(value = "info")
    @org.jetbrains.annotations.Nullable
    private final com.veltrix.tv.data.models.SeriesInfoDetail info = null;
    @com.google.gson.annotations.SerializedName(value = "episodes")
    @org.jetbrains.annotations.Nullable
    private final java.util.Map<java.lang.String, java.util.List<com.veltrix.tv.data.models.Episode>> episodes = null;
    
    public SeriesInfo(@org.jetbrains.annotations.Nullable
    java.util.List<com.veltrix.tv.data.models.Season> seasons, @org.jetbrains.annotations.Nullable
    com.veltrix.tv.data.models.SeriesInfoDetail info, @org.jetbrains.annotations.Nullable
    java.util.Map<java.lang.String, ? extends java.util.List<com.veltrix.tv.data.models.Episode>> episodes) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<com.veltrix.tv.data.models.Season> getSeasons() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.veltrix.tv.data.models.SeriesInfoDetail getInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.Map<java.lang.String, java.util.List<com.veltrix.tv.data.models.Episode>> getEpisodes() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<com.veltrix.tv.data.models.Season> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.veltrix.tv.data.models.SeriesInfoDetail component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.Map<java.lang.String, java.util.List<com.veltrix.tv.data.models.Episode>> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.veltrix.tv.data.models.SeriesInfo copy(@org.jetbrains.annotations.Nullable
    java.util.List<com.veltrix.tv.data.models.Season> seasons, @org.jetbrains.annotations.Nullable
    com.veltrix.tv.data.models.SeriesInfoDetail info, @org.jetbrains.annotations.Nullable
    java.util.Map<java.lang.String, ? extends java.util.List<com.veltrix.tv.data.models.Episode>> episodes) {
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