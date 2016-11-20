package za.co.retrorabbit.spicerack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import za.co.retrorabbit.paprika.Tip;

public class TipActivity extends AppCompatActivity {

    public static final String TIP_LAYOUT = "tip_layout";
    public static final String TIP_LAYOUT_CODE = "tip_layout_code";
    private boolean fromCode = false;
    private Tip.Builder tipBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch ((TipLayout) getIntent().getSerializableExtra(TIP_LAYOUT)) {
            case COORDINATOR:
                setContentView(R.layout.activity_tip_coordinator);
                break;
            case RELATIVE:
                setContentView(R.layout.activity_tip_relative);
                break;
            case CUSTOM:
                setContentView(R.layout.activity_tip_custom);
                break;
        }
        fromCode = getIntent().getBooleanExtra(TIP_LAYOUT_CODE, false);
        if (fromCode) {
            ((Button) findViewById(R.id.showTipButton)).setText("Show");
            ((ViewGroup) findViewById(R.id.container)).removeView(findViewById(R.id.tip));
            tipBuilder = Tip.Builder.create(this, R.id.container, R.id.tip, R.string.tipText, R.integer.showDuration, R.integer.animationDuration, R.id.appbar, Tip.AnchorGravity.BELOW);
        }

    }

    public void showTipAgain(View view) {
        ((Button) findViewById(R.id.showTipButton)).setText("Show Again");
        if (fromCode) {
            tipBuilder.show();
        } else
            ((Tip) findViewById(R.id.tip)).setTipText(R.string.tipText);
    }

    public enum TipLayout {
        COORDINATOR,
        RELATIVE,
        CUSTOM
    }
}
