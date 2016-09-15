package za.co.retrorabbit.habanero.firebase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import za.co.retrorabbit.habanero.firebase.datatype.FireHashSet;

/**
 * Created by Werner Scheffer on 2016/09/15.
 */
public abstract class FireRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {
    Query mQuery;
    Class<T> mModelClass;
    int mModelLayout;
    Class<VH> mViewHolderClass;
    FireHashSet<T> mData;
    String filterConstraint;
    boolean scrollTo;
    String scrollToKey;
    RecyclerView recyclerView;

    public FireRecyclerAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, Query mQuery) {
        this.mModelClass = mModelClass;
        this.mModelLayout = mModelLayout;
        this.mViewHolderClass = mViewHolderClass;
        this.mQuery = mQuery;
        mData = new FireHashSet<>(mQuery, this.mModelClass);
        addListeners();
    }

    public FireRecyclerAdapter(Class<T> mModelClass, int mModelLayout, Class<VH> mViewHolderClass, DatabaseReference mReference) {
        this(mModelClass, mModelLayout, mViewHolderClass, (Query) mReference);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
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

    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        populateViewHolder(viewHolder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return customFilter();
    }

    public Filter customFilter() {
        return null;
    }

    ;

    @Override
    public int getItemViewType(int position) {
        return mModelLayout;
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mData.getKey(position).hashCode();
    }

    public void cleanup() {
        mData.cleanup();
    }

    public DatabaseReference getReference(int position) {
        return mData.getReference().child(mData.getKey(position));
    }

    public T getItem(int position) {
        return mData.getItem(position);
    }

    protected abstract void populateViewHolder(VH viewHolder, T model, int position);

    public void filterSnapshots() {
        getFilter().filter(getFilterConstraint());
    }

    public int getItemPosition(String key) {
        return getData().indexOf(key);
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

    public void removeListeners() {
        mData.removeOnChangedListener();
    }

    public String getFilterConstraint() {
        return filterConstraint;
    }

    public void setFilterConstraint(String filterConstraint) {
        this.filterConstraint = filterConstraint;
        filterSnapshots();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
    }

    public void setScrollTo(String key) {
        scrollToKey = key;
        scrollTo = true;
    }
}
