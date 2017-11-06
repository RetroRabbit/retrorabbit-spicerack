package za.co.retrorabbit.spicerack

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import java.util.ArrayList

import za.co.retrorabbit.emmenthal.HelpOverlay
import za.co.retrorabbit.emmenthal.utils.HelpOverlayPreferencesManager
import za.co.retrorabbit.spicerack.adapter.Item
import za.co.retrorabbit.spicerack.adapter.ListOverlayAdapter

class ListOverlayActivity : Activity() {

    internal lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_overlay)
        recyclerView = findViewById<View>(R.id.list_item) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ListOverlayAdapter(R.layout.list_overlay_item)

        HelpOverlayPreferencesManager.resetAll(this)
        Toast.makeText(this, "Wait for it", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            HelpOverlay.Builder.start(this@ListOverlayActivity)
                    .setLeftButtonOnClickListener { v -> Toast.makeText(v.context, "LEFT", Toast.LENGTH_SHORT).show() }
                    .setRightButtonOnClickListener { v -> Toast.makeText(v.context, "RIGHT", Toast.LENGTH_SHORT).show() }
                    .setConfiguration(MainActivity.defaultConfiguration.setDelayBeforeShow(100))
                    .setUsageId("list_item") //THIS SHOULD BE UNIQUE ID
                    .show(R.id.imageview_item, R.id.list_frame, recyclerView, 1)
        }, 2000)

    }
}
