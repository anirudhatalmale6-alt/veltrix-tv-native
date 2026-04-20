package com.veltrix.tv.data.models

import com.google.gson.annotations.SerializedName

data class VodInfo(
    @SerializedName("info") val info: VodMovieInfo?,
    @SerializedName("movie_data") val movieData: VodMovieData?
)

data class VodMovieInfo(
    @SerializedName("movie_image") val movieImage: String?,
    @SerializedName("backdrop_path") val backdropPath: List<String>?,
    @SerializedName("tmdb_id") val tmdbId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("o_name") val originalName: String?,
    @SerializedName("plot") val plot: String?,
    @SerializedName("cast") val cast: String?,
    @SerializedName("director") val director: String?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("releasedate") val releaseDate: String?,
    @SerializedName("release_date") val releaseDateAlt: String?,
    @SerializedName("duration_secs") val durationSecs: Int?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("rating_5based") val rating5Based: Double?,
    @SerializedName("youtube_trailer") val youtubeTrailer: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("age") val age: String?,
    @SerializedName("kinopoisk_url") val kinopoiskUrl: String?,
    @SerializedName("video") val video: VodVideoInfo?,
    @SerializedName("audio") val audio: VodAudioInfo?,
    @SerializedName("bitrate") val bitrate: Int?
)

data class VodVideoInfo(
    @SerializedName("codec_name") val codecName: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?
)

data class VodAudioInfo(
    @SerializedName("codec_name") val codecName: String?,
    @SerializedName("channels") val channels: String?,
    @SerializedName("sample_rate") val sampleRate: String?
)

data class VodMovieData(
    @SerializedName("stream_id") val streamId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("added") val added: String?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("container_extension") val containerExtension: String?,
    @SerializedName("custom_sid") val customSid: String?,
    @SerializedName("direct_source") val directSource: String?
)
