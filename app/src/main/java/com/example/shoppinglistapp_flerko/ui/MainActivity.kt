package com.example.shoppinglistapp_flerko.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglistapp_flerko.R
import com.example.shoppinglistapp_flerko.data.ShoppingRepository
import com.example.shoppinglistapp_flerko.data.ShoppingItem
import com.example.shoppinglistapp_flerko.util.NotificationUtil
import com.example.shoppinglistapp_flerko.ui.ThemeManager
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemAdapter
    private var darkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        val listView = findViewById<ListView>(R.id.listView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnToggleTheme = findViewById<Button>(R.id.btnToggleTheme)
        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)

        adapter = ItemAdapter(this, emptyList())
        listView.adapter = adapter

        ShoppingRepository.getInstance(this).getItems().observe(this) { items ->
            adapter.updateItems(items)
            calculateTotalPrice(items, tvTotalPrice)
        }

        btnAdd.setOnClickListener {
            showAddItemDialog()
        }


        btnToggleTheme.setOnClickListener {
            darkMode = !darkMode
            ThemeManager.setDarkMode(this, darkMode)
            recreate()
        }

        ThemeManager.setDarkMode(this, darkMode)
    }

    private fun calculateTotalPrice(items: List<ShoppingItem>, tv: TextView) {
        val total = items.filterNot { it.bought }.sumOf { it.price * it.quantity }
        tv.text = getString(R.string.total_price, total)
    }
}