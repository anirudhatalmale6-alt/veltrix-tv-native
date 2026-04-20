package com.veltrix.tv.data.models

import com.google.gson.annotations.SerializedName

data class EpgResponse(
    @SerializedName("epg_listings") val epgListings: List<EpgEntry> = emptyList()
)
