package tl.com.tlsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tl.com.tlsl.R;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/22.
 */

public class SaleOrderDetailActivity extends BaseActivity implements IMvpView {

    private String mOrderId;
    private Bundle mBundle;
    private SaleOrderPresenter saleOrderPresenter;
    private LinearLayout mApplicantLayout, mBascLayout, mDskLayout, mDetailGoodsLayout;
    private ArrayMap<String, Object> mDataMap;
    private ArrayList<ArrayMap<String, Object>> mDetailsList, mApprovalsList, mDeliveryList;
    private TextView mTerminaNameText, mConstactUserText, mPhoneText, mAddressText, mAddressDetailText, mDeliveryText, mSumPriceText, mDskText, mDocumentMemoText, mApplicantuserText, mApplicantPhoneText;
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private TextView mOrderStatus;
    private String mDeliveryId, mType;
    private LinearLayout mExaminaLayout;
    private TextView mRejectText, mApproveText;
    private String mCurrentApprovalId = "";
    @Override
    protected int getLayoutId() {
        return R.layout.sale_order_detail_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addActivity(this);
        AtyContainerUtils.getInstance().addOrderActivity(this);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mOrderId = mBundle.getString("orderId");
            mType = mBundle.getString("type");
        }
        if (mType.equals("examine")) {
            mExaminaLayout.setVisibility(View.VISIBLE);
        } else {
            mExaminaLayout.setVisibility(View.GONE);
        }
        saleOrderPresenter = new SaleOrderPresenter(this, this);
        saleOrderPresenter.getOrderDetail(mOrderId);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRejectText = findViewById(R.id.reject_text);
        mApproveText = findViewById(R.id.agree_text);
        mRejectText.setOnClickListener(this);
        mApproveText.setOnClickListener(this);
        mExaminaLayout = findViewById(R.id.examination_approval_layout);
        mOrderStatus = findViewById(R.id.order_status);
        mDetailGoodsLayout = findViewById(R.id.details_goods_layout);
        mDskLayout = findViewById(R.id.dsk_layout);
        mApplicantPhoneText = findViewById(R.id.constact_phone);
        mApplicantuserText = findViewById(R.id.user_name_edit);
        mDocumentMemoText = findViewById(R.id.docu_memo);
        mDskText = findViewById(R.id.product_numbers);
        mSumPriceText = findViewById(R.id.sale_order_sum_price);
        mDeliveryText = findViewById(R.id.delivery_text);
        mAddressDetailText = findViewById(R.id.address_detail);
        mAddressText = findViewById(R.id.address_text);
        mPhoneText = findViewById(R.id.phone_contact);
        mConstactUserText = findViewById(R.id.contacts_user_text);
        mTerminaNameText = findViewById(R.id.terminal_name);

        mRecyclerView = findViewById(R.id.autior_recycler_view);
        mApplicantLayout = findViewById(R.id.basc_layout_content);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mApplicantLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 145);
        mApplicantLayout.setLayoutParams(params);

        mBascLayout = findViewById(R.id.basc_layout_content);
        params = (LinearLayout.LayoutParams) mBascLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 390);
        mBascLayout.setLayoutParams(params);
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
        switch (v.getId()) {
            //驳回
            case R.id.reject_text:
                Bundle bundle = new Bundle();
                bundle.putString("type", "reject");
                bundle.putString("id", mOrderId);
                startActivity(ExaminaActivity.class,bundle);
//                openActivityForResult(ExaminaActivity.class, 0, bundle);
                break;
            //通过
            case R.id.agree_text:
                bundle = new Bundle();
                bundle.putString("type", "approve");
                bundle.putString("id", mOrderId);
                startActivity(ExaminaActivity.class,bundle);
//                openActivityForResult(ExaminaActivity.class, 0, bundle);
                break;
        }
    }

    //增加明细
    private void addMxLayout(ArrayMap<String, Object> map) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View childView = inflater.inflate(R.layout.sale_order_detail_content_item, null);
        LinearLayout mContentLayout = childView.findViewById(R.id.sale_content_item_id);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 350);
        mContentLayout.setLayoutParams(params);

        TextView mProductName = childView.findViewById(R.id.product_name);
        TextView mOnePrice = childView.findViewById(R.id.one_price_text);
        TextView mNumbers = childView.findViewById(R.id.product_numbers);
        TextView mSmallPlan = childView.findViewById(R.id.price_sum);
        TextView mMemo = childView.findViewById(R.id.product_memo);
        TextView mGuige = childView.findViewById(R.id.specifications_name);

        mGuige.setText(map.get("productSpecifications")+"");
        mProductName.setText(map.get("productName") + "");
        mOnePrice.setText(map.get("productUnitPrice") + "");
        mNumbers.setText(map.get("productCount") + map.get("baseUnitText").toString());
        mSmallPlan.setText(map.get("productTotalPrice") + "");
        if(map.get("memo")!=null && !map.get("memo").toString().equals("null")){
            mMemo.setText(map.get("memo") + "");
        }
        mDetailGoodsLayout.addView(childView);
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
        switch (type) {
            case "successDetail":
                mDataMap = (ArrayMap<String, Object>) object;
                if (mDataMap != null && mDataMap.get("details") != null) {
                    mDetailsList = Constants.getJsonArray(mDataMap.get("details").toString());
                }
                if (mDataMap != null && mDataMap.get("approvals") != null) {
                    mApprovalsList = Constants.getJsonArray(mDataMap.get("approvals").toString());
                }
                mTerminaNameText.setText(mDataMap.get("companyName") + "");
                mConstactUserText.setText(mDataMap.get("contactName") + "");
                mPhoneText.setText(mDataMap.get("contactPhone") + "");
                mAddressText.setText(mDataMap.get("provinceName").toString() + mDataMap.get("cityName")  + mDataMap.get("countyName"));
//                String addressDetail = mDataMap.get("provinceName").toString()+ mDataMap.get("cityName")+ mDataMap.get("countyName")+ mDataMap.get("townName");
                mAddressDetailText.setText(mDataMap.get("address").toString());
//
                mDeliveryId = mDataMap.get("transportType") + "";
                if (mDataMap.get("payMoney") != null && !mDataMap.get("payMoney").toString().equals("") && !mDataMap.get("payMoney").equals("null")) {
                    mDskText.setText(mDataMap.get("payMoney") + "");
                    mDskLayout.setVisibility(View.VISIBLE);
                } else {
                    mDskLayout.setVisibility(View.GONE);
                }
                if(mDataMap.get("memo")!=null && !mDataMap.get("memo").toString().equals("null")){
                    mDocumentMemoText.setText(mDataMap.get("memo") + "");
                }
                mApplicantuserText.setText(mDataMap.get("createUserName") + "");
                mApplicantPhoneText.setText(mDataMap.get("createUserPhone") + "");
                float mSumPrice = 0;
                if (mDetailsList != null && mDetailsList.size() > 0) {
                    for (int i = 0; i < mDetailsList.size(); i++) {
                        addMxLayout(mDetailsList.get(i));
                        mSumPrice = mSumPrice + Float.parseFloat(mDetailsList.get(i).get("productTotalPrice").toString());
                    }
                }
                mSumPriceText.setText(mSumPrice + "");
                mCurrentApprovalId = mDataMap.get("currentApprovalId") + "";
                if (mApprovalsList != null && mApprovalsList.size() > 0) {
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }
                mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                if (mDataMap.get("saleOrderStatus") != null) {
                    switch (mDataMap.get("saleOrderStatus").toString()) {
                        case "10":
                            mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                            mOrderStatus.setTextColor(getResources().getColor(R.color.color35));
                            mOrderStatus.setBackgroundResource(R.drawable.bg_my_style14_yellow);
                            break;
                        case "20":
                            mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                            mOrderStatus.setTextColor(getResources().getColor(R.color.color25));
                            mOrderStatus.setBackgroundResource(R.drawable.bg_my_style14_blue);
                            break;
                        case "30":
                            mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                            mOrderStatus.setTextColor(getResources().getColor(R.color.color34));
                            mOrderStatus.setBackgroundResource(R.drawable.bg_my_style14_red);
                            break;
                        case "40":
                            mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                            mOrderStatus.setTextColor(getResources().getColor(R.color.color30));
                            mOrderStatus.setBackgroundResource(R.drawable.bg_my_style14);
                            break;
                        case "50":
                            mOrderStatus.setText(mDataMap.get("saleOrderStatusCn").toString());
                            mOrderStatus.setTextColor(getResources().getColor(R.color.color36));
                            mOrderStatus.setBackgroundResource(R.drawable.bg_my_style14_bohui);
                            break;
                    }
                }
                saleOrderPresenter.getDeliveryList();
                break;
            case "deliveryList":
                mDeliveryList = (ArrayList<ArrayMap<String, Object>>) object;
                logcat("获取配送方式"+mDeliveryList+"=============");
                for (int i = 0; i < mDeliveryList.size(); i++) {
                    if (mDeliveryId.equals(mDeliveryList.get(i).get("dictKey") + "")) {
                        mDeliveryText.setText(mDeliveryList.get(i).get("dictValue") + "");
                    }
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tl.com.tlsl>>","====返回返回==="+requestCode);
        Intent intent = new Intent();
        intent.setAction("flushOrderList");
        intent.putExtra("position","1");
        sendBroadcast(intent);
        finish();
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SaleOrderDetailActivity.this).inflate(R.layout.auditor_item_3, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (mApprovalsList.get(position).get("approvalUserName") != null) {
                holder.name.setText(mApprovalsList.get(position).get("approvalUserName") + "");
            }
            if(mApprovalsList.get(position).get("approvalId").equals(mCurrentApprovalId)){
                holder.status.setVisibility(View.VISIBLE);
            }else{
                holder.status.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mApprovalsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,status;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.auditors_text);
                status = view.findViewById(R.id.audition_status);
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
