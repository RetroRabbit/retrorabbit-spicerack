package co.za.retrorabbit.salt;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Werner Scheffer on 2016/09/15.
 */
public class FilterableIndexedHashMap<K, T> {
    HashMap<K, T> mValueMap;
    ArrayList<K> mIndexMap;
    ArrayList<K> mIndexMapBackup;
    boolean mReversed = false;

    public FilterableIndexedHashMap() {
        mValueMap = new HashMap<>();
        mIndexMap = new ArrayList<>();
    }

    private void moveBackup(K key, K parentKey) {
        int oldIndex = mIndexMapBackup.indexOf(key);
        if (oldIndex >= 0 && oldIndex < mIndexMapBackup.size())
            mIndexMapBackup.remove(oldIndex);
        int newIndex = indexOfToAddBackup(parentKey);
        mIndexMapBackup.add(newIndex, key);
    }

    private T addOrUpdateBackup(K key, T value, T oldValue) {
        if (oldValue == null)
            mIndexMapBackup.add(key);
        return oldValue;
    }

    private T addOrUpdateBackup(int index, K key, T value, T oldValue) {
        int oldIndex = mIndexMapBackup.indexOf(key);
        if (oldIndex != index || oldIndex == -1) {
            if (oldValue != null)
                mIndexMapBackup.remove(key);
            mIndexMapBackup.add(index, key);
        }
        return oldValue;
    }

    private int removeBackup(K key) {
        int index = mIndexMapBackup.indexOf(key);
        if (index >= 0) {
            mValueMap.remove(key);
            mIndexMapBackup.remove(key);
        }
        if (mReversed)
            return mIndexMapBackup.size() - 1 - index;
        return index;
    }

    private int indexOfToAdd(K key) {
        int index = mIndexMap.indexOf(key);

        if (index < 0 && key != null)
            return mIndexMap.size();
        else
            return ++index;
    }

    private int indexOfToAddBackup(K key) {
        int index = mIndexMap.indexOf(key);

        if (index < 0)
            return mIndexMapBackup.size();
        else
            return ++index;
    }

    public int indexOf(K key) {
        int index = mIndexMap.indexOf(key);
        if (mReversed)
            return mIndexMapBackup.size() - 1 - index;
        return index;
    }

    public int addOrUpdate(K key, T value) {
        T oldValue = mValueMap.put(key, value);
        if (oldValue == null)
            mIndexMap.add(key);
        if (mIndexMapBackup != null)
            addOrUpdateBackup(key, value, oldValue);
        int index = mIndexMap.indexOf(key);
        if (mReversed)
            return mIndexMap.size() - 1 - index;
        return index;
    }

    public int addOrUpdate(K parentKey, K key, T value) {
        int index = indexOfToAdd(parentKey);
        T oldValue = mValueMap.put(key, value);
        int oldIndex = mIndexMap.indexOf(key);
        if (oldIndex != index || oldIndex == -1) {
            if (oldValue != null)
                mIndexMap.remove(key);
            mIndexMap.add(index, key);
        }
        if (mIndexMapBackup != null)
            addOrUpdateBackup(index, key, value, oldValue);

        if (mReversed)
            return mIndexMap.size() - 1 - index;
        return index;
    }

    public Pair<Integer, Integer> move(K key, K parentKey) {
        int oldIndex = mIndexMap.indexOf(key);
        if (oldIndex >= 0 && oldIndex < mIndexMap.size())
            mIndexMap.remove(oldIndex);
        int newIndex = indexOfToAdd(parentKey);
        mIndexMap.add(newIndex, key);
        if (mIndexMapBackup != null)
            moveBackup(key, parentKey);
        if (mReversed)
            return new Pair<>(mIndexMap.size() - 1 - newIndex, mIndexMap.size() - 1 - oldIndex);
        return new Pair<>(newIndex, oldIndex);
    }

    public int remove(K key) {
        int index = mIndexMap.indexOf(key);
        if (index >= 0) {
            mValueMap.remove(key);
            mIndexMap.remove(key);
        }
        if (mIndexMapBackup != null)
            removeBackup(key);
        if (mReversed)
            return mIndexMap.size() - 1 - index;
        return index;
    }

    public T get(K key) {
        return mValueMap.get(key);
    }

    public T get(int index) {
        if (mReversed)
            index = mIndexMap.size() - index - 1;
        if (mIndexMap != null && index >= 0 && index < mIndexMap.size())
            return mValueMap.get(mIndexMap.get(index));
        return null;
    }

    public T getUnfiltered(int index) {
        if (mIndexMapBackup != null && index >= 0 && index < mIndexMapBackup.size()) {
            if (mReversed)
                index = mIndexMapBackup.size() - index - 1;
            return mValueMap.get(mIndexMapBackup.get(index));
        } else if (mIndexMap != null && index >= 0 && index < mIndexMap.size()) {
            if (mReversed)
                index = mIndexMap.size() - index + 1;
            return mValueMap.get(mIndexMap.get(index));
        } else
            return null;
    }

    public int size() {
        return mIndexMap.size();
    }

    public int sizeUnfiltered() {
        if (mIndexMapBackup != null)
            return mIndexMapBackup.size();
        else
            return mIndexMap.size();
    }

    public void setFilteredIndexMap(ArrayList<K> indexMap) {
        if (mIndexMapBackup == null)
            this.mIndexMapBackup = new ArrayList<>(mIndexMap);

        if (indexMap != null)
            this.mIndexMap = indexMap;
    }

    public void clearFilter() {
        if (mIndexMapBackup != null)
            this.mIndexMap = new ArrayList<>(mIndexMapBackup);
        mIndexMapBackup = null;
    }

    public boolean isFiltered() {
        return mIndexMapBackup != null;
    }

    public HashMap<K, T> getValueMap() {
        return mValueMap;
    }

    public HashMap<K, T> getFilteredMap() {
        HashMap<K, T> filteredMap = new HashMap<>();
        for (K k : new ArrayList<>(mIndexMap)) {
            if (mValueMap.containsKey(k))
                filteredMap.put(k, mValueMap.get(k));
        }
        return filteredMap;
    }

    public K getKey(int index) {
        if (mReversed)
            index = mIndexMap.size() - index + 1;
        if (mIndexMap != null && index >= 0 && index < mIndexMap.size())
            return mIndexMap.get(index);
        return null;
    }

    public void setReversed(boolean reversed) {
        this.mReversed = reversed;
    }
}
