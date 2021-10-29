package com.riski.noteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riski.noteapp.databinding.ActivityAlarmBinding
import com.riski.noteapp.services.AlarmReceiver
import com.riski.noteapp.utils.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        binding.apply {
            btnTime.setOnClickListener {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }

            btnSetAlarm.setOnClickListener {
                val repeatTime = binding.tvTime.text.toString()
                val repeatMessage = edtMessage.text.toString()
                alarmReceiver.setRepeatingAlarm(this@AlarmActivity, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            }

            btnCancelAlarm.setOnClickListener {
                alarmReceiver.cancelAlarm(this@AlarmActivity, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // menyiapkan time format
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_REPEAT_TAG -> binding.tvTime.text = dateFormat.format(calendar.time)
        }

    }
}