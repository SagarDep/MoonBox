package net.qiujuer.jumper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.qiujuer.jumper.sample.account.AccountContract;
import net.qiujuer.jumper.sample.account.AccountPresenter;

import butterknife.OnCheckedChanged;
import butterknife.OnFocusChange;

public class MainActivity extends AppCompatActivity implements AccountContract.View {
    private EditText mNameEdit;
    private EditText mPasswordEdit;
    private AccountContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new AccountPresenter(this);

        mNameEdit = (EditText) findViewById(R.id.et_name);
        mPasswordEdit = (EditText) findViewById(R.id.et_password);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login();
            }
        });


    }

    @Override
    public String getName() {
        return mNameEdit.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordEdit.getText().toString();
    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "showLoading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucceed(String str) {
        Toast.makeText(this, "onSucceed:" + str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(int strRes) {
        Toast.makeText(this, "onFailed", Toast.LENGTH_SHORT).show();
    }





}
