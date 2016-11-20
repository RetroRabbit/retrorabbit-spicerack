package co.za.retrorabbit.habanero.firebase.interfaces;

/**
 * Created by Bernhard Müller on 11/18/2016.
 */

public interface OnGroupClickListener {

    /**
     * @param position the position
     * @return false if click expanded group, true if click collapsed group
     */
    boolean onGroupClick(int position);
}