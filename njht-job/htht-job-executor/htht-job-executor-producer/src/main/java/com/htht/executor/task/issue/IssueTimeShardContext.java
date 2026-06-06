package com.htht.executor.task.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class IssueTimeShardContext {
    @Autowired
    private Map<String, IssueTimeShard> issueTimeShardMap;

    public IssueTimeShard getIssueShardByType(String type){
        return issueTimeShardMap.get(type);
    }

}
