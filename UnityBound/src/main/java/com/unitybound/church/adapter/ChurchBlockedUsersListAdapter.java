package com.unitybound.church.adapter;

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
import com.unitybound.church.beans.blockedUsers.ChurchUserDetailsItem;
import com.unitybound.church.listners.IchurchBlockUsers;

import java.util.ArrayList;

/**
 * Created by N.J 22-02-2017.
 */

public class ChurchBlockedUsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final ArrayList<ChurchUserDetailsItem> mViewListRecord;
    private IchurchBlockUsers ifriendRequestListner= null;

    public ChurchBlockedUsersListAdapter(Context context,
                                         ArrayList<ChurchUserDetailsItem> mViewListRecord,
                                         IchurchBlockUsers ifriendRequestListner) {
        this.mContext = context;
//        this.cellSizeWidth = Utils.getScreenWidth(mContext) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(mContext) / 8;
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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.church_blocked_users_item_row, parent, false);
        return new ChurchBlockedUsersListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((ChurchBlockedUsersListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final ChurchBlockedUsersListAdapter.ViewHolder holder, final int POSITION) {
        ChurchUserDetailsItem mItemData = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml(mItemData.getFirstName()+" "+mItemData.getFirstName()));
        Glide.with(mContext)
                .load(mItemData.getProfileImage())
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
//        holder.tv_city.setText(mItemData.getSendBy().getName());
//        holder.tv_state_country.setText(mItemData.getSendBy().getName());

        holder.tv_unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifriendRequestListner.onUnblockClick(
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
        TextView tvHeaderTxt = null,tv_state_country = null,tv_unblock = null;
        TextView tv_city = null;
        LinearLayout ll_request_layout = null,ll_accepted_layout = null;
        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tv_city = (TextView) view.findViewById(R.id.tv_city);
            tv_state_country =  (TextView) view.findViewById(R.id.tv_state_country);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            tv_unblock =  (TextView) view.findViewById(R.id.tv_unblock);
        }
    }

}
