package com.veltrix.tv.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 (2\u00020\u0001:\u0001(B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010%\u001a\u00020&J\u0006\u0010\'\u001a\u00020\u000bR$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR$\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R$\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00118F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R$\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R$\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b \u0010\u000e\"\u0004\b!\u0010\u0010R$\u0010\"\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010\u000e\"\u0004\b$\u0010\u0010\u00a8\u0006)"}, d2 = {"Lcom/veltrix/tv/data/PrefsManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "isLoggedIn", "()Z", "setLoggedIn", "(Z)V", "", "lastCategory", "getLastCategory", "()Ljava/lang/String;", "setLastCategory", "(Ljava/lang/String;)V", "", "lastSection", "getLastSection", "()I", "setLastSection", "(I)V", "password", "getPassword", "setPassword", "port", "getPort", "setPort", "prefs", "Landroid/content/SharedPreferences;", "serverUrl", "getServerUrl", "setServerUrl", "username", "getUsername", "setUsername", "clear", "", "getBaseUrl", "Companion", "app_release"})
public final class PrefsManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SERVER_URL = "server_url";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_USERNAME = "username";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_PASSWORD = "password";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_PORT = "port";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_LOGGED_IN = "logged_in";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_LAST_CATEGORY = "last_category";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_LAST_SECTION = "last_section";
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile com.veltrix.tv.data.PrefsManager INSTANCE;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.data.PrefsManager.Companion Companion = null;
    
    public PrefsManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getServerUrl() {
        return null;
    }
    
    public final void setServerUrl(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUsername() {
        return null;
    }
    
    public final void setUsername(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassword() {
        return null;
    }
    
    public final void setPassword(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPort() {
        return null;
    }
    
    public final void setPort(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void setLoggedIn(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLastCategory() {
        return null;
    }
    
    public final void setLastCategory(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final int getLastSection() {
        return 0;
    }
    
    public final void setLastSection(int value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBaseUrl() {
        return null;
    }
    
    public final void clear() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/veltrix/tv/data/PrefsManager$Companion;", "", "()V", "INSTANCE", "Lcom/veltrix/tv/data/PrefsManager;", "KEY_LAST_CATEGORY", "", "KEY_LAST_SECTION", "KEY_LOGGED_IN", "KEY_PASSWORD", "KEY_PORT", "KEY_SERVER_URL", "KEY_USERNAME", "getInstance", "context", "Landroid/content/Context;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.veltrix.tv.data.PrefsManager getInstance(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}