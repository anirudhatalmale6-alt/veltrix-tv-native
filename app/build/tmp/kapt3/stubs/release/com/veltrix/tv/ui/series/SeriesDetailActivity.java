package com.veltrix.tv.ui.series;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001f2\u00020\u0001:\u0003\u001f !B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "currentSeasonKey", "", "ivCover", "Landroid/widget/ImageView;", "progressBar", "Landroid/widget/ProgressBar;", "rvEpisodes", "Landroidx/recyclerview/widget/RecyclerView;", "rvSeasons", "seriesInfo", "Lcom/veltrix/tv/data/models/SeriesInfo;", "tvError", "Landroid/widget/TextView;", "tvGenre", "tvPlot", "tvTitle", "loadSeriesInfo", "", "seriesId", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "playEpisode", "episode", "Lcom/veltrix/tv/data/models/Episode;", "showEpisodes", "seasonKey", "Companion", "EpisodeAdapter", "SeasonTabAdapter", "app_release"})
public final class SeriesDetailActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_SERIES_ID = "series_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_SERIES_NAME = "series_name";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_SERIES_COVER = "series_cover";
    private android.widget.ImageView ivCover;
    private android.widget.TextView tvTitle;
    private android.widget.TextView tvPlot;
    private android.widget.TextView tvGenre;
    private androidx.recyclerview.widget.RecyclerView rvSeasons;
    private androidx.recyclerview.widget.RecyclerView rvEpisodes;
    private android.widget.ProgressBar progressBar;
    private android.widget.TextView tvError;
    @org.jetbrains.annotations.Nullable
    private com.veltrix.tv.data.models.SeriesInfo seriesInfo;
    @org.jetbrains.annotations.Nullable
    private java.lang.String currentSeasonKey;
    @org.jetbrains.annotations.NotNull
    public static final com.veltrix.tv.ui.series.SeriesDetailActivity.Companion Companion = null;
    
    public SeriesDetailActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadSeriesInfo(int seriesId) {
    }
    
    private final void showEpisodes(java.lang.String seasonKey) {
    }
    
    private final void playEpisode(com.veltrix.tv.data.models.Episode episode) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity$Companion;", "", "()V", "EXTRA_SERIES_COVER", "", "EXTRA_SERIES_ID", "EXTRA_SERIES_NAME", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0018B=\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J \u0010\u0011\u001a\u00020\r2\u000e\u0010\u0012\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J \u0010\u0014\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0010H\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity$EpisodeAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/veltrix/tv/ui/series/SeriesDetailActivity$EpisodeAdapter$ViewHolder;", "Lcom/veltrix/tv/ui/series/SeriesDetailActivity;", "episodes", "", "Lcom/veltrix/tv/data/models/Episode;", "historyList", "Lcom/veltrix/tv/data/local/WatchHistoryEntity;", "seasonKey", "", "onEpisodeClick", "Lkotlin/Function1;", "", "(Lcom/veltrix/tv/ui/series/SeriesDetailActivity;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_release"})
    public final class EpisodeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.veltrix.tv.ui.series.SeriesDetailActivity.EpisodeAdapter.ViewHolder> {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.veltrix.tv.data.models.Episode> episodes = null;
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity> historyList = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String seasonKey = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function1<com.veltrix.tv.data.models.Episode, kotlin.Unit> onEpisodeClick = null;
        
        public EpisodeAdapter(@org.jetbrains.annotations.NotNull
        java.util.List<com.veltrix.tv.data.models.Episode> episodes, @org.jetbrains.annotations.NotNull
        java.util.List<com.veltrix.tv.data.local.WatchHistoryEntity> historyList, @org.jetbrains.annotations.NotNull
        java.lang.String seasonKey, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.veltrix.tv.data.models.Episode, kotlin.Unit> onEpisodeClick) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public com.veltrix.tv.ui.series.SeriesDetailActivity.EpisodeAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull
        com.veltrix.tv.ui.series.SeriesDetailActivity.EpisodeAdapter.ViewHolder holder, int position) {
        }
        
        @java.lang.Override
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity$EpisodeAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/veltrix/tv/ui/series/SeriesDetailActivity$EpisodeAdapter;Landroid/view/View;)V", "progressEpisode", "Landroid/widget/ProgressBar;", "getProgressEpisode", "()Landroid/widget/ProgressBar;", "tvDuration", "Landroid/widget/TextView;", "getTvDuration", "()Landroid/widget/TextView;", "tvPlot", "getTvPlot", "tvTitle", "getTvTitle", "app_release"})
        public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvTitle = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvPlot = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvDuration = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.ProgressBar progressEpisode = null;
            
            public ViewHolder(@org.jetbrains.annotations.NotNull
            android.view.View itemView) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvTitle() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvPlot() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvDuration() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.ProgressBar getProgressEpisode() {
                return null;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0015B\'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\fH\u0016J \u0010\u000e\u001a\u00020\t2\u000e\u0010\u000f\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0010\u001a\u00020\fH\u0016J \u0010\u0011\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity$SeasonTabAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/veltrix/tv/ui/series/SeriesDetailActivity$SeasonTabAdapter$ViewHolder;", "Lcom/veltrix/tv/ui/series/SeriesDetailActivity;", "seasons", "", "", "onSeasonSelected", "Lkotlin/Function1;", "", "(Lcom/veltrix/tv/ui/series/SeriesDetailActivity;Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "selectedPos", "", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_release"})
    public final class SeasonTabAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.veltrix.tv.ui.series.SeriesDetailActivity.SeasonTabAdapter.ViewHolder> {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<java.lang.String> seasons = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onSeasonSelected = null;
        private int selectedPos = 0;
        
        public SeasonTabAdapter(@org.jetbrains.annotations.NotNull
        java.util.List<java.lang.String> seasons, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSeasonSelected) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public com.veltrix.tv.ui.series.SeriesDetailActivity.SeasonTabAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull
        com.veltrix.tv.ui.series.SeriesDetailActivity.SeasonTabAdapter.ViewHolder holder, int position) {
        }
        
        @java.lang.Override
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/veltrix/tv/ui/series/SeriesDetailActivity$SeasonTabAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/veltrix/tv/ui/series/SeriesDetailActivity$SeasonTabAdapter;Landroid/view/View;)V", "tvSeason", "Landroid/widget/TextView;", "getTvSeason", "()Landroid/widget/TextView;", "app_release"})
        public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView tvSeason = null;
            
            public ViewHolder(@org.jetbrains.annotations.NotNull
            android.view.View itemView) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getTvSeason() {
                return null;
            }
        }
    }
}