package net.qiujuer.jumper.sample.account;

import android.support.annotation.StringRes;

import net.qiujuer.jumper.annotation.JumpType;
import net.qiujuer.jumper.annotation.JumpUiThread;
import net.qiujuer.jumper.annotation.JumpWorkerThread;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface AccountContract {
    interface View {
        @JumpUiThread
        String getName();

        @JumpUiThread
        String getPassword();

        @JumpUiThread(JumpType.ASYNC)
        void showLoading();

        @JumpUiThread(JumpType.ASYNC)
        void onSucceed(String str);

        @JumpUiThread(JumpType.ASYNC)
        void onFailed(@StringRes int strRes);
    }

    interface Presenter {
        void login();

        @JumpWorkerThread
        void start();
    }
}
