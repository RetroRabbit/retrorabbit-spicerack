package za.co.retrorabbit.spicerack.adapter

import android.app.Activity
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import za.co.retrorabbit.emmenthal.HelpOverlay
import za.co.retrorabbit.spicerack.MainActivity
import za.co.retrorabbit.spicerack.R
import java.util.*

/**
 * Created by Werner Scheffer on 2016/10/19.
 */

class ListOverlayAdapter(@field:LayoutRes private val viewLayout: Int) : RecyclerView.Adapter<ListOverlayAdapter.ListViewHolder>() {

    var items: ArrayList<Item>
    val ITEM_COUNT = 50

    init {
        items = ArrayList<Item>()
        for (i in 0 until ITEM_COUNT) {
            items.add(Item("Tile " + i, "Sub Title " + 1))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(viewLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.subTitle.text = items[position].subTitle
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var title: TextView
        internal var subTitle: TextView

        init {

            title = itemView.findViewById<View>(R.id.textview_title) as TextView
            subTitle = itemView.findViewById<View>(R.id.textview_sub_title) as TextView

            itemView.setOnClickListener { v ->
                HelpOverlay.Builder.start(v.context as Activity)
                        .setLeftButtonOnClickListener { Toast.makeText(v.context, "LEFT", Toast.LENGTH_SHORT).show() }
                        .setRightButtonOnClickListener { Toast.makeText(v.context, "RIGHT", Toast.LENGTH_SHORT).show() }
                        .setConfiguration(MainActivity.defaultConfiguration)
                        .setUsageId("list_item_" + (itemView.parent as RecyclerView).getChildLayoutPosition(v)) //THIS SHOULD BE UNIQUE ID
                        .show(v)
            }
        }
    }
}
