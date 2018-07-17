package tl.com.tlsl.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import tl.com.tlsl.R;
import tl.com.tlsl.bean.JsonBean;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.AddressUtil;
import tl.com.tlsl.utils.AtyContainerUtils;
import tl.com.tlsl.utils.DensityUtils;
import tl.com.tlsl.utils.GetJsonDataUtil;
import tl.com.tlsl.view.IMvpView;
import tl.com.tlsl.weiget.LoadingDialog;
import tl.com.tlsl.weiget.MyEditText;

/**
 * Created by admin on 2018/5/18.
 * 销售单申请
 */

public class SaleOrderApplyActivity extends BaseActivity implements IMvpView, View.OnClickListener {

    private LinearLayout mBascLayout, mDetailGoodsLayout;
    private ImageView mAddDetailImage, mSearchTerminalImage, mBackImage;
    private LinearLayout mHeadLayout, mApplicantLayout;
    private int s = 0;
    private ScrollView mScrollView;
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdatper;
    private LinearLayout mSearchLayout;
    private OptionsPickerView pvOptions;
    private ArrayList<ArrayMap<String, Object>> mAuditorList;
    private TextView mAddAuditorText, mAddress, mDeliveryText;
    private SaleOrderPresenter saleOrderPresenter;
    private String mCompanyId;
    private ArrayMap<String, Object> mDataMap;
    private EditText mTerminalValue, mPhone, mAddressDetail;
    private RadioButton mTrueRadio, mFalseRadio;
    private ArrayMap<String, LinearLayout> mViweMaps;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Button mSubBtn;

    private boolean isLoaded = false;
    private ArrayList<ArrayMap<String, Object>> mProductList, mDeliveryList, mDetailsList;
    private ArrayMap<String, Object> mDetailMap;
    private ArrayList<String> mDeliveryItems = new ArrayList<>();
    private LinearLayout mDelveryLayout,mDskLayout;
    private TextView mSumPriceText, mContactsUser, mDataText;
    //    private LoadingDialog loadingDialog;
    private String companyId = "", companyName, contactName, contactPhone, address, memo, provinceId, provinceName, cityId, cityName, areaId, areaName, details;
    private float totalPrice = 0;
    private ArrayList<String> approvalIds;
    private EditText mMemo, mPayMoney;
    private int totalCount = 0, mTag = 0;
    private Bundle mBundle;
    private String mCompanyIdStr, mTransportType, mTotalPrice, mMemoStr, mSaleOrderId, mPayMoneyStr;
    private TextWatcher sumWatcher,numberWatcher,onPriceWatcher;
    private boolean isPayMoney = false;

    @Override
    protected int getLayoutId() {
        return R.layout.sale_order_apply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        AtyContainerUtils.getInstance().addActivity(this);
        AddressUtil.initProvinceDatas(this);

//        loadingDialog = new LoadingDialog(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mAuditorList = new ArrayList<>();
        mAdatper = new RecyAdapter();
        mRecyclerView.setAdapter(mAdatper);
        saleOrderPresenter = new SaleOrderPresenter(this, this);
        saleOrderPresenter.getCompanyId();
        mViweMaps = new ArrayMap<>();
        mProductList = new ArrayList<>();

        initJsonData();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        mDataText.setText(sdf.format(date));
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mCompanyIdStr = mBundle.getString("companyId");
            mTransportType = mBundle.getString("transportType");
            mTotalPrice = mBundle.getString("totalPrice");
            mMemoStr = mBundle.getString("memo");
            mSaleOrderId = mBundle.getString("saleOrderId");
            mPayMoneyStr = mBundle.getString("payMoney");
            if(mMemoStr!=null && !mMemoStr.equals("null")){
                mMemo.setText(mMemoStr);
            }
            if (mPayMoneyStr != null && !mPayMoneyStr.equals("null")) {
                mPayMoney.setText(mPayMoneyStr);
            }
//            saleOrderPresenter.queryDetail(mCompanyIdStr);
            saleOrderPresenter.getOrderDetail(mSaleOrderId);
            totalPrice = Float.parseFloat(mTotalPrice);
            mSumPriceText.setText(mTotalPrice + "");
            if(mTransportType.equals("10")){
                mDeliveryText.setText("仓库配送");
            }else if(mTransportType.equals("20")){
                mDeliveryText.setText("货主自提");
            }else{
                mDeliveryText.setText("快递");
            }
        } else {
            addMxLayout(null);
            provinceName = Constants.provinceName;
            provinceId = Constants.provinceId;
            cityName = Constants.cityName;
            cityId = Constants.cityId;
            areaName = Constants.countyName;
            areaId = Constants.countyId;
            mAddress.setText(provinceName+cityName+areaName);
            mAddressDetail.setText(Constants.address);
            address = Constants.address;
        }
        saleOrderPresenter.getDeliveryList();
//        String s = "SocketTimeoutException";
//        if(s.indexOf("Timeout")!= -1){
//            logcat("连接超时，请重试。");
//        }

    }

    public void initViews() {
        mDskLayout = findViewById(R.id.dsk_price_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDskLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(SaleOrderApplyActivity.this,50);
        mDskLayout.setLayoutParams(params);
        mBackImage = findViewById(R.id.back);
        mBackImage.setOnClickListener(this);
        mDataText = findViewById(R.id.data_show);
        mPayMoney = findViewById(R.id.pay_money);
        mMemo = findViewById(R.id.memo_edit);
        mContactsUser = findViewById(R.id.contacts_user_text);
        mSumPriceText = findViewById(R.id.sale_order_sum_price);
        mDelveryLayout = findViewById(R.id.delvery_layout);
        mDelveryLayout.setOnClickListener(this);
        mSubBtn = findViewById(R.id.sum_btn);
        mSubBtn.setOnClickListener(this);
        mPhone = findViewById(R.id.phone_contact);
        mAddress = findViewById(R.id.address_text);
        mAddressDetail = findViewById(R.id.address_detail);
        mDeliveryText = findViewById(R.id.delivery_mode);

        mApplicantLayout = findViewById(R.id.applicant_layout);
        params = (LinearLayout.LayoutParams) mApplicantLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 145);
        mApplicantLayout.setLayoutParams(params);

        mTrueRadio = findViewById(R.id.is_true_radio);
        mTrueRadio.setOnCheckedChangeListener(trueClick);
        mFalseRadio = findViewById(R.id.is_false_radio);
        mFalseRadio.setOnCheckedChangeListener(falseClick);
        mFalseRadio.setChecked(true);

        mTerminalValue = findViewById(R.id.terminal_value);
        mAddAuditorText = findViewById(R.id.add_auditor_text_show);

        mSearchLayout = findViewById(R.id.select_address_layout);
        mSearchLayout.setOnClickListener(this);
        mSearchTerminalImage = findViewById(R.id.search_terminal);
        mSearchTerminalImage.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.autior_recycler_view);

        mBascLayout = findViewById(R.id.basc_layout_content);
        params = (LinearLayout.LayoutParams) mBascLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 390);
        mBascLayout.setLayoutParams(params);
        mDetailGoodsLayout = findViewById(R.id.details_goods_layout);
        mAddDetailImage = findViewById(R.id.add_detail);
        mAddDetailImage.setOnClickListener(this);

        mHeadLayout = findViewById(R.id.head_text_layout);
        params = (LinearLayout.LayoutParams) mHeadLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 145);
        mHeadLayout.setLayoutParams(params);

        mScrollView = findViewById(R.id.sale_apply_scroll);
        params = (LinearLayout.LayoutParams) mScrollView.getLayoutParams();
        params.setMargins(0, DensityUtils.dp2px(this, -70), 0, 0);
        mScrollView.setLayoutParams(params);
    }

    private CompoundButton.OnCheckedChangeListener trueClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDskLayout.getLayoutParams();
            if(isChecked==true){
                params.height = DensityUtils.dp2px(SaleOrderApplyActivity.this,110);
                isPayMoney = true;
            }else{
                isPayMoney = false;
                params.height = DensityUtils.dp2px(SaleOrderApplyActivity.this,50);
            }
            mDskLayout.setLayoutParams(params);
//            mTrueRadio.setChecked(true);
//            mFalseRadio.setChecked(false);
        }
    };
    private CompoundButton.OnCheckedChangeListener falseClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

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
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            //提交
            case R.id.sum_btn:
                showLoading();
                try {
                    totalCount = 0;
                    com.alibaba.fastjson.JSONArray detalisList = new com.alibaba.fastjson.JSONArray();
                    for (int i = 0; i < mDetailGoodsLayout.getChildCount(); i++) {
                        JSONObject map = new JSONObject();
                        View view = mDetailGoodsLayout.getChildAt(i);
                        TextView inventoryNum = view.findViewById(R.id.inventoryNum);
                        TextView productId = view.findViewById(R.id.product_id);
                        EditText productUnitPrice = view.findViewById(R.id.one_price_text);
                        EditText productCount = view.findViewById(R.id.product_numbers);
                        EditText memo = view.findViewById(R.id.product_memo);
                        EditText productTotalPrice = view.findViewById(R.id.price_sum_edit);

                        map.put("productId", productId.getText().toString());
                        map.put("productUnitPrice", productUnitPrice.getText().toString());
                        map.put("productCount", productCount.getText().toString());
                        map.put("memo", memo.getText().toString());
                        map.put("inventoryNum", inventoryNum.getText().toString());
                        map.put("productTotalPrice", productTotalPrice.getText().toString().replaceAll("元", ""));
                        totalCount = totalCount + Integer.parseInt(productCount.getText().toString());
                        detalisList.add(map);
                    }
                    approvalIds = new ArrayList<>();
                    com.alibaba.fastjson.JSONArray mApprovalIds = new com.alibaba.fastjson.JSONArray();
                    for (int i = 0; i < mAuditorList.size(); i++) {
                        if (mAuditorList.get(i).get("userId") != null) {
//                            approvalIds.add(mAuditorList.get(i).get("userId") + "");
                            mApprovalIds.add(mAuditorList.get(i).get("userId") + "");
                        }
                    }
                    String payMoney;
                    if(isPayMoney){
                        payMoney  = mPayMoney.getText().toString();
                    }else{
                        payMoney = "0";
                    }
                    memo = mMemo.getText().toString();
                    logcat(mApprovalIds + "提交数据打印" + detalisList);
                    logcat("提交数据打印" + companyId + "===" + companyName + "===" + contactName + "===" + contactPhone + "===" + mTransportType + "===" + address + "===" + memo + "===" + provinceId + "===" + provinceName + "===" + cityId + "===" + cityName + "===" + areaId + "===" + areaName + "===" + totalCount + "===" + totalPrice);
                    if(companyId==null || companyId.equals("null")){
                        companyId = "";
                    }
                    companyName = mTerminalValue.getText().toString();
                    contactName = mContactsUser.getText().toString();
                    contactPhone = mPhone.getText().toString();
                    address = mAddressDetail.getText().toString();
//                    if (mApprovalIds != null && mApprovalIds.size()>0 && companyName != null && contactName != null && contactPhone != null && mTransportType != null && address != null && provinceId != null && cityId != null && areaId != null && detalisList != null && totalCount != 0 && totalPrice != 0) {
                        if (mApprovalIds != null && mApprovalIds.size()>0 && detalisList != null && detalisList.size()>0) {
                            hideLoading();
                        saleOrderPresenter.addOrder(companyId, companyName, contactName, contactPhone, mTransportType, address, memo, provinceId, provinceName, cityId, cityName, areaId, areaName, mApprovalIds, totalCount + "", totalPrice + "", "", payMoney, detalisList);
                    } else {
                        mAlertDialog.builder().setTitle("提示")
                                .setMsg("销售单内容填写不完整！")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                        hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //送货方式
            case R.id.delvery_layout:
                pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mDeliveryText.setText(mDeliveryItems.get(options1));
//                        transportType = mDeliveryItems.get(options1);
                        mTransportType = mDeliveryList.get(options1).get("dictKey") + "";
                    }
                })
                        .setTitleText("")
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setDividerColor(getResources().getColor(R.color.color18))
                        .setTextColorCenter(Color.BLACK)
                        .isCenterLabel(true)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setLinkage(true)//设置是否联动
                        .setContentTextSize(20)
                        .setSelectOptions(0)  //设置默认选中项
                        .build();
                pvOptions.setPicker(mDeliveryItems);//三级选择器
                pvOptions.show();
                break;

            case R.id.select_address_layout:
                pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        provinceName = options1Items.get(options1).getPickerViewText();
                        provinceId = AddressUtil.getProvinceCode(provinceName);
                        cityName = options2Items.get(options1).get(options2);
                        if (provinceName.equals("北京市") || provinceName.equals("上海市") || provinceName.equals("天津市") || provinceName.equals("重庆市")) {
                            cityId = provinceId;
                        } else {
                            cityId = AddressUtil.getCityCode(cityName);
                        }
                        areaName = options3Items.get(options1).get(options2).get(options3);
                        areaId = AddressUtil.getAreaCode(areaName);
//                        logcat(mProvinceName+"==="+mProvinceId+"==="+mCityName+"==="+mCityId+"==="+mAreaName+"==="+mAreaId);
                        String tx = options1Items.get(options1).getPickerViewText()  +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3)+"";
                        mAddress.setText(tx);
                    }
                })
                        .setTitleText("")
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setDividerColor(getResources().getColor(R.color.color18))
                        .setTextColorCenter(Color.BLACK)
                        .isCenterLabel(true)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setLinkage(true)//设置是否联动
                        .setContentTextSize(20)
                        .setSelectOptions(0)  //设置默认选中项
                        .build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pvOptions.show();
                break;
            case R.id.add_detail:
                addMxLayout(null);
                break;
            case R.id.search_terminal:
                Bundle bundle = new Bundle();
                bundle.putString("companyId", mCompanyId);
//                bundle.putString("companyId", "9VGZA31843RITQH7C3B5B1BB8");
                Intent intent = new Intent(this, SearchTerminalListActivity.class);
                if (null != bundle)
                    intent.putExtras(bundle);

                startActivityForResult(intent, 1);
                break;
        }
    }


    //增加明细
    private void addMxLayout(ArrayMap<String, Object> map) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View childView = inflater.inflate(R.layout.sale_apply_content_item, null);
        LinearLayout mContentLayout = childView.findViewById(R.id.sale_content_item_id);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 330);
        mContentLayout.setLayoutParams(params);
        LinearLayout mProductNameLayout = childView.findViewById(R.id.product_name_layout);

        ImageView mDeteleImage = childView.findViewById(R.id.delete_image);
        mDeteleImage.setOnClickListener(mxClick);

        final MyEditText mOnePrice = childView.findViewById(R.id.one_price_text);
        final MyEditText mNumbers = childView.findViewById(R.id.product_numbers);
        final MyEditText mSumEdit = childView.findViewById(R.id.price_sum_edit);
        TextView mProductName = childView.findViewById(R.id.product_name);
        TextView mGuigeText = childView.findViewById(R.id.specifications_text);
        TextView productIdText = childView.findViewById(R.id.product_id);
        TextView inventoryNumText = childView.findViewById(R.id.inventoryNum);
        EditText memo = childView.findViewById(R.id.product_memo);

        logcat("获取货品明细数据返回===================" + map);
        if (map != null && map.size() > 0) {
            mProductName.setText(map.get("productName") + "");
            mGuigeText.setText(map.get("productSpecifications") + "");
            mOnePrice.setText(map.get("productUnitPrice") + "");
            mNumbers.setText(map.get("productCount") + "");
            mSumEdit.setText(map.get("productTotalPrice") + "");
            if(map.get("memo")!=null && !map.get("memo").toString().equals("null")){
                memo.setText(map.get("memo") + "");
            }
            productIdText.setText(map.get("productId") + "");
            inventoryNumText.setText(map.get("inventoryNum") + "");
        }
        sumWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNumbers.clearTextChangedListeners();
                mOnePrice.clearTextChangedListeners();
                if (s == null || s.toString().equals("")) {
//                    mSumEdit.setHint("0");
                }
                mHandler.sendEmptyMessageDelayed(0,0);
            }
            @Override
            public void afterTextChanged(Editable s) {
                mNumbers.addTextChangedListener(numberWatcher);
                mOnePrice.addTextChangedListener(onPriceWatcher);
            }
        };
        onPriceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNumbers.clearTextChangedListeners();
                mSumEdit.clearTextChangedListeners();
                String value = mOnePrice.getText().toString();
                float m;
                if (value == null || value.toString().equals("")) {
                    m = 0;
                } else {
                    m = Float.parseFloat(value);
                }
                float numbers;
                if(mNumbers.getText().toString().equals("")){
                    numbers = 0;
                }else{
                    numbers = Float.parseFloat(mNumbers.getText().toString());
                }
                double sum = m * numbers;
                BigDecimal bd1 = new BigDecimal(sum);
                mSumEdit.setText(bd1.toPlainString() + "");
                mHandler.sendEmptyMessageDelayed(0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNumbers.addTextChangedListener(numberWatcher);
                mSumEdit.addTextChangedListener(sumWatcher);
            }
        };
        numberWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOnePrice.clearTextChangedListeners();
                mSumEdit.clearTextChangedListeners();
                float m;
                if (s == null || s.toString().equals("")) {
                    m = 0;
                } else {
                    m = Float.parseFloat(s.toString());
                }
                float onePrice;
                if(mOnePrice.getText().toString().equals("")){
                    onePrice = 0;
                }else{
                    onePrice = Float.parseFloat(mOnePrice.getText().toString());
                }
                double sum = onePrice * m;
                BigDecimal bd1 = new BigDecimal(sum);
                mSumEdit.setText(bd1.toPlainString() + "");
//                mSumEdit.setText(sum+"");
                mHandler.sendEmptyMessageDelayed(0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mOnePrice.addTextChangedListener(onPriceWatcher);
                mSumEdit.addTextChangedListener(sumWatcher);
            }
        };
        mSumEdit.addTextChangedListener(sumWatcher);
        mOnePrice.addTextChangedListener(onPriceWatcher);
        mNumbers.addTextChangedListener(numberWatcher);
        mTag++;
        mDeteleImage.setTag(mTag);
        mProductNameLayout.setTag(mTag);
        logcat(childView.getTag() + "tag========" + mProductNameLayout.getTag());
        mViweMaps.put(mProductNameLayout.getTag() + "", mContentLayout);
        mDetailGoodsLayout.addView(childView);
        mProductNameLayout.setOnClickListener(mxClick);
    }


    private View.OnClickListener mxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_image:
                    mViweMaps.remove(v.getTag());
                    mDetailGoodsLayout.removeView((View) v.getParent().getParent().getParent());
                    mHandler.sendEmptyMessageDelayed(0, 10);
                    break;
                case R.id.product_name_layout:
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", v.getTag() + "");
                    openActivityForResult(SearchProductActivity.class, 2, bundle);
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
    public void onSuccess(String type, Object object) {
        switch (type) {
            case "getCompanyId":
                mDataMap = (ArrayMap<String, Object>) object;
                mCompanyId = mDataMap.get("companyId").toString();
                break;
            case "queryTemina":
                ArrayMap map = (ArrayMap) object;
                mTerminalValue.setText(map.get("companyName").toString());
                mContactsUser.setText(map.get("contactUserName").toString());
                mPhone.setText(map.get("contactUserPhone").toString());

                provinceName = map.get("companyProvinceName").toString();
                provinceId = map.get("companyProvinceId").toString();
                cityName = map.get("companyCityName").toString();
                cityId = map.get("companyCityId").toString();
                areaName = map.get("companyCountyName").toString();
                areaId = map.get("companyCountyId").toString();
                mAddress.setText(provinceName + cityName + areaName);
                mAddressDetail.setText(map.get("address").toString());

                companyId = map.get("companyId").toString();
                companyName = map.get("companyName").toString();
                contactName = map.get("contactUserName").toString();
                contactPhone = map.get("contactUserPhone").toString();
                address = map.get("address").toString();

                break;
            case "deliveryList":
                mDeliveryList = (ArrayList<ArrayMap<String, Object>>) object;
                for (int i = 0; i < mDeliveryList.size(); i++) {
                    mDeliveryItems.add(mDeliveryList.get(i).get("dictValue") + "");
                }
                break;
            case "success":
                Intent intent = new Intent();
                intent.setAction("flushList");
                intent.putExtra("position", "0");
                sendBroadcast(intent);
                finish();
                Toast.makeText(this, object + "", Toast.LENGTH_SHORT).show();
                break;
            case "successDetail":
                mDetailMap = (ArrayMap<String, Object>) object;
                if (mDetailMap != null && mDetailMap.get("details") != null) {
                    mDetailsList = Constants.getJsonArray(mDetailMap.get("details").toString());
                }
                for (int i = 0; i < mDetailsList.size(); i++) {
                    addMxLayout(mDetailsList.get(i));
                }
                provinceName = mDetailMap.get("provinceName")+"";
                provinceId = mDetailMap.get("provinceId")+"";
                cityName = mDetailMap.get("cityName")+"";
                cityId = mDetailMap.get("countyId")+"";
                areaName = mDetailMap.get("countyName")+"";
                areaId = mDetailMap.get("countyId")+"";
                mAddress.setText(provinceName + cityName + areaName);

                if(mDetailMap.get("companyId")!=null){
                    companyId = mDetailMap.get("companyId")+"";
                }
                companyName = mDetailMap.get("companyName")+"";
                contactName = mDetailMap.get("contactName")+"";
                contactPhone = mDetailMap.get("contactPhone")+"";
                address = mDetailMap.get("address")+"";

                mAddressDetail.setText(address);
                mTerminalValue.setText(companyName);
                mContactsUser.setText(contactName);
                mPhone.setText(contactPhone);
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
//        logcat("是否回去刷新状态==="+requestCode+"==="+ data.getExtras().getString("value"));
        String name, userId;
        ArrayMap<String, Object> map;
        switch (requestCode) {
            //审核组返回
            case 0:
                if (data != null && data.getExtras() != null) {
                    String type = data.getExtras().getString("type");
                    name = data.getExtras().getString("name");
                    userId = data.getExtras().getString("userId");
                    int position = Integer.parseInt(data.getExtras().getString("position"));
                    if (type.equals("change")) {

                        if (position >= mAuditorList.size() - 1 && position > 0) {
                            if (!mAuditorList.get(position - 1).get("name").toString().equals(name)) {
                                mAuditorList.get(position).put("name", name);
                                mAuditorList.get(position).put("userId", userId);
                            }
                        } else if (position < mAuditorList.size() - 1 && position > 0) {
                            if (!mAuditorList.get(position - 1).get("name").toString().equals(name) && !mAuditorList.get(position + 1).get("name").toString().equals(name)) {
                                mAuditorList.get(position).put("name", name);
                                mAuditorList.get(position).put("userId", userId);
                            }
                        } else if (position == 0) {
                            if (mAuditorList.size() > 1) {
                                if (!mAuditorList.get(position + 1).get("name").toString().equals(name)) {
                                    mAuditorList.get(position).put("name", name);
                                    mAuditorList.get(position).put("userId", userId);
                                }
                            } else {
                                mAuditorList.get(position).put("name", name);
                                mAuditorList.get(position).put("userId", userId);
                            }
                        }
                    } else if (type.equals("add")) {
                        if (position > 0) {
                            if (!mAuditorList.get(position - 1).get("name").toString().equals(name)) {
                                map = new ArrayMap<>();
                                map.put("name", name);
                                map.put("userId", userId);
                                mAuditorList.add(map);
                            }
                        } else {
                            map = new ArrayMap<>();
                            map.put("name", name);
                            map.put("userId", userId);
                            mAuditorList.add(map);
                        }
                    }
                    mAdatper.notifyDataSetChanged();
                    if (mAuditorList.size() > 0) {
                        mAddAuditorText.setVisibility(View.GONE);
                    }
                }
                break;
            //搜索终端店返回
            case 1:
                if (data != null && data.getExtras() != null) {
                    saleOrderPresenter.queryDetail(data.getExtras().getString("companyId"));
                }
                break;
            //搜索货品名称返回
            case 2:
                if (data != null && data.getExtras() != null) {
                    String tag = data.getExtras().getString("tag");
                    LinearLayout view = mViweMaps.get(tag);
                    TextView mProductName = view.findViewById(R.id.product_name);
                    TextView mGuigeText = view.findViewById(R.id.specifications_text);

                    TextView productIdText = view.findViewById(R.id.product_id);
                    TextView inventoryNumText = view.findViewById(R.id.inventoryNum);


                    productIdText.setText(data.getExtras().getString("productId"));
                    inventoryNumText.setText(data.getExtras().getString("inventoryNum"));

                    mProductName.setText(data.getExtras().getString("productName"));
                    String guige = data.getExtras().getString("packageCount") + data.getExtras().getString("baseUnitCn") + "/" + data.getExtras().getString("packageUnitCn");
                    mGuigeText.setText(guige);
                }
                break;
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (viewType == Constants.VIEW_TYPE) {
                holder = new MyViewHolder(LayoutInflater.from(
                        SaleOrderApplyActivity.this).inflate(R.layout.auditor_add_item, parent,
                        false));
            } else {
                holder = new MyViewHolder2(LayoutInflater.from(
                        SaleOrderApplyActivity.this).inflate(R.layout.auditor_item, parent,
                        false));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (position == mAuditorList.size()) {
                ((MyViewHolder) holder).mContentLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("type", "add");
//                        bundle.putString("position", position + "");
//                        openActivityForResult(ChoseAuditTeamActivity.class, 0, bundle);

                        Bundle bundle = new Bundle();
                        bundle.putString("type", "add");
                        bundle.putString("position", position + "");
                        Intent intent = new Intent(SaleOrderApplyActivity.this, ChoseAuditTeamActivity.class);
                        if (null != bundle)
                            intent.putExtras(bundle);

                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                if (mAuditorList.get(position).get("name") != null) {
                    ((MyViewHolder2) holder).mTextView.setText(mAuditorList.get(position).get("name") + "");
                }
                ((MyViewHolder2) holder).mDeteleImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuditorList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                ((MyViewHolder2) holder).mContentLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("position", position + "");
                        bundle.putString("type", "change");
                        Intent intent = new Intent(SaleOrderApplyActivity.this, ChoseAuditTeamActivity.class);
                        if (null != bundle)
                            intent.putExtras(bundle);

                        startActivityForResult(intent, 0);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mAuditorList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mAuditorList.size()) {
                return Constants.VIEW_TYPE;
            } else {
                return position;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            FrameLayout mContentLayout1;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout1 = view.findViewById(R.id.item_content_id);
            }
        }

        class MyViewHolder2 extends RecyclerView.ViewHolder {
            FrameLayout mContentLayout2;
            TextView mTextView;
            ImageView mDeteleImage;

            public MyViewHolder2(View view) {
                super(view);
                mTextView = view.findViewById(R.id.auditors_text);
                mContentLayout2 = view.findViewById(R.id.item_content_id);
                mDeteleImage = view.findViewById(R.id.delete_image);
            }
        }
    }


    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    totalPrice = 0;
                    for (int i = 0; i < mDetailGoodsLayout.getChildCount(); i++) {
                        View view = mDetailGoodsLayout.getChildAt(i);
                        MyEditText mSumText = view.findViewById(R.id.price_sum_edit);
                        float sum;
                        if(mSumText.getText().toString().equals("")){
                            sum = 0;
                        }else{
                            sum = Float.parseFloat(mSumText.getText().toString().replaceAll("元", ""));
                        }
                        totalPrice = totalPrice + sum;
                    }
                    BigDecimal bd1 = new BigDecimal(totalPrice);
                    mSumPriceText.setText(bd1.toPlainString() + "");
//                    mSumPriceText.setText(totalPrice + "");
                    break;
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        Toast.makeText(SaleOrderApplyActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(SaleOrderApplyActivity.this,"Parse Succeed",Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
//                    Toast.makeText(SaleOrderApplyActivity.this,"Parse Failed",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
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
