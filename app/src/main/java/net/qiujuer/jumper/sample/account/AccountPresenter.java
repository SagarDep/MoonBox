package net.qiujuer.jumper.sample.account;

import android.os.SystemClock;
import android.text.TextUtils;

import net.qiujuer.jumper.Jumper;

import java.util.Random;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.View mView;

    public AccountPresenter(AccountContract.View view) {
        mView = Jumper.wrap(view);
    }


    @Override
    public void login() {
        String name = mView.getName();
        String password = mView.getPassword();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            mView.onFailed(0);
            return;
        }

        mView.showLoading();

        new Thread(new LoginRunnable(name, password)).start();

    }

    /**
     * @param count Count
     * @return String
     * @see #start(int)
     * @since 1.2.0
     */
    public String start(int count) {
        return null;
    }

    @Override
    public void start() {

    }


    private class LoginRunnable implements Runnable {
        String name;
        String password;

        LoginRunnable(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        public void run() {
            SystemClock.sleep(5000);

            if (new Random().nextBoolean()) {
                mView.onSucceed("Name:" + name + " Password:" + password);
            } else {
                mView.onFailed(0);
            }
        }
    }

}
