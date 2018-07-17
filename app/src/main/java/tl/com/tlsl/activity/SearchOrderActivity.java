package tl.com.tlsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tl.com.tlsl.R;
import tl.com.tlsl.fragment.SaleOrderMyFragment;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.utils.RecyclerUtil;
import tl.com.tlsl.utils.ScreenUtils;
import tl.com.tlsl.view.IMvpView;
import tl.com.tlsl.weiget.SlidingButtonView;
import tl.com.tlsl.weiget.SwipeListLayout;

/**
 * Created by admin on 2018/5/31.
 */

public class SearchOrderActivity extends BaseActivity implements IMvpView{
    private SaleOrderPresenter saleOrderPresenter;
    private int page = 1, x = 1;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private RecyAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private EditText mSearchEdit;
    private TextView mSearchText;
    private ImageView mXImage;
    private TextView mNoText;
    private LinearLayout mOrderLayout,mTerminalLayout;
    private View mOrderView,mTerminalView;
    private String orderOrTerminal = "order",mFlag = "0";
    private Bundle mBundle;

    @Override
    protected int getLayoutId() {
        return R.layout.search_order_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saleOrderPresenter = new SaleOrderPresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mFlag = mBundle.getString("flag");
        }
        RecyclerUtil.init(mRecyclerView, this);
        if(mRecyclerView!=null){
            mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            page = 1;
                            x = 1;
                            flush();
                            mRecyclerView.refreshComplete();
                            mRecyclerView.setLoadingMoreEnabled(true);
                        }
                    }, 1000);
                }
                @Override
                public void onLoadMore() {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            page++;
                            x = 2;
                            flush();
                        }
                    }, 1000);
                }
            });
        }
    }
    private void flush(){
        if(mFlag.equals("0")){
            if(orderOrTerminal.equals("order")){
                saleOrderPresenter.getOrderList(mSearchEdit.getText().toString(),"","",page);
            }else if(orderOrTerminal.equals("terminal")){
                saleOrderPresenter.getOrderList("","",mSearchEdit.getText().toString(),page);
            }
        }else if(mFlag.equals("1")){
            if(orderOrTerminal.equals("order")){
                saleOrderPresenter.getMyList(mSearchEdit.getText().toString(),"","",page);
            }else if(orderOrTerminal.equals("terminal")){
                saleOrderPresenter.getMyList("","",mSearchEdit.getText().toString(),page);
            }
        }else if(mFlag.equals("2")){
            if(orderOrTerminal.equals("order")){
                saleOrderPresenter.getHistoryList(mSearchEdit.getText().toString(),"","",page);
            }else if(orderOrTerminal.equals("terminal")){
                saleOrderPresenter.getHistoryList("","",mSearchEdit.getText().toString(),page);
            }
        }

    }
    @Override
    public void initViews() {
        super.initViews();
        mOrderView = findViewById(R.id.order_view);
        mOrderLayout = findViewById(R.id.order_layout);
        mOrderLayout.setOnClickListener(this);
        mTerminalView = findViewById(R.id.terminal_view);
        mTerminalLayout = findViewById(R.id.terminal_layout);
        mTerminalLayout.setOnClickListener(this);
        mNoText = findViewById(R.id.no_data_text);
        mRecyclerView = findViewById(R.id.search_order_recycler_view);
        mSearchEdit = findViewById(R.id.search_edit);
        mSearchText = findViewById(R.id.tv_right);
        mSearchText.setOnClickListener(this);
        mXImage = findViewById(R.id.x_image);
        mXImage.setOnClickListener(this);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.order_layout:
                orderOrTerminal = "order";
                mOrderView.setVisibility(View.VISIBLE);
                mTerminalView.setVisibility(View.GONE);
                break;
            case R.id.terminal_layout:
                orderOrTerminal = "terminal";
                mOrderView.setVisibility(View.GONE);
                mTerminalView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_right:
                flush();
                break;
            case R.id.x_image:
                mSearchEdit.setText("");
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
        switch (type){
            case "success":
//                if(orderOrTerminal.equals("order")){
                    if (x == 1) {
                        if(mList==null){
                            mList = (ArrayList) object;
                            if(mList!=null && mList.size()>0){
                                mAdapter = new RecyAdapter();
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }else{
                            mList = (ArrayList) object;
                            mAdapter.notifiList(mList);
                            mRecyclerView.loadMoreComplete();
                        }
                    } else {
                        mAddList = (ArrayList) object;
                        mAdapter.notifiList(mAddList);
                        mRecyclerView.loadMoreComplete();
                        if (mAddList.size() < 10) {
                            mRecyclerView.setLoadingMoreEnabled(false);
                        }else{
                            mRecyclerView.setLoadingMoreEnabled(true);
                        }
                    }
                break;
            case "revokeSuccess":
                page = 1;
                x = 1;
                flush();
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
                break;
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
                    SearchOrderActivity.this).inflate(R.layout.sale_order_list_item2, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.mItemLayout.getLayoutParams().width = ScreenUtils.getScreenWidth(SearchOrderActivity.this);

            if (mList.get(position).get("saleOrderStatus") != null) {
                switch (mList.get(position).get("saleOrderStatus").toString()) {
                    case "10":
                        holder.mRevokeImage.setVisibility(View.VISIBLE);
                        holder.mRevokeLayout.setVisibility(View.VISIBLE);

                        holder.mStatus.setText("待审核");
                        holder.mStatus.setTextColor(SearchOrderActivity.this.getResources().getColor(R.color.color35));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_yellow);
                        break;
                    case "20":
                        holder.mRevokeImage.setVisibility(View.VISIBLE);
                        holder.mRevokeLayout.setVisibility(View.VISIBLE);
                        holder.mStatus.setText("审核中");
                        holder.mStatus.setTextColor(getResources().getColor(R.color.color25));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_blue);
                        break;
                    case "30":
                        holder.mRevokeImage.setVisibility(View.GONE);
                        holder.mRevokeLayout.setVisibility(View.GONE);
                        holder.mStatus.setText("驳回");
                        holder.mStatus.setTextColor(getResources().getColor(R.color.color34));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_red);
                        break;
                    case "40":
                        holder.mRevokeImage.setVisibility(View.GONE);
                        holder.mRevokeLayout.setVisibility(View.GONE);
                        holder.mStatus.setText("通过");
                        holder.mStatus.setTextColor(getResources().getColor(R.color.color30));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14);
                        break;
                    case "50":
                        holder.mRevokeImage.setVisibility(View.GONE);
                        holder.mRevokeLayout.setVisibility(View.GONE);
                        holder.mStatus.setText("撤销");
                        holder.mStatus.setTextColor(getResources().getColor(R.color.color36));
                        holder.mStatus.setBackgroundResource(R.drawable.bg_my_style14_bohui);
                        break;
                }
            }
            ViewGroup.LayoutParams params = holder.mContentLayout.getLayoutParams();
            params.height = DensityUtils.dp2px(SearchOrderActivity.this, 250);
            if (position > 0) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, DensityUtils.dp2px(SearchOrderActivity.this, -20), 0, 0);
            }
            if (mList.get(position).get("saleOrderCode") != null) {
                holder.mOrderText.setText("订单号:" + mList.get(position).get("saleOrderCode").toString());
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
            holder.mContentLayout.setLayoutParams(params);
            holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tl.com.tlsl>>","===点击点击====");
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId",mList.get(position).get("saleOrderId").toString());
                    bundle.putString("type","myf");
                    Intent intent = new Intent(SearchOrderActivity.this, SaleOrderDetailActivity.class);
                    if (null != bundle)
                        intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                    finish();
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
                    Intent intent = new Intent(SearchOrderActivity.this, SaleOrderApplyActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,0);
                    finish();
                }
            });
            holder.mRevokeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String saleOrderId = mList.get(position).get("saleOrderId")+"";
                    String createUserId = mList.get(position).get("createUserId")+"";
                    String saleOrderStatus = mList.get(position).get("saleOrderStatus")+"";
                    saleOrderPresenter.revoke(saleOrderId,createUserId,saleOrderStatus);
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
                mCopyLayout = view.findViewById(R.id.copy_layout2);
                mRevokeLayout = view.findViewById(R.id.revoke_layout2);
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
