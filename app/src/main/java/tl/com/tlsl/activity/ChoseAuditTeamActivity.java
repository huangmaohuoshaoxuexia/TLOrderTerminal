package tl.com.tlsl.activity;

import android.os.Bundle;
import android.os.Handler;
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
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tl.com.tlsl.R;
import tl.com.tlsl.model.impl.ChoseAuditorImpl;
import tl.com.tlsl.model.impl.ChoseAuditorInterface;
import tl.com.tlsl.presenter.SaleOrderPresenter;
import tl.com.tlsl.utils.RecyclerUtil;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2017/10/20.
 * 选择审核组
 */

public class ChoseAuditTeamActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mListView;
    private ChoseAuditorAdapter mTeamAdapter;
    private ChoseAuditorImpl choseAuditor;
//    private ImageView mSearchImage, mXImage;
    private TextView mTitle;
    private LinearLayout mSearchLayout;
    private SaleOrderPresenter saleOrderPresenter;
    private ArrayList<ArrayMap<String,Object>> mList;
    private String mPosition,mType;
    private Bundle mBundle;

    @Override
    protected int getLayoutId() {
        return R.layout.select_auditor_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mListView, this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mPosition = mBundle.getString("position");
            mType = mBundle.getString("type");
        }
        saleOrderPresenter = new SaleOrderPresenter(this,this);
        saleOrderPresenter.approvalList();

        choseAuditor = new ChoseAuditorImpl();
        choseAuditor.setChoseAuditorInterface(choseAuditorInterface);

        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        saleOrderPresenter.approvalList();
                        mListView.refreshComplete();
                        mListView.setLoadingMoreEnabled(true);
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
        mListView = findViewById(R.id.chose_auditor_list);
//        mSearchImage = findViewById(R.id.sale_search_image);
//        mSearchImage.setOnClickListener(this);
        mTitle = findViewById(R.id.tv_title);
        mSearchLayout = findViewById(R.id.sale_title_search_layout);
//        mXImage = findViewById(R.id.x_image);
//        mXImage.setOnClickListener(this);
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
        mTitle.setText("选择审核人");
    }

    private ChoseAuditorInterface choseAuditorInterface = new ChoseAuditorInterface() {
        @Override
        public void choseAuditor(String name) {
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            setResultOk(bundle);
        }
    };


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sale_search_image:
                mTitle.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.x_image:
                mTitle.setVisibility(View.VISIBLE);
                mSearchLayout.setVisibility(View.GONE);
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
        mList = (ArrayList<ArrayMap<String, Object>>) object;
        mTeamAdapter = new ChoseAuditorAdapter();
        mListView.setAdapter(mTeamAdapter);
    }

    @Override
    public void onFail(String str) {

    }

    public class ChoseAuditorAdapter extends RecyclerView.Adapter<ChoseAuditorAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ChoseAuditTeamActivity.this).inflate(R.layout.chose_audition_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if(mList.get(position).get("userNameCn")!=null){
                holder.mTextView.setText(mList.get(position).get("userNameCn")+"");
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mList.get(position).get("userNameCn")+"");
                    bundle.putString("userId", mList.get(position).get("userId")+"");
                    bundle.putString("position", mPosition+"");
                    bundle.putString("type", mType);
                    setResultOk(bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout mContentLayout;
            TextView mTextView;
            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.auditor_item);
                mTextView = view.findViewById(R.id.item_text);
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
