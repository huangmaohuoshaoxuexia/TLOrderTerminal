package tl.com.tlsl.model.impl;

/**
 * Created by admin on 2018/5/31.
 */

public class OrderStatusImpl implements OrderStatusInterface{
    private OrderStatusInterface orderStatusInterface;


    public OrderStatusInterface getOrderStatusInterface() {
        return orderStatusInterface;
    }

    public void setOrderStatusInterface(OrderStatusInterface orderStatusInterface) {
        this.orderStatusInterface = orderStatusInterface;
    }

    @Override
    public void getStatus(String status,int position) {
        orderStatusInterface.getStatus(status,position);
    }
}
