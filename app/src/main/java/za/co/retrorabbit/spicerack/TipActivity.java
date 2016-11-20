package za.co.retrorabbit.spicerack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TipActivity extends AppCompatActivity {

    public static final String TIP_LAYOUT = "TIP_LAYOUT";

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

    }

    public enum TipLayout {
        COORDINATOR,
        RELATIVE,
        CUSTOM
    }
}
