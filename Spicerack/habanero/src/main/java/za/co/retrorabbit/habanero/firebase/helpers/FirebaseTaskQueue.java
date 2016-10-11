package za.co.retrorabbit.habanero.firebase.helpers;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Bernhard Müller on 9/30/2016.
 */
public class FirebaseTaskQueue<T> {
    private DatabaseReference databaseReference;
    private ArrayList<Pair<List<T>, HashMap<String, Object>>> dataSet;
    private OnCompleteListener<Void> onCompleteListener;
    private OnNextListener<T> onNextListener;
    private HashSet<Integer> dataSetCompletionList;

    private void queueTasks(final int index) {
        databaseReference.updateChildren(dataSet.get(index).second).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if (onNextListener != null) {
                    NextTask<T> nextTask = new NextTask<T>(dataSet.get(index).first);
                    onNextListener.onNext(nextTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            setComplete(index, task);
                        }
                    });
                } else if (onCompleteListener != null) {
                    setComplete(index, task);
                }
            }
        });
    }

    private synchronized void setComplete(@NonNull int index, @NonNull Task task) {
        dataSetCompletionList.remove(index);
        if (onCompleteListener != null && dataSetCompletionList.size() == 0) {
            onCompleteListener.onComplete(task);
        }
    }

    public void addDataSet(List<T> data, HashMap<String, Object> uploadMap) {
        if (dataSet == null)
            dataSet = new ArrayList<>();
        if (dataSetCompletionList == null)
            dataSetCompletionList = new HashSet<>();
        dataSetCompletionList.add(dataSet.size());
        dataSet.add(new Pair<>(data, uploadMap));
    }

    public void setOnCompleteListener(OnCompleteListener<Void> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void queueTasks(@NonNull DatabaseReference databaseReference, OnNextListener<T> onNextListener) {
        this.databaseReference = databaseReference;
        this.onNextListener = onNextListener;

        for (int i = 0; i < dataSet.size(); i++) {
            queueTasks(i);
        }
    }

    public interface OnNextListener<T> {
        Task<Void> onNext(@NonNull NextTask<T> task);
    }

    public static class NextTask<T> {
        List<T> result;

        NextTask(List<T> result) {
            this.result = result;
        }

        public boolean isComplete() {
            return true;
        }

        public List<T> getResult() {
            return result;
        }
    }
}