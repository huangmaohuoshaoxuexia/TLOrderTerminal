package tl.com.tlsl.common;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import tl.com.tlsl.model.entity.User;


/**
 * Created by Administrator on 2016/9/7.
 */
public class Constants {
    public static final int VIEW_TYPE = -100;
    public static String partner = "android";
    public static String key = "g91qycacimz75ony7atdqria2ck4w7i55c5x";
    public static String token = "",provinceId="",provinceName="",cityId="",cityName="",countyName="",countyId="",address="";

    public static final String apiHead = "http://api.fat.tianlu56.com.cn/"; //研发环境
//    public static final String apiHead = "http://wmsapi.tianlu56.com.cn/";//生产环境

    //经销商登录
    public static final String login = "usc/user/distributorLogin";
    //获取短信验证码
    public static final String getTelCode= "usc/user/forgetPassword";
    //校验验证码
    public static final String verificationCode= "usc/user/queryMessage";
    //修改密码
    public static final String modifyPwd= "usc/user/editPasswordByMobil";
    //我发起的订单列表
    public static final String saleList= "order/saleOrder/list";
    //待我审核列表
    public static final String saleMyList= "order/saleOrder/taskList";
    //历史审核
    public static final String saleHistoryList= "order/saleOrder/approvedTaskList";
    //获取已绑定买卖关系的终端店列表
    public static final String queryBoundCompanyList= "crm/company/queryBoundCompanyList";
    //获取已绑定买卖关系的终端店Id
    public static final String getCompanyId= "order/saleOrder/getCompanyId";
    //新增销售订单
    public static final String addOrder= "order/saleOrder/add";
    //获取公司货品名称
    public static final String queryCompanyProdAndInventList= "crm/company/queryCompanyProdAndInventList";
    //通过id获取终端店详细
    public static final String queryDetail= "crm/company/queryDetail";
    //通过id获取终端店详细
    public static final String approvalList= "order/saleOrder/approvalList";
    //获取送货方式
    public static final String getDeliveryList= "order/dict/list";
    //获取销售单详情
    public static final String getOrderDetail= "order/saleOrder/detail";

    //销售单通过
    public static final String approve= "order/saleOrder/approve";
    //销售单驳回
    public static final String reject= "order/saleOrder/reject";
    //撤单
    public static final String revoke= "order/saleOrder/revoke";

    public static User user;
    // 请求接口排序
    public static String sortsStr(String res) {
        String result = "";
//        res = res + partner;
        String[] str = res.split("&");
        Arrays.sort(str);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            if (i < str.length - 1) {
                sb.append(str[i]).append("&");
            } else {
                sb.append(str[i]);
            }
        }
        sb.append(key);
        result = sb.toString();
        Log.i("签名", sb + "=======sb=============");
        return md5(result);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    public static String md5Up(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static ArrayMap<String, Object> getJsonObject(String json) {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayMap<String, Object> getJsonObjectByData(String json) {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        ArrayMap<String, Object> map2 = new ArrayMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
            map2 = getJsonObject(map.get("resultStatus").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map2;
    }

    // 解析json数组数据
    public static ArrayList<ArrayMap<String, Object>> getJsonArray(
            String jsonString) {
        ArrayList<ArrayMap<String, Object>> list = new ArrayList<ArrayMap<String, Object>>();
        try {
            list = JSON.parseObject(jsonString,
                    new TypeReference<ArrayList<ArrayMap<String, Object>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
