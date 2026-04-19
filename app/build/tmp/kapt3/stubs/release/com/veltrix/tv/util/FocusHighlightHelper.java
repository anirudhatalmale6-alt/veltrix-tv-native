package com.veltrix.tv.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0016\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\rJ\u000e\u0010\u0010\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/veltrix/tv/util/FocusHighlightHelper;", "", "()V", "ANIMATION_DURATION", "", "SCALE_FOCUSED", "", "SCALE_NORMAL", "animateScale", "", "view", "Landroid/view/View;", "focused", "", "applyFocusState", "hasFocus", "setupFocusHighlight", "app_release"})
public final class FocusHighlightHelper {
    private static final float SCALE_FOCUSED = 1.05F;
    private static final float SCALE_NORMAL = 1.0F;
    private static final long ANIMATION_DURATION = 150L;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.util.FocusHighlightHelper INSTANCE = null;
    
    private FocusHighlightHelper() {
        super();
    }
    
    public final void setupFocusHighlight(@org.jetbrains.annotations.NotNull
    android.view.View view) {
    }
    
    private final void animateScale(android.view.View view, boolean focused) {
    }
    
    public final void applyFocusState(@org.jetbrains.annotations.NotNull
    android.view.View view, boolean hasFocus) {
    }
}