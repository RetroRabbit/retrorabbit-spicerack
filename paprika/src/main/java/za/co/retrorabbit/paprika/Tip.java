package za.co.retrorabbit.paprika;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Shows text that you can anchor above or below a View.
 */
public class Tip extends FrameLayout {
    private int mAnchor;
    private AnchorGravity mAnchorGravity;
    private String mTipText;
    private TextView mTipTextView;
    private boolean customView = false;

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
                    setTipText(mTipText);
                } else {
                    customView = true;
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
                Tip.this.setVisibility(VISIBLE);
            }
        });

    }

    public String getTipText() {
        if (customView)
            throw new RuntimeException("Can't be used when using a custom view");
        return mTipText;
    }

    public void setTipText(String text) {
        if (customView)
            throw new RuntimeException("Can't be used when using a custom view");
        mTipText = text;
        mTipTextView.setText(text);
    }

    enum AnchorGravity {

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
