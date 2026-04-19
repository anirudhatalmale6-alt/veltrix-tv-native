package com.veltrix.tv.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 !2\u00020\u0001:\u0001!B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0016\u001a\u00020\rJ$\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\u001a\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR.\u0010\t\u001a\u0016\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/veltrix/tv/ui/main/SidebarFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/veltrix/tv/ui/main/SidebarAdapter;", "getAdapter", "()Lcom/veltrix/tv/ui/main/SidebarAdapter;", "setAdapter", "(Lcom/veltrix/tv/ui/main/SidebarAdapter;)V", "onSectionSelected", "Lkotlin/Function2;", "Lcom/veltrix/tv/ui/main/SidebarItem;", "", "", "getOnSectionSelected", "()Lkotlin/jvm/functions/Function2;", "setOnSectionSelected", "(Lkotlin/jvm/functions/Function2;)V", "rvSidebar", "Landroidx/recyclerview/widget/RecyclerView;", "sidebarItems", "", "focusCurrentItem", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Companion", "app_release"})
public final class SidebarFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvSidebar;
    public com.veltrix.tv.ui.main.SidebarAdapter adapter;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function2<? super com.veltrix.tv.ui.main.SidebarItem, ? super java.lang.Integer, kotlin.Unit> onSectionSelected;
    public static final int ID_LIVE = 0;
    public static final int ID_MOVIES = 1;
    public static final int ID_SERIES = 2;
    public static final int ID_FAVORITES = 3;
    public static final int ID_SETTINGS = 4;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.veltrix.tv.ui.main.SidebarItem> sidebarItems = null;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.ui.main.SidebarFragment.Companion Companion = null;
    
    public SidebarFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.veltrix.tv.ui.main.SidebarAdapter getAdapter() {
        return null;
    }
    
    public final void setAdapter(@org.jetbrains.annotations.NotNull
    com.veltrix.tv.ui.main.SidebarAdapter p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function2<com.veltrix.tv.ui.main.SidebarItem, java.lang.Integer, kotlin.Unit> getOnSectionSelected() {
        return null;
    }
    
    public final void setOnSectionSelected(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function2<? super com.veltrix.tv.ui.main.SidebarItem, ? super java.lang.Integer, kotlin.Unit> p0) {
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
    
    public final void focusCurrentItem() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/veltrix/tv/ui/main/SidebarFragment$Companion;", "", "()V", "ID_FAVORITES", "", "ID_LIVE", "ID_MOVIES", "ID_SERIES", "ID_SETTINGS", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}