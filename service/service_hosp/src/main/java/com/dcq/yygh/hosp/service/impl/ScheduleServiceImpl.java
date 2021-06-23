package com.dcq.yygh.hosp.service.impl;

import ch.qos.logback.core.util.AggregationType;
import com.alibaba.fastjson.JSONObject;
import com.dcq.yygh.hosp.repository.ScheduleRepository;
import com.dcq.yygh.hosp.service.DepartmentService;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.hosp.service.ScheduleService;
import com.dcq.yygh.model.hosp.Schedule;
import com.dcq.yygh.vo.hosp.BookingScheduleRuleVo;
import com.dcq.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);

        Schedule scheduleExist = scheduleRepository.
                getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());

        if(schedule != null){
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
        else{
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);

        //创建page对象、example对象
        Pageable pageable = PageRequest.of(page-1,limit);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule,exampleMatcher);

        Page<Schedule> all = scheduleRepository.findAll(example,pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if (schedule != null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(int page, int limit, String hoscode, String depcode) {
        //根据医院编号与科室编号查询排班信息
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        //根据工作日期workData进行分组
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),                    //匹配条件
                Aggregation.group("workDate")          //根据workData字段分组
                .first("workDate").as("workDate")
                //统计挂号数量
                .count().as("docCount")
                .sum("reservedNumber").as("reservedNumber")
                .sum("availableNumber").as("availableNumber"),
                //根据workDate排序
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                //分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)

        );

        //调用方法执行
        AggregationResults<BookingScheduleRuleVo> aggregationResults =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);

        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregationResults.getMappedResults();

        Aggregation totalAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );

        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAggregation, Schedule.class, BookingScheduleRuleVo.class);

        int total = totalAggResults.getMappedResults().size();

        //日期2星期
        for(BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList){
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        result.put("total",total);
        String hosname = hosptialService.getHospName(hoscode);
        Map<String,Object> baseMap = new HashMap<>();
        baseMap.put("hosname",hosname);
        result.put("baseMap",baseMap);
        return result;
    }

    @Override
    public List<Schedule> getDetialSchedule(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.
                findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate).toDate());
        scheduleList.stream().forEach(item ->{
            this.pakeageSchedule(item);
        });
        return scheduleList;
    }

    private void pakeageSchedule(Schedule item) {
        Map<String, Object> param = item.getParam();
        param.put("hosname",hosptialService.getHospName(item.getHoscode()));
        param.put("depname",departmentService.getDepName(item.getHoscode(),item.getDepcode()));
        param.put("dayOfWeek",this.getDayOfWeek(new DateTime(item.getWorkDate())));
    }


    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
