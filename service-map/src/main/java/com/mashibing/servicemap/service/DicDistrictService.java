package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.dto.DicDistrict;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.mapper.DicDistrictMapper;
import com.mashibing.servicemap.remote.MapDicDistrictClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Autowired
    private MapDicDistrictClient mapDicDistrictClient;

    @Autowired
    private DicDistrictMapper dicDistrictMapper;

    public ResponseResult initDicDistrict(String keywords){

        String dicDistrictResult = mapDicDistrictClient.dicDistrict(keywords);
        ///System.out.println(dicDistrictResult);
        JSONObject dicDistrictjsonObject = JSONObject.fromObject(dicDistrictResult);
        int status = dicDistrictjsonObject.getInt(AmapConfigComstants.STATUS);
        if (status != 1){
            return ResponseResult.fail(CommonStatusEnum.MAP_DICDISTRICT_ERROR.getCode(),CommonStatusEnum.MAP_DICDISTRICT_ERROR.getValue());
        }
        JSONArray countryjsonArray = dicDistrictjsonObject.getJSONArray(AmapConfigComstants.DISTRICTS);
        for (int i = 0; i < countryjsonArray.size(); i++){
            JSONObject countryjsonObject = countryjsonArray.getJSONObject(i);
            String addressCode = countryjsonObject.getString(AmapConfigComstants.ADCODE);
            String addressName = countryjsonObject.getString(AmapConfigComstants.NAME);
            String parentAddressCode = "0";
            String level = countryjsonObject.getString(AmapConfigComstants.LEVEL);
            insertDicDistrict(addressCode,addressName,parentAddressCode,level);

            JSONArray provincejsonArray = countryjsonObject.getJSONArray(AmapConfigComstants.DISTRICTS);
            for (int j = 0; j < provincejsonArray.size(); j++){
                JSONObject provincejsonObject = provincejsonArray.getJSONObject(j);
                String provinceaddressCode = provincejsonObject.getString(AmapConfigComstants.ADCODE);
                String provinceaddressName = provincejsonObject.getString(AmapConfigComstants.NAME);
                String provinceparentAddressCode = parentAddressCode;
                String provincelevel = provincejsonObject.getString(AmapConfigComstants.LEVEL);
                insertDicDistrict(provinceaddressCode,provinceaddressName,provinceparentAddressCode,provincelevel);

                JSONArray cityjsonArray = provincejsonObject.getJSONArray(AmapConfigComstants.DISTRICTS);
                for (int k = 0; k < cityjsonArray.size(); k++){
                    JSONObject cityjsonObject = cityjsonArray.getJSONObject(k);
                    String cityaddressCode = cityjsonObject.getString(AmapConfigComstants.ADCODE);
                    String cityaddressName = cityjsonObject.getString(AmapConfigComstants.NAME);
                    String cityparentAddressCode = provinceparentAddressCode;
                    String citylevel = cityjsonObject.getString(AmapConfigComstants.LEVEL);
                    insertDicDistrict(cityaddressCode,cityaddressName,cityparentAddressCode,citylevel);



                    JSONArray districtjsonArray = cityjsonObject.getJSONArray(AmapConfigComstants.DISTRICTS);
                    for (int p = 0; p < districtjsonArray.size(); p++){
                        JSONObject districtjsonObject = districtjsonArray.getJSONObject(p);
                        String districtaddressCode = districtjsonObject.getString(AmapConfigComstants.ADCODE);
                        String districtaddressName = districtjsonObject.getString(AmapConfigComstants.NAME);
                        String districtparentAddressCode = cityparentAddressCode;
                        String districtlevel = districtjsonObject.getString(AmapConfigComstants.LEVEL);
                        if (districtlevel.equals(AmapConfigComstants.STREET)){
                            continue;
                        }
                        insertDicDistrict(districtaddressCode,districtaddressName,districtparentAddressCode,districtlevel);
                    }
                }
            }
        }

        return ResponseResult.success("");
    }

    public void insertDicDistrict(String addressCode,String addressName,String parentAddressCode,String level){
        //数据库对象
        DicDistrict dicDistrict = new DicDistrict();
        dicDistrict.setAddressCode(addressCode);
        dicDistrict.setAddressName(addressName);
        int levelInt = generatorLevel(level);
        dicDistrict.setLevel(levelInt);

        dicDistrict.setParentAddressCode(parentAddressCode);
        //存入数据库
        dicDistrictMapper.insert(dicDistrict);
    }

    public int generatorLevel(String level){
        int levelInt = 0;
        if (level.trim().equals("country")){
            levelInt = 0;
        } else if (level.trim().equals("province")) {
            levelInt = 1;
        }else if (level.trim().equals("city")) {
            levelInt = 2;
        }else if (level.trim().equals("district")) {
            levelInt = 3;
        }
        return levelInt;
    }
}
