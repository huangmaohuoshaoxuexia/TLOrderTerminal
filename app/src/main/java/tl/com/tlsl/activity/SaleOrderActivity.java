package tl.com.tlsl.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tl.com.tlsl.R;
import tl.com.tlsl.adapter.FragmentAdapter;
import tl.com.tlsl.adapter.SaleOrderAdapter;
import tl.com.tlsl.fragment.SaleOrderExamineFragment;
import tl.com.tlsl.fragment.SaleOrderHistoryExamineFragment;
import tl.com.tlsl.fragment.SaleOrderMyFragment;
import tl.com.tlsl.model.impl.OrderStatusImpl;
import tl.com.tlsl.model.impl.OrderStatusInterface;
import tl.com.tlsl.popupwindow.SaleOrderPopupwindow;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2017/10/13.
 */

public class SaleOrderActivity extends BaseActivity implements IMvpView {

    //    private ViewPager mViewPager;
    private FragmentManager fm;
    private FragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> mFragmentList;
    private SaleOrderMyFragment saleOrderMyFragment = new SaleOrderMyFragment();
    private SaleOrderExamineFragment saleOrderExamineFragment = new SaleOrderExamineFragment();
    private SaleOrderHistoryExamineFragment saleOrderHistoryExamineFragment = new SaleOrderHistoryExamineFragment();
    private TextView mTitleText1, mTitleText2, mTitleText3, mScreenText;
    private View mTitleView1, mTitleView2, mTitleView3;
    private RelativeLayout mTitleLayout1, mTitleLayout2, mTitleLayout3;
    private SaleOrderPopupwindow saleOrderPopupwindow;
    private LinearLayout mHeadTextLayout;
    private android.support.v4.app.FragmentTransaction transaction;
    private FrameLayout mContentLayout;
    private LinearLayout mTitleMenuLayout, mTitleSearchLayout;
    private ImageView mSearchImage, mXimage;
    private TextView mSearchText;
    private ImageView mAddImage;
    private int page = 1, x = 1,mPosition = 0;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private SaleOrderAdapter mAdapter;
    private EditText mSearchEdit;
    private OrderStatusImpl orderStatusImpl;
    private String mFlag = "0",mOrderStatus="";
    private BroadcastReceiver myBroadcastReceiver;
    @Override
    protected int getLayoutId() {
        return R.layout.sale_order_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
        fm = getSupportFragmentManager();
        initFragment();
        setTabSelection(0);
        orderStatusImpl = new OrderStatusImpl();
        orderStatusImpl.setOrderStatusInterface(orderStatusInterface);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("flushList");
        registerReceiver(myBroadcastReceiver,filter);
    }

    @Override
    public void initViews() {
        super.initViews();
        mSearchEdit = findViewById(R.id.search_edit);
        mXimage = findViewById(R.id.x_image);
        mXimage.setOnClickListener(this);
        mTitleMenuLayout = findViewById(R.id.sale_title_text_layout);
        mTitleSearchLayout = findViewById(R.id.sale_title_search_layout);
        mSearchImage = findViewById(R.id.sale_search_image);
        mSearchImage.setOnClickListener(this);
        mSearchText = findViewById(R.id.tv_right);
        mSearchText.setOnClickListener(this);
        mContentLayout = findViewById(R.id.content);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.setMargins(0, DensityUtils.dp2px(this, 100), 0, 0);
        mContentLayout.setLayoutParams(params);

        mTitleText1 = findViewById(R.id.tv_title);
        mTitleText2 = findViewById(R.id.tv_title2);
        mTitleText3 = findViewById(R.id.tv_title3);
        mTitleView1 = findViewById(R.id.title_view1);
        mTitleView2 = findViewById(R.id.title_view2);
        mTitleView3 = findViewById(R.id.title_view3);
        mTitleLayout1 = findViewById(R.id.title_layout1);
        mTitleLayout2 = findViewById(R.id.title_layout2);
        mTitleLayout3 = findViewById(R.id.title_layout3);
        mTitleLayout1.setOnClickListener(this);
        mTitleLayout2.setOnClickListener(this);
        mTitleLayout3.setOnClickListener(this);
        mScreenText = findViewById(R.id.screen_text);
        mScreenText.setOnClickListener(this);
        mHeadTextLayout = findViewById(R.id.head_text_layout);

        mAddImage = findViewById(R.id.add_image);
        mAddImage.setOnClickListener(this);

    }

    private void initFragment() {
        // 每次选中之前先清楚掉上次的选中状态
        initBack();
        // 开启一个Fragment事务
        transaction = fm.beginTransaction();

        transaction.add(R.id.content, saleOrderMyFragment);
        transaction.add(R.id.content, saleOrderExamineFragment);
        transaction.add(R.id.content, saleOrderHistoryExamineFragment);
        transaction.commit();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
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
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.add_image:
                startActivity(SaleOrderApplyActivity.class, null);
                break;
            case R.id.sale_search_image:
                Bundle bundle = new Bundle();
                bundle.putString("flag",mFlag);
                startActivity(SearchOrderActivity.class,bundle);
                break;
            case R.id.title_layout1:
                setTabSelection(0);
                mFlag = "0";
                saleOrderMyFragment.flushList("");
                mScreenText.setVisibility(View.VISIBLE);
                break;
            case R.id.title_layout2:
                setTabSelection(1);
                mFlag = "1";
                saleOrderExamineFragment.flushList("");
                mScreenText.setVisibility(View.GONE);
                break;
            case R.id.title_layout3:
                setTabSelection(2);
                mFlag = "2";
                saleOrderHistoryExamineFragment.flushList("");
                mScreenText.setVisibility(View.VISIBLE);
                break;
            case R.id.screen_text:
                saleOrderPopupwindow = new SaleOrderPopupwindow(this,orderStatusImpl,mOrderStatus,mFlag,mPosition);
                saleOrderPopupwindow.showAsDropDown(mScreenText);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tl.com.tlsl>>","====返回返回333==="+requestCode);
        switch (requestCode) {
            case 0:
                break;
        }
    }
    private void setTabSelection(int index) {
        mContentLayout.setVisibility(View.VISIBLE);
        // 每次选中之前先清楚掉上次的选中状态
        initBack();
        transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                mTitleView1.setVisibility(View.VISIBLE);
                mTitleText1.setTextColor(getResources().getColor(R.color.white));
                if (saleOrderMyFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    saleOrderMyFragment = new SaleOrderMyFragment();

                    transaction.add(R.id.content, saleOrderMyFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(saleOrderMyFragment);
                }
                break;
            case 1:
                mTitleView2.setVisibility(View.VISIBLE);
                mTitleText2.setTextColor(getResources().getColor(R.color.white));
                if (saleOrderExamineFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    saleOrderExamineFragment = new SaleOrderExamineFragment();
                    transaction.add(R.id.content, saleOrderExamineFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(saleOrderExamineFragment);
                }
                break;
            case 2:
                mTitleView3.setVisibility(View.VISIBLE);
                mTitleText3.setTextColor(getResources().getColor(R.color.white));
                if (saleOrderHistoryExamineFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    saleOrderHistoryExamineFragment = new SaleOrderHistoryExamineFragment();
                    transaction.add(R.id.content, saleOrderHistoryExamineFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(saleOrderHistoryExamineFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void initBack() {
        mTitleView1.setVisibility(View.GONE);
        mTitleText1.setTextColor(getResources().getColor(R.color.color28));
        mTitleView2.setVisibility(View.GONE);
        mTitleText2.setTextColor(getResources().getColor(R.color.color28));
        mTitleView3.setVisibility(View.GONE);
        mTitleText3.setTextColor(getResources().getColor(R.color.color28));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (saleOrderMyFragment != null) {
            transaction.hide(saleOrderMyFragment);
        }
        if (saleOrderExamineFragment != null) {
            transaction.hide(saleOrderExamineFragment);
        }
        if (saleOrderHistoryExamineFragment != null) {
            transaction.hide(saleOrderHistoryExamineFragment);
        }
    }

    @Override
    public void showLoading() {
        loadingDialog.builder().show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onSuccess(String type, Object o) {
    }

    @Override
    public void onFail(String str) {
        mAlertDialog.builder().setTitle("提示")
                .setMsg(str)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
    private OrderStatusInterface orderStatusInterface = new OrderStatusInterface() {
        @Override
        public void getStatus(String status,int position) {
            mOrderStatus = status;
            mPosition = position;
            if(mFlag.equals("0")){
                saleOrderMyFragment.flushList(status);
//                Intent intent = new Intent();
//                intent.setAction("flushOrderList");
//                intent.putExtra("status",status);
//                sendBroadcast(intent);
            }else if(mFlag.equals("1")){
                saleOrderExamineFragment.flushList(status);
            }else if(mFlag.equals("2")){
                saleOrderHistoryExamineFragment.flushList(status);
            }
        }
    };
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String position = null;
            if(bundle!=null){
                position = bundle.getString("position");
            }
            if(position!=null){
                if(position.equals("0")){
                    saleOrderMyFragment.flushList("");
                }else if(position.equals("1")){
                    saleOrderExamineFragment.flushList("");
                }if(position.equals("2")){
                    saleOrderHistoryExamineFragment.flushList("");
                }
            }
            logcat("广播接受的值"+position);
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
