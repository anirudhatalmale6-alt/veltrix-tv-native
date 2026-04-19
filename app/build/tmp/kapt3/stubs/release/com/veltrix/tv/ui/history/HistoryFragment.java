package com.veltrix.tv.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u001a\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/veltrix/tv/ui/history/HistoryFragment;", "Landroidx/fragment/app/Fragment;", "()V", "rvHistory", "Landroidx/recyclerview/widget/RecyclerView;", "tvEmpty", "Landroid/widget/TextView;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "view", "openHistoryItem", "item", "Lcom/veltrix/tv/data/local/WatchHistoryEntity;", "HistoryAdapter", "app_release"})
public final class HistoryFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvHistory;
    private android.widget.TextView tvEmpty;
    
    public HistoryFragment() {
        super();
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
    
    private final void openHistoryItem(com.veltrix.tv.data.local.WatchHistoryEntity item) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0018B\'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J \u0010\u0011\u001a\u00020\t2\u000e\u0010\u0012\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0013\u001a\u00020\fH\u0016J \u0010\u0014\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\fH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/veltrix/tv/ui/history/HistoryFragment$HistoryAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/veltrix/tv/ui/history/HistoryFragment$HistoryAdapter$ViewHolder;", "Lcom/veltrix/tv/ui/history/HistoryFragment;", "items", "", "Lcom/veltrix/tv/data/local/WatchHistoryEntity;", "onClick", "Lkotlin/Function1;", "", "(Lcom/veltrix/tv/ui/history/HistoryFragment;Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "getTimeAgo", "", "timestamp", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_release"})
    public final class HistoryAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.veltrix.tv.ui.history.HistoryFragment.HistoryAdapter.ViewHolder> {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity> items = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function1<com.veltrix.tv.data.local.WatchHistoryEntity, kotlin.Unit> onClick = null;
        
        public HistoryAdapter(@org.jetbrains.annotations.NotNull
        java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity> items, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.veltrix.tv.data.local.WatchHistoryEntity, kotlin.Unit> onClick) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public com.veltrix.tv.ui.history.HistoryFragment.HistoryAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull
        com.veltrix.tv.ui.history.HistoryFragment.HistoryAdapter.ViewHolder holder, int position) {
        }
        
        @java.lang.Override
        public int getItemCount() {
            return 0;
        }
        
        private final java.lang.String getTimeAgo(long timestamp) {
            return null;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0013\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010\u00a8\u0006\u0015"}, d2 = {"Lcom/veltrix/tv/ui/history/HistoryFragment$HistoryAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/veltrix/tv/ui/history/HistoryFragment$HistoryAdapter;Landroid/view/View;)V", "ivIcon", "Landroid/widget/ImageView;", "getIvIcon", "()Landroid/widget/ImageView;", "progressWatch", "Landroid/widget/ProgressBar;", "getProgressWatch", "()Landroid/widget/ProgressBar;", "tvDetails", "Landroid/widget/TextView;", "getTvDetails", "()Landroid/widget/TextView;", "tvName", "getTvName", "tvTime", "getTvTime", "app_release"})
        public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull
            private final android.widget.ImageView ivIcon = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvName = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvDetails = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvTime = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.ProgressBar progressWatch = null;
            
            public ViewHolder(@org.jetbrains.annotations.NotNull
            android.view.View itemView) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.ImageView getIvIcon() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvDetails() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvTime() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.ProgressBar getProgressWatch() {
                return null;
            }
        }
    }
}