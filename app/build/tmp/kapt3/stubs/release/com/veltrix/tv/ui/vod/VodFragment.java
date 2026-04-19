package com.veltrix.tv.ui.vod;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J$\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\u001a\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010\"\u001a\u00020\u0014H\u0002J\u0010\u0010#\u001a\u00020\u00142\u0006\u0010$\u001a\u00020%H\u0002J\u0014\u0010&\u001a\u00020\u0012*\u00020\t2\u0006\u0010!\u001a\u00020\u0019H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/veltrix/tv/ui/vod/VodFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/veltrix/tv/ui/main/MainActivity$DpadNavigable;", "()V", "categoryAdapter", "Lcom/veltrix/tv/ui/live/CategoryAdapter;", "progressBar", "Landroid/widget/ProgressBar;", "rvCategories", "Landroidx/recyclerview/widget/RecyclerView;", "rvMovies", "tvEmpty", "Landroid/widget/TextView;", "vodAdapter", "Lcom/veltrix/tv/ui/vod/VodAdapter;", "calculateGridColumns", "", "canGoLeft", "", "loadCategories", "", "loadMovies", "categoryId", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupAdapters", "toggleFavorite", "movie", "Lcom/veltrix/tv/data/models/VodStream;", "isAncestorOf", "app_release"})
public final class VodFragment extends androidx.fragment.app.Fragment implements com.veltrix.tv.ui.main.MainActivity.DpadNavigable {
    private androidx.recyclerview.widget.RecyclerView rvCategories;
    private androidx.recyclerview.widget.RecyclerView rvMovies;
    private android.widget.ProgressBar progressBar;
    private android.widget.TextView tvEmpty;
    private com.veltrix.tv.ui.live.CategoryAdapter categoryAdapter;
    private com.veltrix.tv.ui.vod.VodAdapter vodAdapter;
    
    public VodFragment() {
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
    
    private final void toggleFavorite(com.veltrix.tv.data.models.VodStream movie) {
    }
    
    private final void loadCategories() {
    }
    
    private final void loadMovies(java.lang.String categoryId) {
    }
}