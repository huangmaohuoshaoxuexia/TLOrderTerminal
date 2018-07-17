package tl.com.tlsl.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import tl.com.tlsl.R;
import tl.com.tlsl.wheel.adapter.ArrayWheelAdapter;
import tl.com.tlsl.wheel.view.OnWheelChangedListener;
import tl.com.tlsl.wheel.view.WheelView;

/**
 * Created by admin on 2018/5/17.
 */

public class WheelPopupwindow extends PopupWindow implements  View.OnClickListener, OnWheelChangedListener {
    private View mView;
    private Context mContext;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mConfirmText,mCancelText;
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    public WheelPopupwindow(Context context){
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.wheel_item, null);
        mViewProvince = mView.findViewById(R.id.id_province);
        mViewCity = mView.findViewById(R.id.id_city);
        mViewDistrict = mView.findViewById(R.id.id_district);
        mConfirmText = mView.findViewById(R.id.confirm_text);
        mCancelText = mView.findViewById(R.id.cancel_text);

        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw2 = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw2);

        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.content_id).getTop();
                int bottom = mView.findViewById(R.id.content_id).getBottom();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height || y>bottom){

                        dismiss();
                    }
                }
                return true;
            }
        });
    }
//    private void setUpData() {
//        initProvinceDatas();
//        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(MainActivity.this, mProvinceDatas));
//        // 设置可见条目数量
//        mViewProvince.setVisibleItems(7);
//        mViewCity.setVisibleItems(7);
//        mViewDistrict.setVisibleItems(7);
//        updateCities();
//        updateAreas();
//    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
//    private void updateAreas() {
//        int pCurrent = mViewCity.getCurrentItem();
//        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
//        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//
//        if (areas == null) {
//            areas = new String[] { "" };
//        }
//        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//        mViewDistrict.setCurrentItem(0);
//    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
//    private void updateCities() {
//        int pCurrent = mViewProvince.getCurrentItem();
//        mCurrentProviceName = mProvinceDatas[pCurrent];
//        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//        if (cities == null) {
//            cities = new String[] { "" };
//        }
//        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
//        mViewCity.setCurrentItem(0);
//        updateAreas();
//    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//        if (wheel == mViewProvince) {
//            updateCities();
//        } else if (wheel == mViewCity) {
//            updateAreas();
//        } else if (wheel == mViewDistrict) {
//            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
//        }
    }
}
