package tl.com.tlsl.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/29.
 */

public class AtyContainerUtils {
    private AtyContainerUtils() {
    }

    private static AtyContainerUtils instance = new AtyContainerUtils();
    private static List<Activity> activityStack = new ArrayList<Activity>();
    private static List<Activity> orderDetailActivityStack = new ArrayList<Activity>();
    public static AtyContainerUtils getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }
    public void addOrderActivity(Activity aty) {
        orderDetailActivityStack.add(aty);
    }
    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }
    public void finishDetailActivity() {
        for (int i = 0, size = orderDetailActivityStack.size(); i < size; i++) {
            if (null != orderDetailActivityStack.get(i)) {
                orderDetailActivityStack.get(i).finish();
            }
        }
        orderDetailActivityStack.clear();
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
