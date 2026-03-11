package service;

import model.Post;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import stores.Store;

import java.util.List;

public class SuperJobGrab implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var parse = (HabrCareerParse) context.getJobDetail().getJobDataMap().get("parse");
        var store = (Store) context.getMergedJobDataMap().get("store");
        List<Post> posts = parse.fetch();
        for (Post post : posts) {
            store.save(post);
        }
        }
    }

