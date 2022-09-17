package com.udacity.jdnd.course3.critter.schedule;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ActivitySchedule {
    @Id
    private long scheduleId;
    private String activity;

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public ActivitySchedule(long scheduleId, String activity) {
        this.scheduleId = scheduleId;
        this.activity = activity;
    }
}
