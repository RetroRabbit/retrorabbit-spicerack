package za.co.retrorabbit.habanero.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import za.co.retrorabbit.habanero.firebase.datatype.FireHashSet;
import za.co.retrorabbit.habanero.firebase.datatype.SparseBooleanArrayParcelable;
import za.co.retrorabbit.habanero.firebase.interfaces.ExpandCollapseListener;
import za.co.retrorabbit.habanero.firebase.interfaces.GroupExpandCollapseListener;
import za.co.retrorabbit.habanero.firebase.interfaces.OnGroupClickListener;

/**
 * Created by Bernhard Müller on 11/18/2016.
 */

public abstract class ExpandableFireRecyclerViewAdapter<G, C, GVH extends ExpandableFireRecyclerViewAdapter.ViewHolder, CVH extends ExpandableFireRecyclerViewAdapter.ViewHolder>
        extends RecyclerView.Adapter implements ExpandCollapseListener<G>, OnGroupClickListener, Filterable {

    private static final String EXPAND_STATE_MAP = "expandable_recyclerview_adapter_expand_state_map";
    public SparseBooleanArray expandedGroupIndexes;
    String filterConstraint;
    private FireHashSet<G> mData;
    private Comparator<? super G> sorter;
    private FireHashSet.OnChangedListener onChangedListener = new FireHashSet.OnChangedListener() {
        @Override
        public void onChanged(EventType type, int index, int oldIndex) {
            switch (type) {
                case Added:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyItemInserted(index);
                    if (sorter != null)
                        sort();
                    break;
                case Changed:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyItemChanged(index);
                    if (sorter != null)
                        sort();
                    break;
                case Removed:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyItemRemoved(index);
                    if (sorter != null)
                        sort();
                    break;
                case Moved:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyItemMoved(oldIndex, index);
                    if (sorter != null)
                        sort();
                    break;
                default:
                    throw new IllegalStateException("Incomplete case statement");
            }
        }
    };
    private OnGroupClickListener groupClickListener;
    private GroupExpandCollapseListener<G> expandCollapseListener;

    public ExpandableFireRecyclerViewAdapter(FireHashSet mData) {
        this.mData = mData;
        addListeners();
        this.expandedGroupIndexes = new SparseBooleanArray();
    }

    public ExpandableFireRecyclerViewAdapter(Query mQuery, Class<G> mGroupModelClass) {
        this(new FireHashSet(mQuery, mGroupModelClass));
    }

    public ExpandableFireRecyclerViewAdapter(DatabaseReference mReference, Class<G> mGroupModelClass) {
        this((Query) mReference, mGroupModelClass);
    }

    public void addListeners() {
        mData.addOnChangedListener(onChangedListener);
    }

    public void removeListeners() {
        if (mData != null)
            mData.removeOnChangedListener(onChangedListener);
    }

    public int getItemPosition(String key) {
        return getData().indexOf(key);
    }

    public FireHashSet<G> getData() {
        return mData;
    }

    public void cleanup() {
        mData.cleanup();
    }

    public DatabaseReference getRef(int position) {
        return mData.getReference().child(mData.getKey(position));
    }

    public void setReversed(boolean reversed) {
        mData.setReversed(reversed);
        if (getGroupCount() > 0)
            notifyDataSetChanged();
    }

    public void setValueParser(FireHashSet.ValueParser<G> valueParser) {
        mData.setValueParser(valueParser);
    }

    public long getGroupId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mData.getKey(position).hashCode();
    }

    public String getFilterConstraint() {
        return filterConstraint;
    }

    public void setFilterConstraint(String filterConstraint) {
        this.filterConstraint = filterConstraint;
        filterSnapshots();
    }

    public void filterSnapshots() {
        getFilter().filter(getFilterConstraint());
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public abstract List<C> getGroupChildren(G item);

    public int getChildrenCount(int groupPosition) {
        return getGroupChildren(mData.getItem(groupPosition)).size();
    }

    public int getGroupCount() {
        return mData.size();
    }

    public final C getChild(int groupPosition, int childPosition) {
        List<C> items = getGroupChildren(getGroup(groupPosition));
        return items.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public void setGroupSorter(Comparator<? super G> sorter) {
        this.sorter = sorter;
    }

    /**
     * Sorts the content of this adapter using the set sorter.
     */
    public void sort() {
        if (sorter != null)
            sort(sorter);
    }

    /**
     * Sorts the content of this adapter using the specified sorter.
     *
     * @param comparator The sorter used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(@NonNull Comparator<? super G> comparator) {
        ArrayList<G> sortedList = new ArrayList<G>();
        if (mData.isFiltered()) {
            sortedList = new ArrayList<G>(mData.getFilteredValues().values());
        } else {
            sortedList = new ArrayList<G>(mData.getValues().values());
        }
        Collections.sort(sortedList, comparator);

        mData.createIndexMapFromList(sortedList, mData.isFiltered() ? mData.getFilteredValues() : mData.getValues());
        notifyDataSetChanged();
    }

    public G getGroup(int position) {
        return mData.getItem(position);
    }

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either {@link #onCreateGroupViewHolder(ViewGroup, int)}
     * or {@link #onCreateChildViewHolder(ViewGroup, int)}}.
     *
     * @param parent   The {@link ViewGroup} into which the new {@link android.view.View}
     *                 will be added after it is bound to an adapter position.
     * @param viewType The view type of the new {@code android.view.View}.
     * @return Either a new {@link GVH} or a new {@link CVH}
     * that holds a {@code android.view.View} of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ExpandableListPosition.GROUP:
                GVH gvh = onCreateGroupViewHolder(parent, viewType);
                return gvh;
            case ExpandableListPosition.CHILD:
                CVH cvh = onCreateChildViewHolder(parent, viewType);
                return cvh;
            default:
                throw new IllegalArgumentException("viewType is not valid");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableListPosition listPos = getUnflattenedPosition(position);
        G group = getGroup(listPos.groupPos);
        switch (listPos.type) {
            case ExpandableListPosition.GROUP:
                onBindGroupViewHolder((GVH) holder, position, group);
                break;
            case ExpandableListPosition.CHILD:
                onBindChildViewHolder((CVH) holder, position, group, listPos.childPos, getChild(listPos.groupPos, listPos.childPos));
                break;
        }
    }

    /**
     * @return the number of group and child objects currently expanded
     * @see #getVisibleItemCount()
     */
    @Override
    public int getItemCount() {
        return getVisibleItemCount();
    }

    /**
     * @param listPosition representing either a child or a group
     * @return the index of a group within the {@link #getVisibleItemCount()}
     */
    public int getFlattenedGroupIndex(ExpandableListPosition listPosition) {
        return getFlattenedGroupIndex(listPosition.groupPos);
    }

    public int getFlattenedGroupIndex(int groupIndex) {
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }

    /**
     * @return the total number visible rows
     */
    public int getVisibleItemCount() {
        int count = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            count += numberOfVisibleItemsInGroup(i);
        }
        return count;
    }

    /**
     * @param group the index of the {@link G} in the full collection {@link G}
     * @return the number of visible row items for the particular group. If the group is collapsed,
     * return 1 for the group header. If the group is expanded return total number of children in the
     * group + 1 for the group header
     */
    private int numberOfVisibleItemsInGroup(int group) {
        if (expandedGroupIndexes.get(group)) {
            return getGroupChildren(getGroup(group)).size() + 1;
        } else {
            return 1;
        }
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The flat position in the list to get the view type of
     * @return {@value ExpandableListPosition#CHILD} or {@value ExpandableListPosition#GROUP}
     * @throws RuntimeException if the item at the given position in the list is not found
     */
    @Override
    public int getItemViewType(int position) {
        return getUnflattenedPosition(position).type;
    }

    public ExpandableListPosition getUnflattenedPosition(int flPos) {
        int groupItemCount;
        int adapted = flPos;
        for (int i = 0; i < mData.size(); i++) {
            groupItemCount = numberOfVisibleItemsInGroup(i);
            if (adapted == 0) {
                return ExpandableListPosition.obtain(ExpandableListPosition.GROUP, i, -1, flPos);
            } else if (adapted < groupItemCount) {
                return ExpandableListPosition.obtain(ExpandableListPosition.CHILD, i, adapted - 1, flPos);
            }
            adapted -= groupItemCount;
        }
        throw new RuntimeException("Unknown state");
    }

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the {@link G}
     * @param itemCount     the total number of children in the {@link G}
     */
    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        notifyItemRangeInserted(positionStart, itemCount);
        if (expandCollapseListener != null) {
            int groupIndex = getUnflattenedPosition(positionStart).groupPos;
            expandCollapseListener.onGroupExpanded(getGroup(groupIndex));
        }
    }

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the {@link G}
     * @param itemCount     the total number of children in the {@link G}
     */
    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        notifyItemRangeRemoved(positionStart, itemCount);
        if (expandCollapseListener != null) {
            //minus one to return the position of the header, not first child
            int groupIndex = getUnflattenedPosition(positionStart - 1).groupPos;
            expandCollapseListener.onGroupCollapsed(getGroup(groupIndex));
        }
    }

    /**
     * Triggered by a click on a {@link ViewHolder}
     *
     * @param flatPos the flat position of the {@link ViewHolder} that was clicked
     * @return false if click expanded group, true if click collapsed group
     */
    @Override
    public boolean onGroupClick(int flatPos) {
        if (groupClickListener != null) {
            groupClickListener.onGroupClick(flatPos);
        }
        return toggleGroup(flatPos);
    }

    /**
     * @param flatPos The flat list position of the group
     * @return false if the group is expanded, *after* the toggle, true if the group is now collapsed
     */
    public boolean toggleGroup(int flatPos) {
        ExpandableListPosition listPos = getUnflattenedPosition(flatPos);
        boolean expanded = expandedGroupIndexes.get(listPos.groupPos);
        if (expanded) {
            collapseGroup(listPos);
        } else {
            expandGroup(listPos);
        }
        return expanded;
    }

    public boolean toggleGroup(int flatPos, boolean expand) {
        ExpandableListPosition listPos = getUnflattenedPosition(flatPos);
        boolean expanded = expandedGroupIndexes.get(listPos.groupPos);
        if (expanded == expand)
            return true;
        if (expand) {
            expandGroup(listPos);
        } else {
            collapseGroup(listPos);
        }
        return expanded;
    }

    /**
     * Collapse a group
     *
     * @param listPosition position of the group to collapse
     */
    private void collapseGroup(ExpandableListPosition listPosition) {
        expandedGroupIndexes.put(listPosition.groupPos, false);
        onGroupCollapsed(getFlattenedGroupIndex(listPosition) + 1,
                getChildrenCount(listPosition.groupPos));
    }

    /**
     * Expand a group
     *
     * @param listPosition the group to be expanded
     */
    private void expandGroup(ExpandableListPosition listPosition) {
        expandedGroupIndexes.put(listPosition.groupPos, true);
        onGroupExpanded(getFlattenedGroupIndex(listPosition) + 1,
                getChildrenCount(listPosition.groupPos));
    }


    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    public boolean isGroupExpanded(int flatPos) {
        return expandedGroupIndexes.get(getUnflattenedPosition(flatPos).groupPos);
    }

    /**
     * @param groupKey the {@link G} key being checked for its collapsed state
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    public boolean isGroupExpanded(String groupKey) {
        int groupIndex = mData.indexOf(groupKey);
        return expandedGroupIndexes.get(groupIndex);
    }

    /**
     * Stores the expanded state map across state loss.
     * <p>
     * Should be called from whatever {@link Activity} that hosts the RecyclerView that {@link
     * ExpandableFireRecyclerViewAdapter} is attached to.
     * <p>
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in {@link #onRestoreInstanceState(Bundle)}.
     *
     * @param savedInstanceState The {@code Bundle} into which to store the
     *                           expanded state map
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(EXPAND_STATE_MAP,
                new SparseBooleanArrayParcelable(expandedGroupIndexes));
    }

    /**
     * Fetches the expandable state map from the saved instance state {@link Bundle}
     * and restores the expanded states of all of the list items.
     * <p>
     * Should be called from {@link Activity#onRestoreInstanceState(Bundle)}  in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableFireRecyclerViewAdapter} is attached to.
     * <p>
     *
     * @param savedInstanceState The {@code Bundle} from which the expanded
     *                           state map is loaded
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPAND_STATE_MAP)) {
            return;
        }
        expandedGroupIndexes = savedInstanceState.getParcelable(EXPAND_STATE_MAP);
        notifyDataSetChanged();
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        groupClickListener = listener;
    }

    public void setOnGroupExpandCollapseListener(GroupExpandCollapseListener listener) {
        expandCollapseListener = listener;
    }

    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when  the list item created is a group
     *
     * @param viewType an int returned by {@link #getItemViewType(int)}
     * @param parent   the {@link ViewGroup} in the list for which a {@link GVH}  is being created
     * @return A {@link GVH} corresponding to the group list item with the  {@code ViewGroup} parent
     */
    public abstract GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);

    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when the list item created is a child
     *
     * @param viewType an int returned by {@link #getItemViewType(int)}
     * @param parent   the {@link ViewGroup} in the list for which a {@link CVH}  is being created
     * @return A {@link CVH} corresponding to child list item with the  {@code ViewGroup} parent
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item
     * bound to is a  child.
     * <p>
     * Bind data to the {@link CVH} here.
     *
     * @param holder       The {@code CVH} to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the child
     * @param group        The {@link G} that the the child list item belongs to
     * @param childIndex   the index of this child within it's {@link G}
     */
    public abstract void onBindChildViewHolder(CVH holder, int flatPosition, G group,
                                               int childIndex, C child);

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item bound to is a
     * group
     * <p>
     * Bind data to the {@link GVH} here.
     *
     * @param holder       The {@code GVH} to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the group
     * @param group        The {@link G} to be used to bind data to this {@link GVH}
     */
    public abstract void onBindGroupViewHolder(GVH holder, int flatPosition, G group);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public View getItemView() {
            return itemView;
        }

        public Context getContext() {
            return itemView.getContext();
        }

        public Activity getActivity() {
            return (Activity) itemView.getContext();
        }

        public String getString(@StringRes int res) {
            return getContext().getString(res);
        }

        public String getString(@StringRes int res, Object... formatArgs) {
            return getContext().getString(res, formatArgs);
        }
    }


    public static class ExpandableListPosition {

        /**
         * This data type represents a child position
         */
        public final static int CHILD = 1;
        /**
         * This data type represents a group position
         */
        public final static int GROUP = 2;
        private static final int MAX_POOL_SIZE = 5;
        private static ArrayList<ExpandableListPosition> sPool =
                new ArrayList<ExpandableListPosition>(MAX_POOL_SIZE);
        /**
         * The position of either the group being referred to, or the parent
         * group of the child being referred to
         */
        public int groupPos;

        /**
         * The position of the child within its parent group
         */
        public int childPos;
        /**
         * What type of position this ExpandableListPosition represents
         */
        public int type;
        /**
         * The position of the item in the flat list (optional, used internally when
         * the corresponding flat list position for the group or child is known)
         */
        int flatListPos;

        private ExpandableListPosition() {
        }

        static ExpandableListPosition obtainGroupPosition(int groupPosition) {
            return obtain(GROUP, groupPosition, 0, 0);
        }

        static ExpandableListPosition obtainChildPosition(int groupPosition, int childPosition) {
            return obtain(CHILD, groupPosition, childPosition, 0);
        }

        static ExpandableListPosition obtainPosition(long packedPosition) {
            if (packedPosition == ExpandableListView.PACKED_POSITION_VALUE_NULL) {
                return null;
            }

            ExpandableListPosition elp = getRecycledOrCreate();
            elp.groupPos = ExpandableListView.getPackedPositionGroup(packedPosition);
            if (ExpandableListView.getPackedPositionType(packedPosition) ==
                    ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                elp.type = CHILD;
                elp.childPos = ExpandableListView.getPackedPositionChild(packedPosition);
            } else {
                elp.type = GROUP;
            }
            return elp;
        }

        public static ExpandableListPosition obtain(int type, int groupPos, int childPos,
                                                    int flatListPos) {
            ExpandableListPosition elp = getRecycledOrCreate();
            elp.type = type;
            elp.groupPos = groupPos;
            elp.childPos = childPos;
            elp.flatListPos = flatListPos;
            return elp;
        }

        private static ExpandableListPosition getRecycledOrCreate() {
            ExpandableListPosition elp;
            synchronized (sPool) {
                if (sPool.size() > 0) {
                    elp = sPool.remove(0);
                } else {
                    return new ExpandableListPosition();
                }
            }
            elp.resetState();
            return elp;
        }

        private void resetState() {
            groupPos = 0;
            childPos = 0;
            flatListPos = 0;
            type = 0;
        }

        public long getPackedPosition() {
            if (type == CHILD) {
                return ExpandableListView.getPackedPositionForChild(groupPos, childPos);
            } else {
                return ExpandableListView.getPackedPositionForGroup(groupPos);
            }
        }

        /**
         * Do not call this unless you obtained this via ExpandableListPosition.obtain().
         * PositionMetadata will handle recycling its own children.
         */
        public void recycle() {
            synchronized (sPool) {
                if (sPool.size() < MAX_POOL_SIZE) {
                    sPool.add(this);
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ExpandableListPosition that = (ExpandableListPosition) o;

            if (groupPos != that.groupPos) return false;
            if (childPos != that.childPos) return false;
            if (flatListPos != that.flatListPos) return false;
            return type == that.type;

        }

        @Override
        public int hashCode() {
            int result = groupPos;
            result = 31 * result + childPos;
            result = 31 * result + flatListPos;
            result = 31 * result + type;
            return result;
        }

        @Override
        public String toString() {
            return "ExpandableListPosition{" +
                    "groupPos=" + groupPos +
                    ", childPos=" + childPos +
                    ", flatListPos=" + flatListPos +
                    ", type=" + type +
                    '}';
        }
    }
}