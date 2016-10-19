package za.co.retrorabbit.spicerack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.za.retrorabbit.emmenthal.HelpOverlay;
import co.za.retrorabbit.emmenthal.shape.Focus;
import co.za.retrorabbit.emmenthal.shape.FocusGravity;
import co.za.retrorabbit.emmenthal.utils.HelpOverlayConfiguration;
import co.za.retrorabbit.emmenthal.utils.Utils;

public class StaticOverlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_overlay);
    }

    public void showOverlay(View v) {

        HelpOverlayConfiguration configuration = new HelpOverlayConfiguration();

        configuration.setDotViewEnabled(true)
                .setMessageText("Click this card and see what happens.")
                .setTitleText("Hi There!")
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .setFadeAnimationEnabled(true)
                .setClickTargetOnTouch(false)
                .setDismissOnTouch(true)
                .setDotViewEnabled(true)
                .setTitleStyle(R.style.AppTheme_TextView_Medium_Medium)
                .setMessageStyle(R.style.AppTheme_TextView_Light_Medium)
                .setDotSize(Utils.getDimention(this,R.dimen.dotSize))
                .setCutoutPadding(Utils.getDimention(this,R.dimen.cutoutPadding))
                .setInfoMargin(Utils.getDimention(this,R.dimen.infoMargin))
                .setCutoutStroke(Utils.getDimention(this,R.dimen.cutoutStrokeDiameter))
                .setOverlayColorResource(R.color.overlayBackground)
                .setStrokeResourceColor(R.color.overlayStroke)
                .setTitleResourceColor(R.color.overlayTitleColor)
                .setMessageResourceColor(R.color.overlayMessageColor)
                .setButtonTextColorResourceLeft(android.R.color.white)
                .setButtonTextColorResourceRight(android.R.color.white)
                .setButtonColorResourceLeft(R.color.blue_grey_700)
                .setButtonColorResourceRight(R.color.blue_grey_700);

        HelpOverlay.Builder.start(this)
                .setConfiguration(configuration)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .show(v);
    }
}
