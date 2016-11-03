package cn.feicui.com.videoplayer.anotation;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class GetProxy {

    private  static String TAG = "GetProxy";

    public static void bind(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(GET.class)) {
                GET inject = method.getAnnotation(GET.class);
                String value = inject.value();
                //获取方法头上带有注解的参数
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                    for (Annotation annotation : parameterAnnotation) {
                        Query query = (Query) annotation;
                        String s = query.value();
                        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
                    }
                }
                if (!TextUtils.isEmpty(value)) {
                    method.setAccessible(true); //
//                        method.set(activity,activity.findViewById(value));
                    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
