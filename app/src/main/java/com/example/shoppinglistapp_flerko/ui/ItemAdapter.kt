package com.example.shoppinglistapp_flerko.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.shoppinglistapp_flerko.R
import com.example.shoppinglistapp_flerko.data.ShoppingItem
import com.example.shoppinglistapp_flerko.data.ShoppingRepository

class ItemAdapter(private val context: Context, private var items: List<ShoppingItem>) : BaseAdapter() {

    fun updateItems(newItems: List<ShoppingItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.item_shopping, parent, false)

        val item = items[position]

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvDetails = view.findViewById<TextView>(R.id.tvDetails)
        val cbBought = view.findViewById<CheckBox>(R.id.cbBought)

        tvName.text = item.name
        tvDetails.text = "Кол-во: ${item.quantity}, Цена: ${item.price} ₽"
        cbBought.isChecked = item.bought

        cbBought.setOnCheckedChangeListener { _, isChecked ->
            val updatedItem = item.copy(bought = isChecked)
            ShoppingRepository.getInstance(context).updateItem(updatedItem)
        }

        return view
    }
}