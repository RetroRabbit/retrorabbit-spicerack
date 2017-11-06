package za.co.retrorabbit.emmenthal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import za.co.retrorabbit.emmenthal.animation.AnimationFactory;
import za.co.retrorabbit.emmenthal.animation.AnimationListener;
import za.co.retrorabbit.emmenthal.animation.HelpOverlayListener;
import za.co.retrorabbit.emmenthal.shape.Circle;
import za.co.retrorabbit.emmenthal.target.Target;
import za.co.retrorabbit.emmenthal.target.ViewTarget;
import za.co.retrorabbit.emmenthal.utils.HelpOverlayConfiguration;
import za.co.retrorabbit.emmenthal.utils.HelpOverlayPreferencesManager;

/**
 * Shows a overlay that focuses on a specific view and allows two actions.
 */
public class HelpOverlay extends RelativeLayout {


    /**
     * Configuration to use for Overlay
     */
    private HelpOverlayConfiguration configuration;
    /**
     * We don't draw HelpOverlay
     * until isReady field set to true
     */
    private boolean isReady;
    /**
     * circleShape focus on target
     * and clear circle to focus
     */
    private Circle circleShape, circleShapeStroke;
    /**
     * Target View
     */
    private Target targetView;
    /**
     * Eraser
     */
    private Paint eraser;
    /**
     * Stroke
     */
    private Paint stroke;
    /**
     * Handler will be used to
     * delay HelpOverlay
     */
    private Handler handler;
    /**
     * All views will be drawn to
     * this bitmap and canvas then
     * bitmap will be drawn to canvas
     */
    private Bitmap bitmap;
    private Canvas canvas;
    /**
     * Layout width/height
     */
    private int width;
    private int height;
    /**
     * Help Dialog view
     */
    private RelativeLayout infoLayout;
    /**
     * Help Overlay Title Text
     */
    private TextView textViewTitle;
    /**
     * Help Overlay Message Text
     */
    private TextView textViewMessage;
    /**
     * Help Overlay Left Button
     */
    private Button buttonLeft;
    /**
     * Help Overlay Right Button
     */
    private Button buttonRight;
    /**
     * Help Dialog will be shown
     * If this value true
     */
    private boolean isInfoEnabled = true;
    /**
     * Dot view will appear center of
     * cleared target area
     */
    private View dotView;
    /**
     * Save/Retrieve status of HelpOverlay
     * If Intro is already learnt then don't show
     * it again.
     */
    private HelpOverlayPreferencesManager preferencesManager;
    /**
     * Check using this Id whether user learned
     * or not.
     */
    private String materialIntroViewId;
    /**
     * When layout completed, we set this true
     * Otherwise onGlobalLayoutListener stuck on loop.
     */
    private boolean isLayoutCompleted;
    /**
     * Notify user when HelpOverlay is dismissed
     */
    private HelpOverlayListener materialIntroListener;
    /**
     * Set to the Overlay Color
     */
    @ColorInt
    private int maskColor;
    /**
     * Set to the Stroke Color
     */
    @ColorInt
    private int strokeColor;
    /**
     * Set to the Cutout Radius
     */
    @ColorInt
    private int cutoutRadius;
    /**
     * Set to the Dot Color
     */
    @ColorInt
    private int dotColor;
    /**
     * Set to the Cutout Stroke Size
     */
    @Dimension(unit = Dimension.PX)
    private int cutoutStrokeSize;
    /**
     * Set to the Cutout Color
     */
    private int cutoutColor;
    /**
     * Set to the Dot Size
     */
    @Dimension(unit = Dimension.PX)
    private int dotSize;
    /**
     * Set to the Info Section Marging from Cutout
     */
    private int infoMargin;


    /**
     * Touch Even Variables
     */
    float xT,yT ;
    int xV, yV,radius ;
    double dx ,dy;
    boolean isTouchOnFocus ;
    /**
     * Set to the left and right button click listeners
     */
    private OnClickListener leftButtonOnClickListener, rightButtonOnClickListener;

    public HelpOverlay(Context context) {
        super(context);
    }

    public HelpOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelpOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    /**
     * Perform click operation when user
     * touches on target circle.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
          xT = event.getX();
          yT = event.getY();

          xV = circleShape.getPoint().x;
          yV = circleShape.getPoint().y;

          radius = circleShape.getRadius();

          dx = Math.pow(xT - xV, 2);
          dy = Math.pow(yT - yV, 2);

          isTouchOnFocus = (dx + dy) <= Math.pow(radius, 2);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isTouchOnFocus && configuration.isClickTargetOnTouch()) {
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                }

                return true;
            case MotionEvent.ACTION_UP:

                if (isTouchOnFocus || configuration.isDismissOnTouch())
                    dismiss();

                if (isTouchOnFocus && configuration.isClickTargetOnTouch()) {
                    targetView.getView().performClick();
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                    targetView.getView().setPressed(false);
                    targetView.getView().invalidate();
                }

                return true;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public void populate() {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_overlay, null);
        infoLayout = (RelativeLayout) view.findViewById(R.id.info_layout);
        textViewTitle = (TextView) view.findViewById(R.id.textview_title);
        textViewMessage = (TextView) view.findViewById(R.id.textview_message);
        buttonLeft = (Button) view.findViewById(R.id.button_left);
        buttonRight = (Button) view.findViewById(R.id.button_right);
        dotView = LayoutInflater.from(getContext()).inflate(R.layout.dotview, null);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftButtonOnClickListener != null)
                    leftButtonOnClickListener.onClick(v);
                dismiss();
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightButtonOnClickListener != null)
                    rightButtonOnClickListener.onClick(v);
                dismiss();
            }
        });


        setTitleStyle(configuration.getTitleStyle());
        setMessageStyle(configuration.getMessageStyle());
        setTitleText(configuration.getTitleText());
        setMessageText(configuration.getMessageText());

        //Configure Mask Color
        if (configuration.getMaskColor() != 0) {
            maskColor = configuration.getMaskColor();
        } else if (configuration.getOverlayColorResource() != 0) {
            maskColor = ResourcesCompat.getColor(getContext().getResources(), configuration.getOverlayColorResource(), getContext().getTheme());
        }

        //Configure Left Button Color
        if (configuration.getButtonColorLeft() != 0) {
            setButtonColorLeft(configuration.getButtonColorLeft());
        } else if (configuration.getButtonColorResourceLeft() != 0) {
            setButtonColorLeft(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonColorResourceLeft(), getContext().getTheme()));
        }

        //Configure Left Button Text
        if (configuration.getButtonTextLeft() != null) {
            setButtonTextLeft(configuration.getButtonTextLeft());
        } else if (configuration.getButtonTextResourceLeft() != 0) {
            setButtonTextLeft(configuration.getButtonTextResourceLeft());
        }

        //Configure Right Button Text
        if (configuration.getButtonTextRight() != null) {
            setButtonTextRight(configuration.getButtonTextRight());
        } else if (configuration.getButtonTextResourceRight() != 0) {
            setButtonTextRight(configuration.getButtonTextResourceRight());
        }

        //Configure Right Button Color
        if (configuration.getButtonColorRight() != 0) {
            setButtonColorRight(configuration.getButtonColorRight());
        } else if (configuration.getButtonColorResourceRight() != 0) {
            setButtonColorRight(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonColorResourceRight(), getContext().getTheme()));
        }

        //Configure Left Button Text Color
        if (configuration.getButtonTextColorLeft() != 0) {
            setButtonTextColorLeft(configuration.getButtonTextColorLeft());
        } else if (configuration.getButtonTextColorResourceLeft() != 0) {
            setButtonTextColorLeft(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonTextColorResourceLeft(), getContext().getTheme()));
        }

        //Configure Right Button Text Color
        if (configuration.getButtonTextColorRight() != 0) {
            setButtonTextColorRight(configuration.getButtonTextColorRight());
        } else if (configuration.getButtonTextColorResourceRight() != 0) {
            setButtonTextColorRight(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonTextColorResourceRight(), getContext().getTheme()));
        }

        //Configure Right Button Visibility
        if (configuration.getButtonVisibilityRight() != null) {
            setButtonVisibilityRight(configuration.getButtonVisibilityRight());
        }

        //Configure Left Button Visibility
        if (configuration.getButtonVisibilityLeft() != null) {
            setButtonVisibilityLeft(configuration.getButtonVisibilityLeft());
        }

        //Configure Title Text Color
        if (configuration.getTitleColor() != 0) {
            setTitleTextColor(configuration.getTitleColor());
        } else if (configuration.getTitleResourceColor() != 0) {
            setTitleTextColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getTitleResourceColor(), getContext().getTheme()));
        }

        //Configure Message Text Color
        if (configuration.getMessageColor() != 0) {
            setMessageTextColor(configuration.getMessageColor());
        } else if (configuration.getMessageResourceColor() != 0) {
            setMessageTextColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getMessageResourceColor(), getContext().getTheme()));
        }

        //Configure Cutout Color
        if (configuration.getCutoutColor() != 0) {
            setCutoutColor(configuration.getCutoutColor());
        } else if (configuration.getCutoutColorResource() != 0) {
            setCutoutColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getCutoutColorResource(), getContext().getTheme()));
        }

        //Configure Stroke Color
        if (configuration.getStrokeColor() != 0) {
            setStrokeColor(configuration.getStrokeColor());
        } else if (configuration.getStrokeColorResource() != 0) {
            setStrokeColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getStrokeColorResource(), getContext().getTheme()));
        }

        //Configure Cutout Radius
        if (configuration.getCutoutRadius() != 0) {
            setCutoutRadius(configuration.getCutoutRadius());
        } else if (configuration.getCutoutRadiusResource() != 0) {
            setCutoutRadius(getContext().getResources().getDimensionPixelSize(configuration.getCutoutRadiusResource()));
        }

        //Configure Cutout Stroke Size
        if (configuration.getCutoutStrokeSize() != 0) {
            setCutoutStrokeSize(configuration.getCutoutStrokeSize());
        } else if (configuration.getCutoutStrokeSizeResource() != 0) {
            setCutoutStrokeSize(getContext().getResources().getDimensionPixelSize(configuration.getCutoutStrokeSizeResource()));
        }

        //Configure Dot Color
        if (configuration.getDotColor() != 0) {
            setDotColor(configuration.getDotColor());
        } else if (configuration.getDotColorResource() != 0) {
            setDotColor(ContextCompat.getColor(getContext(), configuration.getDotColorResource()));
        }

        //Configure Dot Size
        if (configuration.getDotSize() != 0) {
            setDotSize(configuration.getDotSize());
        } else if (configuration.getDotSizeResource() != 0) {
            setDotSize(getContext().getResources().getDimensionPixelSize(configuration.getDotSizeResource()));
        }

        //Configure Info Margin
        if (configuration.getInfoMargin() != 0) {
            setInfoMargin(configuration.getInfoMargin());
        } else if (configuration.getInfoMarginResource() != 0) {
            setInfoMargin(getContext().getResources().getDimensionPixelSize(configuration.getInfoMarginResource()));
        }

        isLayoutCompleted = false;
        isReady = false;

        /**
         * initialize objects
         */
        handler = new Handler();

        preferencesManager = new HelpOverlayPreferencesManager(getContext());


        //Setup Eraser
        eraser = new Paint();
        eraser.setColor(cutoutColor);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);

        //Setup Stroke
        stroke = new Paint();
        stroke.setColor(strokeColor);
        stroke.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        stroke.setFlags(Paint.ANTI_ALIAS_FLAG);


        Drawable dotImage = ResourcesCompat.getDrawable(getResources(), R.drawable.dotview, getContext().getTheme());
        dotImage.setColorFilter(dotColor, PorterDuff.Mode.MULTIPLY);
        ((ImageView) dotView).setImageDrawable(dotImage);
        dotView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (circleShape != null)
                    circleShape.reCalculateAll();

                if (circleShapeStroke != null)
                    circleShapeStroke.reCalculateAll();

                if (height == 0)
                    return;

                if (circleShape != null && circleShape.getPoint().y != 0 && !isLayoutCompleted) {

                    setDotViewLayout(configuration.isDotViewEnabled());
                    if (isInfoEnabled)
                        setInfoLayout();
                    removeOnGlobalLayoutListener(HelpOverlay.this, this);
                }
            }
        });

    }

    /**
     * Shows material view with fade in
     * animation
     *
     * @param activity
     */
    private void show(Activity activity) {

        if (!preferencesManager.shouldDisplay(materialIntroViewId))
            return;

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setReady(true);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (configuration.isFadeAnimationEnabled())
                    AnimationFactory.animateFadeIn(HelpOverlay.this, configuration.getFadeAnimationDuration(), new AnimationListener.OnAnimationStartListener() {
                        @Override
                        public void onAnimationStart() {
                            setVisibility(VISIBLE);
                        }
                    });
                else
                    setVisibility(VISIBLE);

                preferencesManager.setDisplayed(materialIntroViewId);
            }
        });

    }

    /**
     * Dismiss Material Intro View
     */
    private void dismiss() {
        AnimationFactory.animateFadeOut(this, configuration.getFadeAnimationDuration(), new AnimationListener.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(INVISIBLE);
                removeMaterialView();

                if (materialIntroListener != null)
                    materialIntroListener.onUserClicked(materialIntroViewId);
            }
        });
    }

    private void removeMaterialView() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    /**
     * locate info card view above/below the
     * circle. If circle's Y coordiante is bigger than
     * Y coordinate of root view, then locate cardview
     * above the circle. Otherwise locate below.
     */
    private void setInfoLayout() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                isLayoutCompleted = true;

                if (infoLayout.getParent() != null)
                    ((ViewGroup) infoLayout.getParent()).removeView(infoLayout);

                RelativeLayout.LayoutParams infoDialogParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //Top Half
                if (circleShape.getPoint().y < height / 2) {
                    infoDialogParams.addRule(RelativeLayout.BELOW, R.id.dotView);
                    infoDialogParams.setMargins(
                            0,
                            circleShapeStroke.getRadius() - (dotView.getLayoutParams().height / 2) + infoMargin,
                            0,
                            0);
                } else {
                    infoDialogParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.dotView);
                    infoDialogParams.setMargins(
                            0,
                            0,
                            0,
                            circleShapeStroke.getRadius() + (dotView.getLayoutParams().height / 2) + infoMargin);
                }


                infoLayout.setLayoutParams(infoDialogParams);


                infoLayout.postInvalidate();
                addView(infoLayout);

                infoLayout.setVisibility(VISIBLE);
            }
        });
    }

    private void setDotViewLayout(final boolean isDotViewEnabled) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (dotView.getParent() != null)
                    ((ViewGroup) dotView.getParent()).removeView(dotView);

                RelativeLayout.LayoutParams dotViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


                dotViewLayoutParams.height = dotSize;
                dotViewLayoutParams.width = dotSize;
                dotViewLayoutParams.setMargins(
                        circleShape.getPoint().x - (dotViewLayoutParams.width / 2),
                        circleShape.getPoint().y - (dotViewLayoutParams.height / 2),
                        0,
                        0);
                dotView.setLayoutParams(dotViewLayoutParams);
                dotView.postInvalidate();
                addView(dotView);

                dotView.setVisibility(isDotViewEnabled ? VISIBLE : INVISIBLE);

                if (isDotViewEnabled)
                    AnimationFactory.performAnimation(dotView);
            }
        });
    }

    /**
     * SETTERS
     */
    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady) return;

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }

        /**
         * Draw mask
         */
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);
        /**
         * Clear focus area
         */
        circleShape.draw(this.canvas, eraser, cutoutRadius + cutoutStrokeSize);
        /**
         * Draw Stroke Area
         */
        circleShape.draw(this.canvas, stroke, cutoutRadius + cutoutStrokeSize);

        /**
         * Clear focus area
         */
        circleShape.draw(this.canvas, eraser, cutoutRadius);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void setTarget(Target target) {
        targetView = target;
    }

    private void setCircle(Circle circleShape) {
        this.circleShape = circleShape;
    }

    private void setCircleStroke(Circle circleShapeStroke) {
        this.circleShapeStroke = circleShapeStroke;
    }

    private void setTitleText(String text) {
        if (!TextUtils.isEmpty(text))
            this.textViewTitle.setText(text);
    }

    private void setMessageText(String text) {
        if (!TextUtils.isEmpty(text))
            this.textViewMessage.setText(text);
    }

    private void setTextViewInfoSize(int textViewInfoSize) {
        this.textViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, textViewInfoSize);
    }

    public HelpOverlayConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(HelpOverlayConfiguration configuration) {
        this.configuration = configuration;
    }

    private void setUsageId(String materialIntroViewId) {
        this.materialIntroViewId = materialIntroViewId;
    }

    private void setListener(HelpOverlayListener materialIntroListener) {
        this.materialIntroListener = materialIntroListener;
    }

    public void setButtonColorRight(@ColorInt int color) {
        if (color == Color.TRANSPARENT)
            buttonRight.setBackground(null);
        else
            buttonRight.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setButtonColorLeft(@ColorInt int color) {
        if (color == Color.TRANSPARENT)
            buttonLeft.setBackground(null);
        else
            buttonLeft.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setButtonTextColorRight(@ColorInt int color) {
        buttonRight.setTextColor(color);
    }

    public void setButtonVisibilityLeft(HelpOverlayConfiguration.Visibility visibility) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) buttonRight.getLayoutParams();
        switch (visibility) {
            case GONE:
                lp.removeRule(RelativeLayout.END_OF);
                lp.addRule(RelativeLayout.ALIGN_START, R.id.textview_message);
                buttonRight.setLayoutParams(lp);
                buttonLeft.setVisibility(View.GONE);
                break;
            default:
                lp.removeRule(RelativeLayout.ALIGN_START);
                lp.addRule(RelativeLayout.END_OF, R.id.button_left);
                buttonRight.setLayoutParams(lp);
                buttonLeft.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setButtonVisibilityRight(HelpOverlayConfiguration.Visibility visibility) {
        switch (visibility) {
            case GONE:
                buttonRight.setVisibility(View.GONE);
                break;
            default:
                buttonRight.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setButtonTextColorLeft(@ColorInt int color) {
        buttonLeft.setTextColor(color);
    }

    private void setTitleStyle(@StyleRes int style) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textViewTitle.setTextAppearance(getContext(), style);
        } else {

            textViewTitle.setTextAppearance(style);
        }
    }

    private void setMessageStyle(@StyleRes int style) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textViewMessage.setTextAppearance(getContext(), style);
        } else {

            textViewMessage.setTextAppearance(style);
        }
    }

    public void setTitleTextColor(@ColorInt int color) {
        textViewTitle.setTextColor(color);
    }

    public void setMessageTextColor(@ColorInt int color) {
        textViewMessage.setTextColor(color);
    }

    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = Color.argb(
                (int) (255 * configuration.getStrokeAlpha()),
                Color.red(strokeColor),
                Color.green(strokeColor),
                Color.blue(strokeColor)
        );
    }

    public void setCutoutRadius(int cutoutRadius) {
        this.cutoutRadius = cutoutRadius;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public void setCutoutStrokeSize(int cutoutStrokeSize) {
        this.cutoutStrokeSize = cutoutStrokeSize;
    }

    public void setCutoutColor(int cutoutColor) {
        this.cutoutColor = cutoutColor;
    }

    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    public void setInfoMargin(int infoMargin) {
        this.infoMargin = infoMargin;
    }

    public void setButtonTextLeft(String buttonTextLeft) {
        buttonLeft.setText(buttonTextLeft);
    }

    public void setButtonTextLeft(int buttonTextLeftRes) {
        buttonLeft.setText(buttonTextLeftRes);
    }

    public void setButtonTextRight(String buttonTextRight) {
        buttonRight.setText(buttonTextRight);
    }

    public void setButtonTextRight(int buttonTextRightRes) {
        buttonRight.setText(buttonTextRightRes);
    }

    public void setLeftButtonOnClickListener(OnClickListener onClickListener) {
        leftButtonOnClickListener = onClickListener;
    }

    public void setRightButtonOnClickListener(OnClickListener onClickListener) {
        rightButtonOnClickListener = onClickListener;
    }

    /**
     * Builder Class
     */
    public static class Builder {

        int resId, layoutId, position;
        private BlockingView touchBlocker;
        private HelpOverlay materialIntroView;
        private Activity activity;
        private RecyclerView.OnChildAttachStateChangeListener onChildAttachStateChangeListener;

        private Builder(Activity activity) {
            this.activity = activity;
            materialIntroView = new HelpOverlay(activity);
        }

        public static Builder start(Activity activity) {
            final Builder builder = new Builder(activity);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.disableWindow();
                }
            });
            return builder;
        }

        public Builder setConfiguration(HelpOverlayConfiguration config) {
            materialIntroView.setConfiguration(config);
            return this;
        }

        private HelpOverlay build() {
            materialIntroView.populate();

            materialIntroView.setCircle(new Circle(
                    materialIntroView.targetView,
                    materialIntroView.configuration.getFocusType(),
                    materialIntroView.configuration.getFocusGravity(),
                    materialIntroView.cutoutRadius));

            materialIntroView.setCircleStroke(new Circle(
                    materialIntroView.targetView,
                    materialIntroView.configuration.getFocusType(),
                    materialIntroView.configuration.getFocusGravity(),
                    materialIntroView.cutoutRadius + materialIntroView.cutoutStrokeSize));
            return materialIntroView;
        }

        public Builder setUsageId(String usageId) {
            materialIntroView.setUsageId(usageId);
            return this;
        }

        public Builder setLeftButtonOnClickListener(OnClickListener onClickListener) {
            materialIntroView.setLeftButtonOnClickListener(onClickListener);

            return this;
        }

        public Builder setRightButtonOnClickListener(OnClickListener onClickListener) {
            materialIntroView.setRightButtonOnClickListener(onClickListener);
            return this;
        }

        public void show(final View view) {
            if (materialIntroView.getConfiguration() == null)
                materialIntroView.setConfiguration(new HelpOverlayConfiguration());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    materialIntroView.setTarget(new ViewTarget(view));

                    build().show(activity);

                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableWindow();
                        }
                    }, materialIntroView.getConfiguration().getFadeAnimationDuration() + 100);
                }
            }, materialIntroView.getConfiguration().getDelayBeforeShow());

        }

        public void show(@IdRes int resId, @IdRes int layoutId, RecyclerView recyclerView, int position) {
            this.resId = resId;
            this.layoutId = layoutId;
            this.position = position;
            if (recyclerView.findViewHolderForAdapterPosition(this.position) != null) {
                HelpOverlay.Builder.this.show(recyclerView.findViewHolderForAdapterPosition(this.position).itemView.findViewById(HelpOverlay.Builder.this.resId));

            } else {
                onChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {
                        if (((RecyclerView) view.getParent()).getChildViewHolder(view).getAdapterPosition() == HelpOverlay.Builder.this.position
                                && ((RecyclerView) view.getParent()).getChildViewHolder(view).itemView.getId() == HelpOverlay.Builder.this.layoutId) {

                            HelpOverlay.Builder.this.show(view.findViewById(HelpOverlay.Builder.this.resId));
                            ((RecyclerView) view.getParent()).removeOnChildAttachStateChangeListener(onChildAttachStateChangeListener);

                        }
                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {

                    }
                };

                recyclerView.addOnChildAttachStateChangeListener(onChildAttachStateChangeListener);
            }
        }

        private void enableWindow() {
            createTouchBlocker();

            if (touchBlocker.getParent() != null) {
                ((ViewGroup) activity.getWindow().getDecorView()).removeView(touchBlocker);
            }
        }


        private void disableWindow() {
            createTouchBlocker();

            if (touchBlocker.getParent() == null) {
                ((ViewGroup) activity.getWindow().getDecorView()).addView(touchBlocker);
            }
        }

        private void createTouchBlocker() {
            if (touchBlocker == null) {
                touchBlocker = new BlockingView(activity);
            }
        }
    }

    static class BlockingView extends ViewGroup {

        public BlockingView(Context context) {
            super(context);
        }

        public BlockingView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BlockingView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public BlockingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }




}
