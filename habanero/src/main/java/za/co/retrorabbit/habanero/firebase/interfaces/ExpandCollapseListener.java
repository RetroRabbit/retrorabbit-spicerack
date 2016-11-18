package za.co.retrorabbit.habanero.firebase.interfaces;

/**
 * Created by Bernhard Müller on 11/18/2016.
 */

public interface ExpandCollapseListener<G> {

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the {@link G}
     * @param itemCount     the total number of children in the {@link G}
     */
    void onGroupExpanded(int positionStart, int itemCount);

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the {@link G}
     * @param itemCount     the total number of children in the {@link G}
     */
    void onGroupCollapsed(int positionStart, int itemCount);
}
