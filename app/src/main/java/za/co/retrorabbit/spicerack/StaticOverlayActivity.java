package za.co.retrorabbit.spicerack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import za.co.retrorabbit.emmenthal.HelpOverlay;

public class StaticOverlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_overlay);
    }

    public void showOverlay(View v) {

        HelpOverlay.Builder.start(this)
                .setLeftButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"LEFT",Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"RIGHT",Toast.LENGTH_SHORT).show();
                    }
                })
                .setConfiguration(MainActivity.getDefaultConfiguration())
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .show(v);
    }
}
