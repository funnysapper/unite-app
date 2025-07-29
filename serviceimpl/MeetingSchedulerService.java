package com.Unite.UniteMobileApp.serviceimpl;


import com.Unite.UniteMobileApp.miscellaneous.MeetingReminderJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingSchedulerService {

    @Autowired
    private  Scheduler scheduler;

    public void scheduleMeetingReminder(String meetingId, java.util.Date meetingTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MeetingReminderJob.class)
                .withIdentity("reminder_" + meetingId, "meetings")
                .usingJobData("meetingId", meetingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + meetingId, "meetings")
                .startAt(meetingTime)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}