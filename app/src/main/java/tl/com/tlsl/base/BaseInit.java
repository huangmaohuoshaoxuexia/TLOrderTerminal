package tl.com.tlsl.base;

/**
 * Created by admin on 2017/10/13.
 */

public interface BaseInit {
    /**
     * 初始化布局组件
     */
    public void initViews();

    /**
     * 增加按钮点击事件
     */
    void initListeners();

    /**
     * 初始化数据
     */
    public void initData();

    /**
     * 初始化公共头部
     */
    public void setHeader();
    public void logcat(String msg);
}
