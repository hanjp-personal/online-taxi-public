package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {
    @Autowired
    private TerminalClient terminalClient;

    public ResponseResult<TerminalResponse> add(String name,String desc){
        return terminalClient.createTerminal(name,desc);
    }
}
