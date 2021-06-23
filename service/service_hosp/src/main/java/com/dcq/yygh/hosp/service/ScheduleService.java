package com.dcq.yygh.hosp.service;

import com.dcq.yygh.model.hosp.Schedule;
import com.dcq.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> parameterMap);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(int page, int limit, String hoscode, String depcode);

    List<Schedule> getDetialSchedule(String hoscode, String depcode, String workDate);
}
