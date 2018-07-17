package tl.com.tlsl.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import tl.com.tlsl.model.entity.CityModel;
import tl.com.tlsl.model.entity.DistrictModel;
import tl.com.tlsl.model.entity.ProvinceModel;
import tl.com.tlsl.service.XmlParserHandler;

/**
 * Created by admin on 2018/5/25.
 */

public class AddressUtil{

    /**
     * 所有省
     */
    protected static String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected static Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected static String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected static String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected static String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected static String mCurrentZipCode ="";

    /**
     * 解析省市区的XML数据
     */
    static List<ProvinceModel> provinceList = null;
    public static void initProvinceDatas(Context context)
    {
//        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static String getProvinceCode(String name){
        if (provinceList!= null && !provinceList.isEmpty()) {
            for(int i=0;i<provinceList.size();i++){
                if(name.equals(provinceList.get(i).getName())){
                    return provinceList.get(i).getId();
                }
            }
        }
        return null;
    }
    public static String getCityCode(String name){
        if (provinceList!= null && !provinceList.isEmpty()) {
            for(int i=0;i<provinceList.size();i++){
                List<CityModel> cityList = provinceList.get(i).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    for(int j=0;j<cityList.size();j++){
                        if(name.equals(cityList.get(j).getName())){
                            return cityList.get(j).getId();
                        }
                    }
                }
            }
        }
        return null;
    }
    public static String getAreaCode(String name){
        if (provinceList!= null && !provinceList.isEmpty()) {
            for(int i=0;i<provinceList.size();i++){
                List<CityModel> cityList = provinceList.get(i).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    for(int j=0;j<cityList.size();j++){
                        List<DistrictModel> districtModelList = cityList.get(j).getDistrictList();
                        for(int k=0;k<districtModelList.size();k++){
                           if(name.equals(districtModelList.get(k).getName())){
                               Log.i("tl.com.tlsl>>获取的数据",districtModelList.get(k).getName()+"======="+districtModelList.get(k).getZipcode());
                               return districtModelList.get(k).getZipcode();
                           }
                        }
                    }
                }
            }
        }
        return null;
    }
}
