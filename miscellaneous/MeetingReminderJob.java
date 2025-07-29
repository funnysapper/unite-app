package com.Unite.UniteMobileApp.miscellaneous;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class MeetingReminderJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String meetingId = context.getJobDetail().getJobDataMap().getString("meetingId");
        // Your logic here (send notification, update status, etc.)
        System.out.println("Reminder: Meeting " + meetingId + " is starting soon!");
    }
}