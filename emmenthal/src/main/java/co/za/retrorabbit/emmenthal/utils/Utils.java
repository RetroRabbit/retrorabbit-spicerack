package co.za.retrorabbit.emmenthal.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.util.TypedValue;

/**
 * Created by Werner Scheffer on 14/10/16.
 */
public class Utils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getThemeAccentColor(Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

    public static int getDimention(Context context, @DimenRes int dimenRes) {
        return (int) context.getResources().getDimensionPixelSize(dimenRes);
    }
}
