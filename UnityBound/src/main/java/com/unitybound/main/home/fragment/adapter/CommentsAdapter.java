package com.unitybound.main.home.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.unitybound.R;
import com.unitybound.main.home.fragment.activity.FeedsCommentReplyActivity;
import com.unitybound.main.home.fragment.beans.commentsPost.CommentsItem;
import com.unitybound.utility.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private static int diff = 0;
    private int mLIST_TYPE = 0;
    private EditText edt_comments = null;
    private IListAdapterCallback allProductsActivity = null;
    private Context mContext;
    private List<com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsItem> allCommentsReply;
    private List<CommentsItem> commentsList;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CommentsAdapter(FeedsCommentReplyActivity mContext,
                           List<com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsItem> allCommentsReply,
                           FeedsCommentReplyActivity allProductsActivity) {

        this.mContext = mContext;
        this.allCommentsReply = allCommentsReply;
        this.allProductsActivity = allProductsActivity;
        mLIST_TYPE = 1;
    }

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

        public void onReplyClickListener(String s, int position);

        public void onLikeClickListener(String s, int position);

        public void onDeleteClickListener(String s, int position);

        public void onSendCommentClickListener(String commentMessage,String postId, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeTimeTextView tv_time_ago = null;
        public TextView tv_tittle_text;
        public TextView tv_description, tv_reply, tv_comment,tv_like, tv_delete_comment;
        public ImageView iv_user_image = null,btn_send_comment = null;
        private ImageView iv_image_prev = null;


        public MyViewHolder(View view) {
            super(view);
            tv_tittle_text = view.findViewById(R.id.tv_tittle_text);
            tv_description = view.findViewById(R.id.tv_description);
            tv_like = view.findViewById(R.id.tv_like);
            tv_reply = view.findViewById(R.id.tv_reply);
            iv_user_image = view.findViewById(R.id.iv_user_image);
            btn_send_comment = view.findViewById(R.id.btn_send_comment);
            tv_comment = view.findViewById(R.id.tv_comment);
            tv_time_ago = view.findViewById(R.id.tv_time_ago);
            edt_comments = view.findViewById(R.id.edt_comments);
            iv_image_prev = view.findViewById(R.id.iv_image_prev);
            tv_delete_comment = view.findViewById(R.id.tv_delete_comment);
        }

    }

    public CommentsAdapter(Context mContext, List<CommentsItem> commentsList, IListAdapterCallback allProductsActivity) {
        this.mContext = mContext;
        this.commentsList = commentsList;
        this.allProductsActivity = allProductsActivity;
        mLIST_TYPE = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (mLIST_TYPE==0) {
            /**
             * For Comment List
             */
            holder.tv_tittle_text.setText(commentsList.get(position).getCommentBy().getName());
            holder.tv_description.setText(Html.fromHtml(commentsList.get(position).getComments()));
            try {
                holder.tv_time_ago.setReferenceTime((dateFormat.parse(commentsList.get(position).getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.tv_like.setText(new StringBuilder()
                    .append(commentsList.get(position).getCommentLike()).append(" Likes ").toString());

            Glide.with(mContext)
                    .load(commentsList.get(position).getCommentBy().getProfileImage())
                    .placeholder(R.drawable.user_comment_def)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);

            holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onReplyClickListener(commentsList.get(position).getId(), position);
                }
            });

            holder.tv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commentsList.get(position).isLikeLocal()) {
                        commentsList.get(position).setLikeLocal(false);
                        if (commentsList.get(position).getCommentLike() != 0) {
                            commentsList.get(position).setCommentLike(commentsList.get(position).getCommentLike() - 1);
                        }
                    } else {
                        commentsList.get(position).setLikeLocal(true);
                        commentsList.get(position).setCommentLike(commentsList.get(position).getCommentLike() + 1);
                    }

                    holder.tv_like.setText(new StringBuilder().append(commentsList.get(position).getCommentLike()).append(" Likes").toString());

                    allProductsActivity.onLikeClickListener(commentsList.get(position).getId(), position);
                }
            });
//        holder.btn_send_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                allProductsActivity.onSendCommentClickListener(
//                        edt_comments.getText().toString(),
//                        commentsList.get(position).getId(),position);
//            }
//        });
            String commentImage = commentsList.get(position).getCommentImage();
            if (commentImage != null && commentImage.length() > 0 && commentImage.contains("http")) {
                holder.iv_image_prev.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(commentImage)    // you can pass url too https://unitybound.s3-ap-southeast-1.amazonaws.com/I/u/m/118/14/profile_1515909095_5a16aee27270555ee7763032.png
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                if (resource != null) {
                                    setImage(resource, holder.iv_image_prev);
                                }
                            }
                        });


            } else {
                holder.iv_image_prev.setVisibility(View.GONE);
            }
        } else if (mLIST_TYPE==1) {
            /**
             * For Comment Reply List
             */
            com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsItem mCommentData
                    = allCommentsReply.get(position);

            holder.tv_tittle_text.setText(mCommentData.getReplyCommentBy().getName());
            holder.tv_description.setText(Html.fromHtml(mCommentData.getReplyComments()));
            try {
                holder.tv_time_ago.setReferenceTime(
                        (dateFormat.parse(mCommentData.getCreatedAt()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.tv_like.setText(new StringBuilder()
                    .append(mCommentData.getTotalLike()).append(" Likes ").toString());

            Glide.with(mContext)
                    .load(mCommentData.getReplyCommentBy().getProfileImage())
                    .placeholder(R.drawable.user_comment_def)
                    .skipMemoryCache(false)
                    .into(holder.iv_user_image);

            holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allProductsActivity.onReplyClickListener(mCommentData.getId(), position);
                }
            });

            holder.tv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCommentData.isLikedbyMe()) {
                        mCommentData.setLikedbyMe(false);
                        if (mCommentData.getTotalLike() != 0) {
                            mCommentData.setTotalLike(mCommentData.getTotalLike() - 1);
                        }
                    } else {
                        mCommentData.setLikedbyMe(true);
                        mCommentData.setTotalLike(mCommentData.getTotalLike() + 1);
                    }

                    holder.tv_like.setText(new StringBuilder()
                            .append(mCommentData.getTotalLike()).append(" Likes").toString());

                    allProductsActivity.onLikeClickListener(mCommentData.getId(), position);
                }
            });
//        holder.btn_send_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                allProductsActivity.onSendCommentClickListener(
//                        edt_comments.getText().toString(),
//                        commentsList.get(position).getId(),position);
//            }
//        });
//            String commentImage = mCommentData.getReplyCommentBy().getProfileImage();
//            if (commentImage != null && commentImage.length() > 0 && commentImage.contains("http")) {
//                holder.iv_image_prev.setVisibility(View.VISIBLE);
//                Glide.with(mContext)
//                        .load(commentImage)    // you can pass url too https://unitybound.s3-ap-southeast-1.amazonaws.com/I/u/m/118/14/profile_1515909095_5a16aee27270555ee7763032.png
//                        .asBitmap()
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                // you can do something with loaded bitmap here
//                                if (resource != null) {
//                                    setImage(resource, holder.iv_image_prev);
//                                }
//                            }
//                        });
//
//
//            } else {
//                holder.iv_image_prev.setVisibility(View.GONE);
//            }
        }

        holder.tv_delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsActivity.onDeleteClickListener(commentsList.get(position).getId(), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return commentsList!=null ? commentsList.size() : allCommentsReply!=null ? allCommentsReply.size() : 0;
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
