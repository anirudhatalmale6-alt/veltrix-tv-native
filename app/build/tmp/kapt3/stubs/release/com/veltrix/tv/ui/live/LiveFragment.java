package com.veltrix.tv.ui.live;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J$\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u001a\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0018\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u00062\u0006\u0010\'\u001a\u00020\u0013H\u0002J\b\u0010(\u001a\u00020\u0017H\u0002J\u0010\u0010)\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0006H\u0002J\u0014\u0010*\u001a\u00020\u0015*\u00020\u000e2\u0006\u0010$\u001a\u00020\u001cH\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/veltrix/tv/ui/live/LiveFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/veltrix/tv/ui/main/MainActivity$DpadNavigable;", "()V", "allStreams", "", "Lcom/veltrix/tv/data/models/LiveStream;", "categoryAdapter", "Lcom/veltrix/tv/ui/live/CategoryAdapter;", "channelAdapter", "Lcom/veltrix/tv/ui/live/ChannelAdapter;", "progressBar", "Landroid/widget/ProgressBar;", "rvCategories", "Landroidx/recyclerview/widget/RecyclerView;", "rvChannels", "tvEmpty", "Landroid/widget/TextView;", "calculateGridColumns", "", "canGoLeft", "", "loadCategories", "", "loadStreams", "categoryId", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "openPlayer", "stream", "position", "setupAdapters", "toggleFavorite", "isAncestorOf", "app_release"})
public final class LiveFragment extends androidx.fragment.app.Fragment implements com.veltrix.tv.ui.main.MainActivity.DpadNavigable {
    private androidx.recyclerview.widget.RecyclerView rvCategories;
    private androidx.recyclerview.widget.RecyclerView rvChannels;
    private android.widget.ProgressBar progressBar;
    private android.widget.TextView tvEmpty;
    private com.veltrix.tv.ui.live.CategoryAdapter categoryAdapter;
    private com.veltrix.tv.ui.live.ChannelAdapter channelAdapter;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.veltrix.tv.data.models.LiveStream> allStreams;
    
    public LiveFragment() {
        super();
    }
    
    @java.lang.Override
    public boolean canGoLeft() {
        return false;
    }
    
    private final boolean isAncestorOf(androidx.recyclerview.widget.RecyclerView $this$isAncestorOf, android.view.View view) {
        return false;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupAdapters() {
    }
    
    private final int calculateGridColumns() {
        return 0;
    }
    
    private final void toggleFavorite(com.veltrix.tv.data.models.LiveStream stream) {
    }
    
    private final void loadCategories() {
    }
    
    private final void loadStreams(java.lang.String categoryId) {
    }
    
    private final void openPlayer(com.veltrix.tv.data.models.LiveStream stream, int position) {
    }
}