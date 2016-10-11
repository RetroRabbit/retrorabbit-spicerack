package za.co.retrorabbit.habanero.firebase.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Bernhard Müller on 9/29/2016.
 * <p>
 * Value event listener that has a type and returns updates for that object in a given type or
 * on the onEmptyValue when no value is found.
 */
public abstract class QueriedValueEventListener<T extends Object> implements ValueEventListener {
    Type mType;

    public QueriedValueEventListener() {
        Type mySuperclass = getClass().getGenericSuperclass();
        mType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
    }

    @Override
    public final void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
        };
        Map<String, Object> result = dataSnapshot.getValue(genericTypeIndicator);
        if (result != null) {
            Gson gson = new Gson();
            onValueRetrieved((T) gson.fromJson(gson.toJson(result.values().toArray()[0]), mType));
        } else {
            onEmptyValue();
        }
    }

    public abstract void onValueRetrieved(T value);

    public abstract void onEmptyValue();
}
