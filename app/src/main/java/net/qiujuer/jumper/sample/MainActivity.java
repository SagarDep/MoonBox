package net.qiujuer.jumper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.qiujuer.jumper.sample.factory.HashPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashFragment hashFragment = new HashFragment();
        new HashPresenter(hashFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_content, hashFragment)
                .commit();



    }
}
