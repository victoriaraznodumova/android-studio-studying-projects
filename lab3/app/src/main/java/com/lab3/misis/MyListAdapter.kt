package com.lab3.misis;


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class MyListAdapter (
    context: Activity,
    private val dataSource: MutableList<ListItem>,
    private val coroutineScope: CoroutineScope,
    private val database: AppDatabase
) : ArrayAdapter<ListItem>(context, R.layout.list_item, dataSource) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        val item = dataSource[position]

        checkBox.text = item.task
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = item.status


        if (item.status) {
            view.setBackgroundColor(context.getColor(R.color.green))
        } else {
            view.setBackgroundColor(context.getColor(android.R.color.white))
        }

        deleteButton.setOnClickListener{
            coroutineScope.launch {
                database.listItemDao().delete(item)
                dataSource.removeAt(position)
                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }


        checkBox.setOnCheckedChangeListener {_, isChecked ->
            coroutineScope.launch {
                item.status = isChecked
                database.listItemDao().update(item)
                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }

        return view

    }
}