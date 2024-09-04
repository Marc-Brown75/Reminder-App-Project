package edu.qc.seclass.rlm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ReminderListAdapter(private val context: Context, private val dataset: ArrayList<ReminderList>
) : RecyclerView.Adapter<ReminderListAdapter.ItemViewHolder>() {
    private var onClickListener: ((reminderList:ReminderList) -> Unit)? = null


    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.ListName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  item.ListName //context.resources.getString(item.stringResourceId)
        //Adding on click listener.
        holder.itemView.setOnClickListener {
            // Call the onClickListener with the clicked position
            onClickListener?.invoke(item)

        }


    }



    fun setOnClickListener(listener: (reminderList: ReminderList) -> Unit) {
        onClickListener = listener
    }





}
