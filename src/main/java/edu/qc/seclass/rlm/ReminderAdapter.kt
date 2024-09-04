package edu.qc.seclass.rlm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ReminderAdapter(private val context: Context, private val dataset: ArrayList<Reminder>
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {
    private var onClickListener: ((position: Int) -> Unit)? = null


    class ReminderViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.ReminderName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return ReminderViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  item.reminderName //context.resources.getString(item.stringResourceId)
        //Adding on click listener.
        holder.itemView.setOnClickListener {
            // Call the onClickListener with the clicked position
            onClickListener?.invoke(position)

        }

    }

    fun setOnClickListener(listener: (position: Int) -> Unit) {
        onClickListener = listener
    }





}
