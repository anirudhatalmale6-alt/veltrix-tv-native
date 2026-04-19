package com.veltrix.tv.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\'\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001By\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010$\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001aJ\u000b\u0010%\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\'\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0010\u0010-\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u000b\u0010.\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u009a\u0001\u00100\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u00101J\u0013\u00102\u001a\u0002032\b\u00104\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00105\u001a\u00020\u0003H\u00d6\u0001J\t\u00106\u001a\u00020\u0005H\u00d6\u0001R\u0018\u0010\f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0018\u0010\r\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001b\u001a\u0004\b\u0019\u0010\u001aR\u0018\u0010\t\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013R\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b\u001d\u0010\u001eR\u0018\u0010\b\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0013R\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0013\u00a8\u00067"}, d2 = {"Lcom/veltrix/tv/data/models/VodStream;", "", "num", "", "name", "", "streamType", "streamId", "streamIcon", "rating", "rating5Based", "", "added", "categoryId", "containerExtension", "customSid", "directSource", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAdded", "()Ljava/lang/String;", "getCategoryId", "getContainerExtension", "getCustomSid", "getDirectSource", "getName", "getNum", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getRating", "getRating5Based", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getStreamIcon", "getStreamId", "()I", "getStreamType", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/veltrix/tv/data/models/VodStream;", "equals", "", "other", "hashCode", "toString", "app_release"})
public final class VodStream {
    @com.google.gson.annotations.SerializedName(value = "num")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer num = null;
    @com.google.gson.annotations.SerializedName(value = "name")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @com.google.gson.annotations.SerializedName(value = "stream_type")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String streamType = null;
    @com.google.gson.annotations.SerializedName(value = "stream_id")
    private final int streamId = 0;
    @com.google.gson.annotations.SerializedName(value = "stream_icon")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String streamIcon = null;
    @com.google.gson.annotations.SerializedName(value = "rating")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String rating = null;
    @com.google.gson.annotations.SerializedName(value = "rating_5based")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Double rating5Based = null;
    @com.google.gson.annotations.SerializedName(value = "added")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String added = null;
    @com.google.gson.annotations.SerializedName(value = "category_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String categoryId = null;
    @com.google.gson.annotations.SerializedName(value = "container_extension")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String containerExtension = null;
    @com.google.gson.annotations.SerializedName(value = "custom_sid")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String customSid = null;
    @com.google.gson.annotations.SerializedName(value = "direct_source")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String directSource = null;
    
    public VodStream(@org.jetbrains.annotations.Nullable
    java.lang.Integer num, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.Nullable
    java.lang.String streamType, int streamId, @org.jetbrains.annotations.Nullable
    java.lang.String streamIcon, @org.jetbrains.annotations.Nullable
    java.lang.String rating, @org.jetbrains.annotations.Nullable
    java.lang.Double rating5Based, @org.jetbrains.annotations.Nullable
    java.lang.String added, @org.jetbrains.annotations.Nullable
    java.lang.String categoryId, @org.jetbrains.annotations.Nullable
    java.lang.String containerExtension, @org.jetbrains.annotations.Nullable
    java.lang.String customSid, @org.jetbrains.annotations.Nullable
    java.lang.String directSource) {
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
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getStreamType() {
        return null;
    }
    
    public final int getStreamId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getStreamIcon() {
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
    public final java.lang.String getAdded() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCategoryId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getContainerExtension() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCustomSid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getDirectSource() {
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
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
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
    public final java.lang.Double component7() {
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
    public final com.veltrix.tv.data.models.VodStream copy(@org.jetbrains.annotations.Nullable
    java.lang.Integer num, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.Nullable
    java.lang.String streamType, int streamId, @org.jetbrains.annotations.Nullable
    java.lang.String streamIcon, @org.jetbrains.annotations.Nullable
    java.lang.String rating, @org.jetbrains.annotations.Nullable
    java.lang.Double rating5Based, @org.jetbrains.annotations.Nullable
    java.lang.String added, @org.jetbrains.annotations.Nullable
    java.lang.String categoryId, @org.jetbrains.annotations.Nullable
    java.lang.String containerExtension, @org.jetbrains.annotations.Nullable
    java.lang.String customSid, @org.jetbrains.annotations.Nullable
    java.lang.String directSource) {
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