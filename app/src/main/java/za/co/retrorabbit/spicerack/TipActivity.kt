package za.co.retrorabbit.spicerack

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import za.co.retrorabbit.paprika.Tip

class TipActivity : AppCompatActivity() {
    private var fromCode = false
    private lateinit var tipBuilder: Tip.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent.getSerializableExtra(TIP_LAYOUT) as TipLayout) {
            TipActivity.TipLayout.COORDINATOR -> setContentView(R.layout.activity_tip_coordinator)
            TipActivity.TipLayout.RELATIVE -> setContentView(R.layout.activity_tip_relative)
            TipActivity.TipLayout.CUSTOM -> setContentView(R.layout.activity_tip_custom)
        }
        fromCode = intent.getBooleanExtra(TIP_LAYOUT_CODE, false)
        if (fromCode) {
            (findViewById<View>(R.id.showTipButton) as Button).text = "Show"
            (findViewById<View>(R.id.container) as ViewGroup).removeView(findViewById(R.id.tip))
            tipBuilder = Tip.Builder.create(this, R.id.container, R.id.tip, R.string.tipText, R.integer.showDuration, R.integer.animationDuration, R.id.appbar, Tip.AnchorGravity.BELOW)
        }

    }

    fun showTipAgain(view: View) {
        (findViewById<View>(R.id.showTipButton) as Button).text = "Show Again"
        if (fromCode) {
            tipBuilder.show()
        } else
            (findViewById<View>(R.id.tip) as Tip).setTipText(R.string.tipText)
    }

    enum class TipLayout {
        COORDINATOR,
        RELATIVE,
        CUSTOM
    }

    companion object {

        val TIP_LAYOUT = "tip_layout"
        val TIP_LAYOUT_CODE = "tip_layout_code"
    }
}
