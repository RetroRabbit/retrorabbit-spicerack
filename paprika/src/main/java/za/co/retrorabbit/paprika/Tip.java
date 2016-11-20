package za.co.retrorabbit.paprika;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Shows mText that you can anchor above or below a View.
 */
public class Tip extends FrameLayout {
    private int mAnchor;
    private AnchorGravity mAnchorGravity;
    private String mTipText = "";
    private TextView mTipTextView;
    private boolean mCustomView = false;
    private int mShowDuration = 2000, mAnimateDuration = 600;
    private int mHeight;

    public Tip(Context context) {
        super(context);
        init(null, 0);
    }

    public Tip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Tip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setVisibility(GONE);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Tip, defStyle, 0);

        mAnchor = a.getResourceId(R.styleable.Tip_tipAnchor, 0);
        mAnchorGravity = AnchorGravity.getType(a.getInt(R.styleable.Tip_tipAnchorGravity, 0));
        mTipText = a.getString(R.styleable.Tip_tipText);
        mShowDuration = a.getInteger(R.styleable.Tip_tipShowDuration, mShowDuration);
        mAnimateDuration = a.getInteger(R.styleable.Tip_tipAnimateDuration, mAnimateDuration);
        a.recycle();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.post(new Runnable() {
            @Override
            public void run() {

                if (getChildCount() == 0) {
                    inflate(getContext(), R.layout.tip_layout, Tip.this);
                    mTipTextView = (TextView) findViewById(R.id.tipTextView);
                    mTipTextView.setText(mTipText);
                } else {
                    mCustomView = true;
                }

                if (getParent() instanceof CoordinatorLayout) {
                    CoordinatorLayout.LayoutParams mLayoutParameterMain = (CoordinatorLayout.LayoutParams) Tip.this.getLayoutParams();

                    switch (mAnchorGravity) {
                        case ABOVE:
                            mLayoutParameterMain.anchorGravity = Gravity.TOP;
                            break;
                        case BELOW:
                            mLayoutParameterMain.anchorGravity = Gravity.BOTTOM;
                            break;

                    }

                    mLayoutParameterMain.setAnchorId(mAnchor);
                    mLayoutParameterMain.setBehavior(new AppBarLayout.ScrollingViewBehavior());
                    Tip.this.setLayoutParams(mLayoutParameterMain);
                } else if (getParent() instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams mLayoutParameterMain = (RelativeLayout.LayoutParams) Tip.this.getLayoutParams();
                    switch (mAnchorGravity) {
                        case ABOVE:
                            mLayoutParameterMain.addRule(RelativeLayout.ABOVE, mAnchor);
                            break;
                        case BELOW:
                            mLayoutParameterMain.addRule(RelativeLayout.BELOW, mAnchor);
                            break;

                    }
                    Tip.this.setLayoutParams(mLayoutParameterMain);
                }
                requestLayout();
                Tip.this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHeight = Tip.this.getMeasuredHeight();

                        startShowAnimation();

                    }
                }, 100);
            }
        });

    }

    private void startShowAnimation() {
        ValueAnimator anim = ValueAnimator.ofInt(0, mHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Tip.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Tip.this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startHideAnimation();

                    }
                }, mShowDuration);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Tip.this.getLayoutParams();
                layoutParams.height = val;
                Tip.this.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(mAnimateDuration);
        anim.start();

    }

    private void startHideAnimation() {
        ValueAnimator anim = ValueAnimator.ofInt(mHeight, 0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Tip.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Tip.this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tip.this.setVisibility(GONE);
                    }
                }, mShowDuration);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Tip.this.getLayoutParams();
                layoutParams.height = val;
                Tip.this.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(mAnimateDuration);
        anim.start();
    }

    public String getTipText() {
        if (mCustomView)
            throw new RuntimeException("Can't be used when using a custom view");
        return mTipText;
    }

    public void setTipText(@StringRes int text) {
        setTipText(getContext().getString(text));
    }

    public void setTipText(String text) {
        if (mCustomView)
            throw new RuntimeException("Can't be used when using a custom view");
        mTipText = text;
        if (mTipTextView != null)
            mTipTextView.setText(text);
        startShowAnimation();
    }

    public int getShowDuration() {
        return mShowDuration;
    }

    public void setShowDuration(int duration) {
        this.mShowDuration = duration;
    }

    public int getAnimateDuration() {
        return mAnimateDuration;
    }

    public void setAnimateDuration(int animateDuration) {
        this.mAnimateDuration = animateDuration;
    }

    public int getAnchor() {
        return mAnchor;
    }

    public void setAnchor(int mAnchor) {
        this.mAnchor = mAnchor;
    }

    public AnchorGravity getAnchorGravity() {
        return mAnchorGravity;
    }

    public void setAnchorGravity(AnchorGravity anchorGravity) {
        this.mAnchorGravity = anchorGravity;
    }

    public static class Builder {
        private Activity mActivity;
        private String mText;
        private int mParentId;
        private int mTipId;
        private Tip mTip;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        public static Builder create(Activity activity,
                                     @IdRes int parentId,
                                     @IdRes int tipId,
                                     @StringRes int text,
                                     @IntegerRes int duration,
                                     @IntegerRes int animateDuration,
                                     @IdRes int anchor,
                                     AnchorGravity gravity) {
            return create(activity, parentId, tipId, activity.getString(text), activity.getResources().getInteger(duration), activity.getResources().getInteger(animateDuration), anchor, gravity);
        }

        public static Builder create(Activity activity,
                                     @IdRes int parent,
                                     @IdRes int tipId,
                                     String text,
                                     int duration,
                                     int animateDuration,
                                     @IdRes int anchor,
                                     AnchorGravity gravity) {
            Builder builder = new Builder(activity);
            builder.mParentId = parent;
            builder.mTipId = tipId;
            builder.mText = text;
            builder.mTip = new Tip(activity);
            builder.mTip.setShowDuration(duration);
            builder.mTip.setAnimateDuration(animateDuration);
            builder.mTip.setAnchor(anchor);
            builder.mTip.setAnchorGravity(gravity);
            return builder;
        }

        public void show() {
            ViewGroup viewGroup = (ViewGroup) mActivity.findViewById(mParentId);
            if (viewGroup != null && mTip.getParent() != null) {
                mTip.setTipText(mText);
            } else if (viewGroup != null && mTip.getParent() == null) {
                viewGroup.addView(mTip);
                mTip.setTipText(mText);
            } else {
                mTip.setTipText(mText);
            }
        }

        public void setText(String text) {
            mTip.setTipText(text);
        }

        public void setText(@StringRes int text) {
            mTip.setTipText(text);
        }
    }

    public enum AnchorGravity {

        ABOVE(0),
        BELOW(1);

        private static Map<Integer, AnchorGravity> map = new HashMap<>();

        static {
            for (AnchorGravity anchorGravity : AnchorGravity.values()) {
                map.put(anchorGravity.value, anchorGravity);
            }
        }

        private final int value;

        AnchorGravity(int value) {
            this.value = value;
        }

        public static AnchorGravity getType(int value) {
            return map.containsKey(value) ? map.get(value) : ABOVE;
        }

        public int getValue() {
            return value;
        }
    }
}
