package cn.feicui.com.videoplayer.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class ObjectProvider<T> {
    private T t;

    /**
     * 创建构造方法私有化的类的对象
     * @param aClazz 类对应的Class对象
     * @param args  创建类对象时传递的参数
     * @return
     */
    public  T getInstance(Class aClazz,Object...args)
    {
        if (t == null) {
            Constructor[] constructors = aClazz.getDeclaredConstructors();
            int length = constructors.length;
            try {
                Constructor constructor = constructors[0];
                constructor.setAccessible(true);
                 t = (T) constructor.newInstance(args);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
