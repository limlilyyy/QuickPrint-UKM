package com.example.quickprintukm

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object OrderPreferences {

    private const val PREF_NAME = "order_pref"
    private const val KEY_ORDERS = "order_list"

    fun saveOrders(context: Context, orders: List<Order>) {
        val shared = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = shared.edit()

        val json = Gson().toJson(orders)
        editor.putString(KEY_ORDERS, json)
        editor.apply()
    }

    fun loadOrders(context: Context): MutableList<Order> {
        val shared = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = shared.getString(KEY_ORDERS, null)

        return if (json != null) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
