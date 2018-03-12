package com.unitybound.groups.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.groups.beans.blockUserList.BlockUsersItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class GroupBlockUsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private onBlockUnblockClick unblockClick = null;

    public interface onBlockUnblockClick {
        void onBlockUnblockButtonClick(String id, int position);
    }

    private final Context mContext;
    private final List<BlockUsersItem> mViewListRecord;

    public GroupBlockUsersListAdapter(Context context, List<BlockUsersItem> mViewListRecord,
                                      onBlockUnblockClick unblockClick) {
        this.mContext = context;
        this.unblockClick = unblockClick;
        this.mViewListRecord = mViewListRecord;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.group_user_blocked_item_row, parent, false);
        return new GroupBlockUsersListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((GroupBlockUsersListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final GroupBlockUsersListAdapter.ViewHolder holder, final int POSITION) {
        BlockUsersItem mDataItem = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml("<B>" + mDataItem.getFirstName() + " " + mDataItem.getLastName() + "</B>"));
        Glide.with(mContext)
                .load(Util.isNull(mDataItem.getProfileImage()))
                .placeholder(R.drawable.user_comment_def)
                .into(holder.ivUserIcons);

        if (mDataItem.getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
//            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
        }else{
            holder.tv_accept.setVisibility(View.VISIBLE);
        }

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unblockClick.onBlockUnblockButtonClick(mViewListRecord.get(POSITION).getId(),
                        POSITION);
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
        TextView tvHeaderTxt = null;
        TextView tv_address_1 = null;
        TextView tv_mutuals = null;
        Button tv_accept = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            tv_address_1 = (TextView) view.findViewById(R.id.tv_address_1);
            tv_mutuals = (TextView) view.findViewById(R.id.tv_mutuals);
            tv_accept = (Button) view.findViewById(R.id.tv_accept);
        }
    }

}
