package tl.com.tlsl.activity;

import android.os.Bundle;
import android.view.View;

import tl.com.tlsl.R;

/**
 * Created by admin on 2017/10/19.
 * 筛选
 */

public class ScreenActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.screen_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mTitle.setText("销售订单");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
