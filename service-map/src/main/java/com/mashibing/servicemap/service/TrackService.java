package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.TrackResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.TrackClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    private TrackClient trackClient;

    public ResponseResult<TrackResponse> addTrack(String tid){
        return trackClient.addTrack(tid);
    }
}
