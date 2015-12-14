package com.codepath.apps.restclienttemplate.models;

import java.util.Comparator;

/**
 * Created by mcvjetinovic on 12/13/15.
 */
public class TwitterTimelineComparator implements Comparator<TimelineModel> {


    public int compareByDate(TimelineModel lhs, TimelineModel rhs) {
        if(lhs.getTweetTime().equals(rhs.getRelativeTweetTime())) return 0;
        if(TwitterUtil.isBefore(lhs.getTweetTime(), rhs.getTweetTime())) return 1;
        return -1;
    }

    @Override
    public int compare(TimelineModel lhs, TimelineModel rhs) {
        if(lhs.getTweetId()<rhs.getTweetId()) return 1;
        if(lhs.getTweetId()>rhs.getTweetId()) return -1;
        return 0;
    }
}
