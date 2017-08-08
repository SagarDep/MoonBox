package net.qiujuer.jumper.sample.factory;

import android.os.SystemClock;

import net.qiujuer.jumper.Jumper;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class HashPresenter implements HashContract.Presenter {
    private HashContract.View mView;
    private HashContract.Presenter mPresenter;

    public HashPresenter(HashContract.View view) {
        mView = Jumper.wrap(view);
        mPresenter = Jumper.wrap(this);
        view.setPresenter(mPresenter);
    }

    private HashContract.View getView() {
        return mView;
    }

    @Override
    public void startCalculate() {
        HashContract.View view = getView();

        // 获取文件地址
        String filePath = view.getPath();


        view.onStartCalculate();

        // 模拟耗时
        for (int i = 1; i <= 500; i++) {
            SystemClock.sleep(10);
            view.onProgress(i / 500.0f);
        }

        view.onSucceed("计算成功");
    }

    @Override
    public void dispose() {
        Jumper.dispose(mView);
        Jumper.dispose(mPresenter);
    }
}
