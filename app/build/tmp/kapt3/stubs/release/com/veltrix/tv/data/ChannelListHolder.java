package com.veltrix.tv.data;

/**
 * Holds the current channel list in memory to avoid passing large arrays
 * through Intent extras (which can exceed Android's 1MB Binder transaction limit).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0018\u001a\u00020\u0019J\u0014\u0010\u001a\u001a\u00020\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006\u001c"}, d2 = {"Lcom/veltrix/tv/data/ChannelListHolder;", "", "()V", "streamIds", "", "getStreamIds", "()[I", "setStreamIds", "([I)V", "streamNames", "", "", "getStreamNames", "()[Ljava/lang/String;", "setStreamNames", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "streams", "", "Lcom/veltrix/tv/data/models/LiveStream;", "getStreams", "()Ljava/util/List;", "setStreams", "(Ljava/util/List;)V", "clear", "", "set", "liveStreams", "app_release"})
public final class ChannelListHolder {
    @org.jetbrains.annotations.NotNull
    private static int[] streamIds = {};
    @org.jetbrains.annotations.NotNull
    private static java.lang.String[] streamNames = {};
    @org.jetbrains.annotations.NotNull
    private static java.util.List<com.veltrix.tv.data.models.LiveStream> streams;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.data.ChannelListHolder INSTANCE = null;
    
    private ChannelListHolder() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final int[] getStreamIds() {
        return null;
    }
    
    public final void setStreamIds(@org.jetbrains.annotations.NotNull
    int[] p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String[] getStreamNames() {
        return null;
    }
    
    public final void setStreamNames(@org.jetbrains.annotations.NotNull
    java.lang.String[] p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.veltrix.tv.data.models.LiveStream> getStreams() {
        return null;
    }
    
    public final void setStreams(@org.jetbrains.annotations.NotNull
    java.util.List<com.veltrix.tv.data.models.LiveStream> p0) {
    }
    
    public final void set(@org.jetbrains.annotations.NotNull
    java.util.List<com.veltrix.tv.data.models.LiveStream> liveStreams) {
    }
    
    public final void clear() {
    }
}