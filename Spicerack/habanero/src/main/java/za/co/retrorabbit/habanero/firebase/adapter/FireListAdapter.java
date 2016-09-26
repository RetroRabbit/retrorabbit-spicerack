package za.co.retrorabbit.habanero.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import za.co.retrorabbit.habanero.firebase.datatype.FireHashSet;

/**
 * Created by wsche on 2016/09/15.
 */
public abstract class FireListAdapter<T, VH extends FireListAdapter.ViewHolder> extends BaseAdapter implements Filterable, SectionIndexer {
    Query mQuery;
    Class<T> mModelClass;
    int mModelLayout;
    Class<VH> mViewHolderClass;
    FireHashSet<T> mData;
    String filterConstraint;
    Map<String, Integer> indicatorMap = new LinkedHashMap<>();
    String[] sections = new String[]{};

    public FireListAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, Query mQuery) {
        this.mModelClass = mModelClass;
        this.mModelLayout = mModelLayout;
        this.mViewHolderClass = mViewHolderClass;
        this.mQuery = mQuery;
        mData = new FireHashSet<>(mQuery, this.mModelClass);
        addListeners();
    }

    public FireListAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, DatabaseReference mReference) {
        this(mModelClass, mModelLayout, mViewHolderClass, (Query) mReference);
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewResource) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(viewResource, parent, false);
        try {
            Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
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

    public final void onBindViewHolder(VH viewHolder, int position) {
        populateViewHolder(viewHolder, getItem(position), position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return customFilter();
    }

    public Filter customFilter() {
        return null;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        VH viewHolder;
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, getItemViewResource(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
            convertView = viewHolder.itemView;
        }
        populateViewHolder(viewHolder, getItem(position), position);
        return convertView;
    }

    public int getItemViewResource(int position) {
        return mModelLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mData.getKey(position).hashCode();
    }

    public void cleanup() {
        mData.cleanup();
    }

    public DatabaseReference getRef(int position) {
        return mData.getReference().child(mData.getKey(position));
    }

    @Override
    public T getItem(int position) {
        return mData.getItem(position);
    }

    public void setFilterConstraint(String filterConstraint) {
        this.filterConstraint = filterConstraint;
        filterSnapshots();
    }

    protected abstract void populateViewHolder(VH viewHolder, T model, int position);

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

    public FireHashSet<T> getData() {
        return mData;
    }

    public void addListeners() {
        mData.setOnChangedListener(new FireHashSet.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyDataSetChanged();
                        break;
                    case Changed:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyDataSetChanged();
                        break;
                    case Removed:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyDataSetChanged();
                        break;
                    case Moved:
                        if (filterConstraint != null)
                            filterSnapshots();
                        notifyDataSetChanged();
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });
    }

    public void removeListeners() {
        mData.removeOnChangedListener();
    }

    public String getFilterConstraint() {
        return filterConstraint;
    }

    @Override
    public int getPositionForSection(int section) {
        if (sections != null && sections.length > 0
                && section >= 0 && sections.length > section
                && indicatorMap.containsKey(sections[section]))
            return indicatorMap.get(sections[section]);
        else
            return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = sections.length - 1; i >= 0; i--) {
            if (position > indicatorMap.get(sections[i]))
                return i;
        }
        return 0;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
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
