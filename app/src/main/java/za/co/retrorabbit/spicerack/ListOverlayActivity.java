package za.co.retrorabbit.spicerack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import co.za.retrorabbit.emmenthal.HelpOverlay;
import co.za.retrorabbit.emmenthal.shape.Focus;
import co.za.retrorabbit.emmenthal.shape.FocusGravity;
import co.za.retrorabbit.emmenthal.utils.HelpOverlayConfiguration;
import co.za.retrorabbit.emmenthal.utils.PreferencesManager;
import za.co.retrorabbit.spicerack.adapter.Item;
import za.co.retrorabbit.spicerack.adapter.ListOverlayAdapter;

public class ListOverlayActivity extends Activity {

    private static final int ITEM_COUNT = 50;

    RecyclerView recyclerView;
    HelpOverlayConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overlay);
        recyclerView = ((RecyclerView) findViewById(R.id.list_item));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListOverlayAdapter(getItems(), R.layout.list_overlay_item));

        PreferencesManager.resetAll(this);

        configuration = new HelpOverlayConfiguration().setDotViewEnabled(true)
                .setMessageText("Click this card and see what happens.")
                .setTitleText("Hi There!")
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayBeforeShow(2000)
                .setFadeAnimationEnabled(true)
                .setClickTargetOnTouch(false)
                .setDismissOnTouch(true)
                .setDotViewEnabled(true)
                .setDelayBeforeShow(2000)
                .setDotSizeResource(R.dimen.dotSize)
                .setDotColorResource(R.color.colorAccent)
                .setCutoutColorResource(android.R.color.transparent)
                .setCutoutStrokeColorResource(R.color.blue_grey_200, 0.8f)
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
                .setButtonColorResourceLeft(R.color.blue_grey_700)
                .setButtonColorResourceRight(R.color.blue_grey_700);

        HelpOverlay.Builder.start(ListOverlayActivity.this)
                .setConfiguration(configuration)
                .setUsageId("CBD_0_Location") //THIS SHOULD BE UNIQUE ID
                .show(R.id.imageview_item, R.id.list_frame, recyclerView, 1);
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; i++) {
            items.add(new Item("Tile" + i, "SubTitle" + 1));
        }
        return items;
    }
}
