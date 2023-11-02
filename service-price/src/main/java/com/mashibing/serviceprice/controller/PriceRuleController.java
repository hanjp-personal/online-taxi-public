package com.mashibing.serviceprice.controller;


import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
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
    @GetMapping("/isNew-rule")
    public ResponseResult<Boolean> isNewPriceRule(@RequestParam String fareType,@RequestParam Integer fareVersion){
         return priceRuleService.isNewPriceRule(fareType,fareVersion);
    }
}