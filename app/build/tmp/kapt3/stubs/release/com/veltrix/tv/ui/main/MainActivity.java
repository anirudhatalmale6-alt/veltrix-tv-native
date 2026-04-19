package com.veltrix.tv.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u0000 -2\u00020\u0001:\u0002-.B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u0018H\u0002J\b\u0010\u001a\u001a\u00020\u0011H\u0016J\b\u0010\u001b\u001a\u00020\u0011H\u0002J\b\u0010\u001c\u001a\u00020\u0011H\u0002J\u0018\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0018H\u0002J\b\u0010 \u001a\u00020\bH\u0002J\b\u0010!\u001a\u00020\bH\u0002J\b\u0010\"\u001a\u00020\u0011H\u0017J\u0012\u0010#\u001a\u00020\u00112\b\u0010$\u001a\u0004\u0018\u00010%H\u0014J\u001a\u0010&\u001a\u00020\b2\u0006\u0010\'\u001a\u00020(2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\b\u0010)\u001a\u00020\u0011H\u0002J\b\u0010*\u001a\u00020\u0011H\u0002J\u0010\u0010+\u001a\u00020\u00112\u0006\u0010,\u001a\u00020(H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/veltrix/tv/ui/main/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "contentContainer", "Landroid/widget/FrameLayout;", "currentContentFragment", "Landroidx/fragment/app/Fragment;", "exitConfirmed", "", "prefs", "Lcom/veltrix/tv/data/PrefsManager;", "sidebarContainer", "sidebarFragment", "Lcom/veltrix/tv/ui/main/SidebarFragment;", "tvDebug", "Landroid/widget/TextView;", "debug", "", "msg", "", "dispatchKeyEvent", "event", "Landroid/view/KeyEvent;", "findFirstFocusable", "Landroid/view/View;", "view", "finish", "focusFirstInContent", "initApi", "isDescendantOf", "child", "parent", "isFocusInContent", "isFocusInSidebar", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onKeyDown", "keyCode", "", "setupSidebar", "showExitDialog", "switchContent", "sectionId", "Companion", "DpadNavigable", "app_release"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.veltrix.tv.ui.main.SidebarFragment sidebarFragment;
    private com.veltrix.tv.data.PrefsManager prefs;
    private android.widget.FrameLayout contentContainer;
    private android.widget.FrameLayout sidebarContainer;
    private android.widget.TextView tvDebug;
    @org.jetbrains.annotations.Nullable
    private androidx.fragment.app.Fragment currentContentFragment;
    private boolean exitConfirmed = false;
    private static com.veltrix.tv.data.api.XtreamApiService apiService;
    private static com.veltrix.tv.data.PrefsManager prefsInstance;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.ui.main.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void debug(java.lang.String msg) {
    }
    
    private final void initApi() {
    }
    
    private final void setupSidebar() {
    }
    
    private final void switchContent(int sectionId) {
    }
    
    private final boolean isFocusInSidebar() {
        return false;
    }
    
    private final boolean isFocusInContent() {
        return false;
    }
    
    private final boolean isDescendantOf(android.view.View child, android.view.View parent) {
        return false;
    }
    
    private final void focusFirstInContent() {
    }
    
    private final android.view.View findFirstFocusable(android.view.View view) {
        return null;
    }
    
    @java.lang.Override
    public boolean dispatchKeyEvent(@org.jetbrains.annotations.NotNull
    android.view.KeyEvent event) {
        return false;
    }
    
    @java.lang.Override
    public boolean onKeyDown(int keyCode, @org.jetbrains.annotations.Nullable
    android.view.KeyEvent event) {
        return false;
    }
    
    @java.lang.Override
    @java.lang.Deprecated
    public void onBackPressed() {
    }
    
    @java.lang.Override
    public void finish() {
    }
    
    private final void showExitDialog() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/veltrix/tv/ui/main/MainActivity$Companion;", "", "()V", "<set-?>", "Lcom/veltrix/tv/data/api/XtreamApiService;", "apiService", "getApiService", "()Lcom/veltrix/tv/data/api/XtreamApiService;", "Lcom/veltrix/tv/data/PrefsManager;", "prefsInstance", "getPrefsInstance", "()Lcom/veltrix/tv/data/PrefsManager;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.veltrix.tv.data.api.XtreamApiService getApiService() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.veltrix.tv.data.PrefsManager getPrefsInstance() {
            return null;
        }
    }
    
    /**
     * Interface for fragments that handle D-pad navigation
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&\u00a8\u0006\u0004"}, d2 = {"Lcom/veltrix/tv/ui/main/MainActivity$DpadNavigable;", "", "canGoLeft", "", "app_release"})
    public static abstract interface DpadNavigable {
        
        public abstract boolean canGoLeft();
    }
}