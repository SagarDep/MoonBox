package net.qiujuer.jumper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.qiujuer.jumper.sample.factory.HashPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.lay_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashFragment hashFragment = new HashFragment();
                new HashPresenter(hashFragment);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.lay_content, hashFragment)
                        .commit();
            }
        });
    }

}
