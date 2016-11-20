package za.co.retrorabbit.spicerack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import za.co.retrorabbit.emmenthal.HelpOverlay;
import za.co.retrorabbit.emmenthal.utils.HelpOverlayPreferencesManager;
import za.co.retrorabbit.spicerack.adapter.Item;
import za.co.retrorabbit.spicerack.adapter.ListOverlayAdapter;

public class ListOverlayActivity extends Activity {

    private static final int ITEM_COUNT = 50;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overlay);
        recyclerView = ((RecyclerView) findViewById(R.id.list_item));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListOverlayAdapter(getItems(), R.layout.list_overlay_item));

        HelpOverlayPreferencesManager.resetAll(this);
        Toast.makeText(this, "Wait for it", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HelpOverlay.Builder.start(ListOverlayActivity.this)
                        .setLeftButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "LEFT", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setRightButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "RIGHT", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setConfiguration(MainActivity.getDefaultConfiguration().setDelayBeforeShow(2000))
                        .setUsageId("list_item") //THIS SHOULD BE UNIQUE ID
                        .show(R.id.imageview_item, R.id.list_frame, recyclerView, 1);
            }
        }, 2000);

    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; i++) {
            items.add(new Item("Tile " + i, "Sub Title " + 1));
        }
        return items;
    }
}
