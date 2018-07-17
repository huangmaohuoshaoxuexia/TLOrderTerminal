package tl.com.tlsl.application;

import android.app.Application;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by admin on 2018/6/22.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,"5b2ca723b27b0a60f500005d","Umeng", UMConfigure.DEVICE_TYPE_PHONE,"2fc27454eb83dd318eba2539bdc337ae");
    }
}
