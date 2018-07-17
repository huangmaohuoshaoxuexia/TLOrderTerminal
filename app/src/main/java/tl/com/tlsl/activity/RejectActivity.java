package tl.com.tlsl.activity;

import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import tl.com.tlsl.utils.AtyContainerUtils;

/**
 * Created by admin on 2017/10/23.
 */

public class RejectActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
    }

    @Override
    public void initViews() {
        super.initViews();
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
        mTitle.setText("驳回");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
