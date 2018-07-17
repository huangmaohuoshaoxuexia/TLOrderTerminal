package tl.com.tlsl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import tl.com.tlsl.R;
import tl.com.tlsl.base.BaseInit;
import tl.com.tlsl.base.PublishActivityCallBack;
import tl.com.tlsl.service.DemoPushService;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.weiget.AlertDialog;
import tl.com.tlsl.weiget.LoadingDialog;
import tl.com.tlsl.weiget.MsgLoadingDialog;

public abstract class BaseActivity extends FragmentActivity implements BaseInit,View.OnClickListener, PublishActivityCallBack{

    protected ImageView mBackImage;
    protected TextView mTitle, mRight;
    protected WindowManager.LayoutParams window;
    protected LinearLayout mTitleLayout;
    protected AlertDialog mAlertDialog;
    protected LoadingDialog loadingDialog;
    protected MsgLoadingDialog msgLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
//        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        setContentView(getLayoutId());
        setImmerseLayout(findViewById(R.id.head_id));
        //第二个参数是想要设置的颜色
//        StatusBarCompat.compat(this, getResources().getColor(R.color.title_back_color));
        initViews();
        setHeader();
        initData();
        initListeners();
        window = this.getWindow().getAttributes();
        mAlertDialog = new AlertDialog(this);
        loadingDialog = new LoadingDialog(this);
        msgLoadingDialog = new MsgLoadingDialog(this);
    }
    protected abstract int getLayoutId();
    @Override
    public void initViews() {
    }
    @Override
    public void initListeners() {
    }
    @Override
    public void initData() {
    }
    @Override
    public void logcat(String msg) {
        Log.i("tl.com.tlsl>>",msg+"=======");
    }
    @Override
    public void setHeader() {
        mBackImage =  findViewById(R.id.back);
        mTitle = findViewById(R.id.tv_title);
        mRight = findViewById(R.id.tv_right);
        mTitleLayout = findViewById(R.id.title);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
