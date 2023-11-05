package com.mashibing.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
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
        priceRule.setFareType(cityCode +"$"+ vehicleType);

        //添加版本号后，city_code和vehicle_type，无法保证唯一性，需进行排序，选择最大的版本号记录
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> list = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if (list.size() > 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EXIST.getCode(),CommonStatusEnum.PRICE_RULE_EXIST.getValue());
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success("");

    }

    public ResponseResult edit(PriceRule priceRule){
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        priceRule.setFareType(cityCode +"$"+ vehicleType);

        //添加版本号后，city_code和vehicle_type，无法保证唯一性，需进行排序，选择最大的版本号记录
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> list = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if (list.size() > 0){
            PriceRule lastPriceRule = list.get(0);
            Double startFare = lastPriceRule.getStartFare();
            Integer startMile = lastPriceRule.getStartMile();
            Double unitPricePerMile = lastPriceRule.getUnitPricePerMile();
            Double unitPricePerMinute = lastPriceRule.getUnitPricePerMinute();
            if(startFare.doubleValue() == priceRule.getStartFare().doubleValue()
            &&startMile.intValue() == priceRule.getStartMile().intValue()
            &&unitPricePerMile.doubleValue() == priceRule.getUnitPricePerMile().doubleValue()
            &&unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute().doubleValue()){
                return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NO_EDIT.getCode(),CommonStatusEnum.PRICE_RULE_NO_EDIT.getValue());
            }
            fareVersion = lastPriceRule.getFareVersion();

        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success("");
    }

    public ResponseResult getNewestPriceRule(String fareType){
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fare_type",fareType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() > 0){
            return ResponseResult.success(priceRules.get(0));
        }else {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }

    }

    public ResponseResult<Boolean> isNewPriceRule(String fareType,Integer fareVersion){
        ResponseResult<PriceRule> newestPriceRule = getNewestPriceRule(fareType);
        if (newestPriceRule.getCode() == CommonStatusEnum.PRICE_RULE_EMPTY.getCode()){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }

        PriceRule priceRule = newestPriceRule.getData();
        Integer fareVersionDB = priceRule.getFareVersion();
        if(fareVersionDB > fareVersion){
            return ResponseResult.success(false);
        }else {
            return ResponseResult.success(true);
        }
    }

    public ResponseResult<Boolean> priceRuleExist(PriceRule priceRule){
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() > 0){
            return ResponseResult.success(true);
        }else {
            return ResponseResult.success(false);
        }

    }
}
