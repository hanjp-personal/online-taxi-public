package com.mashibing.servicemap.controller;

import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(@RequestParam String name, @RequestParam Long desc){
        return terminalService.add(name,desc);
    }
    @PostMapping("/aroundsearch")
    public ResponseResult<List<TerminalResponse>>  aroundsearch(@RequestParam String center, @RequestParam Integer radius){
        return terminalService.aroundsearch(center,radius);

    }
}