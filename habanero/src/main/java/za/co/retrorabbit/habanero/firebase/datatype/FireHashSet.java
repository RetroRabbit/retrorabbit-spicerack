package za.co.retrorabbit.habanero.firebase.datatype;

import android.util.Pair;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import za.co.retrorabbit.salt.FilterableIndexedHashMap;

/**
 * Created by wsche on 2016/09/15.
 */
public class FireHashSet<T> implements ChildEventListener {

    private Class<T> mModelClass;
    private Query mQuery;
    private OnChangedListener mListener;
    private FilterableIndexedHashMap<String, T> mObjects;

    public FireHashSet(Query mQuery, Class<T> mModelClass) {
        this.mQuery = mQuery;
        this.mModelClass = mModelClass;
        this.mObjects = new FilterableIndexedHashMap<>();
        mQuery.addChildEventListener(this);
    }

    public void cleanup() {
        mQuery.removeEventListener(this);
    }

    public int getCount() {
        return mObjects.size();

    }

    protected Query getQuery() {
        return mQuery;
    }

    public DatabaseReference getReference() {
        return getQuery().getRef();
    }

    public T getItem(int index) {
        return mObjects.get(index);
    }

    public T getItemUnfiltered(int index) {
        return mObjects.getUnfiltered(index);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        int index = mObjects.indexOfToAdd(previousChildName);
        mObjects.addOrUpdate(index, dataSnapshot.getKey(), dataSnapshot.getValue(mModelClass));
        notifyChangedListeners(OnChangedListener.EventType.Added, index);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        mObjects.addOrUpdate(dataSnapshot.getKey(), dataSnapshot.getValue(mModelClass));
        int index = mObjects.indexOf(dataSnapshot.getKey());
        notifyChangedListeners(OnChangedListener.EventType.Changed, index);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        int index = mObjects.remove(dataSnapshot.getKey());
        if (index != -1)
            notifyChangedListeners(OnChangedListener.EventType.Removed, index);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        Pair<Integer, Integer> indexes = mObjects.move(dataSnapshot.getKey(), previousChildName);
        notifyChangedListeners(OnChangedListener.EventType.Moved, indexes.first, indexes.second);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setOnChangedListener(OnChangedListener listener) {
        mListener = listener;
    }

    public void removeOnChangedListener() {
        mListener = null;
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChanged(type, index, oldIndex);
        }
    }

    public int size() {
        return mObjects.size();
    }

    public int sizeUnfiltered() {
        return mObjects.sizeUnfiltered();
    }

    public String getKey(int position) {
        return mObjects.getIndexMap().get(position);
    }

    public int indexOf(String key) {
        return mObjects.getIndexMap().indexOf(key);
    }

    public void setFilteredMap(ArrayList<String> keys) {
        mObjects.setFilteredIndexMap(keys);
    }

    public void clearFilter() {
        mObjects.clearFilter();
    }

    public boolean isFiltered() {
        return mObjects.isFiltered();
    }

    public HashMap<String, T> getValues() {
        return mObjects.getValueMap();
    }

    public interface OnChangedListener {
        void onChanged(EventType type, int index, int oldIndex);

        enum EventType {Added, Changed, Removed, Moved}
    }

}
