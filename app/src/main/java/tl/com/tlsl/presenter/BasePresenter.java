package tl.com.tlsl.presenter;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;

import tl.com.tlsl.view.IMvpView;


/**
 * Created by admin on 2017/10/13.
 */

public abstract class BasePresenter{
    protected ArrayMap<String, Object>  mTempMap,mTempMap2,mDataMap;
    protected Context mContext;
    protected IMvpView mImvpView;
    protected ArrayList<ArrayMap<String, Object>> mTempList;
}
