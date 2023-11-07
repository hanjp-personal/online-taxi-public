package com.mashibing.serviceprice.controller;


import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.PriceRuleIsNewRequest;
import com.mashibing.serviceprice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hanjp
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    private PriceRuleService priceRuleService;
    @PostMapping("/add")
    public ResponseResult addPriceRule(@RequestBody PriceRule priceRule){
        return priceRuleService.addPriceRule(priceRule);

    }
    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule){
        return priceRuleService.edit(priceRule);
    }

    @GetMapping("/get-newest-rule")
    public ResponseResult getNewestPriceRule(@RequestParam String fareType){
        return priceRuleService.getNewestPriceRule(fareType);
    }
    @PostMapping("/isNew-rule")
    public ResponseResult<Boolean> isNewPriceRule(@RequestBody PriceRuleIsNewRequest priceRuleIsNewRequest){
         return priceRuleService.isNewPriceRule(priceRuleIsNewRequest.getFareType(),priceRuleIsNewRequest.getFareVersion());
    }
    @GetMapping("/if-exist")
    public ResponseResult<Boolean> priceRuleExist(@RequestBody PriceRule priceRule){
        return priceRuleService.priceRuleExist(priceRule);
    }
}
