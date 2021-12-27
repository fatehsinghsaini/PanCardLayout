package com.os.pancardlayout.ui.main

/**
 * Authentication result : success (user details) or error message.
 */
data class NextResult(
    val success: SavedView? = null,
    val error: Int? = null
)