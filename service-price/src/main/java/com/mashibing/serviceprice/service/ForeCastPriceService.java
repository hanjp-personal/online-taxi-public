package com.mashibing.serviceprice.service;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ForeCastPriceService {

    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult forecasrPrice(String depLongitude, String deplatitude,String destLongitude,String destlatitude){
        log.info("出发地经度： "+ depLongitude);
        log.info("出发地纬度： "+ deplatitude);
        log.info("目的地经度： "+ destLongitude);
        log.info("目的地纬度： "+ destlatitude);

        log.info("调用地图服务，查询距离和时长");
        ForeCastPriceDTO foreCastPriceDTO = new ForeCastPriceDTO();
        foreCastPriceDTO.setDepLongitude(depLongitude);
        foreCastPriceDTO.setDeplatitude(deplatitude);
        foreCastPriceDTO.setDestLongitude(destLongitude);
        foreCastPriceDTO.setDestlatitude(destlatitude);
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(foreCastPriceDTO);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();

        log.info("读取计价规则");
        Map<String,Object> priceRuleMap = new HashMap<>();
        priceRuleMap.put("city_code","110000");
        priceRuleMap.put("vehicle_type",1);
        List<PriceRule> priceRules = priceRuleMapper.selectByMap(priceRuleMap);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长、计价规则，计算预估价格");
        double price = getPrice(distance, duration, priceRule);
        ForeCastPriceResponse foreCastPriceResponse = new ForeCastPriceResponse();
        foreCastPriceResponse.setPrice(price);
        return ResponseResult.success(foreCastPriceResponse);
    }
    /**
     * 根据距离、时长和计价规则，计算最终价格
     * @param distance  距离
     * @param duration  时长
     * @param priceRule 计价规则
     * @return
     */
    private double getPrice(Integer distance, Integer duration, PriceRule priceRule){
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
