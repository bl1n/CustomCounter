package team.lf.task423;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CompositeView mCompositeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompositeView = findViewById(R.id.comp_view);


        findViewById(R.id.add).setOnClickListener(v-> mCompositeView.changeCount(false));
        findViewById(R.id.sub).setOnClickListener(v-> mCompositeView.changeCount(true));

    }
}
