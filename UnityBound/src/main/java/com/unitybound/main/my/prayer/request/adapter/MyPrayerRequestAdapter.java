package com.unitybound.main.my.prayer.request.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.unitybound.R;
import com.unitybound.main.my.prayer.request.model.AllpostsItem;
import com.unitybound.utility.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyPrayerRequestAdapter extends RecyclerView.Adapter<MyPrayerRequestAdapter.MyViewHolder> {

    private static int diff = 0;
    private String mType = "";
    private IListAdapterCallback allProductsActivity = null;
    private Context mContext;
    private List<AllpostsItem> albumList;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

        public void onCommentClickListner(String s, int position);

        public void onUserNameClickListner(String s, int position);

        public void onBookmarkClickListner(String s, int position);

        public void onArchiveClickListner(String postId, int position);

        public void onDoveClickListener(Object postsItem, int position);

        public void onLikeClickListner(Object postsItem, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_like_post = null;
        private RelativeTimeTextView tv_time_ago = null;
        public TextView tv_tittle_text;
        public TextView tv_description,tv_share,tv_comment,tv_description_header = null,
                tv_description_secondary = null;
        public ImageView iv_image_prev = null,iv_user_image= null;
        private ImageView id_dove_icon = null, id_folder_icon = null, id_bookmark_icon = null,
                id_prayer_icon = null, id_testimonials_icon = null;
        private final TextView tv_like_counts, tv_comment_counts;

        public MyViewHolder(View view) {
            super(view);
            tv_tittle_text = (TextView) view.findViewById(R.id.tv_tittle_text);
            tv_time_ago = (RelativeTimeTextView) view.findViewById(R.id.tv_time_ago);
            iv_image_prev = (ImageView) view.findViewById(R.id.iv_image_prev);
            tv_description_header = (TextView) view.findViewById(R.id.tv_description_header);
            tv_description_secondary = (TextView) view.findViewById(R.id.tv_description_secondary);
            iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
//            tv_time_ago = (TextView) view.findViewById(R.id.tv_time_ago);
            tv_like_counts = (TextView) view.findViewById(R.id.tv_like_counts);
            tv_comment_counts = (TextView) view.findViewById(R.id.tv_comment_counts);
            tv_like_post = (TextView) view.findViewById(R.id.tv_like_post);


            id_dove_icon = (ImageView) view.findViewById(R.id.id_dove_icon);
            id_folder_icon = (ImageView) view.findViewById(R.id.id_folder_icon);
            id_bookmark_icon = (ImageView) view.findViewById(R.id.id_bookmark_icon);
            id_prayer_icon = (ImageView) view.findViewById(R.id.id_prayer_icon);
            id_testimonials_icon = (ImageView) view.findViewById(R.id.id_testimonials_icon);
        }
    }

    public MyPrayerRequestAdapter(Context mContext, List<AllpostsItem> albumList, IListAdapterCallback allProductsActivity, String archived) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.allProductsActivity = allProductsActivity;
        this.mType = archived;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_prayer_request_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AllpostsItem mItemData = albumList.get(position);
        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onCommentClickListner("", position);
            }
        });
        holder.tv_tittle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onUserNameClickListner("", position);
            }
        });
        holder.tv_tittle_text.setText(mItemData.getPostBy().getName());
        holder.tv_time_ago.setText(mItemData.getPostBy().getName());
        try {
            holder.tv_time_ago.setReferenceTime(
                    (dateFormat.parse(mItemData.getCreatedAt()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_tittle_text.setText(mItemData.getPostBy().getName());
        holder.tv_description_header.setVisibility(View.GONE);
        if (mItemData.getPost()!=null && mItemData.getPost().length()>0){
            holder.tv_description_secondary.setText(mItemData.getPost().trim());
            holder.tv_description_secondary.setVisibility(View.VISIBLE);
        }else {
            holder.tv_description_secondary.setVisibility(View.GONE);
        }

        if (mItemData.getPostImage() != null && mItemData.getPostImage().length() > 0) {
            holder.iv_image_prev.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mItemData.getPostImage())    // you can pass url too
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

        if (mItemData.getPostLike()!=0) {
            holder.tv_like_counts.setText(new StringBuilder()
                    .append(mItemData.getPostLike()).append(" likes and ").toString());
        }
        if (mItemData.getPostComments()!=0) {
            holder.tv_comment_counts.setText(new StringBuilder()
                    .append(mItemData.getPostComments()).append(" comments").toString());
        }

        Glide.with(mContext)
                .load(mItemData.getPostBy().getProfileImage())
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.iv_user_image);

        changePostTypeIconVisibility(holder, mItemData.getPostType());

        if (mItemData.getPostBy().getId()
                .equalsIgnoreCase(
                        Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
            holder.id_dove_icon.setVisibility(View.VISIBLE);
            holder.id_dove_icon.setClickable(true);
            holder.id_folder_icon.setVisibility(View.VISIBLE);
            holder.id_bookmark_icon.setVisibility(View.GONE);
        } else {
            holder.id_dove_icon.setVisibility(View.VISIBLE);
            holder.id_dove_icon.setClickable(false);
            holder.id_folder_icon.setVisibility(View.GONE);
            holder.id_bookmark_icon.setVisibility(View.VISIBLE);
        }

        /*if (mItemData.isBookmarkLocal()) {
            holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
        } else {
            holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
        }*/
        if (mItemData.getPrayerArchive()!=null && mItemData.getPrayerArchive().equalsIgnoreCase("Archive")
                && mType!=null && mType.equalsIgnoreCase("Archived")){
            holder.id_folder_icon.setImageResource(R.drawable.ic_archived_active);
        }else{
            holder.id_folder_icon.setImageResource(R.drawable.ic_archived);
        }

        if (mItemData.isLikeLocal()) {
            Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like );
            holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        } else {
            Drawable img = mContext.getResources().getDrawable( R.drawable.ic_like_unselected );
            holder.tv_like_post.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        }

        holder.tv_like_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mItemData.isLikeLocal()) {
//                    mItemData.setLikeLocal(false);
//                    if (mItemData.getPostLike() != 0) {
//                        mItemData.setPostLike(mItemData.getPostLike() - 1);
//                    }
//                } else {
//                    mItemData.setLikeLocal(true);
//                    mItemData.setPostLike(mItemData.getPostLike() + 1);
//                }

                int likes = 0;
                if (albumList.get(position).isLikeLocal()) {
                    albumList.get(position).setLikeLocal(false);
                    if (albumList.get(position).getPostLike() != 0) {
                        likes = albumList.get(position).getPostLike() - 1;
                        albumList.get(position).setPostLike(likes);
                    }
                } else {
                    albumList.get(position).setLikeLocal(true);
                    likes = albumList.get(position).getPostLike() + 1;
                    albumList.get(position).setPostLike(albumList.get(position).getPostLike() + 1);
                }

//                holder.tv_like_counts.setText(new StringBuilder()
//                        .append(likes+"").append(" likes and ").toString());

                allProductsActivity.onLikeClickListner(mItemData.getId(), position);
            }
        });

        holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemData.getPostImage() != null && mItemData.getPostImage().length() > 0) {
                    Util.navigateTOFullScreenActivity(mContext,
                            mItemData.getPostImage(),
                            mItemData.getId(),
                            mItemData.getPostLike() + "",
                            mItemData.getCommentLike() + "");
                } else {
                    Toast.makeText(mContext, "Image not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (mItemData.getPrayerArchive()!=null && mItemData.getPrayerArchive().equalsIgnoreCase("Archive")){
            holder.id_folder_icon.setImageResource(R.drawable.ic_archived_active);
            holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark);
        }else {
            holder.id_folder_icon.setImageResource(R.drawable.ic_archived);
            holder.id_bookmark_icon.setImageResource(R.drawable.ic_bookmark_active);
        }

        holder.id_bookmark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onBookmarkClickListner(mItemData.getId(),position);
            }
        });

        holder.id_folder_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onArchiveClickListner(mItemData.getId(),position);
            }
        });

        holder.id_dove_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemData.getPostBy().getId()
                        .equalsIgnoreCase(
                                Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
                    allProductsActivity.onDoveClickListener(mItemData.getId(), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void changePostTypeIconVisibility(MyViewHolder holder, String type) {
        if (type==null){
//            Toast.makeText(mContext, "Post type : null coming server", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case "Devotional":
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_folder_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.GONE);
                break;
            case "Prayer":
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_folder_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
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
                holder.id_folder_icon.setVisibility(View.VISIBLE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.VISIBLE);
                break;
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

}
