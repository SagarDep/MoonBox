package net.qiujuer.jumper;

import android.support.test.runner.AndroidJUnit4;

import net.qiujuer.jumper.mock.MockJumperInterface;
import net.qiujuer.jumper.mock.MockJumperInterfaceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4.class)
public class JumperTest {
    @Test
    public void with() throws Exception {
        MockJumperInterface impl = new MockJumperInterfaceImpl();
        MockJumperInterface jumperInterface = Jumper.wrap(impl);

        jumperInterface.callReturnVoid();

        String a = "A";
        double b = 12.3;
        String ret = jumperInterface.callReturn(a, b);
        assertEquals(ret, a + " " + b);

        boolean retBool = jumperInterface.callReturnBool(a, b);
        assertEquals(retBool, false);
    }

}