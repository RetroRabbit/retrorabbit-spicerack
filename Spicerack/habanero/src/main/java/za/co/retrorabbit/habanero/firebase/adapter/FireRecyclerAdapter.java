package za.co.retrorabbit.habanero.firebase.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import za.co.retrorabbit.habanero.firebase.datatype.FireHashSet;

/**
 * Created by Werner Scheffer on 2016/09/15.
 */
public abstract class FireRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    protected static final int TYPE_HEADER = -2;
    protected static final int TYPE_FOOTER = -3;
    protected static final int TYPE_SUB_HEADER = -4;
    Query mQuery;
    Class<T> mModelClass;
    int mModelLayout;
    Class<VH> mViewHolderClass;
    FireHashSet<T> mData;
    String filterConstraint;
    boolean scrollTo;
    String scrollToKey;
    RecyclerView recyclerView;
    Handler filterHandler;
    Runnable filterRunnable;
    private boolean showFooter = false, showHeader = false, showSubHeader = false;

    public FireRecyclerAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, DatabaseReference mReference) {
        this(mModelClass, mModelLayout, mViewHolderClass, (Query) mReference);
    }

    public FireRecyclerAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, Query mQuery) {
        this.mModelClass = mModelClass;
        this.mModelLayout = mModelLayout;
        this.mViewHolderClass = mViewHolderClass;
        this.mQuery = mQuery;
        mData = new FireHashSet<>(mQuery, this.mModelClass);
        addListeners();
    }

    public void addListeners() {
        mData.setOnChangedListener(new FireHashSet.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyItemInserted(index);
                        /*if (scrollTo) {
                            int scrollToIndex = FireRecyclerAdapter.this.mData.indexOf(scrollToKey);
                            if (scrollToIndex != -1) {
                                recyclerView.getLayoutManager().scrollToPosition(scrollToIndex);
                                //TODO: Firebase
                                *//*if (deleteScrollTo) {
                                    adapter.setDeeplinkPosition(deeplinkItemPosition);
                                    adapter.setDeleteId(scrollToId);
                                }*//*
                                scrollTo = false;
                            }
                        }*/
                        break;
                    case Changed:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyItemChanged(index);
                        break;
                    case Removed:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyItemRemoved(index);
                        break;
                    case Moved:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyItemMoved(oldIndex, index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });
    }

    protected void filterSnapshots() {
        if (filterHandler == null)
            filterHandler = new Handler();
        if (filterRunnable == null) {
            filterRunnable = new Runnable() {
                @Override
                public void run() {
                    getFilter().filter(getFilterConstraint());
                }
            };
        }
        filterHandler.removeCallbacks(filterRunnable);
        filterHandler.postDelayed(filterRunnable, 300);
    }

    @Override
    public Filter getFilter() {
        return customFilter();
    }

    public String getFilterConstraint() {
        return filterConstraint;
    }

    public Filter customFilter() {
        return null;
    }

    public void setFilterConstraint(String filterConstraint) {
        this.filterConstraint = filterConstraint;
        filterSnapshots();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        if (isHeaderType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (isSubHeaderType(viewType)) {
            viewHolder = onCreateSubHeaderViewHolder(parent, viewType);
        } else if (isFooterType(viewType)) {
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;


    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isSubHeaderPosition(position)) {
            onBindSubHeaderViewHolder(holder, position);
        } else if (isFooterPosition(position)) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, getItem(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = mModelLayout;
        if (isHeaderPosition(position)) {
            viewType = TYPE_HEADER;
        } else if (isSubHeaderPosition(position)) {
            viewType = TYPE_SUB_HEADER;
        } else if (isFooterPosition(position)) {
            viewType = TYPE_FOOTER;
        }
        return viewType;
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mData.getKey(position).hashCode();
    }

    @Override
    public int getItemCount() {
        int size = mData.size();
        if (showHeader) {
            size++;
        }
        if (showSubHeader) {
            size++;
        }
        if (showFooter) {
            size++;
        }
        return size;
    }

    /**
     * Invokes onHeaderViewRecycled, onItemViewRecycled or onFooterViewRecycled methods based
     * on the holder.getAdapterPosition()
     */
    @Override
    public final void onViewRecycled(VH holder) {
        int position = holder.getAdapterPosition();

        if (isHeaderPosition(position)) {
            onHeaderViewRecycled(holder);
        } else if (isSubHeaderPosition(position)) {
            onSubHeaderViewRecycled(holder);
        } else if (isFooterPosition(position)) {
            onFooterViewRecycled(holder);
        } else {
            onItemViewRecycled(holder);
        }
    }

    protected void onHeaderViewRecycled(VH holder) {
    }

    protected void onSubHeaderViewRecycled(VH holder) {
    }

    protected void onFooterViewRecycled(VH holder) {
    }

    protected void onItemViewRecycled(VH holder) {
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to 0 and the adapter
     * has a not null header already configured.
     */
    public boolean isHeaderPosition(int position) {
        return showHeader && position == 0;
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected void onBindHeaderViewHolder(VH holder, int position) {
    }

    /*
     * Returns true if the position type parameter passed as argument is equals to
     * <code>getItemCount() - 1</code>
     * and the adapter has a not null header already configured.
     */
    public boolean isSubHeaderPosition(int position) {
        if (showHeader) {
            return showSubHeader && position == 1;
        } else {
            return showSubHeader && position == 0;
        }
    }

    /**
     * If you don't need sub header feature, you can bypass overriding this method.
     */
    protected void onBindSubHeaderViewHolder(VH holder, int position) {
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to
     * <code>getItemCount() - 1</code>
     * and the adapter has a not null header already configured.
     */
    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return showFooter && position == lastPosition;
    }

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected void onBindFooterViewHolder(VH holder, int position) {
    }

    protected abstract void onBindItemViewHolder(VH holder, T model, int position);

    public T getItem(int position) {
        if (showHeader && hasItems()) {
            --position;
        }
        if (showSubHeader && hasItems()) {
            --position;
        }

        return mData.getItem(position);
    }

    /**
     * Returns true if the item configured is not empty.
     */
    private boolean hasItems() {
        return mData.size() > 0;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isSubHeaderType(int viewType) {
        return viewType == TYPE_SUB_HEADER;
    }

    protected VH onCreateSubHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_FOOTER.
     */
    protected boolean isFooterType(int viewType) {
        return viewType == TYPE_FOOTER;
    }

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public void cleanup() {
        mData.cleanup();
    }

    public DatabaseReference getReference(int position) {
        return mData.getReference().child(mData.getKey(position));
    }

    /**
     * Call this method to show/hide header.
     */
    public void showHeader(boolean value) {
        this.showHeader = value;

        if (!this.showHeader)
            notifyItemRemoved(0);
        else
            notifyItemInserted(0);
        // notifyDataSetChanged();
    }

    /**
     * Call this method to show/hide sub header.
     */
    public void showSubHeader(boolean value) {
        this.showSubHeader = value;

        if (!this.showSubHeader) {
            if (!this.showHeader)
                notifyItemRemoved(0);
            else
                notifyItemRemoved(1);
        } else {
            if (!this.showHeader)
                notifyItemInserted(0);
            else
                notifyItemInserted(1);
        }
    }

    /**
     * Call this method to show/hide footer.
     */
    public void showFooter(boolean value) {
        this.showFooter = value;
        notifyDataSetChanged();
    }

    public void setFilteredMap(ArrayList<String> filteredMap) {
        this.mData.setFilteredMap(filteredMap);
        notifyDataSetChanged();
    }

    public void clearFilter() {
        if (this.mData.isFiltered()) {
            this.mData.clearFilter();
            notifyDataSetChanged();
        }
    }

    public FireHashSet<T> getData() {
        return mData;
    }

    public void removeListeners() {
        mData.removeOnChangedListener();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
    }

    public void setScrollTo(String key) {
        scrollToKey = key;
        scrollTo = true;
    }

    public int getItemPosition(String key) {
        return getData().indexOf(key);
    }
}
