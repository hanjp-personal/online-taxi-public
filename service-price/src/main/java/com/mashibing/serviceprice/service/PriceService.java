package com.mashibing.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.Response.DirectionResponse;
import com.mashibing.internalcommon.Response.ForeCastPriceResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import com.mashibing.internalcommon.util.BigDecimalUtils;
import com.mashibing.serviceprice.mapper.PriceRuleMapper;
import com.mashibing.serviceprice.remote.ServiceMapClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PriceService {

    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private PriceRuleMapper priceRuleMapper;

    /**
     * 计算预估价格
     * @param depLongitude
     * @param deplatitude
     * @param destLongitude
     * @param destlatitude
     * @param cityCode
     * @param vehicleType
     * @return
     */
    public ResponseResult forecasrPrice(String depLongitude, String deplatitude, String destLongitude, String destlatitude, String cityCode, String vehicleType){
        log.info("出发地经度： "+ depLongitude);
        log.info("出发地纬度： "+ deplatitude);
        log.info("目的地经度： "+ destLongitude);
        log.info("目的地纬度： "+ destlatitude);
        log.info("城市代码："+ cityCode);
        log.info("车辆类型："+ vehicleType);

        log.info("调用地图服务，查询距离和时长");
        ForeCastPriceDTO foreCastPriceDTO = new ForeCastPriceDTO();
        foreCastPriceDTO.setDepLongitude(depLongitude);
        foreCastPriceDTO.setDeplatitude(deplatitude);
        foreCastPriceDTO.setDestLongitude(destLongitude);
        foreCastPriceDTO.setDestlatitude(destlatitude);
        foreCastPriceDTO.setCityCode(cityCode);
        foreCastPriceDTO.setVehicleType(vehicleType);
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(foreCastPriceDTO);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();

        log.info("读取计价规则");
        //考虑到计价规则会随时进行改变，因此需要选择最近一次修改的计价规则，故需要将查询的计价规则进行排序
//        Map<String,Object> priceRuleMap = new HashMap<>();
//        priceRuleMap.put("city_code",cityCode);
//        priceRuleMap.put("vehicle_type",vehicleType);
//        List<PriceRule> priceRules = priceRuleMapper.selectByMap(priceRuleMap);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules= priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长、计价规则，计算预估价格");
        double price = getPrice(distance, duration, priceRule);
        ForeCastPriceResponse foreCastPriceResponse = new ForeCastPriceResponse();
        foreCastPriceResponse.setPrice(price);
        foreCastPriceResponse.setCityCode(cityCode);
        foreCastPriceResponse.setVehicleType(vehicleType);
        foreCastPriceResponse.setFareType(priceRule.getFareType());
        foreCastPriceResponse.setFareVersion(priceRule.getFareVersion());
        return ResponseResult.success(foreCastPriceResponse);
    }

    /**
     * 计算实际价格
     * @param distance
     * @param duration
     * @param cityCode
     * @param vehicleType
     * @return
     */
    public ResponseResult calculatePrice(Integer distance, Integer duration,String cityCode,String vehicleType){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules= priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }

        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长、计价规则，计算实际价格");
        double price = getPrice(distance, duration, priceRule);
        return ResponseResult.success(price);
    }
    /**
     * 根据距离、时长和计价规则，计算最终价格
     * @param distance  距离
     * @param duration  时长
     * @param priceRule 计价规则
     * @return
     */
    public double getPrice(Integer distance, Integer duration, PriceRule priceRule){
        double price = 0;
        //起步价
        double startFare = priceRule.getStartFare();
        price = BigDecimalUtils.add(price,startFare);
        //里程费 单位：m
        double distanceMile = BigDecimalUtils.divide(distance, 1000);
        // 起步里程
        int startMile = priceRule.getStartMile();
        //计价里程
        double distancesSubstract = BigDecimalUtils.substract(distanceMile, startMile);
        double mile = distancesSubstract < 0 ? 0 : distancesSubstract;
        //计程单价
        double unitPricePerMile = priceRule.getUnitPricePerMile();
        //里程价格
        double mileFare = BigDecimalUtils.multiply(mile, unitPricePerMile);
        price = BigDecimalUtils.add(price,mileFare);
        //时长费
        //时长 单位：分钟
        double timeMinute = BigDecimalUtils.divide(duration, 60);
        //计时单价
        double unitPricePerMinute = priceRule.getUnitPricePerMinute();
        //时长费用
        double timeFare = BigDecimalUtils.multiply(timeMinute, unitPricePerMinute);
        price = BigDecimalUtils.add(price, timeFare);
        //设置精度
        BigDecimal priceDecimal = BigDecimal.valueOf(price);
        priceDecimal = priceDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return priceDecimal.doubleValue();
    }
}
