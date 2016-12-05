package za.co.retrorabbit.habanero.firebase.datatype;

import android.text.TextUtils;
import android.util.Pair;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import za.co.retrorabbit.salt.FilterableIndexedHashMap;

/**
 * Created by wsche on 2016/09/15.
 */
public class FireHashSet<T> implements ChildEventListener {

    ValueParser<T> valueParser;
    private Class<T> mModelClass;
    private Query mQuery;
    private Query mBatchedQuery;
    private ArrayList<OnChangedListener<T>> mListeners;
    private FilterableIndexedHashMap<String, T> mObjects;
    private int mBatchIncrement, mCurrentIncrement;

    public FireHashSet(Query mQuery, Class<T> mModelClass) {
        this.mQuery = mQuery;
        this.mBatchedQuery = mQuery;
        this.mModelClass = mModelClass;
        this.mObjects = new FilterableIndexedHashMap<>();
        this.mListeners = new ArrayList<>();
        mBatchedQuery.addChildEventListener(this);
    }

    public void cleanup() {
        mBatchedQuery.removeEventListener(this);
    }

    public int getCount() {
        return mObjects.size();

    }

    protected Query getBaseQuery() {
        return mQuery;
    }

    protected Query getQuery() {
        return mBatchedQuery;
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

    private T parseDataSnapshot(DataSnapshot dataSnapshot) {
        if (valueParser != null)
            return valueParser.parseDataSnapshot(dataSnapshot);
        return dataSnapshot.getValue(mModelClass);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        if(mObjects.containsKey(dataSnapshot.getKey()))
            return;

        int index = mObjects.addOrUpdate(previousChildName, dataSnapshot.getKey(), parseDataSnapshot(dataSnapshot));
        notifyChangedListeners(OnChangedListener.EventType.Added, index);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        int index = mObjects.addOrUpdate(dataSnapshot.getKey(), parseDataSnapshot(dataSnapshot));
        notifyChangedListeners(OnChangedListener.EventType.Changed, index);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        int index = mObjects.remove(dataSnapshot.getKey());
        if (index > -1)
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

    public void addOnChangedListener(OnChangedListener<T> listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    public void removeOnChangedListener(OnChangedListener<T> listener) {
        if (mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListeners != null) {
            for (OnChangedListener<T> mListener : mListeners) {
                mListener.onChanged(type, index, oldIndex);
            }
        }
    }

    public int size() {
        return mObjects.size();
    }

    public int sizeUnfiltered() {
        return mObjects.sizeUnfiltered();
    }

    public String getKey(int position) {
        return mObjects.getKey(position);
    }

    public int indexOf(String key) {
        return mObjects.indexOf(key);
    }

    public void setReversed(boolean reversed) {
        mObjects.setReversed(reversed);
    }

    public void setBatching(int batchIncrement) {
        if (batchIncrement > 0) {
            mBatchIncrement = batchIncrement;
            incrementBatch();
        } else if (mBatchedQuery != null) {
            mBatchedQuery.removeEventListener(this);
            mBatchedQuery = mQuery;
            mBatchedQuery.addChildEventListener(this);
        }
    }

    public void incrementBatch() {
        if (mBatchedQuery != null && mBatchIncrement > 0) {
            mBatchedQuery.removeEventListener(this);
            mCurrentIncrement += mBatchIncrement;
            mBatchedQuery = mQuery.limitToFirst(mCurrentIncrement);
            mBatchedQuery.addChildEventListener(this);
        }
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

    public HashMap<String, T> getFilteredValues() {
        return mObjects.getFilteredMap();
    }

    public void createIndexMapFromList(ArrayList<T> sortedList, HashMap<String, T> dataMap) {
        ArrayList<String> indexMap = new ArrayList<>();
        for (T t : sortedList) {
            String key = getKeyByValue(dataMap, t);
            if (!TextUtils.isEmpty(key))
                indexMap.add(key);
        }
        setFilteredMap(indexMap);
    }

    private String getKeyByValue(Map<String, T> map, T value) {
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void setValueParser(ValueParser<T> valueParser) {
        this.valueParser = valueParser;
    }

    public interface OnChangedListener<T> {
        void onChanged(EventType type, int index, int oldIndex);

        enum EventType {Added, Changed, Removed, Moved}
    }

    public interface ValueParser<T> {
        T parseDataSnapshot(DataSnapshot dataSnapshot);
    }
}
