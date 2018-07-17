package tl.com.tlsl.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import tl.com.tlsl.R;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.RecyclerUtil;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/21.
 * 终端店搜索
 */

public class SearchTerminalListActivity extends BaseActivity implements IMvpView{

    private SaleOrderPresenter saleOrderPresenter;
    private Bundle mBundle;
    private String mCompanyId;
    private int page = 1, x = 1;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private RecyAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private EditText mSearchEdit;
    private TextView mSearchText;

    @Override
    protected int getLayoutId() {
        return R.layout.search_terminal_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saleOrderPresenter = new SaleOrderPresenter(this,this);
        RecyclerUtil.init(mRecyclerView, this);
        mBundle = getIntent().getExtras();
        mCompanyId = mBundle.getString("companyId");
        saleOrderPresenter.queryBoundCompanyList(mCompanyId,page);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        page = 1;
                        x = 1;
                        saleOrderPresenter.queryBoundCompanyList(mCompanyId,page);
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
                        saleOrderPresenter.queryBoundCompanyList(mCompanyId,page);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.search_terminal_recycler_view);
        mSearchEdit = findViewById(R.id.search_edit);
        mSearchText = findViewById(R.id.tv_right);
        mSearchText.setOnClickListener(this);
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
    public void setHeader() {
        super.setHeader();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_right:
                mCompanyId = mSearchEdit.getText().toString();
                page = 1;
                x = 1;
                saleOrderPresenter.queryBoundCompanyList(mCompanyId,page);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
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
        mRecyclerView.setVisibility(View.VISIBLE);
        if (x == 1) {
            mList = (ArrayList) object;
            if(mList!=null && mList.size()>0){
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
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
    }

    @Override
    public void onFail(String str) {
        mRecyclerView.setVisibility(View.GONE);
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SearchTerminalListActivity.this).inflate(R.layout.search_terminal_list_item, parent,
                    false));
            return holder;
        }
        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(mList.get(position).get("companyName")!=null){
                holder.nameText.setText(mList.get(position).get("companyName").toString());
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mList.get(position).get("companyName")+"");
                    bundle.putString("companyId", mList.get(position).get("companyId")+"");
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
