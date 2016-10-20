package co.za.retrorabbit.emmenthal.utils;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.support.annotation.StyleRes;
import android.util.Log;

import co.za.retrorabbit.emmenthal.shape.Focus;
import co.za.retrorabbit.emmenthal.shape.FocusGravity;

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
    private Integer maskColor;
    /**
     * Mask Color Resource
     */
    @ColorRes
    private Integer maskColorResource;
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
    private Integer strokeResourceColor;
    /**
     * stroke color
     */
    private Integer strokeColor;
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
    private Integer titleColor;
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
    private Integer dotColor;
    /**
     * Dot View Diameter
     */
    private Integer dotSize;
    /**
     * Perform click operation to target
     * if this is true
     */
    private boolean clickTargetOnTouch = false;
    /**
     * Left or Right Button color
     */
    @ColorInt
    private Integer buttonColorLeft, buttonColorRight;
    /**
     * Left or Right Button Color Resource
     */
    @ColorRes
    private Integer buttonColorResourceLeft, buttonColorResourceRight;
    /**
     * Left or Right Button Text Color
     */
    @ColorInt
    private Integer buttonTextColorLeft, buttonTextColorRight;
    /**
     * Left or Right Button Text Color
     */
    @ColorRes
    private Integer buttonTextColorResourceLeft, buttonTextColorResourceRight;
    /**
     * Title or Message Color Resource
     */
    @ColorRes
    private Integer titleResourceColor, messageResourceColor;

    private int cutoutPadding, cutoutStroke;
    private int infoMargin;

    public HelpOverlayConfiguration() {
        setMaskColor(Constants.DEFAULT_MASK_COLOR);
        setDelayBeforeShow(Constants.DEFAULT_DELAY_MILLIS);
        setFadeAnimationDuration(Constants.DEFAULT_FADE_DURATION);
        setCutoutPadding(Utils.dpToPx(Constants.DEFAULT_TARGET_DIAMETER));
        setCutoutStroke(Utils.dpToPx(Constants.DEFAULT_TARGET_STROKE_DIAMETER));
        setTitleColor(Constants.DEFAULT_COLOR_TITLE);
        setMessageColor(Constants.DEFAULT_COLOR_MESSAGE);
        setDotColor(Constants.DEFAULT_DOT_COLOR);
        setDotSize(Utils.dpToPx(Constants.DEFAULT_DOT_SIZE));
        setCutoutColor(Constants.CUTOUT_COLOR);
        setStrokeColor(Constants.STROKE_COLOR);
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

    @StyleRes
    public int getMessageStyle() {
        return messageStyle;
    }

    public HelpOverlayConfiguration setMessageStyle(int messageStyle) {
        this.messageStyle = messageStyle;
        return this;
    }

    @StyleRes
    public int getTitleStyle() {
        return titleStyle;
    }

    public HelpOverlayConfiguration setTitleStyle(int titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    public HelpOverlayConfiguration setMessageText(String messageText) {
        this.messageText = messageText;
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

    public int getCutoutColor() {
        return cutoutColor;
    }

    public HelpOverlayConfiguration setCutoutColor(int cutoutColor) {
        this.cutoutColor = cutoutColor;
        return this;
    }

    @ColorInt
    public Integer getStrokeColor() {
        return strokeColor;
    }

    public HelpOverlayConfiguration setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        if (this.strokeResourceColor != null) {
            Log.w(TAG, "Can't set both strokeColor and strokeResourceColor, strokeResourceColor will be ignored");
            this.strokeResourceColor = null;
        }
        return this;
    }

    @ColorRes
    public Integer getStrokeResourceColor() {
        return strokeResourceColor;
    }

    public HelpOverlayConfiguration setStrokeResourceColor(@ColorRes int strokeResourceColor) {
        this.strokeResourceColor = strokeResourceColor;
        if (this.strokeColor != null) {
            Log.w(TAG, "Can't set both strokeResourceColor and strokeColor, strokeColor will be ignored");
            this.strokeColor = null;
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

    public Integer getMessageResourceColor() {
        return messageResourceColor;
    }

    public HelpOverlayConfiguration setMessageColor(int messageColor) {
        this.messageColor = messageColor;
        return this;
    }

    public boolean isDotViewEnabled() {
        return isDotViewEnabled;
    }

    public HelpOverlayConfiguration setDotViewEnabled(boolean dotViewEnabled) {
        isDotViewEnabled = dotViewEnabled;
        return this;
    }

    public int getDotColor() {
        return dotColor;
    }

    public HelpOverlayConfiguration setDotColor(int dotColor) {
        this.dotColor = dotColor;
        return this;
    }

    public int getDotSize() {
        return dotSize;
    }

    public HelpOverlayConfiguration setDotSize(int dotSize) {
        this.dotSize = dotSize;
        return this;
    }

    public int getCutoutPadding() {
        return cutoutPadding;
    }

    public HelpOverlayConfiguration setCutoutPadding(@Dimension(unit = Dimension.PX) int size) {
        this.cutoutPadding = Utils.dpToPx(size);
        return this;
    }

    public int getCutoutStroke() {
        return cutoutStroke;
    }

    public HelpOverlayConfiguration setCutoutStroke(@Dimension(unit = Dimension.PX) int size) {
        this.cutoutStroke = Utils.dpToPx(size);
        return this;
    }

    public int getDelayBeforeShow() {
        return delayBeforeShow;
    }

    public HelpOverlayConfiguration setDelayBeforeShow(int delayBeforeShow) {
        this.delayBeforeShow = delayBeforeShow;
        return this;
    }

    @ColorInt
    public Integer getMaskColor() {
        return maskColor;
    }

    public HelpOverlayConfiguration setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        if (this.maskColorResource != null) {
            Log.w(TAG, "Can't set both maskColorResource and maskColor, maskColorResource will be ignored");
            this.maskColorResource = null;
        }
        return this;
    }

    @ColorRes
    public Integer getOverlayColorResource() {
        return maskColorResource;
    }

    public HelpOverlayConfiguration setOverlayColorResource(int maskColorResource) {
        this.maskColorResource = maskColorResource;
        if (this.maskColor != null) {
            Log.w(TAG, "Can't set both maskColor and maskColorResource, maskColor will be ignored");
            this.maskColor = null;
        }
        return this;
    }

    @ColorInt
    public Integer getTitleColor() {
        return titleColor;
    }

    public HelpOverlayConfiguration setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        if (this.titleResourceColor != null) {
            Log.w(TAG, "Can't set both titleColor and titleResourceColor, titleResourceColor will be ignored");
            this.titleResourceColor = null;
        }
        return this;
    }

    @ColorRes
    public Integer getTitleResourceColor() {
        return titleResourceColor;
    }

    public HelpOverlayConfiguration setTitleResourceColor(@ColorRes int titleColor) {
        this.titleResourceColor = titleColor;
        if (this.titleColor != null) {
            Log.w(TAG, "Can't set both titleResourceColor and titleColor, titleColor will be ignored");
            this.titleColor = null;
        }
        return this;
    }

    public Integer getMessageColor() {
        return messageColor;
    }

    public HelpOverlayConfiguration setMessageResourceColor(int messageColor) {
        this.messageResourceColor = messageColor;
        return this;
    }

    public boolean isClickTargetOnTouch() {
        return clickTargetOnTouch;
    }

    public HelpOverlayConfiguration setClickTargetOnTouch(boolean clickTargetOnTouch) {
        this.clickTargetOnTouch = clickTargetOnTouch;
        return this;
    }

    public Integer getButtonColorLeft() {
        return buttonColorLeft;
    }

    public HelpOverlayConfiguration setButtonColorLeft(@ColorInt int color) {
        this.buttonColorLeft = color;
        return this;
    }

    public Integer getButtonColorRight() {
        return buttonColorRight;
    }

    public HelpOverlayConfiguration setButtonColorRight(@ColorInt int color) {
        this.buttonColorRight = color;
        return this;
    }

    public Integer getButtonColorResourceLeft() {
        return buttonColorResourceLeft;
    }

    public HelpOverlayConfiguration setButtonColorResourceLeft(@ColorRes int color) {
        this.buttonColorResourceLeft = color;
        if (this.buttonColorLeft != null) {
            Log.w(TAG, "Can't set both buttonColorLeft and buttonColorResourceLeft, buttonColorLeft will be ignored");
            this.buttonColorLeft = null;
        }
        return this;
    }

    public Integer getButtonColorResourceRight() {
        return buttonColorResourceRight;
    }

    public HelpOverlayConfiguration setButtonColorResourceRight(@ColorRes int color) {
        this.buttonColorResourceRight = color;
        if (this.buttonColorRight != null) {
            Log.w(TAG, "Can't set both buttonColorRight and buttonColorResourceRight, buttonColorRight will be ignored");
            this.buttonColorRight = null;
        }
        return this;
    }

    @ColorInt
    public Integer getButtonTextColorLeft() {
        return buttonTextColorLeft;
    }

    public HelpOverlayConfiguration setButtonTextColorLeft(@ColorInt Integer buttonTextColorLeft) {
        this.buttonTextColorLeft = buttonTextColorLeft;
        if (this.buttonTextColorResourceLeft != null) {
            Log.w(TAG, "Can't set both buttonTextColorLeft and buttonTextColorResourceLeft, buttonTextColorResourceLeft will be ignored");
            this.buttonTextColorResourceLeft = null;
        }
        return this;
    }

    @ColorInt
    public Integer getButtonTextColorRight() {
        return buttonTextColorRight;
    }

    public HelpOverlayConfiguration setButtonTextColorRight(@ColorInt Integer buttonTextColorRight) {
        this.buttonTextColorRight = buttonTextColorRight;
        if (this.buttonTextColorResourceRight != null) {
            Log.w(TAG, "Can't set both buttonTextColorRight and buttonTextColorResourceRight, buttonTextColorResourceRight will be ignored");
            this.buttonTextColorResourceRight = null;
        }
        return this;
    }

    @ColorRes
    public Integer getButtonTextColorResourceLeft() {
        return buttonTextColorResourceLeft;
    }

    public HelpOverlayConfiguration setButtonTextColorResourceLeft(@ColorRes Integer buttonTextColorResourceLeft) {
        this.buttonTextColorResourceLeft = buttonTextColorResourceLeft;
        if (this.buttonTextColorLeft != null) {
            Log.w(TAG, "Can't set both buttonTextColorResourceLeft and buttonTextColorLeft, buttonTextColorLeft will be ignored");
            this.buttonTextColorLeft = null;
        }
        return this;
    }

    @ColorRes
    public Integer getButtonTextColorResourceRight() {
        return buttonTextColorResourceRight;
    }

    public HelpOverlayConfiguration setButtonTextColorResourceRight(@ColorRes Integer buttonTextColorResourceRight) {
        this.buttonTextColorResourceRight = buttonTextColorResourceRight;
        if (this.buttonTextColorRight != null) {
            Log.w(TAG, "Can't set both buttonTextColorResourceRight and buttonTextColorRight, buttonTextColorRight will be ignored");
            this.buttonTextColorRight = null;
        }
        return this;
    }


    public int getInfoMargin() {
        return infoMargin;
    }

    public HelpOverlayConfiguration setInfoMargin(int infoMargin) {
        this.infoMargin = infoMargin;
        return this;
    }
}