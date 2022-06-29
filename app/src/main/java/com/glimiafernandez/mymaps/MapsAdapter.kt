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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder  {
       val view = LayoutInflater.from(context).inflate(R.layout.item_user_map,parent,false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap =userMap[position]
        holder.itemView.setOnClickListener {
            Log.i(TAG, "clicked positions $position")
            onClickListener.onItemCLick(position)
        }
        holder.bind(userMap)
    }

    override fun getItemCount()= userMap.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvMapsTitle)
    private var place = itemView.findViewById<TextView>(R.id.tvPlaces)

        fun bind(userMap: UserMap) {
            //Bind the data in the UserMap into the views
            tvTitle.text = userMap.title
           place.text = "Markers: ${userMap.places.size}"

        }




    }
}








