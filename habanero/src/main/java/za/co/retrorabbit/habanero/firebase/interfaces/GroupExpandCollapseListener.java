package za.co.retrorabbit.habanero.firebase.interfaces;

/**
 * Created by Bernhard Müller on 11/18/2016.
 */

public interface GroupExpandCollapseListener<G> {
    /**
     * Called when a group is expanded
     *
     * @param group the {@link G} being expanded
     */
    void onGroupExpanded(G group);

    /**
     * Called when a group is collapsed
     *
     * @param group the {@link G} being collapsed
     */
    void onGroupCollapsed(G group);
}