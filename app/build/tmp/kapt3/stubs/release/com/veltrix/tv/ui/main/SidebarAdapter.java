package com.veltrix.tv.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0019B-\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0018\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007\u00a2\u0006\u0002\u0010\nJ\b\u0010\u0011\u001a\u00020\bH\u0016J\u001c\u0010\u0012\u001a\u00020\t2\n\u0010\u0013\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\bH\u0016J\u001c\u0010\u0015\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\bH\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/veltrix/tv/ui/main/SidebarAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/veltrix/tv/ui/main/SidebarAdapter$ViewHolder;", "items", "", "Lcom/veltrix/tv/ui/main/SidebarItem;", "onItemSelected", "Lkotlin/Function2;", "", "", "(Ljava/util/List;Lkotlin/jvm/functions/Function2;)V", "value", "selectedPosition", "getSelectedPosition", "()I", "setSelectedPosition", "(I)V", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_release"})
public final class SidebarAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.veltrix.tv.ui.main.SidebarAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.veltrix.tv.ui.main.SidebarItem> items = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.veltrix.tv.ui.main.SidebarItem, java.lang.Integer, kotlin.Unit> onItemSelected = null;
    private int selectedPosition = 0;
    
    public SidebarAdapter(@org.jetbrains.annotations.NotNull
    java.util.List<com.veltrix.tv.ui.main.SidebarItem> items, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.veltrix.tv.ui.main.SidebarItem, ? super java.lang.Integer, kotlin.Unit> onItemSelected) {
        super();
    }
    
    public final int getSelectedPosition() {
        return 0;
    }
    
    public final void setSelectedPosition(int value) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.veltrix.tv.ui.main.SidebarAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.veltrix.tv.ui.main.SidebarAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/veltrix/tv/ui/main/SidebarAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/veltrix/tv/ui/main/SidebarAdapter;Landroid/view/View;)V", "indicator", "getIndicator", "()Landroid/view/View;", "tvTitle", "Landroid/widget/TextView;", "getTvTitle", "()Landroid/widget/TextView;", "app_release"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvTitle = null;
        @org.jetbrains.annotations.NotNull
        private final android.view.View indicator = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvTitle() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.view.View getIndicator() {
            return null;
        }
    }
}