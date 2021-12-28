package com.os.pancardlayout.ui.main

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.os.pancardlayout.R
import com.os.pancardlayout.databinding.ActivityMainBinding
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var birthDate: EditText
    private lateinit var panNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         panNumber = binding.panno
         birthDate = binding.dob
        val next = binding.next
        val loading = binding.loading

        mainViewModel = ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]

        mainViewModel.mainFormState.observe(this@MainActivity, Observer {
            val mainState = it ?: return@Observer

            // disable next button unless both pan number / dob is valid
            next?.isEnabled = mainState.isDataValid

            if (mainState.panNumberError != null) {
                panNumber.error = getString(mainState.panNumberError)
            }

        })

        mainViewModel.mainResult.observe(this@MainActivity, Observer {
            val nextResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (nextResult.error != null) {
                showNextFailed(nextResult.error)
            }
            if (nextResult.success != null) {
                updateUiWithUser(nextResult.success)
            }
            setResult(Activity.RESULT_OK)


        })

        panNumber.afterTextChanged {
            mainViewModel.nextDataChanged(
                panNumber.text.toString(),
                birthDate.text.toString()
            )
        }

        birthDate.apply {

            setOnClickListener {
                val now = Calendar.getInstance()
                now.get(Calendar.YEAR)
                now.get(Calendar.MONTH)
                now.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog.newInstance(this@MainActivity,now)
                dpd.maxDate = now
                dpd.show(supportFragmentManager, "Datepickerdialog")
            }

             next?.setOnClickListener {
                loading.visibility = View.VISIBLE
                mainViewModel.nextFun(panNumber.text.toString(), birthDate.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: SavedView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showNextFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date =dayOfMonth.toString() + "/" + (monthOfYear + 1).toString() + "/" + year
        birthDate.setText(date.dateFormator())
        mainViewModel.nextDataChanged(
            panNumber.text.toString(),
            birthDate.text.toString()
        )
    }
}
fun String.dateFormator():String?{
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date  = formatter.parse(this)
    val newFormat = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
    return date?.let { newFormat.format(date) }
}
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}