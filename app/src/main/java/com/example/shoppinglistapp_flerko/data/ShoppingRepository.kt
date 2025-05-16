package com.example.shoppinglistapp_flerko.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.*

class ShoppingRepository private constructor(private val context: Context) {

    private val FILE_NAME = "shopping_list.json"
    private val shoppingItemsLiveData = MutableLiveData<List<ShoppingItem>>(emptyList())

    companion object {
        private var instance: ShoppingRepository? = null
        fun getInstance(context: Context): ShoppingRepository {
            return instance ?: synchronized(this) {
                instance ?: ShoppingRepository(context).also { instance = it }
            }
        }
    }

    init {
        loadItemsFromFile(context)
    }

    fun getItems(): LiveData<List<ShoppingItem>> = shoppingItemsLiveData

    fun saveItem(item: ShoppingItem) {
        val currentList = shoppingItemsLiveData.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        updateAndSave(currentList, context)
    }

    fun updateItem(item: ShoppingItem) {
        val currentList = shoppingItemsLiveData.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList[index] = item
            updateAndSave(currentList, context)
        }
    }

    fun deleteItem(item: ShoppingItem) {
        val currentList = shoppingItemsLiveData.value?.toMutableList() ?: return
        currentList.remove(item)
        updateAndSave(currentList, context)
    }

    private fun updateAndSave(list: List<ShoppingItem>, context: Context) {
        shoppingItemsLiveData.postValue(list)
        saveItemsToFile(context, list)
    }

    private fun saveItemsToFile(context: Context, items: List<ShoppingItem>) {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            val json = android.util.JsonWriter(OutputStreamWriter(fileOutputStream))
            json.beginArray()
            for (item in items) {
                json.beginObject()
                json.name("id").value(item.id)
                json.name("name").value(item.name)
                json.name("quantity").value(item.quantity)
                json.name("price").value(item.price)
                json.name("bought").value(item.bought)
                json.name("reminderTime").value(item.reminderTime)
                json.endObject()
            }
            json.endArray()
            json.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadItemsFromFile(context: Context) {
        try {
            val fileInputStream: FileInputStream = context.openFileInput(FILE_NAME)
            val reader = InputStreamReader(fileInputStream)
            val json = android.util.JsonReader(reader)
            val items = mutableListOf<ShoppingItem>()
            json.beginArray()
            while (json.hasNext()) {
                json.beginObject()
                var id = ""
                var name = ""
                var quantity = 1
                var price = 0.0
                var bought = false
                var reminderTime: Long? = null
                while (json.hasNext()) {
                    val nameField = json.nextName()
                    when (nameField) {
                        "id" -> id = json.nextString()
                        "name" -> name = json.nextString()
                        "quantity" -> quantity = json.nextInt()
                        "price" -> price = json.nextDouble()
                        "bought" -> bought = json.nextBoolean()
                        "reminderTime" -> reminderTime = json.nextLong()
                        else -> json.skipValue()
                    }
                }
                json.endObject()
                items.add(ShoppingItem(id, name, quantity, price, bought, reminderTime))
            }
            json.endArray()
            updateAndSave(items, context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}