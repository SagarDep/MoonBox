package net.qiujuer.jumper.sample.factory;

import net.qiujuer.jumper.annotation.JumpType;
import net.qiujuer.jumper.annotation.JumpUiThread;
import net.qiujuer.jumper.annotation.JumpWorkerThread;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface HashContract {
    interface View {

        void setPresenter(Presenter presenter);

        @JumpUiThread(JumpType.ASYNC)
        String getPath();

        @JumpUiThread(JumpType.ASYNC)
        void onStartCalculate();

        @JumpUiThread(JumpType.AWAIT)
        void onProgress(float progress);

        @JumpUiThread(JumpType.ASYNC)
        void onSucceed(String str);

        @JumpUiThread(JumpType.ASYNC)
        void onFailed(String str);
    }

    interface Presenter {
        @JumpWorkerThread
        void startCalculate();

        void dispose();
    }
}
