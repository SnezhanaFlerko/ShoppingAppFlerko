package com.example.shoppinglistapp_flerko.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglistapp_flerko.R
import com.example.shoppinglistapp_flerko.data.ShoppingItem
import com.example.shoppinglistapp_flerko.data.ShoppingRepository
import com.example.shoppinglistapp_flerko.util.NotificationUtil
import java.text.SimpleDateFormat
import java.util.Calendar

fun AppCompatActivity.showAddItemDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
    val etName = view.findViewById<EditText>(R.id.etName)
    val etQuantity = view.findViewById<EditText>(R.id.etQuantity)
    val etPrice = view.findViewById<EditText>(R.id.etPrice)
    val cbSetReminder = view.findViewById<CheckBox>(R.id.cbSetReminder)
    val tvReminderTime = view.findViewById<TextView>(R.id.tvReminderTime)

    val calendar = Calendar.getInstance()

    tvReminderTime.setOnClickListener {
        val dpd = DatePickerDialog(this, { _, year, month, day ->
            val tpd = TimePickerDialog(this, { _, hour, minute ->
                calendar.set(year, month, day, hour, minute)
                tvReminderTime.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            tpd.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dpd.show()
    }

    val dialog = AlertDialog.Builder(this)
        .setTitle("Добавить покупку")
        .setView(view)
        .setPositiveButton("OK", null)
        .setNegativeButton("Отмена", null)
        .create()

    dialog.setOnShowListener {
        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val name = etName.text.toString()
            val quantity = etQuantity.text.toString().toIntOrNull() ?: 1
            val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val reminderTime = if (cbSetReminder.isChecked) calendar.timeInMillis else null

            if (name.isNotBlank()) {
                val item = ShoppingItem(name = name, quantity = quantity, price = price, reminderTime = reminderTime)
                ShoppingRepository.getInstance(this).saveItem(item)

                if (reminderTime != null) {
                    NotificationUtil.scheduleReminder(this, reminderTime, name)
                }

                dialog.dismiss()
            } else {
                Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
            }
        }
    }

    dialog.show()
}