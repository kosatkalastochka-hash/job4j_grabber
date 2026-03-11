package service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stores.Store;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerManager implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerManager.class);
    private Scheduler scheduler;

    public void init() {
       try {
           scheduler = StdSchedulerFactory.getDefaultScheduler();
           scheduler.start();
       } catch (SchedulerException e) {
           LOG.error("Ошибка при инициализации планировщика", e);
       }

    }

    public void load(int period, Class<SuperJobGrab> task, HabrCareerParse parse, Store store) {
        try {
            var data = new JobDataMap();
            data.put("parse", parse);
            data.put("store", store);
            var job = newJob(task)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(period)
                    .repeatForever();

            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.error("Ошибка при загрузке планировщика", e);
        }
    }

    public void close() {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
                LOG.info("Планировщик остановлен");
            } catch (SchedulerException e) {
                LOG.error("Ошибка при остановке планировщика", e);
            }
        }
    }

}
