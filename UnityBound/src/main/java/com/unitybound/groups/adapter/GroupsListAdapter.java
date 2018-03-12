package com.unitybound.groups.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.groups.beans.UserGroupsItem;
import com.unitybound.groups.beans.joinedGroups.UserJoinedGroupsItem;
import com.unitybound.groups.fragment.GroupsFragment;

import java.util.List;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> mAllMyClosedGrops = null;

    public interface IListAdapterCallback {
        public void onItemClickListner(String s, int position);
    }

    private Context context;
    private List<UserJoinedGroupsItem> mMyJoinedGrops = null;
    private List<UserGroupsItem> mViewListRecord = null;
    private IListAdapterCallback iListAdapterCallback = null;
    private List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> mAllPublicGrops = null;
    private int mListType = 0;

    /**
     * My groups adapter
     * @param context
     * @param mViewListRecord
     * @param iListAdapterCallback
     */
    public GroupsListAdapter(Context context, List<UserGroupsItem> mViewListRecord,
                             IListAdapterCallback iListAdapterCallback) {
        this.context = context;
//        this.cellSizeWidth = Utils.getScreenWidth(context) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(context) / 8;
        this.mViewListRecord = mViewListRecord;
        this.iListAdapterCallback = iListAdapterCallback;
        mListType = 0;
    }

    /**
     * Joined group adapter
     * @param context
     * @param iListAdapterCallback
     * @param allMyJoinedGrops
     */
    public GroupsListAdapter(Context context,
                             IListAdapterCallback iListAdapterCallback,
                             List<UserJoinedGroupsItem> allMyJoinedGrops) {
        this.context = context;
        this.mMyJoinedGrops = allMyJoinedGrops;
        this.iListAdapterCallback = iListAdapterCallback;
        mListType = 1;
    }

    /**
     * Public group adapter
     * @param allAllPublicGrops
     * @param context
     * @param groupsFragment
     */
    public GroupsListAdapter(List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> allAllPublicGrops, Context context, GroupsFragment groupsFragment) {
        this.context = context;
        this.mAllPublicGrops = allAllPublicGrops;
        this.iListAdapterCallback = groupsFragment;
        mListType = 2;
    }

    /**
     * Closed groups adapter
     * @param iListAdapterCallback
     * @param context
     * @param allMyClosedGrops
     */
    public GroupsListAdapter(IListAdapterCallback iListAdapterCallback,Context context, List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> allMyClosedGrops) {
        this.context = context;
        this.mAllMyClosedGrops = allMyClosedGrops;
        this.iListAdapterCallback = iListAdapterCallback;
        mListType = 3;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.groups_item_row, parent, false);
        return new GroupsListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((GroupsListAdapter.ViewHolder) holder, position);
    }

    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final GroupsListAdapter.ViewHolder holder, final int POSITION) {
        if (mListType == 0 && mViewListRecord != null) {
            UserGroupsItem mData = mViewListRecord.get(POSITION);
            holder.tvHeaderTxt.setText(mData.getGroupName());
            holder.tvDescription.setText(mData.getGroupDescription());
            holder.tvMembers.setText(mData.getGroupMembers() + " Members");
            Glide.with(context)
                    .load(mData.getGroupImage())
                    .placeholder(R.drawable.profile_def_user_image)
                    .skipMemoryCache(false)
                    .into(holder.ivUserIcons);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
//                }
//            });
//
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
                }
            });
        } else if (mListType == 1 && mMyJoinedGrops != null) {

            UserJoinedGroupsItem mData = mMyJoinedGrops.get(POSITION);
            holder.tvHeaderTxt.setText(mData.getGroupName());
            holder.tvDescription.setText(mData.getGroupDescription());
            holder.tvMembers.setText(mData.getGroupMembers() + " Members");
            Glide.with(context)
                    .load(mData.getGroupImage())
                    .placeholder(R.drawable.mobile_churches_screen)
                    .skipMemoryCache(false)
                    .into(holder.ivUserIcons);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
//                }
//            });
//
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
                }
            });
        }  else if (mListType == 2 && mAllPublicGrops != null) {

            com.unitybound.groups.beans.PublicGroups.UserGroupsItem mData = mAllPublicGrops.get(POSITION);
            holder.tvHeaderTxt.setText(mData.getGroupName());
            holder.tvDescription.setText(mData.getGroupDescription());
            holder.tvMembers.setText(mData.getGroupMembers() + " Members");
            Glide.with(context)
                    .load(mData.getGroupImage())
                    .placeholder(R.drawable.mobile_churches_screen)
                    .skipMemoryCache(false)
                    .into(holder.ivUserIcons);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
//                }
//            });
//
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
                }
            });
        } else if (mListType == 3 && mAllMyClosedGrops != null) {

            com.unitybound.groups.beans.PublicGroups.UserGroupsItem mData = mAllMyClosedGrops.get(POSITION);
            holder.tvHeaderTxt.setText(mData.getGroupName());
            holder.tvDescription.setText(mData.getGroupDescription());
            holder.tvMembers.setText(mData.getGroupMembers() + " Members");
            Glide.with(context)
                    .load(mData.getGroupImage())
                    .placeholder(R.drawable.mobile_churches_screen)
                    .skipMemoryCache(false)
                    .into(holder.ivUserIcons);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
//                }
//            });
//
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner(mData.getId(), POSITION);
                }
            });
        }

    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        if (mListType == 0) {
            return mViewListRecord != null ? mViewListRecord.size() : 0;
        } else if (mListType == 1) {
            return mMyJoinedGrops != null ? mMyJoinedGrops.size() : 0;
        } else if (mListType == 2) {
            return mAllPublicGrops != null ? mAllPublicGrops.size() : 0;
        } else if (mListType == 3) {
            return mAllMyClosedGrops != null ? mAllMyClosedGrops.size() : 0;
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llMain;
        private TextView tvMembers = null;
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
            tvMembers = (TextView) view.findViewById(R.id.tv_address_1);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }

}
