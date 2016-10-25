package cn.feicui.com.videoplayer.anotation;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/10/25 0025.
 */

public class ViewInjectProxy {
    public static void bind(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ViewInject.class)) {
                ViewInject inject = field.getAnnotation(ViewInject.class);
                int value = inject.value();
                if (value>0) {
                    field.setAccessible(true); //
                    try {
                        field.set(activity,activity.findViewById(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
