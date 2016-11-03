package za.co.retrorabbit.habanero.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import za.co.retrorabbit.habanero.firebase.datatype.FireHashSet;

/**
 * Created by wsche on 2016/09/15.
 */
public abstract class FireExpandableListAdapter<G, I, GVH extends FireExpandableListAdapter.ViewHolder, IVH extends FireExpandableListAdapter.ViewHolder> extends BaseExpandableListAdapter implements Filterable {
    private Class<G> mGroupModelClass;
    private Class<I> mItemModelClass;
    private int mGroupModelLayout, mItemModelLayout;
    private Class<GVH> mViewGroupHolderClass;
    private Class<IVH> mViewItemHolderClass;
    private FireHashSet<G> mData;
    private String filterConstraint;
    private Map<String, Integer> indicatorMap = new LinkedHashMap<>();
    private String[] sections = new String[]{};
    private Comparator<? super G> sorter;
    private Comparator<? super I> childSorter;
    private FireHashSet.OnChangedListener onChangedListener = new FireHashSet.OnChangedListener() {
        @Override
        public void onChanged(EventType type, int index, int oldIndex) {
            switch (type) {
                case Added:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyDataSetChanged();
                    if (sorter != null)
                        sort();
                    break;
                case Changed:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyDataSetChanged();
                    if (sorter != null)
                        sort();
                    break;
                case Removed:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyDataSetChanged();
                    if (sorter != null)
                        sort();
                    break;
                case Moved:
                    if (filterConstraint != null)
                        filterSnapshots();
                    notifyDataSetChanged();
                    if (sorter != null)
                        sort();
                    break;
                default:
                    throw new IllegalStateException("Incomplete case statement");
            }
        }
    };

    public FireExpandableListAdapter(Class<G> mGroupModelClass, Class<I> mItemModelClass, int mGroupModelLayout, int mItemModelLayout, Class<GVH> mViewHolderClass, Class<IVH> mViewItemHolderClass, FireHashSet mData) {
        this.mGroupModelClass = mGroupModelClass;
        this.mGroupModelLayout = mGroupModelLayout;
        this.mItemModelClass = mItemModelClass;
        this.mItemModelLayout = mItemModelLayout;
        this.mViewGroupHolderClass = mViewHolderClass;
        this.mViewItemHolderClass = mViewItemHolderClass;
        this.mData = mData;
        addListeners();
    }

    public FireExpandableListAdapter(Class<G> mGroupModelClass, Class<I> mItemModelClass, int mGroupModelLayout, int mItemModelLayout, Class<GVH> mViewHolderClass, Class<IVH> mViewItemHolderClass, Query mQuery) {
        this(mGroupModelClass, mItemModelClass, mGroupModelLayout, mItemModelLayout, mViewHolderClass, mViewItemHolderClass, new FireHashSet(mQuery, mModelClass));
    }

    public FireExpandableListAdapter(Class<G> mGroupModelClass, Class<I> mItemModelClass, int mGroupModelLayout, int mItemModelLayout, Class<GVH> mViewHolderClass, Class<IVH> mViewItemHolderClass, DatabaseReference mReference) {
        this(mGroupModelClass, mItemModelClass, mGroupModelLayout, mItemModelLayout, mViewHolderClass, mViewItemHolderClass, (Query) mReference);
    }

    public void setReversed(boolean reversed) {
        mData.setReversed(reversed);
        if (getGroupCount() > 0)
            notifyDataSetChanged();
    }

    public void setValueParser(FireHashSet.ValueParser<G> valueParser) {
        mData.setValueParser(valueParser);
    }

    public GVH onCreateGroupViewHolder(ViewGroup parent, int viewResource) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(viewResource, parent, false);
        try {
            Constructor<GVH> constructor = mViewGroupHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public IVH onCreateItemViewHolder(ViewGroup parent, int viewResource) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(viewResource, parent, false);
        try {
            Constructor<IVH> constructor = mViewItemHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public final View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
        GVH viewHolder;
        if (convertView == null) {
            viewHolder = onCreateGroupViewHolder(parent, getGroupResource(position));
            convertView = viewHolder.getItemView();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GVH) convertView.getTag();
            convertView = viewHolder.getItemView();
        }
        onPopulateGroupViewHolder(viewHolder, getGroup(position), position);
        return convertView;
    }

    public int getGroupResource(int position) {
        return mGroupModelLayout;
    }

    public int getItemResource(int groupPosition, int childPosition) {
        return mItemModelLayout;
    }

    @Override
    public int getGroupType(int position) {
        return 1;
    }

    @Override
    public long getGroupId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mData.getKey(position).hashCode();
    }

    public abstract List<I> getGroupChildren(G item);

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroupChildren(mData.getItem(groupPosition)).size();
    }

    @Override
    public final I getChild(int groupPosition, int childPosition) {
        List<I> items = getGroupChildren(getGroup(groupPosition));
        Collections.sort(items, childSorter);
        return items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        IVH viewHolder;
        if (convertView == null) {
            viewHolder = onCreateItemViewHolder(parent, getItemResource(groupPosition, childPosition));
            convertView = viewHolder.getItemView();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (IVH) convertView.getTag();
            convertView = viewHolder.getItemView();
        }
        onPopulateItemViewHolder(viewHolder, getChild(groupPosition, childPosition), childPosition);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return customFilter();
    }

    public Filter customFilter() {
        return null;
    }

    public void cleanup() {
        mData.cleanup();
    }

    public DatabaseReference getRef(int position) {
        return mData.getReference().child(mData.getKey(position));
    }

    @Override
    public G getGroup(int position) {
        return mData.getItem(position);
    }

    protected abstract void onPopulateGroupViewHolder(GVH viewHolder, G model, int position);

    protected abstract void onPopulateItemViewHolder(IVH viewHolder, I model, int position);

    public void filterSnapshots() {
        getFilter().filter(getFilterConstraint());
    }

    public void setFilteredMap(ArrayList<String> filteredMap) {
        this.mData.setFilteredMap(filteredMap);
        notifyDataSetChanged();
    }

    public void clearFilter() {
        this.mData.clearFilter();
        notifyDataSetChanged();
    }

    public int getItemPosition(String key) {
        return getData().indexOf(key);
    }

    public FireHashSet<G> getData() {
        return mData;
    }

    public void addListeners() {
        mData.addOnChangedListener(onChangedListener);
    }

    public void removeListeners() {
        if (mData != null)
            mData.removeOnChangedListener(onChangedListener);
    }

    public String getFilterConstraint() {
        return filterConstraint;
    }

    public void setFilterConstraint(String filterConstraint) {
        this.filterConstraint = filterConstraint;
        filterSnapshots();
    }

    public void setGroupSorter(Comparator<? super G> sorter) {
        this.sorter = sorter;
    }

    public void setChildSorter(Comparator<? super I> childSorter) {
        this.childSorter = childSorter;
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

    public static class ViewHolder {
        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
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
}
