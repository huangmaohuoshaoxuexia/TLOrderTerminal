package tl.com.tlsl.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import tl.com.tlsl.R;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.model.entity.User;
import tl.com.tlsl.service.DemoIntentService;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.utils.ScreenUtils;

/**
 * Created by admin on 2017/10/19.
 */

public class MainActivity extends BaseActivity{

    private LinearLayout mHeadBack,mContentLayout;
    private ImageView mExitImage;
    @Override
    protected int getLayoutId() {
        return R.layout.main_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        mExitImage = findViewById(R.id.exit_image);
        mExitImage.setOnClickListener(this);
        mHeadBack = findViewById(R.id.main_head_back);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeadBack.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenHeight(this)*0.31);
        mHeadBack.setLayoutParams(params);
        mContentLayout = findViewById(R.id.main_content_layout);
        params = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.setMargins(0,DensityUtils.dp2px(this,-100),0,0);
        mContentLayout.setLayoutParams(params);
        mContentLayout.setOnClickListener(this);
    }

    @Override
    public void initListeners() {
        super.initListeners();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void setHeader() {
        super.setHeader();
        mTitleLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.main_content_layout:
                startActivity(SaleOrderActivity.class,null);
                break;
            case R.id.exit_image:
                Constants.token = "";
                startActivity(LogActivity.class,null);
                finish();
                break;
        }
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
