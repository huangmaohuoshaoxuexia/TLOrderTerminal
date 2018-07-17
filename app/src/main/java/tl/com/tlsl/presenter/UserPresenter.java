package tl.com.tlsl.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.http.RetrofitHttp;
import tl.com.tlsl.model.impl.UserInterface;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2017/10/23.
 */

public class UserPresenter extends BasePresenter implements UserInterface {
    public UserPresenter(IMvpView iLoginView, Context context){
        this.mImvpView = iLoginView;
        this.mContext = context;
    }

    @Override
    public void login(String username, String password) {
        Map map = new HashMap();
        map.put("userName",username);
        map.put("password",password);
        RetrofitHttp.getInstance(mContext).postJson(Constants.login, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                mImvpView.hideLoading();
                if(e.toString().indexOf("Timeout")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    Constants.token = mTempMap2.get("token").toString();
                    Constants.provinceName = mTempMap2.get("provinceName").toString();
                    Constants.provinceId = mTempMap2.get("provinceId").toString();
                    Constants.cityId = mTempMap2.get("cityId").toString();
                    Constants.cityName = mTempMap2.get("cityName").toString();
                    Constants.countyId = mTempMap2.get("countyId").toString();
                    Constants.countyName = mTempMap2.get("countyName").toString();
                    Constants.address = mTempMap2.get("address").toString();
                    mImvpView.onSuccess("success",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void getCode(String tel) {
        Map map = new HashMap();
        map.put("tel",tel);
        RetrofitHttp.getInstance(mContext).postJson(Constants.getTelCode, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl获得数据",e+"====onNext====");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("code",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void verificationCode(String tel, String code) {
        mImvpView.showLoading();
        Map map = new HashMap();
        map.put("tel",tel);
        map.put("text",code);
        RetrofitHttp.getInstance(mContext).postJson(Constants.verificationCode, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl获得数据",e+"====onNext====");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("next",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void setPwd(String tel, String pwd) {
        mImvpView.showLoading();
        Map map = new HashMap();
        map.put("mobil",tel);
        map.put("password",Constants.md5Up(pwd));
        RetrofitHttp.getInstance(mContext).postJson(Constants.modifyPwd, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl获得数据",e+"====onNext====");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("modify",mDataMap.get("resultMessage")+"");
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

}
