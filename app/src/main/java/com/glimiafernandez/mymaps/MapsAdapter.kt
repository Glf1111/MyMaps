package com.glimiafernandez.mymaps

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glimiafernandez.mymaps.models.UserMap

private const val TAG= "MapsAdapter"
class MapsAdapter(private val context: Context, private val userMap:List <UserMap>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onItemCLick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap =userMap[position]
        holder.itemView.setOnClickListener {
            Log.i(TAG, "clicked positions $position")
            onClickListener.onItemCLick(position)
        }
        val textViewTitle = holder.itemView.findViewById<TextView>(android.R.id.text1)
        textViewTitle.text = userMap.title
    }

    override fun getItemCount()= userMap.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
