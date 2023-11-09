package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.Response.TrsearchResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {
    @Autowired
    private TerminalClient terminalClient;

    /**
     * 添加终端
     * @param name
     * @param desc
     * @return
     */
    public ResponseResult<TerminalResponse> add(String name,Long desc){
        return terminalClient.createTerminal(name,desc);
    }

    /**
     * 终端搜索
     * @param center
     * @param radius
     * @return
     */
    public ResponseResult<List<TerminalResponse>>  aroundsearch(String center, Integer radius){
        return terminalClient.aroundsearch(center,radius);

    }
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime, Long endtime){
        return terminalClient.trsearch(tid,starttime,endtime);
    }
}
