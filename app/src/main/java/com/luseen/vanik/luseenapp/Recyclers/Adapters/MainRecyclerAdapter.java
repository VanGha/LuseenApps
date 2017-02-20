package com.luseen.vanik.luseenapp.Recyclers.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Parse.LuseenNews;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.luseen.vanik.luseenapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;
import java.util.regex.Pattern;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    Context context;

    List<LuseenPosts> luseenPosts;
    List<LuseenNews> luseenNews;

    public MainRecyclerAdapter(Context context, List<LuseenPosts> luseenPosts, List<LuseenNews> luseenNews) {
        this.context = context;
        this.luseenPosts = luseenPosts;
        this.luseenNews = luseenNews;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (luseenNews == null) {
            return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_item,
                    parent, false));
        }

        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {

        if (luseenNews == null) {

            final LinearLayout.LayoutParams layoutParamsForComments = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            final LinearLayout.LayoutParams layoutParamsForLiner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            holder.userImage.setImageDrawable(LoggedUser.getPhoto().getDrawable());
            holder.posterName.setText(luseenPosts.get(position).getPosterName());
            holder.information.setText(luseenPosts.get(position).getInformation());

            String currentPostComments = luseenPosts.get(position).getComments();

            if (!currentPostComments.isEmpty()) {

                String[] currentPostComment = currentPostComments.split(Pattern.quote("|=|s-p|-"));

                holder.commentsLoadProgress.setVisibility(View.VISIBLE);

                for (String aCurrentPostComment : currentPostComment) {

                    String[] namesAndComments = aCurrentPostComment.split(Pattern.quote("-s@|ed|"));

                    LinearLayout commentsField = new LinearLayout(context);
                    commentsField.setLayoutParams(layoutParamsForLiner);
                    commentsField.setOrientation(LinearLayout.HORIZONTAL);

                    TextView commenterName = new TextView(context);
                    commenterName.setLayoutParams(layoutParamsForComments);
                    commenterName.setTextColor(Color.BLACK);
                    commenterName.setText(namesAndComments[0]);

                    TextView comment = new TextView(context);
                    comment.setLayoutParams(layoutParamsForComments);
                    comment.setTextColor(Color.GRAY);
                    comment.setText(namesAndComments[1]);

                    commentsField.addView(commenterName);
                    commentsField.addView(comment);
                    holder.commentsField.addView(commentsField);


                }

                holder.commentsLoadProgress.setVisibility(View.GONE);

            }

            holder.sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!holder.commentField.getText().toString().isEmpty() &&
                            !holder.commentField.getText().toString().trim().isEmpty()) {

                        String senderName = LoggedUser.getName() +
                                " " + LoggedUser.getSurName();

                        LinearLayout commentsField = new LinearLayout(context);
                        commentsField.setLayoutParams(layoutParamsForLiner);
                        commentsField.setOrientation(LinearLayout.HORIZONTAL);

                        TextView commenterName = new TextView(context);
                        commenterName.setLayoutParams(layoutParamsForComments);
                        commenterName.setTextColor(Color.BLACK);
                        commenterName.setText(senderName);

                        TextView comment = new TextView(context);
                        comment.setLayoutParams(layoutParamsForComments);
                        comment.setTextColor(Color.GRAY);
                        comment.setText(String.valueOf(holder.commentField.getText()));

                        commentsField.addView(commenterName);
                        commentsField.addView(comment);
                        holder.commentsField.addView(commentsField);

                        addCommentToServer(senderName + "-s@|ed|" + holder.commentField.getText(), position);

                        holder.commentField.setText("");

                    }
                }

            });

        } else {

            holder.newsInformation.setText(luseenNews.get(position).getInformation());

        }

    }

    private void addCommentToServer(final String comment, final int postPosition) {

        if (InternetConnection.hasInternetConnection(context)) {

            ParseQuery<LuseenPosts> postsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
            postsParseQuery.getInBackground(luseenPosts.get(postPosition).getObjectId(), new GetCallback<LuseenPosts>() {
                @Override
                public void done(LuseenPosts post, ParseException e) {

                    if (e == null) {

                        post.put("Comments", String.valueOf(luseenPosts.get(postPosition).getComments() + comment + "|=|s-p|-"));
                        post.saveInBackground();

                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if (luseenNews == null) {
            return luseenPosts.size();
        }

        return luseenNews.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        LinearLayout commentsField;
        ProgressBar commentsLoadProgress;

        CircularImageView posterImage, userImage;
        TextView posterName, information;
        EditText commentField;
        Button sendButton;

        TextView newsInformation;

        MainViewHolder(View itemView) {
            super(itemView);

            if (luseenNews == null) {

                posterImage = (CircularImageView) itemView.findViewById(R.id.user_photo);
                userImage = (CircularImageView) itemView.findViewById(R.id.sender_image);
                posterName = (TextView) itemView.findViewById(R.id.user_name);
                information = (TextView) itemView.findViewById(R.id.info_text);
                commentsField = (LinearLayout) itemView.findViewById(R.id.comments_field);
                commentsLoadProgress = (ProgressBar) itemView.findViewById(R.id.load_progress);
                commentField = (EditText) itemView.findViewById(R.id.comment_field);
                sendButton = (Button) itemView.findViewById(R.id.comment_send_button);

            } else {

                newsInformation = (TextView) itemView.findViewById(R.id.news_information);

            }

        }

    }

}


