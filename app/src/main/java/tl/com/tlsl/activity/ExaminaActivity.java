package tl.com.tlsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import tl.com.tlsl.R;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/31.
 */

public class ExaminaActivity extends BaseActivity implements IMvpView {

    private TextView mTitleText;
    private EditText mContentEdit;
    private Button mSubBtn;
    private Bundle mBundle;
    private String mType, mOrderId;
    private SaleOrderPresenter saleOrderPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.examina_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addOrderActivity(this);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mType = mBundle.getString("type");
            mOrderId = mBundle.getString("id");
        }
        if (mType.equals("reject")) {
            mTitleText.setText("驳回审核");
            mContentEdit.setText("驳回");
        } else if (mType.equals("approve")) {
            mTitleText.setText("通过审核");
            mContentEdit.setText("通过");
        }
        saleOrderPresenter = new SaleOrderPresenter(this, this);
    }

    @Override
    public void initViews() {
        super.initViews();
        mTitleText = findViewById(R.id.tv_title);
        mContentEdit = findViewById(R.id.examina_edit);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentEdit.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 200);
        mContentEdit.setLayoutParams(params);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
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
    public void logcat(String msg) {
        super.logcat(msg);
    }

    @Override
    public void setHeader() {
        super.setHeader();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sub_btn:
                if (!mContentEdit.getText().toString().equals("")) {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("确认提交")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mType.equals("reject")) {
                                        saleOrderPresenter.reject(mOrderId, mContentEdit.getText().toString());
                                    } else if (mType.equals("approve")) {
                                        saleOrderPresenter.approve(mOrderId, mContentEdit.getText().toString());
                                    }
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAlertDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(this, "请输入审核内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                finish();
                break;
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
    public void onSuccess(String type, Object object) {
        Intent intent = new Intent();
        intent.setAction("flushList");
        intent.putExtra("position", "1");
        sendBroadcast(intent);
        AtyContainerUtils.getInstance().finishDetailActivity();
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
