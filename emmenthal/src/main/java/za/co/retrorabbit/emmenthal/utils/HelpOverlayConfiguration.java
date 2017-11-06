package za.co.retrorabbit.emmenthal.utils;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.util.Log;

import za.co.retrorabbit.emmenthal.shape.Focus;
import za.co.retrorabbit.emmenthal.shape.FocusGravity;

/**
 * Created by Werner Scheffer on 14/10/16.
 */
public class HelpOverlayConfiguration {

    private static final String TAG = HelpOverlayConfiguration.class.getSimpleName();
    /**
     * Title Text
     */
    private String titleText;
    /**
     * Message Text
     */
    private String messageText;
    /**
     * Title Font Style
     */
    @StyleRes
    private int titleStyle;
    /**
     * Message Font Style
     */
    @StyleRes
    private int messageStyle;
    /**
     * HelpOverlay will start
     * showing after delayMillis seconds
     * passed
     */
    private int delayBeforeShow;
    /**
     * Mask Color
     */
    @ColorInt
    private int maskColor;
    /**
     * Mask Color Resource
     */
    @ColorRes
    private int maskColorResource;
    /**
     * Show/Dismiss HelpOverlay
     * with fade in/out animation if
     * this is enabled.
     */
    private boolean fadeAnimationEnabled;
    /**
     * Animation duration
     */
    private long fadeAnimationDuration;
    /**
     * circleShape color
     */
    private int cutoutColor;
    /**
     * Stroke Color Resource
     */
    private int strokeColorResource;
    /**
     * Cutout Stroke Color
     */
    private int strokeColor;
    /**
     * Cutout Stroke Alpha Color
     */
    private float strokeAlpha = 1f;
    /**
     * Focus Type
     */
    private Focus focusType;
    /**
     * FocusGravity type
     */
    private FocusGravity focusGravity;
    /**
     * Dismiss on touch any position
     */
    private boolean dismissOnTouch;
    /**
     * Help Dialog Title text color
     */
    private int titleColor;
    /**
     * Help Dialog Message text color
     */
    private int messageColor;
    /**
     * Dot View will be shown if
     * this is true
     */
    private boolean isDotViewEnabled;
    /**
     * Dot View Color
     */
    private int dotColor;
    /**
     * Dot View Diameter
     */
    private int dotSize;
    /**
     * Dot View Diameter Resource
     */
    private int dotSizeResource;
    /**
     * Perform click operation to target
     * if this is true
     */
    private boolean clickTargetOnTouch = false;
    /**
     * Left or Right Button color
     */
    @ColorInt
    private int buttonColorLeft, buttonColorRight;
    /**
     * Left or Right Button Color Resource
     */
    @ColorRes
    private int buttonColorResourceLeft, buttonColorResourceRight;
    /**
     * Left or Right Button Text Color
     */
    @ColorInt
    private int buttonTextColorLeft, buttonTextColorRight;
    /**
     * Left or Right Button Text Color
     */
    @ColorRes
    private int buttonTextColorResourceLeft, buttonTextColorResourceRight;
    /**
     * Title or Message Color Resource
     */
    @ColorRes
    private int titleResourceColor, messageResourceColor;

    /**
     * Current Visible state for Left and Right Button
     */
    private Visibility buttonVisibilityLeft, buttonVisibilityRight;

    private int cutoutRadius;
    private int cutoutStroke;
    private int infoMargin;
    private int cutoutRadiusResource;
    private int cutoutStrokeSizeResource;
    private int dotColorResource;
    private int cutoutColorResource;
    private int infoMarginResource;
    private int buttonTextResourceLeft;
    private int buttonTextResourceRight;
    private String buttonTextLeft;
    private String buttonTextRight;

    public HelpOverlayConfiguration() {
        setMaskColor(Constants.DEFAULT_MASK_COLOR);
        setDelayBeforeShow(Constants.DEFAULT_DELAY_MILLIS);
        setFadeAnimationDuration(Constants.DEFAULT_FADE_DURATION);
        setFocusType(Focus.NORMAL);
        setFocusGravity(FocusGravity.CENTER);
        setFadeAnimationEnabled(true);
        setDismissOnTouch(false);
    }

    public String getTitleText() {
        return titleText;
    }

    public HelpOverlayConfiguration setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public String getMessageText() {
        return messageText;
    }

    public HelpOverlayConfiguration setMessageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    @StyleRes
    public int getMessageStyle() {
        return messageStyle;
    }

    public HelpOverlayConfiguration setMessageStyle(Integer messageStyle) {
        this.messageStyle = messageStyle;
        return this;
    }

    @StyleRes
    public int getTitleStyle() {
        return titleStyle;
    }

    public HelpOverlayConfiguration setTitleStyle(Integer titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    public boolean isFadeAnimationEnabled() {
        return fadeAnimationEnabled;
    }

    public HelpOverlayConfiguration setFadeAnimationEnabled(boolean fadeAnimationEnabled) {
        this.fadeAnimationEnabled = fadeAnimationEnabled;
        return this;
    }

    public long getFadeAnimationDuration() {
        return fadeAnimationDuration;
    }

    public HelpOverlayConfiguration setFadeAnimationDuration(long fadeAnimationDuration) {
        this.fadeAnimationDuration = fadeAnimationDuration;
        return this;
    }

    @ColorInt
    public int getCutoutColor() {
        return cutoutColor;
    }

    public HelpOverlayConfiguration setCutoutColor(@ColorInt int cutoutColor) {
        this.cutoutColor = cutoutColor;
        return this;
    }

    @ColorRes
    public int getCutoutColorResource() {
        return cutoutColorResource;
    }

    public HelpOverlayConfiguration setCutoutColorResource(@ColorRes int cutoutColorResource) {
        this.cutoutColorResource = cutoutColorResource;
        return this;
    }

    @ColorInt
    public int getStrokeColor() {
        return strokeColor;
    }

    public HelpOverlayConfiguration setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        if (this.strokeColorResource != 0) {
            Log.w(TAG, "Can't set both strokeColor and strokeColorResource, strokeColorResource will be ignored");
            this.strokeColorResource = 0;
        }
        return this;
    }

    public HelpOverlayConfiguration setCutoutStrokeColorResource(@ColorRes int strokeColor, @FloatRange(from = 0f, to = 1f) float alpha) {
        setCutoutStrokeColorResource(strokeColor);
        setStrokeAlpha(alpha);
        return this;
    }

    public float getStrokeAlpha() {
        return strokeAlpha;
    }

    public void setStrokeAlpha(@FloatRange(from = 0f, to = 1f) float strokeAlpha) {
        this.strokeAlpha = strokeAlpha;
    }

    @ColorRes
    public int getStrokeColorResource() {
        return strokeColorResource;
    }

    public HelpOverlayConfiguration setCutoutStrokeColorResource(@ColorRes int strokeResourceColor) {
        this.strokeColorResource = strokeResourceColor;
        if (this.strokeColor != 0) {
            Log.w(TAG, "Can't set both strokeColorResource and strokeColor, strokeColor will be ignored");
            this.strokeColor = 0;
        }
        return this;
    }

    public Focus getFocusType() {
        return focusType;
    }

    public HelpOverlayConfiguration setFocusType(Focus focusType) {
        this.focusType = focusType;
        return this;
    }

    public FocusGravity getFocusGravity() {
        return focusGravity;
    }

    public HelpOverlayConfiguration setFocusGravity(FocusGravity focusGravity) {
        this.focusGravity = focusGravity;
        return this;
    }

    public boolean isDismissOnTouch() {
        return dismissOnTouch;
    }

    public HelpOverlayConfiguration setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
        return this;
    }

    public int getMessageResourceColor() {
        return messageResourceColor;
    }

    public HelpOverlayConfiguration setMessageResourceColor(Integer messageColor) {
        this.messageResourceColor = messageColor;
        return this;
    }

    public boolean isDotViewEnabled() {
        return isDotViewEnabled;
    }

    public HelpOverlayConfiguration setDotViewEnabled(boolean dotViewEnabled) {
        isDotViewEnabled = dotViewEnabled;
        return this;
    }

    @ColorInt
    public int getDotColor() {
        return dotColor;
    }

    public HelpOverlayConfiguration setDotColor(@ColorInt int dotColor) {
        this.dotColor = dotColor;
        return this;
    }

    @ColorRes
    public int getDotColorResource() {
        return dotColorResource;
    }

    public HelpOverlayConfiguration setDotColorResource(@ColorRes int dotColorResource) {
        this.dotColorResource = dotColorResource;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public int getDotSize() {
        return dotSize;
    }

    public HelpOverlayConfiguration setDotSize(@Dimension(unit = Dimension.PX) int dotSize) {
        this.dotSize = dotSize;
        return this;
    }

    @DimenRes
    public int getDotSizeResource() {
        return dotSizeResource;
    }

    public HelpOverlayConfiguration setDotSizeResource(@DimenRes int dotSizeResource) {
        this.dotSizeResource = dotSizeResource;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public int getCutoutStrokeSize() {
        return cutoutStroke;
    }

    public HelpOverlayConfiguration setCutoutStrokeSize(@Dimension(unit = Dimension.PX) int size) {
        this.cutoutStroke = Utils.dpToPx(size);
        return this;
    }

    public int getCutoutStrokeSizeResource() {
        return cutoutStrokeSizeResource;
    }

    public HelpOverlayConfiguration setCutoutStrokeSizeResource(Integer cutoutStrokeSizeResource) {
        this.cutoutStrokeSizeResource = cutoutStrokeSizeResource;
        return this;
    }

    public int getDelayBeforeShow() {
        return delayBeforeShow;
    }

    public HelpOverlayConfiguration setDelayBeforeShow(Integer delayBeforeShow) {
        this.delayBeforeShow = delayBeforeShow;
        return this;
    }

    @ColorInt
    public int getMaskColor() {
        return maskColor;
    }

    public HelpOverlayConfiguration setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        if (this.maskColorResource != 0) {
            Log.w(TAG, "Can't set both maskColorResource and maskColor, maskColorResource will be ignored");
            this.maskColorResource = 0;
        }
        return this;
    }

    @ColorRes
    public int getOverlayColorResource() {
        return maskColorResource;
    }

    public HelpOverlayConfiguration setOverlayColorResource(int maskColorResource) {
        this.maskColorResource = maskColorResource;
        if (this.maskColor != 0) {
            Log.w(TAG, "Can't set both maskColor and maskColorResource, maskColor will be ignored");
            this.maskColor = 0;
        }
        return this;
    }

    @ColorInt
    public int getTitleColor() {
        return titleColor;
    }

    public HelpOverlayConfiguration setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        if (this.titleResourceColor != 0) {
            Log.w(TAG, "Can't set both titleColor and titleResourceColor, titleResourceColor will be ignored");
            this.titleResourceColor = 0;
        }
        return this;
    }

    @ColorRes
    public int getTitleResourceColor() {
        return titleResourceColor;
    }

    public HelpOverlayConfiguration setTitleResourceColor(@ColorRes int titleColor) {
        this.titleResourceColor = titleColor;
        if (this.titleColor != 0) {
            Log.w(TAG, "Can't set both titleResourceColor and titleColor, titleColor will be ignored");
            this.titleColor = 0;
        }
        return this;
    }

    public int getMessageColor() {
        return messageColor;
    }

    public HelpOverlayConfiguration setMessageColor(@ColorInt int messageColor) {
        this.messageColor = messageColor;
        return this;
    }

    public boolean isClickTargetOnTouch() {
        return clickTargetOnTouch;
    }

    public HelpOverlayConfiguration setClickTargetOnTouch(boolean clickTargetOnTouch) {
        this.clickTargetOnTouch = clickTargetOnTouch;
        return this;
    }

    public int getButtonColorLeft() {
        return buttonColorLeft;
    }

    public HelpOverlayConfiguration setButtonColorLeft(@ColorInt int color) {
        this.buttonColorLeft = color;
        return this;
    }

    @ColorInt
    public int getButtonColorRight() {
        return buttonColorRight;
    }

    public HelpOverlayConfiguration setButtonColorRight(@ColorInt int color) {
        this.buttonColorRight = color;
        return this;
    }

    @ColorRes
    public int getButtonColorResourceLeft() {
        return buttonColorResourceLeft;
    }

    public HelpOverlayConfiguration setButtonColorResourceLeft(@ColorRes int color) {
        this.buttonColorResourceLeft = color;
        if (this.buttonColorLeft != 0) {
            Log.w(TAG, "Can't set both buttonColorLeft and buttonColorResourceLeft, buttonColorLeft will be ignored");
            this.buttonColorLeft = 0;
        }
        return this;
    }

    public int getButtonColorResourceRight() {
        return buttonColorResourceRight;
    }

    public HelpOverlayConfiguration setButtonColorResourceRight(@ColorRes int color) {
        this.buttonColorResourceRight = color;
        if (this.buttonColorRight != 0) {
            Log.w(TAG, "Can't set both buttonColorRight and buttonColorResourceRight, buttonColorRight will be ignored");
            this.buttonColorRight = 0;
        }
        return this;
    }

    @ColorInt
    public int getButtonTextColorLeft() {
        return buttonTextColorLeft;
    }

    public HelpOverlayConfiguration setButtonTextColorLeft(@ColorInt int buttonTextColorLeft) {
        this.buttonTextColorLeft = buttonTextColorLeft;
        if (this.buttonTextColorResourceLeft != 0) {
            Log.w(TAG, "Can't set both buttonTextColorLeft and buttonTextColorResourceLeft, buttonTextColorResourceLeft will be ignored");
            this.buttonTextColorResourceLeft = 0;
        }
        return this;
    }

    @ColorInt
    public int getButtonTextColorRight() {
        return buttonTextColorRight;
    }

    public HelpOverlayConfiguration setButtonTextColorRight(@ColorInt int buttonTextColorRight) {
        this.buttonTextColorRight = buttonTextColorRight;
        if (this.buttonTextColorResourceRight != 0) {
            Log.w(TAG, "Can't set both buttonTextColorRight and buttonTextColorResourceRight, buttonTextColorResourceRight will be ignored");
            this.buttonTextColorResourceRight = 0;
        }
        return this;
    }

    @ColorRes
    public int getButtonTextColorResourceLeft() {
        return buttonTextColorResourceLeft;
    }

    public HelpOverlayConfiguration setButtonTextColorResourceLeft(@ColorRes int buttonTextColorResourceLeft) {
        this.buttonTextColorResourceLeft = buttonTextColorResourceLeft;
        if (this.buttonTextColorLeft != 0) {
            Log.w(TAG, "Can't set both buttonTextColorResourceLeft and buttonTextColorLeft, buttonTextColorLeft will be ignored");
            this.buttonTextColorLeft = 0;
        }
        return this;
    }

    @ColorRes
    public int getButtonTextColorResourceRight() {
        return buttonTextColorResourceRight;
    }

    public HelpOverlayConfiguration setButtonTextColorResourceRight(@ColorRes int buttonTextColorResourceRight) {
        this.buttonTextColorResourceRight = buttonTextColorResourceRight;
        if (this.buttonTextColorRight != 0) {
            Log.w(TAG, "Can't set both buttonTextColorResourceRight and buttonTextColorRight, buttonTextColorRight will be ignored");
            this.buttonTextColorRight = 0;
        }
        return this;
    }

    public int getInfoMargin() {
        return infoMargin;
    }

    public HelpOverlayConfiguration setInfoMargin(Integer infoMargin) {
        this.infoMargin = infoMargin;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public int getCutoutRadius() {
        return cutoutRadius;
    }

    public HelpOverlayConfiguration setCutoutRadius(@Dimension(unit = Dimension.PX) int size) {
        this.cutoutRadius = size;
        return this;
    }

    @DimenRes
    @Dimension(unit = Dimension.DP)
    public int getCutoutRadiusResource() {
        return cutoutRadiusResource;
    }

    public HelpOverlayConfiguration setCutoutRadiusResource(@DimenRes @Dimension(unit = Dimension.DP) int cutoutRadiusResource) {
        this.cutoutRadiusResource = cutoutRadiusResource;
        return this;
    }

    @DimenRes
    public int getInfoMarginResource() {
        return infoMarginResource;
    }

    public HelpOverlayConfiguration setInfoMarginResource(@DimenRes int infoMarginResource) {
        this.infoMarginResource = infoMarginResource;
        return this;
    }

    @StringRes
    public int getButtonTextResourceLeft() {
        return buttonTextResourceLeft;
    }

    public HelpOverlayConfiguration setButtonTextResourceLeft(@StringRes int res) {
        buttonTextResourceLeft = res;
        return this;
    }

    @StringRes
    public int getButtonTextResourceRight() {
        return buttonTextResourceRight;
    }

    public HelpOverlayConfiguration setButtonTextResourceRight(@StringRes int res) {
        buttonTextResourceRight = res;
        return this;
    }

    public Visibility getButtonVisibilityRight() {
        return buttonVisibilityRight;
    }

    public HelpOverlayConfiguration setButtonVisibilityRight(Visibility visibility) {
        buttonVisibilityRight = visibility;
        return this;
    }

    public Visibility getButtonVisibilityLeft() {
        return buttonVisibilityLeft;
    }

    public HelpOverlayConfiguration setButtonVisibilityLeft(Visibility visibility) {
        buttonVisibilityLeft = visibility;
        return this;
    }

    public String getButtonTextLeft() {
        return buttonTextLeft;
    }

    public HelpOverlayConfiguration setButtonTextLeft(String res) {
        buttonTextLeft = res;
        return this;
    }

    public String getButtonTextRight() {
        return buttonTextRight;
    }

    public HelpOverlayConfiguration setButtonTextRight(String res) {
        buttonTextRight = res;
        return this;
    }

    //Visible states for Left and Right Button
    public enum Visibility {
        VISIBLE,
        GONE
    }
}