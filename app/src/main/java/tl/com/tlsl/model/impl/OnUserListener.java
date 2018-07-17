package tl.com.tlsl.model.impl;

import tl.com.tlsl.model.entity.User;

/**
 * Created by admin on 2017/10/23.
 */

public interface OnUserListener {
    void loginSuccess(User user);

    void loginFailed();
}
