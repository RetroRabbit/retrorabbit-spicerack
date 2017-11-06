package za.co.retrorabbit.spicerack

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import za.co.retrorabbit.emmenthal.HelpOverlay

class StaticOverlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_overlay)
    }

    fun showOverlay(v: View) {

        HelpOverlay.Builder.start(this)
                .setLeftButtonOnClickListener { Toast.makeText(v.context, "LEFT", Toast.LENGTH_SHORT).show() }
                .setRightButtonOnClickListener { Toast.makeText(v.context, "RIGHT", Toast.LENGTH_SHORT).show() }
                .setConfiguration(MainActivity.defaultConfiguration)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .show(v)
    }
}
