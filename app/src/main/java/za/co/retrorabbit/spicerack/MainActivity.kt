package za.co.retrorabbit.spicerack

import android.content.Intent
import android.os.Bundle
import android.support.transition.VisibilityPropagation
import android.support.v7.app.AppCompatActivity
import android.view.View

import za.co.retrorabbit.emmenthal.shape.Focus
import za.co.retrorabbit.emmenthal.shape.FocusGravity
import za.co.retrorabbit.emmenthal.utils.HelpOverlayConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun previewEmmenthalStatic(v: View) {
        val intent = Intent(this, StaticOverlayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    fun previewEmmenthalList(v: View) {
        val intent = Intent(this, ListOverlayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    fun previewPaprikaTip(v: View) {
        val intent = Intent(this, TipActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        when (v.id) {
            R.id.button_paprika_tip_coordinator -> intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.COORDINATOR)
            R.id.button_paprika_tip_relative -> intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.RELATIVE)
            R.id.button_paprika_tip_custom -> intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.CUSTOM)
            R.id.button_paprika_tip_code -> {
                intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.COORDINATOR)
                intent.putExtra(TipActivity.TIP_LAYOUT_CODE, true)
            }
        }
        startActivity(intent)
    }

    companion object {

        //.setButtonColorResourceLeft(R.color.brown_200)
        //.setButtonColorResourceRight(R.color.brown_200)
        val defaultConfiguration: HelpOverlayConfiguration
            get() = HelpOverlayConfiguration().setDotViewEnabled(true)
                    .setMessageText("Click this card and see what happens.")
                    .setTitleText("Hi There!")
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setFadeAnimationEnabled(true)
                    .setClickTargetOnTouch(false)
                    .setDismissOnTouch(true)
                    .setDotViewEnabled(true)
                    .setDotSizeResource(R.dimen.dotSize)
                    .setDotColorResource(R.color.overlayDotColor)
                    .setCutoutColorResource(android.R.color.transparent)
                    .setCutoutStrokeColorResource(R.color.brown_200, 0.8f)
                    .setCutoutRadiusResource(R.dimen.cutoutRadius)
                    .setInfoMarginResource(R.dimen.infoMargin)
                    .setCutoutStrokeSizeResource(R.dimen.cutoutStrokeSize)
                    .setTitleStyle(R.style.AppTheme_TextView_Medium_Medium)
                    .setMessageStyle(R.style.AppTheme_TextView_Light_Medium)
                    .setOverlayColorResource(R.color.overlayBackground)
                    .setTitleResourceColor(R.color.overlayTitleColor)
                    .setMessageResourceColor(R.color.overlayMessageColor)
                    .setButtonTextColorResourceLeft(android.R.color.white)
                    .setButtonTextColorResourceRight(android.R.color.white)
                    .setButtonColorResourceLeft(android.R.color.transparent)
                    .setButtonColorResourceRight(android.R.color.transparent)
                    .setButtonTextResourceLeft(android.R.string.yes)
                    .setButtonTextResourceRight(android.R.string.no)
                    .setButtonVisibilityRight(HelpOverlayConfiguration.Visibility.VISIBLE)
                    .setButtonVisibilityLeft(HelpOverlayConfiguration.Visibility.VISIBLE)
    }
}
