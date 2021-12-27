package com.os.pancardlayout.data

import com.os.pancardlayout.data.model.SavedDetails
import java.io.IOException


class MainDataSource {

    fun save(panNumber: String, dob: String): Result<SavedDetails> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = SavedDetails(java.util.UUID.randomUUID().toString(), panNumber)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error in save data", e))
        }
    }

    fun deleteData() {
        // TODO: revoke authentication
    }
}