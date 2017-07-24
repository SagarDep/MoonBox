package net.qiujuer.jumper.mock;


import net.qiujuer.jumper.Log;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MockJumperInterfaceImpl implements MockJumperInterface {
    private static final String TAG = MockJumperInterfaceImpl.class.getSimpleName();


    @Override
    public String callReturn(String a, double b) {
        Log.p(TAG, "callReturn wrap:" + a + " " + b + " Thread:" + Thread.currentThread().getName());
        return a + " " + b;
    }

    @Override
    public void callReturnVoid() {
        Log.p(TAG, "callReturnVoid wrap:Thread:" + Thread.currentThread().getName());
    }

    @Override
    public Boolean callReturnBool(String a, double b) {
        Log.p(TAG, "callReturnBool wrap:" + a + " " + b + " Thread:" + Thread.currentThread().getName());
        return true;
    }
}
