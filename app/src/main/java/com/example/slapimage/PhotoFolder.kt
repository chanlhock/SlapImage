package com.example.slapimage

data class PhotoFolder(
    val id: String,          // Can remain val since it won't change
    val name: String,        // Can remain val since it won't change
    val path: String,        // Can remain val since it won't change
    var thumbnailPath: String, // Changed to var (will be modified)
    var photoCount: Int,     // Changed to var (will be modified)
    var newestPhotoDate: Long, // Changed to var (will be modified)
    var isSinglePhoto: Boolean = false
)