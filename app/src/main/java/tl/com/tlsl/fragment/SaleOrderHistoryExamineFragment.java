package tl.com.tlsl.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tl.com.tlsl.R;
import tl.com.tlsl.activity.SaleOrderApplyActivity;
import tl.com.tlsl.activity.SaleOrderDetailActivity;
import tl.com.tlsl.adapter.SaleOrderAdapter;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.utils.RecyclerUtil;
import tl.com.tlsl.utils.ScreenUtils;
import tl.com.tlsl.view.IMvpView;
import tl.com.tlsl.weiget.AlertDialog;
import tl.com.tlsl.weiget.LoadingDialog;
import tl.com.tlsl.weiget.SlidingButtonView;
import tl.com.tlsl.weiget.SwipeListLayout;

/**
 * Created by admin on 2018/5/16.
 * 历史审核
 */

public class SaleOrderHistoryExamineFragment extends Fragment implements IMvpView{
    private View v;
    private XRecyclerView xRecyclerView;
//    private SaleOrderAdapter mAdapter;
    private RecyAdapter mAdapter;
    private SaleOrderPresenter saleOrderPresenter;
    private int page = 1,x = 1;
    private ArrayList<ArrayMap<String, Object>> mList,mAddList;
    protected LoadingDialog loadingDialog;
    protected AlertDialog mAlertDialog;
    private String mOrderStatus = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void flushList(String status){
        page = 1;
        x = 1;
        mOrderStatus = status;
        saleOrderPresenter.getHistoryList("",status,"",page);
        xRecyclerView.refreshComplete();
        xRecyclerView.setLoadingMoreEnabled(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sale_order_fragment, null);
        init();
        loadingDialog = new LoadingDialog(getActivity());
        mAlertDialog = new AlertDialog(getActivity());
        RecyclerUtil.init(xRecyclerView,getActivity());

        saleOrderPresenter = new SaleOrderPresenter(this,getActivity());
        saleOrderPresenter.getHistoryList("",mOrderStatus,"",page);
        if(xRecyclerView!=null){
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        page = 1;
                        x = 1;
                        saleOrderPresenter.getHistoryList("",mOrderStatus,"",page);
                        xRecyclerView.refreshComplete();
                        xRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        page++;
                        x = 2;
                        saleOrderPresenter.getHistoryList("",mOrderStatus,"",page);
                    }
                }, 1000);
            }
        });
        }
        return v;
    }
    private void init(){
        xRecyclerView = v.findViewById(R.id.sale_order_xrecycler);
    }

    @Override
    public void showLoading() {
        if(loadingDialog!=null){
            loadingDialog.builder().show();
        }
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onSuccess(String type, Object object) {
        if (x == 1) {
            if(mList==null){
                mList = (ArrayList) object;
                if(mList!=null && mList.size()>0){
                    mAdapter = new RecyAdapter();
                    xRecyclerView.setAdapter(mAdapter);
                }
            }else{
                mList = (ArrayList) object;
                mAdapter.notifyDataSetChanged();
                xRecyclerView.loadMoreComplete();
            }
            if (mList.size() < 10) {
                xRecyclerView.setLoadingMoreEnabled(false);
            }else{
                xRecyclerView.setLoadingMoreEnabled(true);
            }
        } else {
            mAddList = (ArrayList) object;
            mAdapter.notifiList(mAddList);
            xRecyclerView.loadMoreComplete();
            if (mAddList.size() < 10) {
                xRecyclerView.setLoadingMoreEnabled(false);
            }else{
                xRecyclerView.setLoadingMoreEnabled(true);
            }
        }
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

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
        private SlidingButtonView mMenu = null;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.sale_order_list_item2, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            ViewGroup.LayoutParams params = holder.mContentLayout.getLayoutParams();
            params.height = DensityUtils.dp2px(getActivity(), 250);
            holder.mItemLayout.getLayoutParams().width = ScreenUtils.getScreenWidth(getActivity());

            if (position > 0) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, DensityUtils.dp2px(getActivity(), -20), 0, 0);
            }
            holder.mRevokeLayout.setVisibility(View.GONE);
            if (mList.get(position).get("saleOrderCode") != null) {
                holder.mOrderText.setText("订单号：" + mList.get(position).get("saleOrderCode").toString());
            }
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(mList.get(position).get("updateTime").toString());
            Date date = new Date(lt);
            if (mList.get(position).get("updateTime") != null) {
                holder.mDate.setText(time.format(date));
            }
            if (mList.get(position).get("companyName") != null) {
                holder.mTerminal.setText(mList.get(position).get("companyName").toString());
            }
            if (mList.get(position).get("totalCount") != null) {
                holder.mProductNumber.setText(mList.get(position).get("totalCount").toString());
            }
            if (mList.get(position).get("memo") != null && !mList.get(position).get("memo").toString().equals("null")) {
                holder.mMemo.setText(mList.get(position).get("memo").toString());
            }
            if (mList.get(position).get("saleOrderStatus") != null) {
                switch (mList.get(position).get("saleOrderStatus").toString()) {
                    case "10":
                        holder.mStatus.setText("待审核");
                        holder.mStatus.setTextColor(getActivity().getResources().getColor(R.color.color35));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_yellow);
                        break;
                    case "20":
                        holder.mStatus.setText("审核中");
                        holder.mStatus.setTextColor(getActivity().getResources().getColor(R.color.color25));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_blue);
                        break;
                    case "30":
                        holder.mStatus.setText("驳回");
                        holder.mStatus.setTextColor(getActivity().getResources().getColor(R.color.color34));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_red);
                        break;
                    case "40":
                        holder.mStatus.setText("通过");
                        holder.mStatus.setTextColor(getActivity().getResources().getColor(R.color.color30));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14);
                        break;
                    case "50":
                        holder.mStatus.setText("撤回");
                        holder.mStatus.setTextColor(getActivity().getResources().getColor(R.color.color36));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_bohui);
                        break;
                }
            }
            holder.mContentLayout.setLayoutParams(params);
            holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tl.com.tlsl>>","===点击点击====");
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId",mList.get(position).get("saleOrderId").toString());
                    bundle.putString("type","history");
                    Intent intent = new Intent(getActivity(), SaleOrderDetailActivity.class);
                    if (null != bundle)
                        intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.mCopyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("companyId",mList.get(position).get("companyId")+"");
                    bundle.putString("transportType",mList.get(position).get("transportType")+"");
                    bundle.putString("totalPrice",mList.get(position).get("totalPrice")+"");
                    bundle.putString("memo",mList.get(position).get("memo")+"");
                    bundle.putString("saleOrderId",mList.get(position).get("saleOrderId")+"");
                    bundle.putString("payMoney",mList.get(position).get("payMoney")+"");
                    Intent intent = new Intent(getActivity(), SaleOrderApplyActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public void onMenuIsOpen(View view) {
            mMenu = (SlidingButtonView) view;
        }

        @Override
        public void onDownOrMove(SlidingButtonView slidingButtonView) {
            if (menuIsOpen()) {
                if (mMenu != slidingButtonView) {
                    closeMenu();
                }
            }
        }
        //关闭菜单
        public void closeMenu() {
            mMenu.closeMenu();
            mMenu = null;
        }

        //判断是否有菜单打开
        public Boolean menuIsOpen() {
            if (mMenu != null) {
                return true;
            }
            return false;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public SlidingButtonView mContentLayout;
            LinearLayout mItemLayout,mCopyLayout,mRevokeLayout;
            TextView mOrderText, mTerminal, mProductNumber, mDate, mStatus, mMemo;
            ImageView mCopyImage,mRevokeImage;
            public MyViewHolder(View view) {
                super(view);
                mCopyImage = view.findViewById(R.id.copy_image);
                mRevokeImage = view.findViewById(R.id.revoke_image);
                mRevokeImage.setVisibility(View.GONE);
                mCopyLayout = view.findViewById(R.id.copy_layout2);
                mRevokeLayout = view.findViewById(R.id.revoke_layout2);
                mRevokeLayout.setVisibility(View.GONE);
                mItemLayout = view.findViewById(R.id.content_layout);
                mContentLayout = view.findViewById(R.id.sale_order_item_layout);
                mOrderText = view.findViewById(R.id.order_name);
                mStatus = view.findViewById(R.id.order_status);
                mTerminal = view.findViewById(R.id.terminal_name);
                mProductNumber = view.findViewById(R.id.product_name);
                mDate = view.findViewById(R.id.date);
                mMemo = view.findViewById(R.id.memo);
                ((SlidingButtonView) view).setSlidingButtonListener(RecyAdapter.this);
            }
        }
    }
}
