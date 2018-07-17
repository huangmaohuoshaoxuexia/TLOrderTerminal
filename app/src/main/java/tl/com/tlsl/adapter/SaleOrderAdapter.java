package tl.com.tlsl.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tl.com.tlsl.R;
import tl.com.tlsl.weiget.SwipeListLayout;
import tl.com.tlsl.utils.DensityUtils;

/**
 * Created by admin on 2018/5/16.
 */

public class SaleOrderAdapter extends RecyclerView.Adapter<SaleOrderAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ArrayMap<String, Object>> mList;
    public SaleOrderAdapter(Context context,ArrayList<ArrayMap<String, Object>> list){
        this.mContext = context;
        this.mList = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.sale_order_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.mContentLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(mContext,220);
        if(position>0){
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
            marginParams.setMargins(0,DensityUtils.dp2px(mContext,-20),0,0);
        }
        if(mList.get(position).get("saleOrderId")!=null){
            holder.mOrderText.setText("订单号："+mList.get(position).get("saleOrderId").toString());
        }
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(mList.get(position).get("updateTime").toString());
        Date date = new Date(lt);
        if(mList.get(position).get("updateTime")!=null){
            holder.mDate.setText(time.format(date));
        }
        if(mList.get(position).get("companyName")!=null){
            holder.mTerminal.setText(mList.get(position).get("companyName").toString());
        }
        if(mList.get(position).get("totalCount")!=null){
            holder.mProductNumber.setText(mList.get(position).get("totalCount").toString());
        }
        if(mList.get(position).get("memo")!=null){
            holder.mMemo.setText(mList.get(position).get("memo").toString());
        }
        if(mList.get(position).get("saleOrderStatus")!=null){
            switch (mList.get(position).get("saleOrderStatus").toString()){
                case "10":
                    holder.mStatus.setText("待审核");
                    holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.color35));
                    holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_yellow);
                    break;
                case "20":
                    holder.mStatus.setText("审核中");
                    holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.color25));
                    holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_blue);
                    break;
                case "30":
                    holder.mStatus.setText("驳回");
                    holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.color34));
                    holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_red);
                    break;
                case "40":
                    holder.mStatus.setText("通过");
                    holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.color30));
                    holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14);
                    break;
                case "50":
                    holder.mStatus.setText("撤回");
                    holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.color36));
                    holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_bohui);
                    break;
            }
        }
        holder.mContentLayout.setLayoutParams(params);

        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v,position);
            }
        });
    }
    public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       public SwipeListLayout mContentLayout;
        TextView mOrderText,mTerminal,mProductNumber,mDate,mStatus,mMemo;
        LinearLayout mItemLayout;
        public MyViewHolder(View view) {
            super(view);
            mItemLayout = view.findViewById(R.id.content_layout);
            mContentLayout = view.findViewById(R.id.sale_order_item_layout);
            mOrderText = view.findViewById(R.id.order_name);
            mStatus = view.findViewById(R.id.order_status);
            mTerminal = view.findViewById(R.id.terminal_name);
            mProductNumber = view.findViewById(R.id.product_name);
            mDate = view.findViewById(R.id.date);
            mMemo = view.findViewById(R.id.memo);
        }
    }
    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;

    }
    public interface OnMyItemClickListener{
        void onClick(View v,int pos);
        void mLongClick(View v,int pos);
    }

}
