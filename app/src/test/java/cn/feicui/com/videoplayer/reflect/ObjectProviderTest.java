package cn.feicui.com.videoplayer.reflect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2016/11/2 0002.
 */
public class ObjectProviderTest {
    ObjectProvider<Person> objectProvider;
    @Before
    public void setUp() throws Exception {
        objectProvider = new ObjectProvider<>();
    }

    @Test
    public void getInstance() throws Exception {
        Person haha = objectProvider.getInstance(Person.class, "0606");
        Assert.assertEquals("haha",haha.name);
    }

}