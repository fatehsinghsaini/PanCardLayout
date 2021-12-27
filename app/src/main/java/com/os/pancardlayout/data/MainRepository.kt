package com.os.pancardlayout.data

import com.os.pancardlayout.data.model.SavedDetails



class MainRepository(val dataSource: MainDataSource) {

    // in-memory cache of the loggedInUser object
    var user: SavedDetails? = null
        private set


    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.deleteData()
    }

    fun next(panNumber: String, dob: String): Result<SavedDetails> {
        // handle result
        val result = dataSource.save(panNumber, dob)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: SavedDetails) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}