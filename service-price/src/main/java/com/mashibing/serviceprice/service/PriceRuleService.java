package com.mashibing.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.serviceprice.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hanjp
 * @since 2023-11-02
 */
@Service
public class PriceRuleService {

    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult addPriceRule(PriceRule priceRule){

        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        priceRule.setFareType(cityCode + vehicleType);

        //添加版本号后，city_code和vehicle_type，无法保证唯一性，需进行排序，选择最大的版本号记录
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> list = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if (list.size() > 0){
            fareVersion = list.get(0).getFareVersion();
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success("");

    }

}
