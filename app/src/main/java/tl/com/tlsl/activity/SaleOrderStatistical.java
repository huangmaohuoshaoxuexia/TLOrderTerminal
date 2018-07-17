package tl.com.tlsl.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import tl.com.tlsl.R;
import tl.com.tlsl.view.CircleProgressView;
import tl.com.tlsl.wheel.adapter.NumericWheelAdapter;

/**
 * Created by admin on 2017/10/16.
 * 统计分析
 */

public class SaleOrderStatistical extends BaseActivity{

    private CircleProgressView mCircleProgressView;
    private TextView tvTime,mStartTimeText,mEndTimeText;
    private TimePickerView mTimePickerView;
    private OptionsPickerView optionsPickerView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        super.initViews();
        mCircleProgressView = findViewById(R.id.circle_view);
        mCircleProgressView.setProgress(60);
        mStartTimeText = findViewById(R.id.start_date);
        mEndTimeText = findViewById(R.id.end_date);
        mStartTimeText.setOnClickListener(this);
        mEndTimeText.setOnClickListener(this);
    }

    @Override
    public void initListeners() {
        super.initListeners();
    }

    @Override
    public void initData() {
        super.initData();
        NumericWheelAdapter numericAdapter1 = new NumericWheelAdapter(this, 1997, 2020,"%02d");
        numericAdapter1.setLabel("  :");
        numericAdapter1.setTextSize(20);
    }

    @Override
    public void setHeader() {
        super.setHeader();
        mTitle.setText("统计分析");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.start_date:
                optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mStartTimeText.setText(options1+options2+options3+"");
                    }
                })
//                        .setSubmitText("确定")//确定按钮文字
//                        .setCancelText("取消")//取消按钮文字
//                        .setTitleText("城市选择")//标题
                        .build();
//                optionsPickerView.setPicker(options1Items, options2Items, options3Items);//添加数据源
                //时间选择器
                mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        mStartTimeText.setText(simpleDateFormat.format(date)+"");
                    }
                }).setCancelText("取消")//取消按钮文字
                        .setSubmitText("确认")//确认按钮文字
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                        .build();
                mTimePickerView.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                mTimePickerView.show();
                break;
            case R.id.end_date:
                //时间选择器
                mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        mEndTimeText.setText(simpleDateFormat.format(date)+"");
                    }
                }).setCancelText("取消")//取消按钮文字
                        .setSubmitText("确认")//确认按钮文字
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                        .build();
                mTimePickerView.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                mTimePickerView.show();
                break;
        }
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        super.startActivity(openClass, bundle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sale_order_statistical;
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
