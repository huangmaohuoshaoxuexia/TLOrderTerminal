package tl.com.tlsl.model.impl;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by admin on 2017/10/25.
 */

public interface SaleOrderInterface {
    public void getOrderList(String type,String status,String copanyName,int page);
    public void getMyList(String type,String status,String copanyName,int page);
    public void getHistoryList(String type,String status,String copanyName,int page);
    public void queryBoundCompanyList(String companyId,int page);
    public void getCompanyId();
    public void queryCompanyProdAndInventList(String product);
    public void queryDetail(String companyId);
    public void approvalList();
    public void addOrder(String companyId,String companyName,String contactName,String contactPhone,String transportType,String address,String memo,String provinceId,String provinceName,String cityId,String cityName,String countyId,String countyName,JSONArray approvalIds,String totalCount,String totalPrice,String payChannel,String payMoney,JSONArray details);
    public void getDeliveryList();
    void getOrderDetail(String orderId);
    void approve(String orderId,String content);
    void reject(String orderId,String content);
    void revoke(String saleOrderId,String createUserId,String saleOrderStatus);
}
