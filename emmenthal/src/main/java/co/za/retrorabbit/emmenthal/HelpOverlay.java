package co.za.retrorabbit.emmenthal;

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
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import co.za.retrorabbit.emmenthal.animation.AnimationFactory;
import co.za.retrorabbit.emmenthal.animation.AnimationListener;
import co.za.retrorabbit.emmenthal.animation.HelpOverlayListener;
import co.za.retrorabbit.emmenthal.prefs.PreferencesManager;
import co.za.retrorabbit.emmenthal.shape.Circle;
import co.za.retrorabbit.emmenthal.target.Target;
import co.za.retrorabbit.emmenthal.target.ViewTarget;
import co.za.retrorabbit.emmenthal.utils.HelpOverlayConfiguration;

/**
 * TODO: document your custom view class.
 */
public class HelpOverlay extends ConstraintLayout {

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
    private ConstraintLayout infoLayout;

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
    private PreferencesManager preferencesManager;

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
     * Show Overlay Info Section
     * if this is enabled
     */
    private boolean infoDialogEnabled = false;
    /**
     * Show Dot
     * if this is enabled
     */
    private boolean dotViewEnabled = false;
    /**
     * Set to the Overlay Color
     */
    private int maskColor;
    private Integer strokeColor;

    public HelpOverlay(Context context) {
        super(context);
    }

    public HelpOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelpOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void populate() {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_overlay, null);
        infoLayout = (ConstraintLayout) view.findViewById(R.id.info_layout);
        textViewTitle = (TextView) view.findViewById(R.id.textview_title);
        textViewMessage = (TextView) view.findViewById(R.id.textview_message);
        buttonLeft = (Button) view.findViewById(R.id.button_left);
        buttonRight = (Button) view.findViewById(R.id.button_right);
        dotView = LayoutInflater.from(getContext()).inflate(R.layout.dotview, null);


        setTitleStyle(configuration.getTitleStyle());
        setMessageStyle(configuration.getMessageStyle());
        setTitleText(configuration.getTitleText());
        setMessageText(configuration.getMessageText());

        //Configure Mask Color
        if (configuration.getMaskColor() != null) {
            maskColor = configuration.getMaskColor();
        } else if (configuration.getOverlayColorResource() != null) {
            maskColor = ResourcesCompat.getColor(getContext().getResources(), configuration.getOverlayColorResource(), getContext().getTheme());
        }

        //Configure Left Button Color
        if (configuration.getButtonColorLeft() != null) {
            setButtonColorLeft(configuration.getButtonColorLeft());
        } else if (configuration.getButtonColorResourceLeft() != null) {
            setButtonColorLeft(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonColorResourceLeft(), getContext().getTheme()));
        }

        //Configure Right Button Color
        if (configuration.getButtonColorRight() != null) {
            setButtonColorRight(configuration.getButtonColorRight());
        } else if (configuration.getButtonColorResourceRight() != null) {
            setButtonColorRight(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonColorResourceRight(), getContext().getTheme()));
        }

        //Configure Left Button Text Color
        if (configuration.getButtonTextColorLeft() != null) {
            setButtonTextColorLeft(configuration.getButtonTextColorLeft());
        } else if (configuration.getButtonTextColorResourceLeft() != null) {
            setButtonTextColorLeft(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonTextColorResourceLeft(), getContext().getTheme()));
        }

        //Configure Right Button Text Color
        if (configuration.getButtonTextColorRight() != null) {
            setButtonTextColorRight(configuration.getButtonTextColorRight());
        } else if (configuration.getButtonTextColorResourceRight() != null) {
            setButtonTextColorRight(ResourcesCompat.getColor(getContext().getResources(), configuration.getButtonTextColorResourceRight(), getContext().getTheme()));
        }

        //Configure Title Text Color
        if (configuration.getTitleColor() != null) {
            setTitleTextColor(configuration.getTitleColor());
        } else if (configuration.getTitleResourceColor() != null) {
            setTitleTextColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getTitleResourceColor(), getContext().getTheme()));
        }

        //Configure Message Text Color
        if (configuration.getMessageColor() != null) {
            setMessageTextColor(configuration.getMessageColor());
        } else if (configuration.getMessageResourceColor() != null) {
            setMessageTextColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getMessageResourceColor(), getContext().getTheme()));
        }

        //Configure Stroke Color
        if (configuration.getStrokeColor() != null) {
            setStrokeColor(configuration.getStrokeColor());
        } else if (configuration.getStrokeResourceColor() != null) {
            setStrokeColor(ResourcesCompat.getColor(getContext().getResources(), configuration.getStrokeResourceColor(), getContext().getTheme()));
        }


        isLayoutCompleted = false;
        isReady = false;

        /**
         * initialize objects
         */
        handler = new Handler();

        preferencesManager = new PreferencesManager(getContext());


        //Setup Eraser
        eraser = new Paint();
        eraser.setColor(configuration.getCutoutColor());
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);

        //Setup Stroke
        stroke = new Paint();
        stroke.setColor(strokeColor);
        stroke.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        stroke.setFlags(Paint.ANTI_ALIAS_FLAG);


        Drawable dotImage = ResourcesCompat.getDrawable(getResources(), R.drawable.dotview, getContext().getTheme());
        dotImage.setColorFilter(configuration.getDotColor(), PorterDuff.Mode.MULTIPLY);
        ((ImageView) dotView).setImageDrawable(dotImage);
        dotView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (circleShape != null)
                    circleShape.reCalculateAll();

                if (circleShapeStroke != null)
                    circleShapeStroke.reCalculateAll();

                if (circleShape != null && circleShape.getPoint().y != 0 && !isLayoutCompleted) {

                    setDotViewLayout(configuration.isDotViewEnabled());
                    if (isInfoEnabled)
                        setInfoLayout();
                    removeOnGlobalLayoutListener(HelpOverlay.this, this);
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
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
        circleShape.draw(this.canvas, eraser, configuration.getCutoutPadding() + configuration.getCutoutStroke());
        /**
         * Draw Stroke Area
         */
        circleShape.draw(this.canvas, stroke, configuration.getCutoutPadding() + configuration.getCutoutStroke());

        /**
         * Clear focus area
         */
        circleShape.draw(this.canvas, eraser, configuration.getCutoutPadding());

        canvas.drawBitmap(bitmap, 0, 0, null);
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
        float xT = event.getX();
        float yT = event.getY();

        int xV = circleShape.getPoint().x;
        int yV = circleShape.getPoint().y;

        int radius = circleShape.getRadius();

        double dx = Math.pow(xT - xV, 2);
        double dy = Math.pow(yT - yV, 2);

        boolean isTouchOnFocus = (dx + dy) <= Math.pow(radius, 2);

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

    /**
     * Shows material view with fade in
     * animation
     *
     * @param activity
     */

    private void show(Activity activity) {

        if (preferencesManager.isDisplayed(materialIntroViewId))
            return;

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setReady(true);

        handler.postDelayed(new Runnable() {
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
            }
        }, configuration.getDelayMillis());

    }

    /**
     * Dismiss Material Intro View
     */
    private void dismiss() {
        AnimationFactory.animateFadeOut(this, configuration.getFadeAnimationDuration(), new AnimationListener.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(GONE);
                removeMaterialView();
                preferencesManager.reset(materialIntroViewId);

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

                ConstraintLayout.LayoutParams infoDialogParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //Top Half
                if (circleShape.getPoint().y < height / 2) {
                    infoDialogParams.topToBottom = R.id.dotView;
                    infoDialogParams.setMargins(
                            0,
                            circleShapeStroke.getRadius() - (dotView.getLayoutParams().height / 2) + configuration.getInfoMargin(),
                            0,
                            0);
                } else {
                    infoDialogParams.bottomToTop = R.id.dotView;
                    infoDialogParams.setMargins(
                            0,
                            0,
                            0,
                            circleShapeStroke.getRadius() - (dotView.getLayoutParams().height / 2) + configuration.getInfoMargin());
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

                ConstraintLayout.LayoutParams dotViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dotViewLayoutParams.topToTop = LayoutParams.PARENT_ID;
                dotViewLayoutParams.startToStart = LayoutParams.PARENT_ID;
                dotViewLayoutParams.height = configuration.getDotSize();
                dotViewLayoutParams.width = configuration.getDotSize();
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
        buttonRight.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setButtonColorLeft(@ColorInt int color) {
        buttonLeft.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setButtonTextColorRight(@ColorInt int color) {
        buttonRight.setTextColor(color);
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

    public void setStrokeColor(Integer strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Builder Class
     */
    public static class Builder {

        private HelpOverlay materialIntroView;

        private Activity activity;
        private int targetId;

        private Builder(Activity activity) {
            this.activity = activity;
            materialIntroView = new HelpOverlay(activity);
        }

        public static Builder start(Activity activity) {
            return new Builder(activity);
        }

        public Builder setConfiguration(HelpOverlayConfiguration config) {
            materialIntroView.setConfiguration(config);
            return this;
        }

        private HelpOverlay build() {
            if (materialIntroView.getConfiguration() == null)
                materialIntroView.setConfiguration(new HelpOverlayConfiguration());

            materialIntroView.populate();

            materialIntroView.setCircle(new Circle(
                    materialIntroView.targetView,
                    materialIntroView.configuration.getFocusType(),
                    materialIntroView.configuration.getFocusGravity(),
                    materialIntroView.configuration.getCutoutPadding()));

            materialIntroView.setCircleStroke(new Circle(
                    materialIntroView.targetView,
                    materialIntroView.configuration.getFocusType(),
                    materialIntroView.configuration.getFocusGravity(),
                    materialIntroView.configuration.getCutoutPadding() + materialIntroView.configuration.getCutoutStroke()));
            return materialIntroView;
        }

        public Builder setUsageId(String usageId) {
            materialIntroView.setUsageId(usageId);
            return this;
        }

        public HelpOverlay show(View view) {
            materialIntroView.setTarget(new ViewTarget(view));
            build().show(activity);
            return materialIntroView;
        }

        public Builder show(@IdRes final int resId, final RecyclerView recyclerView, final int position) {
            recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (positionStart == position && recyclerView.findViewHolderForAdapterPosition(positionStart).itemView.findViewById(resId) != null) {
                        materialIntroView.setTarget(new ViewTarget(recyclerView.findViewHolderForAdapterPosition(positionStart).itemView.findViewById(resId)));
                        build().show(activity);
                    }
                }
            });
            return this;
        }
    }
}
