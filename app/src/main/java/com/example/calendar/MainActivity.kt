package com.example.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthday: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var cbTerms: CheckBox
    private lateinit var calendarView: CalendarView
    private lateinit var btnSelectDate: Button

    private var isCalendarVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupDateSelection()
        setupRegisterButton()

        // Ẩn CalendarView ban đầu
        calendarView.visibility = View.GONE
    }

    private fun initializeViews() {
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etBirthday = findViewById(R.id.etBirthday)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        cbTerms = findViewById(R.id.cbTerms)
        calendarView = findViewById(R.id.calendarView)
        btnSelectDate = findViewById(R.id.btnSelectDate)
    }

    private fun setupDateSelection() {
        btnSelectDate.setOnClickListener {
            toggleCalendarVisibility()
        }

        // Khi click vào EditText birthday cũng hiện calendar
        etBirthday.setOnClickListener {
            if (!isCalendarVisible) {
                showCalendar()
            }
        }

        // Thiết lập sự kiện chọn ngày
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            etBirthday.setText(formattedDate)
            hideCalendar()
        }
    }

    private fun toggleCalendarVisibility() {
        if (isCalendarVisible) {
            hideCalendar()
        } else {
            showCalendar()
        }
    }

    private fun showCalendar() {
        calendarView.visibility = View.VISIBLE
        isCalendarVisible = true
        btnSelectDate.text = "Hide"
    }

    private fun hideCalendar() {
        calendarView.visibility = View.GONE
        isCalendarVisible = false
        btnSelectDate.text = "Select"
    }

    private fun setupRegisterButton() {
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        resetFieldColors()

        var isValid = true

        // Validate First Name
        if (etFirstName.text.toString().trim().isEmpty()) {
            highlightErrorField(etFirstName)
            isValid = false
        }

        // Validate Last Name
        if (etLastName.text.toString().trim().isEmpty()) {
            highlightErrorField(etLastName)
            isValid = false
        }

        // Validate Gender
        if (rgGender.checkedRadioButtonId == -1) {
            highlightErrorField(rbMale)
            highlightErrorField(rbFemale)
            isValid = false
        }

        // Validate Birthday
        if (etBirthday.text.toString().trim().isEmpty()) {
            highlightErrorField(etBirthday)
            isValid = false
        }

        // Validate Address
        if (etAddress.text.toString().trim().isEmpty()) {
            highlightErrorField(etAddress)
            isValid = false
        }

        // Validate Email
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            highlightErrorField(etEmail)
            isValid = false
        } else if (!isValidEmail(email)) {
            highlightErrorField(etEmail)
            isValid = false
            showToast("Please enter a valid email address")
        }

        // Validate Terms
        if (!cbTerms.isChecked) {
            highlightErrorField(cbTerms)
            isValid = false
        }

        if (isValid) {
            registerSuccess()
        } else {
            showToast("Please fill all required fields correctly")
        }
    }

    private fun highlightErrorField(view: View) {
        when (view) {
            is EditText -> view.setBackgroundColor(Color.parseColor("#FFCDD2")) // Light red
            is RadioButton -> view.setBackgroundColor(Color.parseColor("#FFCDD2"))
            is CheckBox -> view.setBackgroundColor(Color.parseColor("#FFCDD2"))
        }
    }

    private fun resetFieldColors() {
        val defaultEditTextColor = Color.parseColor("#E0E0E0")
        val defaultBackgroundColor = Color.TRANSPARENT

        // Reset EditText backgrounds
        etFirstName.setBackgroundColor(defaultEditTextColor)
        etLastName.setBackgroundColor(defaultEditTextColor)
        etBirthday.setBackgroundColor(defaultEditTextColor)
        etAddress.setBackgroundColor(defaultEditTextColor)
        etEmail.setBackgroundColor(defaultEditTextColor)

        // Reset RadioButtons and CheckBox
        rbMale.setBackgroundColor(defaultBackgroundColor)
        rbFemale.setBackgroundColor(defaultBackgroundColor)
        cbTerms.setBackgroundColor(defaultBackgroundColor)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(emailPattern.toRegex())
    }

    private fun registerSuccess() {
        val gender = if (rbMale.isChecked) "Male" else "Female"
        val userInfo = """
            Registration Successful!
            
            First Name: ${etFirstName.text}
            Last Name: ${etLastName.text}
            Gender: $gender
            Birthday: ${etBirthday.text}
            Address: ${etAddress.text}
            Email: ${etEmail.text}
        """.trimIndent()

        showToast(userInfo)

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}