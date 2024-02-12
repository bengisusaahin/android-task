package com.bengisusahin.android_task.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.android_task.R
import com.bengisusahin.android_task.model.DataModel

class RecyclerViewAdapter(private var dataList : ArrayList<DataModel>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTask: TextView = itemView.findViewById(R.id.text_task)
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        val textDescription: TextView = itemView.findViewById(R.id.text_description)
        val colorCode: TextView = itemView.findViewById(R.id.text_colorCode)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelClassItem = dataList[position]
        holder.textTask.text = modelClassItem.task
        holder.textDescription.text = modelClassItem.title
        holder.textTitle.text = modelClassItem.description
        holder.colorCode.text= modelClassItem.colorCode

        val colorCode = modelClassItem.colorCode
        if (!colorCode.isNullOrEmpty()) {
            holder.itemView.setBackgroundColor(Color.parseColor(colorCode))
        } else {
            holder.itemView.setBackgroundColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    fun setData(dataList: List<DataModel>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun filterList(filteredData: List<DataModel>) {
        dataList = filteredData as ArrayList<DataModel>
        notifyDataSetChanged() // Adapter'a veri değiştiğini bildir
    }
}