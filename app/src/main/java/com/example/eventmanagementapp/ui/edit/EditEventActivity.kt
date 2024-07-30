package com.example.eventmanagementapp.ui.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eventmanagementapp.utils.Constants
import com.example.eventmanagementapp.R
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.databinding.ActivityEditBinding
import com.example.eventmanagementapp.utils.Converters
import com.example.eventmanagementapp.utils.convert24HourTo12Hour
import com.google.android.material.chip.Chip
import java.util.Calendar
import java.util.Locale


class EditEventActivity : AppCompatActivity() {

    private val editEventViewModel by viewModels<EditEventViewModel>()
    private var isNewEvent: Boolean = false
    private val binding by lazy { ActivityEditBinding.inflate(layoutInflater) }
    private var mSelectedDay = 0
    private var mSelectedMonth = 0
    private var mSelectedYear = 0
    private var mSelectedHour = 0
    private var mSelectedMinute = 0
    private var selectedParticipant :MutableList<String> = mutableListOf()
    private var eventId = 0
    private val toolBarTitle by lazy { binding.root.findViewById<TextView>(R.id.toolbarTitle) }
    private val ivSave by lazy { binding.root.findViewById<ImageView>(R.id.ivSave) }
    private val ivDelete by lazy { binding.root.findViewById<ImageView>(R.id.ivDelete) }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EditEventActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.btnSelectDate.setOnClickListener {
            getDateFromPicker()
        }

        binding.btnSelectTime.setOnClickListener {
            getTimeFromPicker()
        }

        binding.btnAddParticipant.setOnClickListener {
            if (binding.edtEnterParticipant.text.toString().isBlank())
                Toast.makeText(this,"Participant cannot be empty",Toast.LENGTH_LONG).show()
            else
                 addChipToGroup(binding.edtEnterParticipant.text.toString())
        }

        ivSave.setOnClickListener {
            if (binding.edtEventName.text.isNotBlank())
                saveAndReturn()
            else
                Toast.makeText(this,"Event Name cannot be Empty or Blank",Toast.LENGTH_LONG).show()
        }

        ivDelete.setOnClickListener {
            editEventViewModel.deleteEvent()
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDateFromPicker() {
        // Get Current Date
        if (mSelectedYear == 0) {
            val c: Calendar = Calendar.getInstance()
            mSelectedYear = c.get(Calendar.YEAR)
            mSelectedMonth = c.get(Calendar.MONTH)
            mSelectedDay = c.get(Calendar.DAY_OF_MONTH)
        }

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->run {
                binding.tvDate.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                mSelectedYear = year
                mSelectedMonth = monthOfYear
                mSelectedMinute = dayOfMonth
            }
            },
            mSelectedYear,
            mSelectedMonth,
            mSelectedDay
        )
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun getTimeFromPicker() {

        // Get Current Time
        if (mSelectedHour == 0) {
            val c = Calendar.getInstance()
            mSelectedHour = c[Calendar.HOUR_OF_DAY]
            mSelectedMinute = c[Calendar.MINUTE]
        }


        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                run {
                    val timeStr = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    val convertedTime = convert24HourTo12Hour(timeStr)
                    binding.tvTime.text = convertedTime
                    mSelectedHour = hourOfDay
                    mSelectedMinute = minute
                }
            },
            mSelectedHour,
            mSelectedMinute,
            false
        )
        timePickerDialog.show()
    }

    private fun initViewModel() {
        editEventViewModel.mLiveData.observe(this) { event ->
            if (event != null) {
                binding.edtEventName.setText(event.name)
                binding.tvDate.text = event.date
                binding.tvTime.text = event.time
                binding.edtLocation.setText(event.location)
                binding.edtDescription.setText(event.description)
                if (event.participantsList.isNotBlank()) {
                    val selectedParticipants = Converters().jsonStringToList(event.participantsList)
                    selectedParticipants.map {
                        addChipToGroup(it)
                    }
                }
            }
        }
        val extras = intent.extras
        if (extras == null) {
            toolBarTitle.text= getString(R.string.new_event)
            isNewEvent = true
        } else {
            ivDelete.visibility = View.VISIBLE
            toolBarTitle.text = getString(R.string.edit_event)
            eventId = extras.getInt(Constants.EVENT_ID_KEY)
            editEventViewModel.loadData(eventId)
        }
    }

    private fun saveAndReturn() {
        val eventEntity = EventEntity(
            id = eventId,
            name = binding.edtEventName.text.toString(),
            date = binding.tvDate.text.toString(),
            time = binding.tvTime.text.toString(),
            location = binding.edtLocation.text.toString(),
            description = binding.edtDescription.text.toString(),
            participantsList = Converters().listToJsonString(selectedParticipant)
        )
        if (isNewEvent)
            editEventViewModel.saveEvent(eventEntity)
        else
            editEventViewModel.updateEvent(eventEntity)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun addChipToGroup(person: String) {
        binding.edtEnterParticipant.setText("")
        selectedParticipant.add(person)
        val chip = Chip(this)
        chip.text = person
        chip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = true
        chip.isClickable = true
        chip.isCheckable = false
        binding.chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener {
            selectedParticipant.remove(chip.text.toString())
            binding.chipGroup.removeView(chip as View)
        }
    }

}