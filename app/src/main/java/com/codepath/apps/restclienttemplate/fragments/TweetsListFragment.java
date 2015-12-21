package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapter.TimelineListAdapter;
import com.codepath.apps.restclienttemplate.listener.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.TimelineModel;
import com.codepath.apps.restclienttemplate.models.TwitterTimelineComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.AdapterView.*;


public abstract class TweetsListFragment extends Fragment {
    private static final String TIMELINE_FRAGMENT = "TIMELINE_FRAGMENT";

    protected ArrayList<TimelineModel> timeLineList;
    protected TimelineListAdapter timelineAdapter;
    protected ListView lvTweets;
    protected SwipeRefreshLayout swipeContainer;
    protected long sinceId;
    protected long maxId;


    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate view for the fragment false not attaching to the root yet
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        //this is view or requrire one so it goes int the create view

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(timelineAdapter);
        lvTweets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TimelineModel timelineModel = timeLineList.get(position);
                itemClicked(timelineModel);
                return true;
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d(TIMELINE_FRAGMENT,"activate endless scoreller: sinceId:" + sinceId + " maxId:" + maxId );
                fetchTimeline(sinceId, maxId, false);
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
                return true;
            }
        });


        //get swipe container
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TIMELINE_FRAGMENT, "Do SWIPE_REFRESH with params: sinceId:" + sinceId);
                fetchTimeline(sinceId, 0, true);
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
            }
        });


        return v;
    }

    protected abstract void itemClicked(TimelineModel timelineModel);

    public abstract void fetchTimeline(long sinceId, long maxId, boolean b);

    //creation
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //code not required view but just
        timeLineList = new ArrayList<>();
        timelineAdapter = new TimelineListAdapter(this.getActivity(), timeLineList);
    }

    public void addAll(List<TimelineModel> tweets) {
        //add all to the timeline adapter
        timelineAdapter.addAll(tweets);
    }

    public void add(TimelineModel tweet) {
        timeLineList.add(tweet);
        sortTimeline(timeLineList);
        setSinceMaxId(timeLineList);
        timelineAdapter.notifyDataSetChanged();
        tweet.save();
    }


    public void sortTimeline(ArrayList<TimelineModel> timeLineList) {
        Comparator<? super TimelineModel> tweeterComparator = new TwitterTimelineComparator();
        Collections.sort(timeLineList, tweeterComparator);
    }

    public void setSinceMaxId(ArrayList<TimelineModel> timeLineList) {
        if(timeLineList == null || timeLineList.isEmpty()) return;
        this.maxId = timeLineList.get(timeLineList.size()-1).getTweetId() - 1;
        this.sinceId = timeLineList.get(0).getTweetId();
    }



    protected void saveToDb(ArrayList<TimelineModel> timeLineList) {
        TimelineModel.save(timeLineList);
        loadFromDb();
    }


    protected List<TimelineModel> loadFromDb() {
        List<TimelineModel> dbList = TimelineModel.get(10);
        if(dbList == null) {
            Log.d(TIMELINE_FRAGMENT, "DB list is empty");
        }
        for (int i = 0; i < dbList.size(); i++) {
            Log.d(TIMELINE_FRAGMENT, "DB tweetId:" + dbList.get(i).getTweetId() + "user:" + dbList.get(0).getUser());
        }
        return dbList;
    }
}
