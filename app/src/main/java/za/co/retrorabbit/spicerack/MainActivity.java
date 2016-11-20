package za.co.retrorabbit.spicerack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import za.co.retrorabbit.emmenthal.shape.Focus;
import za.co.retrorabbit.emmenthal.shape.FocusGravity;
import za.co.retrorabbit.emmenthal.utils.HelpOverlayConfiguration;

public class MainActivity extends AppCompatActivity {

    public static HelpOverlayConfiguration getDefaultConfiguration() {
        return new HelpOverlayConfiguration().setDotViewEnabled(true)
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
                .setButtonColorResourceLeft(R.color.brown_200)
                .setButtonColorResourceRight(R.color.brown_200)
                .setButtonTextResourceLeft(android.R.string.yes)
                .setButtonTextResourceRight(android.R.string.no);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void previewEmmenthalStatic(View v) {
        Intent intent = new Intent(this, StaticOverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    public void previewEmmenthalList(View v) {
        Intent intent = new Intent(this, ListOverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    public void previewPaprikaTip(View v) {
        Intent intent = new Intent(this, TipActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        switch (v.getId()) {
            case R.id.button_paprika_tip_coordinator:
                intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.COORDINATOR);
                break;
            case R.id.button_paprika_tip_relative:
                intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.RELATIVE);
                break;
            case R.id.button_paprika_tip_custom:
                intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.CUSTOM);
                break;
            case R.id.button_paprika_tip_code:
                intent.putExtra(TipActivity.TIP_LAYOUT, TipActivity.TipLayout.COORDINATOR);
                intent.putExtra(TipActivity.TIP_LAYOUT_CODE, true);
                break;

        }
        startActivity(intent);
    }
}
