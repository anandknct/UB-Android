package com.unitybound.main.friendrequest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.main.friendrequest.beans.FriendRequestListItem;
import com.unitybound.main.friendrequest.listner.IfriendRequestListner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class FriendRequestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Context mContext;
    private final List<FriendRequestListItem> mViewListRecord;
    private IfriendRequestListner ifriendRequestListner= null;

    public FriendRequestListAdapter(Context context,
                                    List<FriendRequestListItem> mViewListRecord,
                                    IfriendRequestListner ifriendRequestListner) {
        this.mContext = context;
        this.mViewListRecord = mViewListRecord;
        this.ifriendRequestListner = ifriendRequestListner;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.friend_request_item_row, parent, false);
        return new FriendRequestListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((FriendRequestListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final FriendRequestListAdapter.ViewHolder holder, final int POSITION) {
        FriendRequestListItem mItemData = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml("<B>"+mItemData.getSendBy().getName()+"</B>"));
        Glide.with(mContext)
                .load(mItemData.getSendBy().getProfileImage())
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
//        holder.tv_mutuals.setText(mItemData.getSendBy().getName());

        if (mItemData.getIsAccept()==0) { // 0 = false
            holder.ll_request_layout.setVisibility(View.VISIBLE);
            holder.ll_accepted_layout.setVisibility(View.GONE);
        }else {
            holder.ll_request_layout.setVisibility(View.GONE);
            holder.ll_accepted_layout.setVisibility(View.VISIBLE);
        }

        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifriendRequestListner.onRejectReq(
                        mViewListRecord.get(POSITION).getId(), POSITION);
            }
        });
        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifriendRequestListner.onAcceptReq(
                        mViewListRecord.get(POSITION).getId(), POSITION);
            }
        });
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
        TextView tvHeaderTxt = null,tv_accept = null,tv_reject = null,tv_accepted = null;
        TextView tv_mutuals = null;
        LinearLayout ll_request_layout = null,ll_accepted_layout = null;
        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tv_mutuals = (TextView) view.findViewById(R.id.tv_mutuals);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            ll_request_layout = (LinearLayout) view.findViewById(R.id.ll_request_layout);
            ll_accepted_layout = (LinearLayout) view.findViewById(R.id.ll_accepted_layout);
            tv_accept =  (TextView) view.findViewById(R.id.tv_accept);
            tv_reject =  (TextView) view.findViewById(R.id.tv_reject);
            tv_accepted =  (TextView) view.findViewById(R.id.tv_accepted);
        }
    }

}
