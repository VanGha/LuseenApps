package com.luseen.vanik.luseenapp.Recyclers.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Parse.LuseenNews;
import com.luseen.vanik.luseenapp.Parse.LuseenPostComment;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private Context context;

    private List<LuseenPosts> luseenPosts;
    private List<LuseenPostComment> luseenPostComments;
    private List<LuseenNews> luseenNews;

    public MainRecyclerAdapter(Context context, List<LuseenPosts> luseenPosts, List<LuseenNews> luseenNews,
                               List<LuseenPostComment> luseenPostComments) {
        this.context = context;
        this.luseenPosts = luseenPosts;
        this.luseenNews = luseenNews;
        this.luseenPostComments = luseenPostComments;
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

        YoYo.with(Techniques.SlideInLeft).duration(500).playOn(holder.cardView);

        if (luseenNews == null) {

            holder.userImage.setBackground(LoggedUser.getPhoto().getDrawable());
            holder.posterName.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterName());
            holder.posterSurname.setText(luseenPosts.get(holder.getAdapterPosition()).getPosterSurname());
            holder.information.setText(luseenPosts.get(holder.getAdapterPosition()).getInformation());

            final boolean[] isFirstFocus = {true};

            holder.commentField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (holder.commentField.getText().toString().length() == 1 && isFirstFocus[0]) {
                        holder.sendButtonSwitcher.showNext();
                        isFirstFocus[0] = false;
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (holder.commentField.getText().toString().isEmpty()) {
                        holder.sendButtonSwitcher.showPrevious();
                        isFirstFocus[0] = true;
                    }

                }
            });

            holder.commentsLoadProgress.setVisibility(View.VISIBLE);

            for (int i = 0; i < luseenPostComments.size(); i++) {

                if (luseenPostComments.get(i).getPostId().equals(luseenPosts.get(holder.getAdapterPosition()).getObjectId())) {

                    if (!luseenPostComments.get(i).isAdded()) {

                        holder.commentsField.addView(createCommentView(
                                luseenPostComments.get(i).getSenderName(),
                                luseenPostComments.get(i).getSenderSurname(),
                                luseenPostComments.get(i).getComment()));
                        luseenPostComments.get(i).setAdded(true);

                    }

                }

            }

            YoYo.with(Techniques.FlipInY).duration(1000).playOn(holder.posterName);
            YoYo.with(Techniques.FlipInX).duration(2000).playOn(holder.posterSurname);
            YoYo.with(Techniques.Landing).duration(1000).playOn(holder.information);
            YoYo.with(Techniques.FadeInDown).duration(500).playOn(holder.commentsField);

            holder.commentsLoadProgress.setVisibility(View.GONE);

            holder.sendButtonGray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    YoYo.with(Techniques.Shake).duration(500).playOn(holder.commentCreateField);
                }

            });

            holder.sendButtonBlue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.commentsLoadProgress.setVisibility(View.VISIBLE);

                    if (!holder.commentField.getText().toString().isEmpty() &&
                            !holder.commentField.getText().toString().trim().isEmpty()) {

                        addCommentToServer(LoggedUser.getName(), LoggedUser.getSurName(),
                                holder.commentField.getText().toString(),
                                luseenPosts.get(holder.getAdapterPosition()).getObjectId());

                        if (holder.noComment != null) {
                            holder.commentsField.removeView(holder.noComment);
                        }

                        holder.commentsField.addView(createCommentView(
                                LoggedUser.getName(),
                                LoggedUser.getSurName(),
                                holder.commentField.getText().toString()));

                        holder.commentField.setText("");

                    }

                    holder.commentsLoadProgress.setVisibility(View.GONE);
                }
            });

            final PopupMenu popupMenu = new PopupMenu(context, holder.itemOptionsMenu);
            popupMenu.inflate(R.menu.recycler_item_options);
            final MenuItem itemNotificationChecker = popupMenu.getMenu().getItem(0);
            final MenuItem itemPostEdit = popupMenu.getMenu().getItem(1);
            final MenuItem itemPostDelete = popupMenu.getMenu().getItem(2);

            if (!LoggedUser.getEmail().equals(luseenPosts.get(holder.getAdapterPosition()).getPosterEmail())) {
                itemPostEdit.setEnabled(false);
                itemPostDelete.setEnabled(false);
            }

            final SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.NOTIFICATION_CHECKER_SHARED_PREFERENCE,
                    Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean isChecked = sharedPreferences.getBoolean(String.valueOf(AppConstants.NOTIFICATION_POST_COMMENTS_CHECKED +
                    holder.getAdapterPosition()), false);
            String checkedPostId = sharedPreferences.getString(String.valueOf(AppConstants.NOTIFICATION_POST_ID +
                    holder.getAdapterPosition()), "0");

            if (isChecked && checkedPostId.equals(luseenPosts.get(holder.getAdapterPosition()).getObjectId())) {
                itemNotificationChecker.setChecked(true);
            }

            holder.itemOptionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.action_item_notify_for_new_comment: {

                                    if (!itemNotificationChecker.isChecked()) {
                                        itemNotificationChecker.setChecked(true);
                                        editor.putBoolean(String.valueOf(AppConstants.NOTIFICATION_POST_COMMENTS_CHECKED +
                                                holder.getAdapterPosition()), true);
                                        editor.putString(String.valueOf(AppConstants.NOTIFICATION_POST_ID + holder.getAdapterPosition()),
                                                luseenPosts.get(holder.getAdapterPosition()).getObjectId());

                                    } else {
                                        itemNotificationChecker.setChecked(false);
                                        editor.remove(String.valueOf(AppConstants.NOTIFICATION_POST_COMMENTS_CHECKED +
                                                holder.getAdapterPosition()));
                                        editor.remove(String.valueOf(AppConstants.NOTIFICATION_POST_ID +
                                                holder.getAdapterPosition()));
                                    }

                                    editor.apply();

                                    break;
                                }

                                case R.id.action_edit_post: {

                                    ArrayList<String> dateStringArray = new ArrayList<>();
                                    ArrayList<String> timeStringArray = new ArrayList<>();

                                    dateStringArray.add("");
                                    timeStringArray.add("");

                                    dateStringArray.add("Сегодня");
                                    dateStringArray.add("Завтра");
                                    dateStringArray.add("Послезавтра");

                                    for (int i = 0; i <= 23; i++) {
                                        timeStringArray.add(String.valueOf(i + ":" + "00"));
                                        timeStringArray.add(String.valueOf(i + ":" + "30"));
                                    }

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    final View postAddDialog = View.inflate(context, R.layout.post_add_dialog, null);
                                    builder.setView(postAddDialog);

                                    final EditText addPostInformation = (EditText) postAddDialog.findViewById(R.id.add_post_information);
                                    final Spinner dateSpinner = (Spinner) postAddDialog.findViewById(R.id.date_spinner);
                                    final Spinner timeSpinner = (Spinner) postAddDialog.findViewById(R.id.time_spinner);
                                    final Switch useExamples = (Switch) postAddDialog.findViewById(R.id.use_example_switch);

                                    ArrayAdapter<String> stringArrayAdapterDate = new ArrayAdapter<>(context,
                                            android.R.layout.simple_spinner_item, dateStringArray);
                                    stringArrayAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    dateSpinner.setAdapter(stringArrayAdapterDate);

                                    ArrayAdapter<String> stringArrayAdapterTime = new ArrayAdapter<>(context,
                                            android.R.layout.simple_spinner_item, timeStringArray);
                                    stringArrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    timeSpinner.setAdapter(stringArrayAdapterTime);

                                    addPostInformation.setText(luseenPosts.get(holder.getAdapterPosition()).getInformation());
                                    useExamples.setChecked(luseenPosts.get(holder.getAdapterPosition()).isExampleUsedWhenCreated());

                                    builder.setPositiveButton(context.getResources().getString(R.string.action_edit_post), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            if (InternetConnection.hasInternetConnection(context)) {

                                                ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
                                                luseenPostsParseQuery.getInBackground(luseenPosts.get(holder.getAdapterPosition()).getObjectId(),
                                                        new GetCallback<LuseenPosts>() {
                                                            @Override
                                                            public void done(LuseenPosts post, ParseException e) {

                                                                post.put("PosterInformation", addPostInformation.getText().toString());

                                                                post.saveInBackground();

                                                                holder.information.setText(addPostInformation.getText().toString());
                                                                YoYo.with(Techniques.Landing).duration(1000).playOn(holder.information);

                                                            }
                                                        });
                                            }
                                        }
                                    });

                                    builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    builder.create().show();

                                    dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            addPostInformation.setText(String.valueOf(addPostInformation.getText().toString()) +
                                                    " " + adapterView.getSelectedItem().toString());
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            addPostInformation.setText(String.valueOf(addPostInformation.getText().toString()) +
                                                    " " + adapterView.getSelectedItem().toString());
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    useExamples.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                            dateSpinner.setSelection(0);
                                            timeSpinner.setSelection(0);

                                            if (useExamples.isChecked()) {
                                                addPostInformation.setText("Example!"); // TODO: 15-Feb-17 MAKE A EXAMPLE OF POST
                                            } else {
                                                addPostInformation.setText("");
                                            }
                                        }
                                    });

                                    break;
                                }

                                case R.id.action_delete_post: {

                                    if (InternetConnection.hasInternetConnection(context)) {

                                        ParseQuery<LuseenPostComment> luseenPostCommentParseQuery =
                                                ParseQuery.getQuery(LuseenPostComment.class);
                                        luseenPostCommentParseQuery.findInBackground(new FindCallback<LuseenPostComment>() {
                                            @Override
                                            public void done(List<LuseenPostComment> comments, ParseException e) {

                                                if (e == null) {

                                                    for (int i = 0; i < comments.size(); i++) {

                                                        if (luseenPosts.get(holder.getAdapterPosition()).getObjectId().
                                                                equals(comments.get(i).getPostId())) {

                                                            try {
                                                                comments.get(i).delete();
                                                                updatePostsComments();

                                                                boolean tmp = sharedPreferences.getBoolean(String.valueOf(AppConstants.NOTIFICATION_POST_COMMENTS_CHECKED +
                                                                        holder.getAdapterPosition()), false);
                                                                String tmpS = sharedPreferences.getString(String.valueOf(AppConstants.NOTIFICATION_POST_ID +
                                                                        holder.getAdapterPosition()), "0");

                                                                if (tmp && !tmpS.equals("0")) {

                                                                    editor.remove(String.valueOf(AppConstants.NOTIFICATION_POST_COMMENTS_CHECKED +
                                                                            holder.getAdapterPosition()));
                                                                    editor.remove(String.valueOf(AppConstants.NOTIFICATION_POST_ID +
                                                                            holder.getAdapterPosition()));
                                                                    editor.apply();

                                                                }

                                                            } catch (ParseException e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        }

                                                    }

                                                }

                                            }
                                        });

                                        ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
                                        luseenPostsParseQuery.getInBackground(luseenPosts.get(holder.getAdapterPosition()).getObjectId(),
                                                new GetCallback<LuseenPosts>() {
                                                    @Override
                                                    public void done(LuseenPosts post, ParseException e) {

                                                        if (e == null) {

                                                            try {
                                                                post.delete();

                                                                luseenPosts.remove(holder.getAdapterPosition());
                                                                notifyItemRemoved(holder.getAdapterPosition());

                                                            } catch (ParseException e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        }

                                                    }
                                                });

                                    }

                                    break;
                                }

                            }

                            return false;
                        }
                    });

                    popupMenu.show();

                }
            });

        } else {
            holder.newsInformation.setText(luseenNews.get(holder.getAdapterPosition()).getInformation());
        }

    }

    private void updatePostsComments() {

        ParseQuery<LuseenPostComment> luseenPostCommentParseQuery = ParseQuery.getQuery(LuseenPostComment.class);
        luseenPostCommentParseQuery.findInBackground(new FindCallback<LuseenPostComment>() {

            @Override
            public void done(List<LuseenPostComment> comments, ParseException e) {

                if (e == null) {

                    luseenPostComments = comments;

                }

            }
        });

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

    public void setLuseenPosts(List<LuseenPosts> luseenPosts) {
        this.luseenPosts = luseenPosts;
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

        CardView cardView;

        LinearLayout commentsField;
        RelativeLayout commentCreateField;
        ProgressBar commentsLoadProgress;

        PorterShapeImageView posterImage, userImage;
        TextView posterName, posterSurname, information, itemOptionsMenu, noComment;
        EditText commentField;
        ImageSwitcher sendButtonSwitcher;
        ImageView sendButtonGray;
        ImageView sendButtonBlue;

        TextView newsInformation;

        MainViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.info_cards);

            if (luseenNews == null) {

                posterImage = (PorterShapeImageView) itemView.findViewById(R.id.user_photo);
                userImage = (PorterShapeImageView) itemView.findViewById(R.id.sender_image);
                posterName = (TextView) itemView.findViewById(R.id.user_name);
                posterSurname = (TextView) itemView.findViewById(R.id.user_surname);
                information = (TextView) itemView.findViewById(R.id.info_text);
                itemOptionsMenu = (TextView) itemView.findViewById(R.id.item_options_menu);
                noComment = (TextView) itemView.findViewById(R.id.no_comment);
                commentsField = (LinearLayout) itemView.findViewById(R.id.comments_field);
                commentCreateField = (RelativeLayout) itemView.findViewById(R.id.comment_create_field);
                commentsLoadProgress = (ProgressBar) itemView.findViewById(R.id.load_progress);
                commentField = (EditText) itemView.findViewById(R.id.comment_field);
                sendButtonSwitcher = (ImageSwitcher) itemView.findViewById(R.id.send_button);
                sendButtonGray = (ImageView) itemView.findViewById(R.id.comment_send_button_gray);
                sendButtonBlue = (ImageView) itemView.findViewById(R.id.comment_send_button_blue);

                Animation inAnimation = new AlphaAnimation(0, 1);
                inAnimation.setDuration(1000);
                Animation outAnimation = new AlphaAnimation(1, 0);
                outAnimation.setDuration(1000);

                sendButtonSwitcher.setInAnimation(inAnimation);
                sendButtonSwitcher.setOutAnimation(outAnimation);


            } else {

                newsInformation = (TextView) itemView.findViewById(R.id.news_information);

            }

        }

    }

}


