package net.qiujuer.jumper.mock;


import net.qiujuer.jumper.annotation.JumpUiThread;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface MockJumperInterface {

    @JumpUiThread
    String callReturn(String a, double b);

    @JumpUiThread
    void callReturnVoid();

    @JumpUiThread
    Boolean callReturnBool(String a, double b);
}
