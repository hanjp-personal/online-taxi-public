package com.mashibing.serviceorder.remote;

import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-price")
public interface ServicePriceClient {

    @RequestMapping(method = RequestMethod.GET,value = "price-rule/isNew-rule")
    public ResponseResult<Boolean> isNewPriceRule(@RequestParam String fareType,@RequestParam Integer fareVersion);
    @RequestMapping(method = RequestMethod.GET,value ="/price-rule/if-exist")
    public ResponseResult<Boolean> priceRuleIsExist(@RequestBody PriceRule priceRule);

}
