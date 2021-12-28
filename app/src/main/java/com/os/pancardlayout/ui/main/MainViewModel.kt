package com.os.pancardlayout.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.os.pancardlayout.data.MainRepository
import com.os.pancardlayout.data.Result

import com.os.pancardlayout.R
import java.util.regex.Pattern


class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _mainForm = MutableLiveData<MainFormState>()
    val mainFormState: LiveData<MainFormState> = _mainForm

    private val _mainResult = MutableLiveData<NextResult>()
    val mainResult: LiveData<NextResult> = _mainResult

     fun nextFun(panNumber: String, birthDate: String) {
            // can be launched in a separate asynchronous job
            val result = mainRepository.next(panNumber, birthDate)

            if (result is Result.Success) {
                _mainResult.value =
                    NextResult(success = SavedView(displayName = result.data.displayName))
            } else {
                _mainResult.value = NextResult(error = R.string.validation_failed)
            }

    }

    fun nextDataChanged(panNumber: String, dob: String) {
        if (!isPanNoValid(panNumber)) {
            _mainForm.value = MainFormState(panNumberError = R.string.invalid_panno)
        } else if (!isDOBValid(dob)) {
            _mainForm.value = MainFormState(dobError = R.string.invalid_dob)
        } else {
            _mainForm.value = MainFormState(isDataValid = true)
        }
    }

    // A placeholder pan number validation check
    private fun isPanNoValid(panNo: String): Boolean {
        val pattern  = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher = pattern.matcher(panNo)
        return panNo.isNotEmpty() && matcher.matches()

    }

    // A placeholder dob validation check
    private fun isDOBValid(dob: String): Boolean {
        return dob.length > 8
    }
}