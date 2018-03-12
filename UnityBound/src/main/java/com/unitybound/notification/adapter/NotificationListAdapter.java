package com.unitybound.notification.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.notification.beans.NotificationListItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final List<NotificationListItem> mViewListRecord;

    public NotificationListAdapter(Context context, List<NotificationListItem> mViewListRecord) {
        this.mContext = context;
        this.mViewListRecord = mViewListRecord;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item_row, parent, false);
        return new NotificationListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((NotificationListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final NotificationListAdapter.ViewHolder holder, final int POSITION) {
        NotificationListItem mData = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml(mData.getMessage()));
        Glide.with(mContext).load(mData.getSendBy().getProfileImage()).into(holder.ivUserIcons);
        holder.tvDescription.setText("Received: "+Util.dateFormator(mData.getCreatedAt()));
        Glide.with(mContext)
                .load(mData.getSendBy().getProfileImage())
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
//        String mobileNumber = String.valueOf(mViewListRecord.get(POSITION).getMobileNumber());
//        holder.tv_number.setText(Util.getContactNumberEnc(mobileNumber));
//        holder.flRoot.setMinimumHeight(cellSizeHeight - 20);
//        holder.flRoot.setMinimumWidth(cellSizeWidth - 20);
//        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iAdaperClickListener.OnNavigationItemSelectedListener("btnAccept", holder.flRoot, POSITION);
//            }
//        });
//
//        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iAdaperClickListener.OnNavigationItemSelectedListener("btnRejct", mViewListRecord.get(POSITION), POSITION);
//            }
//        });
    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        return mViewListRecord != null ? mViewListRecord.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivUserIcons = null;
        TextView tvHeaderTxt = null;
        TextView tvDescription = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
        }
    }

}
