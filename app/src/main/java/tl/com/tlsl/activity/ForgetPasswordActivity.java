package tl.com.tlsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import tl.com.tlsl.R;
import tl.com.tlsl.presenter.UserPresenter;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.ScreenUtils;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2017/10/18.
 * 忘记密码
 */

public class ForgetPasswordActivity extends BaseActivity implements IMvpView {

    private ImageView mBackImage,mCancelImage;
    private Button mNextBtn;
    private LinearLayout mVerifiPhoneLayout, mVerfiPwdLayout;
    private EditText mPhoneEdit, mCodeEdit,mInputPwd,mInputPwd2;
    private TextView mGetCodeText,mTimeCodeText;
    private int time = 30;
    private Timer mTimer;
    private boolean isSend = true;
    private UserPresenter userPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.forget_pwd_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPresenter = new UserPresenter(this,this);
        AtyContainerUtils.getInstance().addActivity(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        mCancelImage = findViewById(R.id.back_cancel);
        mCancelImage.setOnClickListener(this);
        mInputPwd = findViewById(R.id.pwd);
        mInputPwd2 = findViewById(R.id.pwd_verifi);
        mTimeCodeText = findViewById(R.id.get_code_time);
        mPhoneEdit = findViewById(R.id.phone_edit);
        mCodeEdit = findViewById(R.id.code_edit);
        mGetCodeText = findViewById(R.id.get_code_text);
        mGetCodeText.setOnClickListener(this);
        mBackImage = findViewById(R.id.back_image);
        ViewGroup.LayoutParams params = mBackImage.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenHeight(this) * 0.3);
        mBackImage.setLayoutParams(params);

        mNextBtn = findViewById(R.id.sub_btn);
        mNextBtn.setOnClickListener(this);
        mVerifiPhoneLayout = findViewById(R.id.input_verifi_code_layout);
        mVerfiPwdLayout = findViewById(R.id.input_pwd_layout);
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
        switch (v.getId()) {
            case R.id.back_cancel:
                finish();
                break;
            case R.id.get_code_text:
                if (!mPhoneEdit.getText().toString().equals("")) {
                    if (isSend) {
                        showLoading();
                        userPresenter.getCode(mPhoneEdit.getText().toString());
                    }
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("请输入手机号码")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.sub_btn:
//                userPresenter.setPwd(mPhoneEdit.getText().toString(),mGetCodeText.getText().toString());
                if (mNextBtn.getText().toString().equals("下一步")) {
                    if(!mPhoneEdit.getText().toString().equals("") && !mGetCodeText.getText().toString().equals("")){
                        userPresenter.verificationCode(mPhoneEdit.getText().toString(),mCodeEdit.getText().toString());
                    }else{
                        mAlertDialog.builder().setTitle("提示")
                                .setMsg("请输入手机号码和验证码")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                } else if (mNextBtn.getText().toString().equals("确定")) {
                    if(!mInputPwd.getText().toString().equals("") && !mInputPwd2.getText().toString().equals("") && mInputPwd.getText().toString().equals(mInputPwd2.getText().toString())){
                            userPresenter.setPwd(mPhoneEdit.getText().toString(),mInputPwd.getText().toString());
                    }else{
                        mAlertDialog.builder().setTitle("提示")
                                .setMsg("两次密码输入不一致！")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    isSend = true;
                    mGetCodeText.setVisibility(View.VISIBLE);
                    mTimeCodeText.setVisibility(View.GONE);
                    time = 30;
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                        mTimer = null;
                    }
                    break;
                case 2:
                    mTimeCodeText.setText("("+ time +")");
                    break;
            }
        }
    };


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
        switch (type){
            case "code":

                isSend = false;
                mGetCodeText.setVisibility(View.GONE);
                mTimeCodeText.setVisibility(View.VISIBLE);
                mTimer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        time--;
                        if (time < 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        } else {
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                };
                mTimer.schedule(task, 30, 1000);
                break;
            case "next":
                mVerfiPwdLayout.setVisibility(View.VISIBLE);
                mVerifiPhoneLayout.setVisibility(View.GONE);
                mNextBtn.setText("确定");
                break;
            case "modify":
                Bundle bundle = new Bundle();
                bundle.putString("pwd", mInputPwd.getText().toString());
                setResultOk(bundle);
                break;
        }
    }

    @Override
    public void onFail(String errorMsg) {
        mAlertDialog.builder().setTitle("提示")
                .setMsg(errorMsg)
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
