package tl.com.tlsl.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;

import tl.com.tlsl.R;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.RecyclerUtil;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/28.
 */

public class SearchProductActivity extends BaseActivity implements IMvpView{

    private SaleOrderPresenter saleOrderPresenter;
    private Bundle mBundle;
    private String mProductName="";
    private int page = 1, x = 1;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private RecyAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private EditText mSearchEdit;
    private TextView mSearchText;
    private String mTag = "0";
    private ImageView mXImage;
    private TextView mNoText;

    @Override
    protected int getLayoutId() {
        return R.layout.search_product_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saleOrderPresenter = new SaleOrderPresenter(this,this);
        RecyclerUtil.init(mRecyclerView, this);
        mBundle = getIntent().getExtras();
        mTag = mBundle.getString("tag");
//        mContentLayout = (View) mBundle.getSerializable("view");
        saleOrderPresenter.queryCompanyProdAndInventList(mProductName);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {

                        saleOrderPresenter.queryCompanyProdAndInventList(mProductName);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        mNoText = findViewById(R.id.no_data_text);
        mRecyclerView = findViewById(R.id.search_product_recycler_view);
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
            case R.id.tv_right:
                mProductName = mSearchEdit.getText().toString();
                page = 1;
                x = 1;
                saleOrderPresenter.queryCompanyProdAndInventList(mProductName);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
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
        mList = (ArrayList) object;
        if(mList!=null && mList.size()>0){
            mAdapter = new RecyAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mNoText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else{
            mNoText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFail(String str) {
        mNoText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        private ArrayMap<String,Object> mDataMap;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SearchProductActivity.this).inflate(R.layout.search_terminal_list_item, parent,
                    false));
            return holder;
        }
        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            mDataMap = Constants.getJsonObject(mList.get(position).get("product").toString());
            if(mDataMap.get("productName")!=null){
                holder.nameText.setText(mDataMap.get("productName").toString());
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    logcat("传送的数据"+mDataMap.get("productName")+"==========");
                    bundle.putString("productName", Constants.getJsonObject(mList.get(position).get("product").toString()).get("productName")+"");
                    bundle.putString("packageCount", Constants.getJsonObject(mList.get(position).get("product").toString()).get("packageCount")+"");
                    bundle.putString("productId", Constants.getJsonObject(mList.get(position).get("product").toString()).get("productId")+"");

                    bundle.putString("packageUnitCn", mList.get(position).get("packageUnitCn")+"");
                    bundle.putString("baseUnitCn", mList.get(position).get("baseUnitCn")+"");
                    bundle.putString("inventoryNum", mList.get(position).get("inventoryNum")+"");
                    bundle.putString("tag",mTag);
//                    bundle.putSerializable("view", (Serializable) mContentLayout);
                    setResultOk(bundle);
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
