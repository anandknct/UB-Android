package com.unitybound.main.home.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;

import java.util.List;


public class HomeFeedsAdapter extends RecyclerView.Adapter<HomeFeedsAdapter.MyViewHolder> {

    private static int diff = 0;
    private IListAdapterCallback allProductsActivity = null;
    private Context mContext;
    private List<String> albumList;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

        public void onCommentClickListner(String s, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time_ago = null;
        public TextView tv_tittle_text;
        public TextView tv_description,tv_share,tv_comment;
        public ImageView iv_image_prev = null,iv_user_image= null;

        public MyViewHolder(View view) {
            super(view);
//            tv_tittle_text = (TextView) view.findViewById(R.id.tv_tittle_text);
//            tv_description = (TextView) view.findViewById(R.id.tv_description);
//            iv_image_prev = (ImageView) view.findViewById(R.id.iv_image_prev);
//            iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image);
//            tv_share = (TextView) view.findViewById(R.id.tv_share);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
//            tv_time_ago = (TextView) view.findViewById(R.id.tv_time_ago);
        }
    }

    public HomeFeedsAdapter(Context mContext, List<String> albumList, IListAdapterCallback allProductsActivity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.allProductsActivity = allProductsActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_feeds_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onCommentClickListner("", position);
            }
        });
       /* holder.tv_tittle_text.setText(albumList.get(position).getUserName());
//        holder.tv_description.setText(albumList.get(position).getDescription());
        holder.tv_description.setText( Html.fromHtml(albumList.get(position).getDescription()));
        holder.tv_description.setMovementMethod(LinkMovementMethod.getInstance());

//        if (albumList.get(position).getImage() != null && !albumList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            Log.e("nik", "IMAGE + " + albumList.get(position).getImage());
            Glide.with(mContext)
                    .load(albumList.get(position)
                            .getImage())
//                    .placeholder(R.drawable.placeholder)
                    .skipMemoryCache(false).centerCrop()
                    .into(holder.iv_image_prev);
//        }

        Glide.with(mContext)
                .load(albumList.get(position)
                        .getProfileImage())
                .placeholder(R.drawable.user_placeholder)
                .skipMemoryCache(false)
                .into(holder.iv_user_image);

        holder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.onItemClickListner(albumList.get(position).getNewsPid(), position);
            }
        });

        holder.iv_image_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
                    Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    intent.putExtra("URI", albumList.get(position).getVideo());
                    mContext.startActivity(intent);
                }
            }
        });

        long[] eclapsedTime = MapsActivity.calculateEclapsedTime(albumList.get(position).getDateCreated());
        String time = MapsActivity.formatTime(eclapsedTime);
        holder.tv_time_ago.setText(time);*/
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
