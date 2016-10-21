package co.za.retrorabbit.emmenthal.utils;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.FloatRange;
import android.support.annotation.StringRes;
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
    private Integer titleStyle;
    /**
     * Message Font Style
     */
    @StyleRes
    private Integer messageStyle;
    /**
     * HelpOverlay will start
     * showing after delayMillis seconds
     * passed
     */
    private Integer delayBeforeShow;
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
    private Integer cutoutColor;
    /**
     * Stroke Color Resource
     */
    private Integer strokeColorResource;
    /**
     * Cutout Stroke Color
     */
    private Integer strokeColor;
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
    private Integer titleColor;
    /**
     * Help Dialog Message text color
     */
    private Integer messageColor;
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
     * Dot View Diameter Resource
     */
    private Integer dotSizeResource;
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

    private Integer cutoutRadius;
    private Integer cutoutStroke;
    private Integer infoMargin;
    private Integer cutoutRadiusResource;
    private Integer cutoutStrokeSizeResource;
    private Integer dotColorResource;
    private Integer cutoutColorResource;
    private Integer infoMarginResource;
    private Integer buttonTextResourceLeft;
    private Integer buttonTextResourceRight;
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

    @StyleRes
    public Integer getMessageStyle() {
        return messageStyle;
    }

    public HelpOverlayConfiguration setMessageStyle(Integer messageStyle) {
        this.messageStyle = messageStyle;
        return this;
    }

    @StyleRes
    public Integer getTitleStyle() {
        return titleStyle;
    }

    public HelpOverlayConfiguration setTitleStyle(Integer titleStyle) {
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

    @ColorInt
    public Integer getCutoutColor() {
        return cutoutColor;
    }

    public HelpOverlayConfiguration setCutoutColor(@ColorInt Integer cutoutColor) {
        this.cutoutColor = cutoutColor;
        return this;
    }

    public HelpOverlayConfiguration setCutoutColorResource(@ColorRes Integer cutoutColorResource) {
        this.cutoutColorResource = cutoutColorResource;
        return this;
    }

    @ColorRes
    public Integer getCutoutColorResource() {
        return cutoutColorResource;
    }

    @ColorInt
    public Integer getStrokeColor() {
        return strokeColor;
    }

    public HelpOverlayConfiguration setStrokeColor(@ColorInt Integer strokeColor) {
        this.strokeColor = strokeColor;
        if (this.strokeColorResource != null) {
            Log.w(TAG, "Can't set both strokeColor and strokeColorResource, strokeColorResource will be ignored");
            this.strokeColorResource = null;
        }
        return this;
    }

    public HelpOverlayConfiguration setCutoutStrokeColorResource(@ColorRes Integer strokeColor, @FloatRange(from = 0f, to = 1f) float alpha) {
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
    public Integer getStrokeColorResource() {
        return strokeColorResource;
    }

    public HelpOverlayConfiguration setCutoutStrokeColorResource(@ColorRes Integer strokeResourceColor) {
        this.strokeColorResource = strokeResourceColor;
        if (this.strokeColor != null) {
            Log.w(TAG, "Can't set both strokeColorResource and strokeColor, strokeColor will be ignored");
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

    public HelpOverlayConfiguration setMessageColor(Integer messageColor) {
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

    @ColorInt
    public Integer getDotColor() {
        return dotColor;
    }

    public HelpOverlayConfiguration setDotColor(@ColorInt Integer dotColor) {
        this.dotColor = dotColor;
        return this;
    }

    @ColorRes
    public Integer getDotColorResource() {
        return dotColorResource;
    }

    public HelpOverlayConfiguration setDotColorResource(@ColorRes Integer dotColorResource) {
        this.dotColorResource = dotColorResource;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public Integer getDotSize() {
        return dotSize;
    }

    public HelpOverlayConfiguration setDotSize(@Dimension(unit = Dimension.PX) Integer dotSize) {
        this.dotSize = dotSize;
        return this;
    }

    @DimenRes
    public Integer getDotSizeResource() {
        return dotSizeResource;
    }

    public HelpOverlayConfiguration setDotSizeResource(@DimenRes Integer dotSizeResource) {
        this.dotSizeResource = dotSizeResource;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public Integer getCutoutStrokeSize() {
        return cutoutStroke;
    }

    public HelpOverlayConfiguration setCutoutStrokeSize(@Dimension(unit = Dimension.PX) Integer size) {
        this.cutoutStroke = Utils.dpToPx(size);
        return this;
    }

    public Integer getCutoutStrokeSizeResource() {
        return cutoutStrokeSizeResource;
    }

    public HelpOverlayConfiguration setCutoutStrokeSizeResource(Integer cutoutStrokeSizeResource) {
        this.cutoutStrokeSizeResource = cutoutStrokeSizeResource;
        return this;
    }

    public Integer getDelayBeforeShow() {
        return delayBeforeShow;
    }

    public HelpOverlayConfiguration setDelayBeforeShow(Integer delayBeforeShow) {
        this.delayBeforeShow = delayBeforeShow;
        return this;
    }

    @ColorInt
    public Integer getMaskColor() {
        return maskColor;
    }

    public HelpOverlayConfiguration setMaskColor(Integer maskColor) {
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

    public HelpOverlayConfiguration setOverlayColorResource(Integer maskColorResource) {
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

    public HelpOverlayConfiguration setTitleColor(@ColorInt Integer titleColor) {
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

    public HelpOverlayConfiguration setTitleResourceColor(@ColorRes Integer titleColor) {
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

    public HelpOverlayConfiguration setMessageResourceColor(Integer messageColor) {
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

    public HelpOverlayConfiguration setButtonColorLeft(@ColorInt Integer color) {
        this.buttonColorLeft = color;
        return this;
    }

    @ColorInt
    public Integer getButtonColorRight() {
        return buttonColorRight;
    }

    public HelpOverlayConfiguration setButtonColorRight(@ColorInt Integer color) {
        this.buttonColorRight = color;
        return this;
    }

    @ColorRes
    public Integer getButtonColorResourceLeft() {
        return buttonColorResourceLeft;
    }

    public HelpOverlayConfiguration setButtonColorResourceLeft(@ColorRes Integer color) {
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

    public HelpOverlayConfiguration setButtonColorResourceRight(@ColorRes Integer color) {
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

    public Integer getInfoMargin() {
        return infoMargin;
    }

    public HelpOverlayConfiguration setInfoMargin(Integer infoMargin) {
        this.infoMargin = infoMargin;
        return this;
    }

    @Dimension(unit = Dimension.PX)
    public Integer getCutoutRadius() {
        return cutoutRadius;
    }

    public HelpOverlayConfiguration setCutoutRadius(@Dimension(unit = Dimension.PX) Integer size) {
        this.cutoutRadius = size;
        return this;
    }

    public HelpOverlayConfiguration setCutoutRadiusResource(@DimenRes @Dimension(unit = Dimension.DP) Integer cutoutRadiusResource) {
        this.cutoutRadiusResource = cutoutRadiusResource;
        return this;
    }

    @DimenRes
    @Dimension(unit = Dimension.DP)
    public Integer getCutoutRadiusResource() {
        return cutoutRadiusResource;
    }

    public HelpOverlayConfiguration setInfoMarginResource(@DimenRes Integer infoMarginResource) {
        this.infoMarginResource = infoMarginResource;
        return this;
    }

    @DimenRes
    public Integer getInfoMarginResource() {
        return infoMarginResource;
    }


    public HelpOverlayConfiguration setButtonTextResourceLeft(@StringRes Integer res) {
        buttonTextResourceLeft = res;
        return this;
    }

    @StringRes
    public Integer getButtonTextResourceLeft() {
        return buttonTextResourceLeft;
    }

    public HelpOverlayConfiguration setButtonTextResourceRight(@StringRes Integer res) {
        buttonTextResourceRight = res;
        return this;
    }

    @StringRes
    public Integer getButtonTextResourceRight() {
        return buttonTextResourceRight;
    }

    public HelpOverlayConfiguration setButtonTextLeft(String res) {
        buttonTextLeft = res;
        return this;
    }

    public String getButtonTextLeft() {
        return buttonTextLeft;
    }

    public HelpOverlayConfiguration setButtonTextRight(String res) {
        buttonTextRight = res;
        return this;
    }

    public String getButtonTextRight() {
        return buttonTextRight;
    }
}