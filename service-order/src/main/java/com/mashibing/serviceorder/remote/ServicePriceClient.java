package com.mashibing.serviceorder.remote;

import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.PriceRuleIsNewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-price")
public interface ServicePriceClient {

    @RequestMapping(method = RequestMethod.POST,value = "price-rule/isNew-rule")
    public ResponseResult<Boolean> isNewPriceRule(@RequestBody PriceRuleIsNewRequest priceRuleIsNewRequest);
    @RequestMapping(method = RequestMethod.GET,value ="/price-rule/if-exist")
    public ResponseResult<Boolean> priceRuleIsExist(@RequestBody PriceRule priceRule);
    @RequestMapping(method = RequestMethod.POST,value = "/calculate-price")
    public ResponseResult<Double> calculatePrice(@RequestParam Integer distance, @RequestParam Integer duration, @RequestParam String cityCode, @RequestParam String vehicleType);

}
