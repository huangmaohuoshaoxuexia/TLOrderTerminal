package tl.com.tlsl.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import tl.com.tlsl.R;
import tl.com.tlsl.utils.ScreenUtils;


public class LogPopupwindow extends PopupWindow {
    private View mView;
    private Context mContext;
    private RelativeLayout mContentLayout;

    public LogPopupwindow(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.log_load_layout, null);
        mContentLayout = (RelativeLayout) mView.findViewById(R.id.log_load_content_layout);
        LayoutParams params = mContentLayout.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(mContext)/3;
        params.width = ScreenUtils.getScreenWidth(mContext)/3;
        mContentLayout.setLayoutParams(params);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw2 = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw2);
		ColorDrawable dw = new ColorDrawable(0x40000000);
//		//设置SelectPicPopupWindow弹出窗体的背景
//        mContentLayout.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = mView.findViewById(R.id.mall_type_chose_popup_id).getTop();
//                int bottom = mView.findViewById(R.id.mall_type_chose_popup_id).getBottom();
//                int y=(int) event.getY();
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    if(y<height || y>bottom){
//						dismiss();
//                    }
//                }
//                return true;
//            }
//        });

    }
}
