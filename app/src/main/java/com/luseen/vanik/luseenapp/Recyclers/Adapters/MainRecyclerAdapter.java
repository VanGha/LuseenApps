package com.luseen.vanik.luseenapp.Recyclers.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.luseen.vanik.luseenapp.Parse.LuseenPostComment;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

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

            holder.userImage.setBackground(LoggedUser.getPhoto().getDrawable());
            holder.posterName.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterName());
            holder.posterSurname.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterSurname());
            holder.information.setText(luseenPosts.get(holder.getAdapterPosition()).getInformation());

            holder.commentsLoadProgress.setVisibility(View.VISIBLE);

            if (luseenPosts.get(holder.getAdapterPosition()).hasComments()) {

                if (InternetConnection.hasInternetConnection(context)) {

                    ParseQuery<LuseenPostComment> luseenPostCommentQuery = ParseQuery.getQuery(LuseenPostComment.class);
                    luseenPostCommentQuery.findInBackground(new FindCallback<LuseenPostComment>() {
                        @Override
                        public void done(List<LuseenPostComment> postsComments, ParseException e) {

                            if (e == null) {

                                for (int i = 0; i < postsComments.size(); i++) {

                                    if (postsComments.get(i).getPostId().equals(luseenPosts.get(holder.getAdapterPosition()).getObjectId())) {

                                        holder.commentsField.addView(createCommentView(
                                                postsComments.get(i).getSenderName(),
                                                postsComments.get(i).getSenderSurname(),
                                                postsComments.get(i).getComment()));

                                    }

                                }
                            }

                        }
                    });
                }

            } else {

                holder.commentsField.addView(createEmptyCommentView());

            }

            YoYo.with(Techniques.FlipInY).duration(1000).playOn(holder.posterName);
            YoYo.with(Techniques.FlipInX).duration(2000).playOn(holder.posterSurname);
            YoYo.with(Techniques.Landing).duration(1000).playOn(holder.information);
            YoYo.with(Techniques.FadeInDown).duration(500).playOn(holder.commentsField);

            holder.commentsLoadProgress.setVisibility(View.GONE);

            holder.sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!holder.commentField.getText().toString().isEmpty() &&
                            !holder.commentField.getText().toString().trim().isEmpty()) {

                        addCommentToServer(LoggedUser.getName(), LoggedUser.getSurName(),
                                holder.commentField.getText().toString(),
                                luseenPosts.get(holder.getAdapterPosition()).getObjectId());

                        holder.commentField.setText("");

                    }
                }

            });

        } else {
            holder.newsInformation.setText(luseenNews.get(holder.getAdapterPosition()).getInformation());
        }

    }

    private View createCommentView(String senderName, String senderSurname, String comment) {

        LinearLayout.LayoutParams doubleWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrapAndMatch = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout commentBody = new LinearLayout(context);
        commentBody.setLayoutParams(wrapAndMatch);

        TextView senderNameText = new TextView(context);
        senderNameText.setLayoutParams(doubleWrap);
        senderNameText.setGravity(Gravity.START);
        senderNameText.setTextColor(Color.BLACK);
        senderNameText.setText(senderName);

        TextView senderSurnameText = new TextView(context);
        senderSurnameText.setLayoutParams(doubleWrap);
        senderSurnameText.setGravity(Gravity.START);
        senderSurnameText.setTextColor(Color.BLACK);
        senderSurnameText.setText(senderSurname);

        TextView dots = new TextView(context);
        dots.setLayoutParams(doubleWrap);
        dots.setTextColor(Color.BLACK);
        dots.setGravity(Gravity.CENTER);
        dots.setText("  : ");

        TextView commentText = new TextView(context);
        commentText.setLayoutParams(doubleWrap);
        commentText.setGravity(Gravity.START);
        commentText.setText(comment);

        commentBody.addView(senderNameText);
        commentBody.addView(senderSurnameText);
        commentBody.addView(dots);
        commentBody.addView(commentText);

        return commentBody;

    }

    private View createEmptyCommentView() {

        LinearLayout.LayoutParams doubleMatch = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView emptyComment = new TextView(context);
        emptyComment.setLayoutParams(doubleMatch);
        emptyComment.setGravity(Gravity.CENTER);
        emptyComment.setText(R.string.no_comments);

        return emptyComment;

    }

    private void addCommentToServer(String senderName, String senderSurname, String comment, String postId) {

        if (InternetConnection.hasInternetConnection(context)) {

            ParseObject postComment = ParseObject.create(LuseenPostComment.class);

            postComment.put("SenderName", senderName);
            postComment.put("SenderSurname", senderSurname);
            postComment.put("Comment", comment);
            postComment.put("PostId", postId);

            postComment.saveInBackground();

            ParseQuery<LuseenPosts> luseenPostsQuery = ParseQuery.getQuery(LuseenPosts.class);
            luseenPostsQuery.getInBackground(postId, new GetCallback<LuseenPosts>() {
                @Override
                public void done(LuseenPosts post, ParseException e) {

                    post.put("HasComments", true);

                    post.saveInBackground();

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


