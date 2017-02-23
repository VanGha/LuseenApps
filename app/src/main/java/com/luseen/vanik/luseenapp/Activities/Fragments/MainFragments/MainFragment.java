package com.luseen.vanik.luseenapp.Activities.Fragments.MainFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;

import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Parse.LuseenNews;
import com.luseen.vanik.luseenapp.Parse.LuseenPostComment;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.luseen.vanik.luseenapp.R;
import com.luseen.vanik.luseenapp.Recyclers.Adapters.MainRecyclerAdapter;
import com.mikepenz.itemanimators.DefaultAnimator;
import com.mikepenz.itemanimators.ScaleUpAnimator;
import com.mikepenz.itemanimators.SlideDownAlphaAnimator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragment extends Fragment {

    static Context context;
    ProgressBar loadingProgress;

    static RecyclerView mainElementsRecycler;
    static MainRecyclerAdapter recyclerAdapter;

    public static MainFragment newInstance(String menu, String posterEmail) {

        Bundle args = new Bundle();
        args.putString("menu", menu);
        args.putString("posterEmail", posterEmail);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        mainElementsRecycler = (RecyclerView) view.findViewById(R.id.main_elements_recycler);
        mainElementsRecycler.setItemAnimator(new DefaultItemAnimator());
        loadingProgress = (ProgressBar) view.findViewById(R.id.load_progress);
        loadingProgress.setVisibility(View.VISIBLE);

        String menu = getArguments().getString("menu");

        if (InternetConnection.hasInternetConnection(getContext())) {

            if (menu == null) {
                loadingProgress.setVisibility(View.GONE);
                return;
            } else if (menu.equals(AppConstants.TAG_MENU_NEWS)) {
                loadNews();
            } else if (menu.equals(AppConstants.TAG_MENU_PUBLICATIONS)) {
                loadPublications();
            } else if (menu.equals(AppConstants.TAG_MENU_MY_PUBLICATIONS)) {
                loadMyPublications();
            }

        }
    }

    private void loadNews() {

        if (InternetConnection.hasInternetConnection(getContext())) {

            ParseQuery<LuseenNews> luseenPostParseQuery = ParseQuery.getQuery(LuseenNews.class);
            luseenPostParseQuery.findInBackground(new FindCallback<LuseenNews>() {
                @Override
                public void done(List<LuseenNews> news, ParseException e) {

                    if (e == null) {

                        loadingProgress.setVisibility(View.GONE);

                        recyclerAdapter = new MainRecyclerAdapter(getContext(), null, news, null);

                        mainElementsRecycler.setAdapter(recyclerAdapter);
                        mainElementsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL, false));

                    }
                }
            });
        }
    }

    private void loadPublications() {

        if (InternetConnection.hasInternetConnection(getContext())) {

            ParseQuery<LuseenPosts> luseenPostParseQuery = ParseQuery.getQuery(LuseenPosts.class);
            luseenPostParseQuery.findInBackground(new FindCallback<LuseenPosts>() {
                @Override
                public void done(final List<LuseenPosts> posts, ParseException e) {

                    if (e == null) {

                        loadingProgress.setVisibility(View.GONE);

                        ParseQuery<LuseenPostComment> luseenPostCommentQuery = ParseQuery.getQuery(LuseenPostComment.class);
                        luseenPostCommentQuery.findInBackground(new FindCallback<LuseenPostComment>() {
                            @Override
                            public void done(List<LuseenPostComment> postsComments, ParseException e) {

                                if (e == null) {

                                    Collections.reverse(posts);

                                    recyclerAdapter = new MainRecyclerAdapter(getContext(), posts, null, postsComments);
                                    mainElementsRecycler.setAdapter(recyclerAdapter);
                                    mainElementsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                                            LinearLayoutManager.VERTICAL, false));

                                }

                            }
                        });

                    }
                }
            });
        }
    }

    private void loadMyPublications() {

        final String posterEmail = getArguments().getString("posterEmail");

        if (InternetConnection.hasInternetConnection(getContext())) {

            ParseQuery<LuseenPosts> luseenPostParseQuery = ParseQuery.getQuery(LuseenPosts.class);
            luseenPostParseQuery.findInBackground(new FindCallback<LuseenPosts>() {
                @Override
                public void done(final List<LuseenPosts> posts, ParseException e) {

                    if (e == null) {

                        loadingProgress.setVisibility(View.GONE);

                        final List<LuseenPosts> currentUserPosts = new ArrayList<>();

                        for (int i = 0; i < posts.size(); i++) {

                            if (posts.get(i).getPosterEmail().equals(posterEmail))
                                currentUserPosts.add(posts.get(i));

                        }

                        ParseQuery<LuseenPostComment> luseenPostCommentQuery = ParseQuery.getQuery(LuseenPostComment.class);
                        luseenPostCommentQuery.findInBackground(new FindCallback<LuseenPostComment>() {
                            @Override
                            public void done(List<LuseenPostComment> postsComments, ParseException e) {

                                if (e == null) {

                                    Collections.reverse(currentUserPosts);

                                    recyclerAdapter = new MainRecyclerAdapter(getContext(), currentUserPosts, null, postsComments);
                                    mainElementsRecycler.setAdapter(recyclerAdapter);
                                    mainElementsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                                            LinearLayoutManager.VERTICAL, false));

                                }

                            }
                        });

                    }
                }
            });
        }
    }

    public static void updateRecycler(List<LuseenPosts> posts) {

        Collections.reverse(posts);

        recyclerAdapter.setLuseenPosts(posts);
        recyclerAdapter.notifyDataSetChanged();
        mainElementsRecycler.scrollToPosition(0);

    }

}
