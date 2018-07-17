package tl.com.tlsl.model.impl;

/**
 * Created by admin on 2017/10/23.
 */

public interface UserInterface {
    public void login(String username, String password);
    public void getCode(String tel);
    public void verificationCode(String tel,String code);
    public void setPwd(String tel,String pwd);
}
