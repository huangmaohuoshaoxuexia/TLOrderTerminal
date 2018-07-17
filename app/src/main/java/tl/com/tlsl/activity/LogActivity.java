package tl.com.tlsl.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import tl.com.tlsl.R;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.presenter.UserPresenter;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.utils.ScreenUtils;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/15.
 */

public class LogActivity extends BaseActivity implements IMvpView{

    private ImageView mBackImage;
    private LinearLayout mInputLayout;
    private TextView mForgetText;
    private UserPresenter userPresenter;
    private Button mLogingBtn;
    private EditText mUserEdit,mPwdEdit;
    private Dialog mDialog;
    private RelativeLayout mContentLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.login_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
        userPresenter = new UserPresenter(this,this);
        getUser();
        View view = LayoutInflater.from(this).inflate(
                R.layout.log_load_layout, null);
        mContentLayout = view.findViewById(R.id.log_load_content_layout);
        ViewGroup.LayoutParams params = mContentLayout.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(this)/3;
        params.width = ScreenUtils.getScreenWidth(this)/3;
        mContentLayout.setLayoutParams(params);
        mDialog = new Dialog(this, R.style.AlertDialogStyle);
        mDialog.setContentView(view);
    }

    @Override
    public void initViews() {
        super.initViews();
        mUserEdit = findViewById(R.id.username);
        mPwdEdit = findViewById(R.id.pasword);
        mLogingBtn = findViewById(R.id.login_btn);
        mLogingBtn.setOnClickListener(this);
        mForgetText = findViewById(R.id.forget_pwd_text);
        mForgetText.setOnClickListener(this);
        mBackImage = findViewById(R.id.login_back_image);
        mInputLayout = findViewById(R.id.log_input_layout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mInputLayout.getLayoutParams();
        params.setMargins(DensityUtils.dp2px(this,10),DensityUtils.dp2px(this,200),DensityUtils.dp2px(this,10),DensityUtils.dp2px(this,10));
        mInputLayout.setLayoutParams(params);
//        params.height = ScreenUtils.getScreenHeight(this)/2;
//        mBackImage.setLayoutParams(params);
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
            case R.id.forget_pwd_text:
                Bundle bundle = new Bundle();
                bundle.putString("type","modify");
                openActivityForResult(ForgetPasswordActivity.class,0,bundle);
                break;
            case R.id.login_btn:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPwdEdit.getWindowToken(), 0);
                if(!mUserEdit.getText().toString().equals("") && !mPwdEdit.getText().toString().equals("")){
                    showLoading();
                    handler.sendEmptyMessageDelayed(0,500);
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("请输入帐号或密码！")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    userPresenter.login(mUserEdit.getText().toString(), Constants.md5Up(mPwdEdit.getText().toString()));
                    break;
            }
        }
    };

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        super.startActivity(openClass, bundle);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        super.openActivityForResult(openClass, requestCode, bundle);
    }
    @Override
    public void showLoading() {
        try {
            mDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        if(mDialog!=null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
    @Override
    public void onSuccess(String type, Object object) {
        ArrayMap<String,Object> mDataMap = (ArrayMap<String, Object>) object;
        rememberUser(mUserEdit.getText().toString(),mPwdEdit.getText().toString(),mDataMap.get("value").toString());
        startActivity(MainActivity.class,null);
        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        logcat("是否回去刷新状态==="+requestCode+"==="+ data.getExtras().getString("pwd"));
        switch (requestCode){
            case 0:
                try{
                    if(data!=null){
                        mPwdEdit.setText(data.getExtras().getString("pwd"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
    //记住密码
    private void rememberUser(String username,String pwd,String token) {
        SharedPreferences sp = getSharedPreferences("whmasterUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", username);
        editor.putString("pwd", pwd);
        editor.putString("token", token);
        editor.commit();
    }
    //获取用户
    private void getUser() {
        SharedPreferences sp = getSharedPreferences("whmasterUser", MODE_PRIVATE);
        if (sp != null) {
            mUserEdit.setText(sp.getString("name", ""));
            mPwdEdit.setText(sp.getString("pwd", ""));
        }
        Log.i("获取缓存用户",sp.getString("userName", "")+"==================");
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
