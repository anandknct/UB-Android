package com.unitybound.main.home.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.unitybound.R;
import com.unitybound.account.beans.timeline.UserInfo;
import com.unitybound.church.beans.churchDetail.AllpostsItem;
import com.unitybound.main.home.fragment.HomeFeedsFragment;
import com.unitybound.main.home.fragment.beans.filterPost.DataItem;
import com.unitybound.main.home.fragment.beans.homeFeeds.AllPostsItem;
import com.unitybound.main.home.fragment.beans.homeFeeds.PostBy;
import com.unitybound.utility.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HomeFeedsAdapter extends RecyclerView.Adapter<HomeFeedsAdapter.MyViewHolder> {

    private static int diff = 0;
    private UserInfo mUserInfo = null;
    private ArrayList<com.unitybound.account.beans.timeline.AllpostsItem> mProfileTimelineAllposts = null;
    private ArrayList<AllpostsItem> mChurchDetailAllposts = null;
    private ArrayList<DataItem> mDataItem = null;
    private IListAdapterCallback allProductsActivity = null;
    private Context mContext;
    private ArrayList<AllPostsItem> mAllposts;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private int mScreenWidth;

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

        public void onCommentClickListner(String s, int position);

        public void onOptionClickListener(Object postsItem, int position);

        public void onUserNameClickListner(String s, int position);

        public void onLikeClickListner(Object postsItem, int position);

        public void onDoveClickListener(Object postsItem, int position);

        public void onBookmarkClickListener(Object postsItem, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_like_counts, tv_comment_counts;
        private ImageView iv_image_prev = null;
        private RelativeTimeTextView tv_time_ago = null;
        private TextView tv_postedBy_name;
        private TextView tv_comment, tv_like_post, tv_description_header = null, tv_description_secondary = null,tvAnswerPrayer = null;
        private ImageView rr_options = null, iv_user_image = null;
        private ImageView id_dove_icon = null, id_folder_icon = null, id_bookmark_icon = null,
                id_prayer_icon = null, id_testimonials_icon = null;
        private LinearLayout answerLayout;

        public MyViewHolder(View view) {
            super(view);
            tv_postedBy_name = (TextView) view.findViewById(R.id.tv_postedBy_name);
            tv_time_ago = (RelativeTimeTextView) view.findViewById(R.id.tv_time_ago);
            iv_image_prev = (ImageView) view.findViewById(R.id.iv_image_prev);
            rr_options = (ImageView) view.findViewById(R.id.rr_options);
            iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image);
            tv_like_post = (TextView) view.findViewById(R.id.tv_like_post);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_like_counts = (TextView) view.findViewById(R.id.tv_like_counts);
            tv_comment_counts = (TextView) view.findViewById(R.id.tv_comment_counts);
            tv_description_header = (TextView) view.findViewById(R.id.tv_description_header);
            tv_description_secondary = (TextView) view.findViewById(R.id.tv_description_secondary);
            answerLayout = (LinearLayout) view.findViewById(R.id.ll_answer_layout);
            tvAnswerPrayer = (TextView) view.findViewById(R.id.tv_answer_prayer);

            id_dove_icon = (ImageView) view.findViewById(R.id.id_dove_icon);
            id_folder_icon = (ImageView) view.findViewById(R.id.id_folder_icon);
            id_bookmark_icon = (ImageView) view.findViewById(R.id.id_bookmark_icon);
            id_prayer_icon = (ImageView) view.findViewById(R.id.id_prayer_icon);
            id_testimonials_icon = (ImageView) view.findViewById(R.id.id_testimonials_icon);
        }
    }

    /**
     * Called From Home Screen All post (default) API response
     *
     * @param mContext
     * @param allposts
     * @param allProductsActivity
     */
    public HomeFeedsAdapter(Context mContext, ArrayList<AllPostsItem> allposts, IListAdapterCallback allProductsActivity) {
        this.mContext = mContext;
        this.mAllposts = allposts;
        this.allProductsActivity = allProductsActivity;
        mChurchDetailAllposts = null;
        mDataItem = null;
        mProfileTimelineAllposts = null;
        //get the screenWidth :D optimize everything :D
        if (mContext!=null)
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Called From Church Detail Screen After post tab clicked
     *
     * @param allProductsActivity
     * @param allposts
     * @param mContext
     * @param dummy
     */
    public HomeFeedsAdapter(IListAdapterCallback allProductsActivity,
                            ArrayList<AllpostsItem> allposts, Context mContext, boolean dummy) {
        this.mContext = mContext;
        this.mChurchDetailAllposts = allposts;
        this.allProductsActivity = allProductsActivity;
        mAllposts = null;
        mDataItem = null;
        mProfileTimelineAllposts = null;
    }

    /**
     * Called From Home Screen After Filter Option API response
     *
     * @param mContext
     * @param allposts
     * @param allProductsActivity
     */
    public HomeFeedsAdapter(Context mContext, ArrayList<DataItem> allposts, HomeFeedsFragment allProductsActivity) {
        this.mContext = mContext;
        this.mDataItem = allposts;
        mChurchDetailAllposts = null;
        mAllposts = null;
        mProfileTimelineAllposts = null;
        this.allProductsActivity = allProductsActivity;
    }

    /**
     * Called From Profile Screen After post tab clicked
     *
     * @param allProductsActivity
     * @param allposts
     * @param UserInfo
     * @param mContext
     * @param dummy
     */
    public HomeFeedsAdapter(IListAdapterCallback allProductsActivity,
                            ArrayList<com.unitybound.account.beans.timeline.AllpostsItem> allposts,
                            UserInfo UserInfo, Context mContext, int dummy) {
        this.mContext = mContext;
        this.mProfileTimelineAllposts = allposts;
        this.allProductsActivity = allProductsActivity;
        mAllposts = null;
        mDataItem = null;
        mUserInfo = UserInfo;
        mChurchDetailAllposts = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_feeds_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /**
         Called From Home Screen All post (default) API response *
         */
        if (mAllposts != null) {
            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onCommentClickListner("", position);
                }
            });
            holder.tv_postedBy_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onUserNameClickListner(mAllposts.get(position).getPostBy().getId()+"", position);
                }
            });
            final AllPostsItem mAllPostItem = mAllposts.get(position);
            PostBy mPostBy = mAllposts.get(position).getPostBy();
            holder.tv_postedBy_name.setText(mPostBy.getName());
            try {
                holder.tv_time_ago.setReferenceTime((dateFormat.parse(mAllPostItem.getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_like_counts.setText(new StringBuilder().append(mAllPostItem.getPostLike()).append(" likes and ").toString());
            holder.tv_comment_counts.setText(new StringBuilder().append(mAllPostItem.getPostComments()).append(" comments").toString());


            if (mAllPostItem.getPrayerAnswer() != 0 && mAllPostItem.getPrayerAnswer() >= 1) {
                holder.answerLayout.setVisibility(View.VISIBLE);
                holder.tvAnswerPrayer.setText(Util.isNull(mAllPostItem.getPrayerAnswerComment().trim()));
                holder.id_dove_icon.setImageResource(R.drawable.ic_dove_active);
                holder.id_dove_icon.setClickable(false);
                holder.id_dove_icon.setOnClickListener(null);
            } else {
                holder.answerLayout.setVisibility(View.GONE);
                holder.id_dove_icon.setImageResource(R.drawable.ic_dove);
                holder.id_dove_icon.setClickable(true);
                holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mAllPostItem.getPostBy().getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                            allProductsActivity.onDoveClickListener(mAllPostItem.getId(), position);
                        }
                    }
                });
            }

            holder.tv_description_header.setVisibility(View.GONE);
            holder.tv_description_secondary.setText(mAllPostItem.getPost());
            if (mAllPostItem.getPostImage() != null && mAllPostItem.getPostImage().length() > 0) {
                holder.iv_image_prev.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(mAllPostItem.getPostImage())    // you can pass url too
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                if (resource!=null) {
                                 setImage(resource,holder.iv_image_prev);
                                }
                            }
                        });


            } else {
                holder.iv_image_prev.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(mPostBy.getProfileImage())
                    .placeholder(R.drawable.ic_me)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);

            changePostTypeIconVisibility(holder, mAllPostItem.getPostType());

            if (mAllPostItem.getPostBy().getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext)) && mAllPostItem.getPostType().equalsIgnoreCase("prayer")) {
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
            } else if (mAllPostItem.getPostType().equalsIgnoreCase("prayer")){
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.VISIBLE);
            }



            if (mAllPostItem.isBookmarkLocal()) {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
            } else {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
            }

            holder.id_bookmark_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onBookmarkClickListener(mAllposts.get(position), position);
                }
            });

            holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAllposts.get(position).getPostImage() != null && mAllposts.get(position).getPostImage().length() > 0) {
                        Util.navigateTOFullScreenActivity(mContext,
                                mAllposts.get(position).getPostImage(),
                                mAllposts.get(position).getId(),
                                mAllposts.get(position).getPostLike() + "",
                                mAllposts.get(position).getCommentLike() + "");
                    } else {
                        Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (mAllposts.get(position).isLikeLocal()) {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            } else {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like_unselected );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            }
            holder.tv_like_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAllposts.get(position).isLikeLocal()) {
                        mAllposts.get(position).setLikeLocal(false);
                        if (mAllposts.get(position).getPostLike() != 0) {
                            mAllposts.get(position).setPostLike(mAllposts.get(position).getPostLike() - 1);
                        }
                    } else {
                        mAllposts.get(position).setLikeLocal(true);
                        mAllposts.get(position).setPostLike(mAllposts.get(position).getPostLike() + 1);
                    }

                    holder.tv_like_counts.setText(new StringBuilder().append(mAllposts.get(position).getPostLike()).append(" likes and ").toString());

                    allProductsActivity.onLikeClickListner(mAllposts.get(position), position);
                }
            });
            holder.rr_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onOptionClickListener(mAllposts.get(position), position);
                }
            });
        } else if (mDataItem != null) {
            /*Called From Home Screen After Filter Option API response*/

            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onCommentClickListner("", position);
                }
            });
            holder.tv_postedBy_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onUserNameClickListner(""+
                            mAllposts.get(position).getPostBy().getId(), position);
                }
            });
            final DataItem dataItem = mDataItem.get(position);
            com.unitybound.main.home.fragment.beans.filterPost.PostBy mPostBy = mDataItem.get(position).getPostBy();
            holder.tv_postedBy_name.setText(mPostBy.getName());
            try {
                holder.tv_time_ago.setReferenceTime(
                        (dateFormat.parse(dataItem.getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_like_counts.setText(new StringBuilder()
                    .append(dataItem.getPostLike()).append(" likes and ").toString());
            holder.tv_comment_counts.setText(new StringBuilder()
                    .append(dataItem.getPostComments()).append(" comments").toString());

            holder.tv_description_header.setVisibility(View.GONE);
            holder.tv_description_secondary.setText(dataItem.getPost());

            if (dataItem.getPrayerAnswer() != 0 && dataItem.getPrayerAnswer() >= 1) {
                holder.answerLayout.setVisibility(View.VISIBLE);
                holder.tvAnswerPrayer.setText(Util.isNull(dataItem.getPrayerAnswerComment().trim()));
                holder.id_dove_icon.setImageResource(R.drawable.ic_dove_active);
                holder.id_dove_icon.setClickable(false);
                holder.id_dove_icon.setOnClickListener(null);
            } else {
                holder.answerLayout.setVisibility(View.GONE);
                holder.id_dove_icon.setImageResource(R.drawable.ic_dove);
                holder.id_dove_icon.setClickable(true);
                holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dataItem.getPostBy().getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                            allProductsActivity.onDoveClickListener(dataItem.getId(), position);
                        }
                    }
                });
            }

//            Log.e("nik", "User IMAGE + " + mPostBy.getProfileImage());
//            Log.e("nik", "Post IMAGE + " + dataItem.getPostImage());
            changePostTypeIconVisibility(holder, dataItem.getPostType());

            if (dataItem.getPostBy().getId().equalsIgnoreCase(
                            Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))  && dataItem.getPostType().equalsIgnoreCase("prayer")) {
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
            } else if (dataItem.getPostType()!=null && dataItem.getPostType().equalsIgnoreCase("prayer")){
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.VISIBLE);
            }

            if (dataItem.isBookmarkLocal()) {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
            } else {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
            }

            holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.getPostBy().getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                        allProductsActivity.onDoveClickListener(dataItem.getId(), position);
                    }
                }
            });

            holder.id_bookmark_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onBookmarkClickListener(mAllposts.get(position), position);
                }
            });

            if (dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                holder.iv_image_prev.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(dataItem.getPostImage())    // you can pass url too
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                if (resource!=null) {
                                    setImage(resource,holder.iv_image_prev);
                                }
                            }
                        });
            } else {
                holder.iv_image_prev.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(mPostBy.getProfileImage())
                    .placeholder(R.drawable.ic_me)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);
            holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                        Util.navigateTOFullScreenActivity(mContext,
                                dataItem.getPostImage(),
                                dataItem.getId(),
                                dataItem.getPostLike() + "",
                                dataItem.getCommentLike() + "");
                    } else {
                        Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (dataItem.isLikeLocal()) {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            } else {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like_unselected );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            }
            holder.tv_like_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDataItem.get(position).isLikeLocal()) {
                        mDataItem.get(position).setLikeLocal(false);
                        if (mDataItem.get(position).getPostLike() != 0) {
                            mDataItem.get(position).setPostLike(mDataItem.get(position).getPostLike() - 1);
                        }
                    } else {
                        mDataItem.get(position).setLikeLocal(true);
                        mDataItem.get(position).setPostLike(mDataItem.get(position).getPostLike() + 1);
                    }

                    holder.tv_like_counts.setText(new StringBuilder()
                            .append(mDataItem.get(position).getPostLike()).append(" likes and ").toString());

                    allProductsActivity.onLikeClickListner(mDataItem.get(position), position);
                }
            });
            holder.rr_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onOptionClickListener(mDataItem.get(position), position);
                }
            });
        } else if (mChurchDetailAllposts != null) {
            /*Called From Home Screen After Filter Option API response*/

            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onCommentClickListner("", position);
                }
            });
            holder.tv_postedBy_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onUserNameClickListner(""+
                            mAllposts.get(position).getPostBy().getId(), position);
                }
            });
            final AllpostsItem dataItem = mChurchDetailAllposts.get(position);
            com.unitybound.church.beans.churchDetail.PostBy mPostBy = dataItem.getPostBy();
            holder.tv_postedBy_name.setText(mPostBy.getName());
            try {
                holder.tv_time_ago.setReferenceTime(
                        (dateFormat.parse(dataItem.getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_like_counts.setText(new StringBuilder()
                    .append(dataItem.getPostLikes()).append(" likes and ").toString());
            holder.tv_comment_counts.setText(new StringBuilder()
                    .append(dataItem.getPostComments()).append(" comments").toString());
            holder.tv_description_header.setVisibility(View.GONE);
            holder.tv_description_secondary.setText(dataItem.getPost());

            if (dataItem!=null && dataItem.getPrayerAnswer()!=0 && dataItem.getPrayerAnswer()>=1) {
                holder.answerLayout.setVisibility(View.VISIBLE);
                holder.tvAnswerPrayer.setText(Util.isNull(dataItem.getPrayerAnswerComment().trim()));
            } else {
                holder.answerLayout.setVisibility(View.GONE);
            }
//            Log.e("nik", "User IMAGE + " + mPostBy.getProfileImage());
//            Log.e("nik", "Post IMAGE + " + dataItem.getPostImage());
            changePostTypeIconVisibility(holder, dataItem.getPostType());

            if (dataItem.getPostBy().getId()
                    .equalsIgnoreCase(
                            Util.loadPrefrence(Util.PREF_USER_ID, "", mContext)) && dataItem.getPostType().equalsIgnoreCase("prayer")) {
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
            } else if (dataItem.getPostType()!=null && dataItem.getPostType().equalsIgnoreCase("prayer")){
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.VISIBLE);
            }

            /*if (dataItem.isBookmarkLocal()) {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
            } else {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
            }*/

            holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.getPostBy().getId()
                            .equalsIgnoreCase(
                                    Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                        allProductsActivity.onDoveClickListener(dataItem.getId(), position);
                    }
                }
            });

            holder.id_bookmark_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onBookmarkClickListener(mAllposts.get(position), position);
                }
            });

            if (dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                holder.iv_image_prev.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(dataItem.getPostImage())    // you can pass url too
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                setImage(resource,holder.iv_image_prev);
                            }
                        });
            } else {
                holder.iv_image_prev.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(mPostBy.getProfileImage())
                    .placeholder(R.drawable.ic_me)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);
            holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem!=null && dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                        Util.navigateTOFullScreenActivity(mContext,
                                dataItem.getPostImage(),
                                dataItem.getId(),
                                null,
                                dataItem.getCommentLike() + "");
                    } else {
                        Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (dataItem.isLikeLocal()) {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            } else {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like_unselected );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            }
            holder.tv_like_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem!=null && dataItem.isLikeLocal()) {
                        dataItem.setLikeLocal(false);
                        int likes = dataItem.getPostLikes();
                        if (likes > 0) {
                            dataItem.setPostLikes(likes - 1);
                        }
                    } else if (dataItem!=null) {
                        dataItem.setLikeLocal(true);
                        dataItem.setPostLikes(dataItem.getPostLikes() + 1);
                    }

                    holder.tv_like_counts.setText(new StringBuilder()
                            .append(dataItem.getPostComments()).append(" likes and ").toString());

                    allProductsActivity.onLikeClickListner(dataItem.getId(), position);
                }
            });
            holder.rr_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onOptionClickListener(mChurchDetailAllposts.get(position), position);
                }
            });
        } else if (mProfileTimelineAllposts != null) {
            /*Called From Timeline screen of profile page API response*/

            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onCommentClickListner(mProfileTimelineAllposts.get(position).getId(), position);
                }
            });
            holder.tv_postedBy_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onUserNameClickListner(""+
                            mProfileTimelineAllposts.get(position).getId(), position);
                }
            });

            final com.unitybound.account.beans.timeline.AllpostsItem dataItem = mProfileTimelineAllposts.get(position);
            holder.tv_postedBy_name.setText(mUserInfo != null ? mUserInfo.getFirstName() + " " +
                    mUserInfo.getLastName() : "No Name");
            try {
                holder.tv_time_ago.setReferenceTime(
                        (dateFormat.parse(dataItem.getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_like_counts.setText(new StringBuilder()
                    .append(dataItem.getPostLike()).append(" likes and ").toString());
            holder.tv_comment_counts.setText(new StringBuilder()
                    .append(dataItem.getPostComments()).append(" comments").toString());
            holder.tv_description_header.setVisibility(View.GONE);
            holder.tv_description_secondary.setText(dataItem.getPost());

            if (dataItem!=null && dataItem.getPrayerAnswer()!=0 && dataItem.getPrayerAnswer()>=1) {
                holder.answerLayout.setVisibility(View.VISIBLE);
                holder.tvAnswerPrayer.setText(Util.isNull(dataItem.getPrayerAnswerComment().trim()));
            } else {
                holder.answerLayout.setVisibility(View.GONE);
            }
//            Log.e("nik", "User IMAGE + " + mPostBy.getProfileImage());
//            Log.e("nik", "Post IMAGE + " + dataItem.getPostImage());
            changePostTypeIconVisibility(holder, dataItem.getPostType());

            if (mContext!=null && dataItem.getPostBy().getId()
                    .equalsIgnoreCase(
                            Util.loadPrefrence(Util.PREF_USER_ID, "", mContext)) &&
            dataItem.getPostType()!=null&& dataItem.getPostType().equalsIgnoreCase("prayer")) {
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
            } else if (dataItem.getPostType()!=null && dataItem.getPostType().equalsIgnoreCase("prayer")){
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.VISIBLE);
            }

            if (dataItem.isBookmarkLocal()) {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
            } else {
                holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
            }

            holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.getPostBy().getId()
                            .equalsIgnoreCase(
                                    Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                        allProductsActivity.onDoveClickListener(dataItem.getId(), position);
                    }
                }
            });

            holder.id_bookmark_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onBookmarkClickListener(mProfileTimelineAllposts.get(position), position);
                }
            });

            if (dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                holder.iv_image_prev.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(dataItem.getPostImage())    // you can pass url too
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                setImage(resource,holder.iv_image_prev);
                            }
                        });
            } else {
                holder.iv_image_prev.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(dataItem.getPostBy().getProfileImage()) // dataItem.getPostBy()
                    .placeholder(R.drawable.ic_me)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);
            holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.getPostImage() != null && dataItem.getPostImage().length() > 0) {
                        Util.navigateTOFullScreenActivity(mContext,
                                dataItem.getPostImage(),
                                dataItem.getId(),
                                null,
                                dataItem.getCommentLike() + "");
                    } else {
                        Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (dataItem.isLikeLocal()) {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            } else {
                Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like_unselected );
                holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            }

            holder.tv_like_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataItem.isLikeLocal()) {
                        dataItem.setLikeLocal(false);
                        if (dataItem.getPostLike() != 0) {
                            dataItem.setPostLike(dataItem.getPostLike() - 1);
                        }
                    } else {
                        dataItem.setLikeLocal(true);
                        dataItem.setPostLike(dataItem.getPostLike() + 1);
                    }

                    holder.tv_like_counts.setText(new StringBuilder()
                            .append(dataItem.getPostLike()).append(" likes and ").toString());

                    allProductsActivity.onLikeClickListner(dataItem, position);
                }
            });
            holder.rr_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onOptionClickListener(mProfileTimelineAllposts.get(position), position);
                }
            });
        }
    }

    private void setImage(Bitmap resource,ImageView iv_image_prev) {
        if (resource!=null) {
            int mWidth = Util.getScreenWidth(mContext);
            int mHeight = Util.getScreenHeight(mContext);
            int width = resource.getWidth();
            int height = resource.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) mWidth / (float) mHeight;

            int finalWidth = 0;
            int finalHeight = 0;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)mWidth * ratioBitmap);
            } else {
                finalHeight = (int) ((float)mWidth / ratioBitmap);
            }

            iv_image_prev.setMinimumWidth(finalWidth);
            iv_image_prev.setMinimumHeight(finalHeight);
            iv_image_prev.setMaxHeight(finalHeight);
            iv_image_prev.setImageBitmap(resource);
        }
    }

    @Override
    public int getItemCount() {
        return mAllposts != null ?
                mAllposts.size() : mDataItem != null ?
                mDataItem.size() : mChurchDetailAllposts != null ?
                mChurchDetailAllposts.size() : mProfileTimelineAllposts != null ?
                mProfileTimelineAllposts.size() : 0;
    }

    private void changePostTypeIconVisibility(MyViewHolder holder, String type) {
        if (type==null){
//            Toast.makeText(mContext, "Post type : null coming server", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case "Devotional":
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_folder_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.GONE);
                break;
            case "Prayer":
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_folder_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.VISIBLE);
                holder.id_prayer_icon.setVisibility(View.VISIBLE);
                holder.id_testimonials_icon.setVisibility(View.GONE);

                break;
            case "Praise":
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_folder_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.GONE);
                break;
            case "Testimonial":
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_folder_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.VISIBLE);
                break;
        }
    }

}
