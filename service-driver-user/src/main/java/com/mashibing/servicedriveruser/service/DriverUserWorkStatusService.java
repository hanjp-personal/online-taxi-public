package com.mashibing.servicedriveruser.service;

import com.mashibing.internalcommon.dto.DriverUserWorkStatus;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hanjp
 * @since 2023-10-30
 */
@Service
public class DriverUserWorkStatusService {
    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult changeWorkStatus(Long driveId,Integer workStatus){
        LocalDateTime now = LocalDateTime.now();
        Map<String,Object> map = new HashMap<>();
        map.put("driver_id",driveId);
        List<DriverUserWorkStatus> driverUserWorkStatuses = driverUserWorkStatusMapper.selectByMap(map);
        DriverUserWorkStatus driverUserWorkStatusDb = driverUserWorkStatuses.get(0);

        driverUserWorkStatusDb.setWorkStatus(workStatus);
        driverUserWorkStatusDb.setGmtCreate(now);
        driverUserWorkStatusDb.setGmtModified(now);
        driverUserWorkStatusMapper.updateById(driverUserWorkStatusDb);

        return ResponseResult.success("");
    }


}
