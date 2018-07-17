package tl.com.tlsl.view;

import java.util.HashMap;

/**
 * Created by admin on 2017/10/13.
 */

public interface IMvpView {

    void showLoading();

    void hideLoading();
    void onSuccess(String type, Object object);
    void onFail(String str);
}
