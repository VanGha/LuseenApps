package com.luseen.vanik.luseenapp.Recyclers.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
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

    private Context context;

    private List<LuseenPosts> luseenPosts;
    private List<LuseenNews> luseenNews;

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
    public void onBindViewHolder(final MainViewHolder holder, int position) {

        if (luseenNews == null) {

            final LinearLayout.LayoutParams layoutParamsDoubleWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            final LinearLayout.LayoutParams layoutParamsMatchWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            holder.userImage.setBackground(LoggedUser.getPhoto().getDrawable());
            holder.posterName.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterName());
            holder.posterSurname.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterSurname());
            holder.information.setText(luseenPosts.get(holder.getAdapterPosition()).getInformation());

            YoYo.with(Techniques.FlipInY).duration(1000).playOn(holder.posterName);
            YoYo.with(Techniques.FlipInX).duration(2000).playOn(holder.posterSurname);
            YoYo.with(Techniques.Landing).duration(1000).playOn(holder.information);
            YoYo.with(Techniques.FadeInDown).duration(500).playOn(holder.commentsField);

            String currentPostComments = luseenPosts.get(holder.getAdapterPosition()).getComments();

            if (!currentPostComments.isEmpty()) {

                String[] currentPostComment = currentPostComments.split(Pattern.quote("|=|s-p|-"));

                holder.commentsLoadProgress.setVisibility(View.VISIBLE);

                for (String aCurrentPostComment : currentPostComment) {

                    String[] namesAndComments = aCurrentPostComment.split(Pattern.quote("-s@|ed|"));

                    LinearLayout commentsField = new LinearLayout(context);
                    commentsField.setLayoutParams(layoutParamsMatchWrap);
                    commentsField.setOrientation(LinearLayout.HORIZONTAL);

                    TextView commenterName = new TextView(context);
                    commenterName.setLayoutParams(layoutParamsDoubleWrap);
                    commenterName.setTextColor(Color.BLACK);
                    commenterName.setGravity(Gravity.START);
                    commenterName.setText(namesAndComments[0]);

                    TextView dots = new TextView(context);
                    dots.setLayoutParams(layoutParamsDoubleWrap);
                    dots.setTextColor(Color.BLACK);
                    dots.setText("  :  ");

                    TextView comment = new TextView(context);
                    comment.setLayoutParams(layoutParamsDoubleWrap);
                    comment.setTextColor(Color.GRAY);
                    comment.setGravity(Gravity.START);
                    comment.setText(String.valueOf(namesAndComments[1]));

                    commentsField.addView(commenterName);
                    commentsField.addView(dots);
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
                        commentsField.setLayoutParams(layoutParamsMatchWrap);
                        commentsField.setOrientation(LinearLayout.HORIZONTAL);

                        TextView commenterName = new TextView(context);
                        commenterName.setLayoutParams(layoutParamsDoubleWrap);
                        commenterName.setTextColor(Color.BLACK);
                        commenterName.setGravity(Gravity.START);
                        commenterName.setText(senderName);

                        TextView dots = new TextView(context);
                        dots.setLayoutParams(layoutParamsDoubleWrap);
                        dots.setTextColor(Color.BLACK);
                        dots.setText("  :  ");

                        TextView comment = new TextView(context);
                        comment.setLayoutParams(layoutParamsDoubleWrap);
                        comment.setTextColor(Color.GRAY);
                        comment.setGravity(Gravity.START);
                        comment.setText(String.valueOf(holder.commentField.getText()));

                        commentsField.addView(commenterName);
                        commentsField.addView(dots);
                        commentsField.addView(comment);
                        holder.commentsField.addView(commentsField);

                        addCommentToServer(senderName + "-s@|ed|" + holder.commentField.getText(), holder.getAdapterPosition());

                        holder.commentField.setText("");

                    }
                }

            });

        } else {

            holder.newsInformation.setText(luseenNews.get(holder.getAdapterPosition()).getInformation());

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

        PorterShapeImageView posterImage, userImage;
        TextView posterName, posterSurname, information;
        EditText commentField;
        Button sendButton;

        TextView newsInformation;

        MainViewHolder(View itemView) {
            super(itemView);

            if (luseenNews == null) {

                posterImage = (PorterShapeImageView) itemView.findViewById(R.id.user_photo);
                userImage = (PorterShapeImageView) itemView.findViewById(R.id.sender_image);
                posterName = (TextView) itemView.findViewById(R.id.user_name);
                posterSurname = (TextView) itemView.findViewById(R.id.user_surname);
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


