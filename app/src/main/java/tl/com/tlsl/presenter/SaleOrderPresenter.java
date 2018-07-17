package tl.com.tlsl.presenter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import tl.com.tlsl.common.Constants;
import tl.com.tlsl.http.RetrofitHttp;
import tl.com.tlsl.model.impl.SaleOrderInterface;
import tl.com.tlsl.view.IMvpView;

/**
 * Created by admin on 2018/5/23.
 */

public class SaleOrderPresenter extends BasePresenter implements SaleOrderInterface{

    public SaleOrderPresenter(IMvpView iMvpView,Context context){
        this.mImvpView = iMvpView;
        this.mContext = context;
    }
    //我发起的
    @Override
    public void getOrderList(String code, String status, String copanyName,int page) {
        Map map = new HashMap();
        map.put("saleOrderCode",code);
        map.put("transportType","");
        map.put("saleOrderStatus",status);
        map.put("pageSize","10");
        map.put("pageNum",page);
        map.put("requestType","3");
        map.put("companyName",copanyName);
        map.put("token",Constants.token);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.saleList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                mImvpView.hideLoading();
                if(e.toString().indexOf("Timeout")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    if(mTempList!=null && mTempList.size()>0){
                        mImvpView.onSuccess("success",mTempList);
                    }
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }
    //待我审核
    @Override
    public void getMyList(String code, String status,String copanyName, int page) {
        Map map = new HashMap();
        map.put("saleOrderCode",code);
        map.put("transportType","");
        map.put("saleOrderStatus",status);
        map.put("pageSize","10");
        map.put("pageNum",page);
        map.put("requestType","3");
        map.put("token",Constants.token);
        map.put("companyName",copanyName);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.saleMyList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
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
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    if(mTempList!=null && mTempList.size()>0){
                        mImvpView.onSuccess("success",mTempList);
                    }
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }
    //历史审核
    @Override
    public void getHistoryList(String code, String status, String copanyName,int page) {
        Map map = new HashMap();
        map.put("saleOrderCode",code);
        map.put("transportType","");
        map.put("saleOrderStatus",status);
        map.put("pageSize","10");
        map.put("pageNum",page);
        map.put("requestType","3");
        map.put("token",Constants.token);
        map.put("companyName",copanyName);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.saleHistoryList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                mImvpView.hideLoading();
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    if(mTempList!=null && mTempList.size()>0){
                        mImvpView.onSuccess("success",mTempList);
                    }
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void queryBoundCompanyList(String companyId, int page) {
        Map map = new HashMap();
        map.put("companyId",companyId);
        map.put("pageSize","10");
        map.put("companyType","20");
        map.put("pageNo",page);
        map.put("token",Constants.token);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryBoundCompanyList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                mImvpView.onFail(e+"");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    mImvpView.onSuccess("success",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void getCompanyId() {
        Map map = new HashMap();
        map.put("token",Constants.token);
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getCompanyId, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
//                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    mImvpView.onSuccess("getCompanyId",Constants.getJsonObject(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void queryCompanyProdAndInventList(String product) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("productName",product);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryCompanyProdAndInventList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("list",Constants.getJsonArray(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void queryDetail(String companyId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("companyId",companyId);
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryDetail, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("queryTemina",Constants.getJsonObject(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据queryDetail",s+"====onNext====");
            }
        });
    }

    @Override
    public void approvalList() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("requestType","3");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.approvalList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("auditorList",Constants.getJsonArray(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void addOrder(String companyId, String companyName, String contactName, String contactPhone, String transportType, String address, String memo, String provinceId, String provinceName, String cityId, String cityName, String countyId, String countyName, JSONArray approvalIds, String totalCount, String totalPrice, String payChannel, String payMoney, JSONArray details) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("requestType","3");
        map.put("companyId",companyId);
        map.put("companyName",companyName);
        map.put("contactName",contactName);
        map.put("contactPhone",contactPhone);
        map.put("transportType",transportType);
        map.put("address",address);
        map.put("memo",memo);
        map.put("provinceId",provinceId);
        map.put("provinceName",provinceName);
        map.put("cityId",cityId);
        map.put("cityName",cityName);
        map.put("details",details);
        map.put("approvalIds",approvalIds);
        map.put("countyId",countyId);

        map.put("countyName",countyName);
        map.put("totalCount",totalCount);
        map.put("totalPrice",totalPrice);
        map.put("payMoney",payMoney);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.addOrder, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("success",mDataMap.get("resultMessage")+"");
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获得数据",s+"====onNext====");
            }
        });
    }

    @Override
    public void getDeliveryList() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("dictCode","TRANSPORT_TYPE");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getDeliveryList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("deliveryList",Constants.getJsonArray(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获取送货方式",s+"====onNext====");
            }
        });
    }

    @Override
    public void getOrderDetail(String orderId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("saleOrderId",orderId);
        map.put("requestType","3");
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getOrderDetail, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("successDetail",Constants.getJsonObject(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl获取订单明细",s+"====onNext====");
            }
        });
    }

    @Override
    public void approve(String orderId,String content) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("saleOrderId",orderId);
        map.put("approvalSuggestion",content);
        map.put("requestType","3");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.approve, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("success",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl",s+"====onNext====");
            }
        });
    }

    @Override
    public void reject(String orderId,String content) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("saleOrderId",orderId);
        map.put("approvalSuggestion",content);
        map.put("requestType","3");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.reject, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("success",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl",s+"====onNext====");
            }
        });
    }

    @Override
    public void revoke(String saleOrderId, String createUserId, String saleOrderStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("saleOrderId",saleOrderId);
        map.put("createUserId",createUserId);
        map.put("saleOrderStatus",saleOrderStatus);
        map.put("requestType","3");
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.revoke, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("tl.com.tlsl返回数据",e+"=====onError=======");
                mImvpView.onFail(e+"");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("revokeSuccess",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("tl.com.tlsl",s+"====onNext====");
            }
        });
    }
}
