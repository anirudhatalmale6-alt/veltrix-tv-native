package com.veltrix.tv.ui.live;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0019B7\u0012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0004\u0012\u0016\b\u0002\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0007\u0018\u00010\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\u0006H\u0016J\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fJ\u001c\u0010\u0010\u001a\u00020\u00072\n\u0010\u0011\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0006H\u0016J\u001c\u0010\u0013\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0006H\u0016J\u0014\u0010\u0017\u001a\u00020\u00072\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0007\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/veltrix/tv/ui/live/ChannelAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/veltrix/tv/ui/live/ChannelAdapter$ViewHolder;", "onChannelClick", "Lkotlin/Function2;", "Lcom/veltrix/tv/data/models/LiveStream;", "", "", "onChannelLongClick", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;)V", "items", "", "getItemCount", "getItems", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "submitList", "streams", "ViewHolder", "app_release"})
public final class ChannelAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.veltrix.tv.ui.live.ChannelAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.veltrix.tv.data.models.LiveStream, java.lang.Integer, kotlin.Unit> onChannelClick = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function1<com.veltrix.tv.data.models.LiveStream, kotlin.Unit> onChannelLongClick = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.veltrix.tv.data.models.LiveStream> items = null;
    
    public ChannelAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.veltrix.tv.data.models.LiveStream, ? super java.lang.Integer, kotlin.Unit> onChannelClick, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super com.veltrix.tv.data.models.LiveStream, kotlin.Unit> onChannelLongClick) {
        super();
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull
    java.util.List<com.veltrix.tv.data.models.LiveStream> streams) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.veltrix.tv.data.models.LiveStream> getItems() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.veltrix.tv.ui.live.ChannelAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.veltrix.tv.ui.live.ChannelAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/veltrix/tv/ui/live/ChannelAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/veltrix/tv/ui/live/ChannelAdapter;Landroid/view/View;)V", "ivIcon", "Landroid/widget/ImageView;", "getIvIcon", "()Landroid/widget/ImageView;", "tvName", "Landroid/widget/TextView;", "getTvName", "()Landroid/widget/TextView;", "app_release"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageView ivIcon = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvName = null;
        
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
    }
}