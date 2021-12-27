package com.os.pancardlayout.ui.main

/**
 * Data validation state of the save form.
 */
data class MainFormState(
    val panNumberError: Int? = null,
    val dobError: Int? = null,
    val isDataValid: Boolean = false
)