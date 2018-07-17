package tl.com.tlsl.model.impl;

/**
 * Created by admin on 2018/6/5.
 */

public class SwipeImpl implements SwipeInterface{
    private SwipeInterface swipeInterface;

    public SwipeInterface getSwipeInterface() {
        return swipeInterface;
    }

    public void setSwipeInterface(SwipeInterface swipeInterface) {
        this.swipeInterface = swipeInterface;
    }

    @Override
    public void copyClick(int str) {
        swipeInterface.copyClick(str);
    }

    @Override
    public void revokeClick(int str) {
        swipeInterface.revokeClick(str);
    }

    @Override
    public void onItemClick(int str) {
        swipeInterface.onItemClick(str);
    }
}
