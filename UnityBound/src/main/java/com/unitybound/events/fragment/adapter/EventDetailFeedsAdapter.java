package com.unitybound.events.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.unitybound.R;
import com.unitybound.events.fragment.beans.eventDiscussion.AllpostsItem;
import com.unitybound.utility.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class EventDetailFeedsAdapter extends RecyclerView.Adapter<EventDetailFeedsAdapter.MyViewHolder> {

    private static int diff = 0;
    private IListAdapterCallback allProductsActivity = null;
    private Context mContext;
    private List<AllpostsItem> albumList;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface IListAdapterCallback {

        public void onItemClickListener(String s, int position);

        public void onCommentClickListener(String s, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_comment_counts = null,tv_like_counts = null;
        private RelativeTimeTextView tv_time_ago = null;
        TextView tv_description_header = null;
        public TextView tv_tittle_text= null,tv_description_secondary = null;
        public TextView tv_description, tv_share, tv_comment;
        public ImageView iv_image_prev = null, iv_user_image = null;
        public ImageView id_dove_icon = null,id_folder_icon = null,id_bookmark_icon = null,
                id_prayer_icon = null,id_testimonials_icon = null;

        public MyViewHolder(View view) {
            super(view);
            tv_tittle_text = (TextView) view.findViewById(R.id.tv_tittle_text);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_description_header = (TextView) view.findViewById(R.id.tv_description_header);
            iv_image_prev = (ImageView) view.findViewById(R.id.iv_image_prev);
            iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image);
            tv_share = (TextView) view.findViewById(R.id.tv_share);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_time_ago = (RelativeTimeTextView) view.findViewById(R.id.tv_time_ago);
            tv_like_counts = (TextView) view.findViewById(R.id.tv_like_counts);
            tv_comment_counts = (TextView) view.findViewById(R.id.tv_comment_counts);
            tv_description_secondary = (TextView) view.findViewById(R.id.tv_description_secondary);

            id_dove_icon = (ImageView) view.findViewById(R.id.id_dove_icon);
            id_folder_icon = (ImageView) view.findViewById(R.id.id_folder_icon);
            id_bookmark_icon = (ImageView) view.findViewById(R.id.id_bookmark_icon);
            id_prayer_icon = (ImageView) view.findViewById(R.id.id_prayer_icon);
            id_testimonials_icon = (ImageView) view.findViewById(R.id.id_testimonials_icon);
        }
    }

    public EventDetailFeedsAdapter(Context mContext, List<AllpostsItem> albumList, IListAdapterCallback allProductsActivity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.allProductsActivity = allProductsActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_detail_feeds_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AllpostsItem mData = albumList.get(position);
        holder.tv_description_header.setText(Util.isNull(mData.getPost()));
        holder.tv_tittle_text.setText(mData.getPostBy().getName());
        holder.tv_like_counts.setText(new StringBuilder()
                .append(mData.getPostLike()).append(" likes and ").toString());
        holder.tv_comment_counts.setText(new StringBuilder()
                .append(mData.getPostComments()).append(" comments").toString());
        try {
            holder.tv_time_ago.setReferenceTime(
                    (dateFormat.parse(mData.getCreatedAt()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_description_secondary.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(Util.isNull(mData
                        .getPostImage()))
                    .placeholder(R.drawable.create_group_place_holder)
                .skipMemoryCache(false).centerCrop()
                .into(holder.iv_image_prev);
        Glide.with(mContext)
                .load(Util.isNull(mData.getPostBy().getProfileImage()))
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.iv_user_image);

        changePostypeIconVisibility(holder,Util.isNull(mData.getPostType()));

        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onCommentClickListener(mData.getId(), position);
            }
        });

        holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (albumList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
//                    Intent intent = new Intent(mContext, FullScreenPhotoActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                    intent.putExtra("URI", albumList.get(position).getVideo());
//                    mContext.startActivity(intent);
//                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void changePostypeIconVisibility(MyViewHolder holder, String type){
        switch (type){
            case "Devotional":
                holder.id_dove_icon.setVisibility(View.VISIBLE);
                holder.id_folder_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.GONE);
                break;
            case "Prayer":
                holder.id_dove_icon.setVisibility(View.GONE);
                holder.id_folder_icon.setVisibility(View.GONE);
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
                holder.id_folder_icon.setVisibility(View.GONE);
                holder.id_bookmark_icon.setVisibility(View.GONE);
                holder.id_prayer_icon.setVisibility(View.GONE);
                holder.id_testimonials_icon.setVisibility(View.VISIBLE);
                break;
        }
    }

}
