package tl.com.tlsl.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import tl.com.tlsl.R;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.model.impl.OrderStatusImpl;
import tl.com.tlsl.utils.RecyclerUtil;

/**
 * Created by admin on 2018/5/17.
 */

public class SaleOrderPopupwindow extends PopupWindow{
    private View mView;
    private Context mContext;
    private TextView mConfirmText,mResetText;
    private OrderStatusImpl mOrderStatusImpl;
    private String mOrderStatus = "",mFlag = "";
    private XRecyclerView xRecyclerView;
    private ArrayList<String> mList;
    private RecyAdapter mAdapter;
    private int mPosition = 0;
    public SaleOrderPopupwindow(Context context,OrderStatusImpl orderStatusImpl,String status,String flag,int position){
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.sale_order_pupwindow, null);
        xRecyclerView = mView.findViewById(R.id.sale_popup_view);
        xRecyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        xRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        xRecyclerView.setLoadingMoreEnabled(false);
        mList = new ArrayList<>();
        mPosition = position;
        mFlag = flag;
        mOrderStatusImpl = orderStatusImpl;
//        mAll = mView.findViewById(R.id.all_status);
//        mTongguo = mView.findViewById(R.id.tongguo_status);
//        mReject = mView.findViewById(R.id.reject_status);
//        mToAudited = mView.findViewById(R.id.to_be_audited_status);
        if(mFlag.equals("2")){
            mList.add("全部");
            mList.add("通过");
            mList.add("驳回");
            mList.add("审核中");
            mList.add("撤销");
        }else{
            mList.add("全部");
            mList.add("通过");
            mList.add("驳回");
            mList.add("待审核");
            mList.add("审核中");
            mList.add("撤销");
        }
        mAdapter = new RecyAdapter();
        xRecyclerView.setAdapter(mAdapter);
//        mAuditingstatus = mView.findViewById(R.id.audit_ing_status);
//        mRevokestatus = mView.findViewById(R.id.revoke_status);
        mConfirmText = mView.findViewById(R.id.confirm_text);
        mResetText = mView.findViewById(R.id.reset_text);
        mConfirmText.setOnClickListener(onClickListener);
        mResetText.setOnClickListener(onClickListener);
//
//        mAll.setOnClickListener(onClickListener);
//        mTongguo.setOnClickListener(onClickListener);
//        mReject.setOnClickListener(onClickListener);
//        mToAudited.setOnClickListener(onClickListener);
//        mAuditingstatus.setOnClickListener(onClickListener);
//        mRevokestatus.setOnClickListener(onClickListener);
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
//        setStatus(status);
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
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //重置
                case R.id.reset_text:
                    mPosition = 0;
                    mAdapter.notifyDataSetChanged();
                    mOrderStatus = "";
                    mOrderStatusImpl.getStatus(mOrderStatus,mPosition);
                    break;
                //完成
                case R.id.confirm_text:
                    mOrderStatusImpl.getStatus(mOrderStatus,mPosition);
                    dismiss();
                    break;
            }
        }
    };

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        private ArrayMap<String,Object> mDataMap;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.sale_order_pupwindow_item, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.nameText.setText(mList.get(position));
            holder.nameText.setBackgroundResource(R.drawable.bg_my_style18);
            if(mPosition == position){
                holder.nameText.setBackgroundResource(R.drawable.bg_my_style15);
            }else{
                holder.nameText.setBackgroundResource(R.drawable.bg_my_style18);
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    notifyDataSetChanged();
                    switch (mList.get(position)){
                        case "全部":
                            mOrderStatus = "";
                            break;
                        case "待审核":
                            mOrderStatus = "10";
                            break;
                        case "审核中":
                            mOrderStatus = "20";
                            break;
                        case "驳回":
                            mOrderStatus = "30";
                            break;
                        case "通过":
                            mOrderStatus = "40";
                            break;
                        case "撤单":
                            mOrderStatus = "50";
                            break;
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            LinearLayout mContentLayout;
            public MyViewHolder(View view) {
                super(view);
                nameText = view.findViewById(R.id.name);
                mContentLayout = view.findViewById(R.id.content_id);
            }
        }
    }
}
