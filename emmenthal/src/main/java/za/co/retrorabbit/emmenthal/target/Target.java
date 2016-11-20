package za.co.retrorabbit.emmenthal.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Werner Scheffer on 14/10/16.
 */
public interface Target {
    /**
     * Returns center point of target.
     * We can get x and y coordinates using
     * point object
     * @return
     */
    Point getPoint();

    /**
     * Returns Rectangle points of target view
     * @return
     */
    Rect getRect();

    /**
     * return target view
     * @return
     */
    View getView();
}
