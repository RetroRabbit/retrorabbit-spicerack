package za.co.retrorabbit.salt;

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

    public FilterableIndexedHashMap() {
        mValueMap = new HashMap<>();
        mIndexMap = new ArrayList<>();
    }

    public T addOrUpdate(K key, T value) {
        T oldValue = mValueMap.put(key, value);
        if (oldValue == null)
            mIndexMap.add(key);
        if (mIndexMapBackup != null)
            addOrUpdateBackup(key, value, oldValue);
        return oldValue;
    }

    private T addOrUpdateBackup(K key, T value, T oldValue) {
        if (oldValue == null)
            mIndexMapBackup.add(key);
        return oldValue;
    }

    public T addOrUpdate(int index, K key, T value) {
        T oldValue = mValueMap.put(key, value);
        int oldIndex = mIndexMap.indexOf(key);
        if (oldIndex != index || oldIndex == -1) {
            if (oldValue != null)
                mIndexMap.remove(key);
            mIndexMap.add(index, key);
        }
        if (mIndexMapBackup != null)
            addOrUpdateBackup(index, key, value, oldValue);
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


    public Pair<Integer, Integer> move(K key, K parentKey) {
        int oldIndex = indexOf(key);
        if (oldIndex >= 0 && oldIndex < mIndexMap.size())
            mIndexMap.remove(oldIndex);
        int newIndex = indexOfToAdd(parentKey);
        mIndexMap.add(newIndex, key);
        if (mIndexMapBackup != null)
            moveBackup(key, parentKey);
        return new Pair<>(newIndex, oldIndex);
    }

    private void moveBackup(K key, K parentKey) {
        int oldIndex = indexOf(key);
        if (oldIndex >= 0 && oldIndex < mIndexMapBackup.size())
            mIndexMapBackup.remove(oldIndex);
        int newIndex = indexOfToAddBackup(parentKey);
        mIndexMapBackup.add(newIndex, key);
    }

    public int remove(K key) {
        int index = mIndexMap.indexOf(key);
        if (index != -1) {
            mValueMap.remove(key);
            mIndexMap.remove(key);
        }
        if (mIndexMapBackup != null)
            removeBackup(key);
        return index;
    }

    private int removeBackup(K key) {
        int index = mIndexMapBackup.indexOf(key);
        if (index != -1) {
            mValueMap.remove(key);
            mIndexMapBackup.remove(key);
        }
        return index;
    }

    public int indexOf(K key) {
        return mIndexMap.indexOf(key);
    }

    public int indexOfToAdd(K key) {
        int index = mIndexMap.indexOf(key);

        if (index == -1 && key != null)
            return mIndexMap.size();
        else
            return ++index;
    }

    private int indexOfToAddBackup(K key) {
        int index = mIndexMapBackup.indexOf(key);

        if (index == -1)
            return mIndexMapBackup.size();
        else
            return ++index;
    }

    public T get(K key) {
        return mValueMap.get(key);
    }

    public T get(int index) {
        if (mIndexMap != null && index >= 0 && index < mIndexMap.size())
            return mValueMap.get(mIndexMap.get(index));
        return null;
    }


    public T getUnfiltered(int index) {
        if (mIndexMapBackup != null && index >= 0 && index < mIndexMapBackup.size())
            return mValueMap.get(mIndexMapBackup.get(index));
        else if (mIndexMap != null && index >= 0 && index < mIndexMap.size())
            return mValueMap.get(mIndexMap.get(index));
        else
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

    public ArrayList<K> getIndexMap(){
        return mIndexMap;
    }

    public HashMap<K, T> getValueMap() {
        return mValueMap;
    }
}
