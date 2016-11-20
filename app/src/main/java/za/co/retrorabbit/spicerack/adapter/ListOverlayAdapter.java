package za.co.retrorabbit.spicerack.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import za.co.retrorabbit.emmenthal.HelpOverlay;
import za.co.retrorabbit.spicerack.MainActivity;
import za.co.retrorabbit.spicerack.R;

/**
 * Created by Werner Scheffer on 2016/10/19.
 */

public class ListOverlayAdapter extends RecyclerView.Adapter<ListOverlayAdapter.ListViewHolder> {

    ArrayList<Item> items;
    @LayoutRes
    private int viewLayout;

    public ListOverlayAdapter(ArrayList<Item> items, int viewLayout) {
        this.items = items;
        this.viewLayout = viewLayout;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.subTitle.setText(items.get(position).getSubTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView title, subTitle;

        public ListViewHolder(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textview_title);
            subTitle = (TextView) itemView.findViewById(R.id.textview_sub_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelpOverlay.Builder.start((Activity) v.getContext())
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
                            .setConfiguration(MainActivity.getDefaultConfiguration())
                            .setUsageId("list_item_" + ((RecyclerView) itemView.getParent()).getChildLayoutPosition(v)) //THIS SHOULD BE UNIQUE ID
                            .show(v);
                }
            });
        }
    }
}
