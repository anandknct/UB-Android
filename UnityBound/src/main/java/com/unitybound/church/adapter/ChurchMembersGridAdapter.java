package com.unitybound.church.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.church.beans.detailMembers.ChurchUserDetailsItem;
import com.unitybound.church.fragment.ChurchDetailsActivity;
import com.unitybound.church.listners.IchurchDetailMemberRow;
import com.unitybound.groups.beans.groupMembers.GroupUserDetailsItem;
import com.unitybound.groups.fragment.GroupsDetailsActivity;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.Jogdand 18-02-2018.
 */
public class ChurchMembersGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private List<GroupUserDetailsItem> mGroupMembers = null;
    private IchurchDetailMemberRow ichurchDetailMemberRow = null;
    private List<ChurchUserDetailsItem> mViewListRecord;
    private List<com.unitybound.church.beans.churchJoinedMembers.ChurchUserDetailsItem> mJoinedMembers = null;
    int LIST_TYPE = 0;
    private String ChurchAddedBy = "";

//    public interface AdapterClickListener {
//        void onBlockMemberClick(String blockedUserId, int positionToRemove);
//
//        void onDeleteDocumentClick(String deletedUserId, int positionToRemove);
//    }


    public ChurchMembersGridAdapter(Context context, List<ChurchUserDetailsItem> mViewListRecord) {
        this.context = context;
//        this.cellSizeWidth = Utils.getScreenWidth(context) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(context) / 8;
        this.mViewListRecord = mViewListRecord;
        LIST_TYPE = 0;
    }

    public ChurchMembersGridAdapter(ChurchDetailsActivity context,
                                    List<ChurchUserDetailsItem> allChurchMembers,
                                    IchurchDetailMemberRow adapterClickListener, String churchadd) {
        this.context = context;
        this.mViewListRecord = allChurchMembers;
        this.ichurchDetailMemberRow = adapterClickListener;
        LIST_TYPE = 0;

        ChurchAddedBy = churchadd;
    }

    public ChurchMembersGridAdapter(Context churchDetailsActivity,
                                    List<com.unitybound.church.beans.churchJoinedMembers
                                            .ChurchUserDetailsItem> allChurchJoinedMembers,
                                    IchurchDetailMemberRow ichurchDetailMemberRow, boolean unused) {

        this.context = churchDetailsActivity;
        this.mJoinedMembers = allChurchJoinedMembers;
        this.ichurchDetailMemberRow = ichurchDetailMemberRow;
        LIST_TYPE = 1;
    }

    /**
     * Constructor called from group Members list response
     */
    public ChurchMembersGridAdapter(Context groupsDetailsActivity,
                                    List<GroupUserDetailsItem> allChurchJoinedMembers,
                                    GroupsDetailsActivity groupsDetailsActivity1,
                                    boolean unused1, boolean unused2) {
        this.context = groupsDetailsActivity;
        this.mGroupMembers = allChurchJoinedMembers;
//        this.ichurchDetailMemberRow = ichurchDetailMemberRow;
        LIST_TYPE = 2;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.members_images_item_row, parent, false);
        return new ChurchMembersGridAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (LIST_TYPE == 0) {
            bindViewListData((ChurchMembersGridAdapter.ViewHolder) holder, position);
        } else if (LIST_TYPE == 1) {
            bindViewListData2((ChurchMembersGridAdapter.ViewHolder) holder, position);
        } else if (LIST_TYPE == 2) {
            bindViewListData3((ChurchMembersGridAdapter.ViewHolder) holder, position);
        }
    }

    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final ChurchMembersGridAdapter.ViewHolder holder, final int POSITION) {
        ChurchUserDetailsItem mData = mViewListRecord.get(POSITION);
        holder.tv_name.setText(Util.isNull(mData.getFirstName()) + " " + Util.isNull(mData.getLastName()));
        holder.tv_address.setText(Util.isNull(mData.getCity()));
        holder.tv_country.setText(Util.isNull(mData.getCountry()));
        Glide.with(context)
                .load(Util.isNull(mData.getProfileImage()))
                .override(60, 60)
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false).centerCrop()
                .into(holder.ivIcon);

        if(ChurchAddedBy.equals(mData.getId())) {
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_disable.setVisibility(View.GONE);
        }
        else
        {
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_disable.setVisibility(View.VISIBLE);
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ichurchDetailMemberRow!=null)
                ichurchDetailMemberRow.onDeleteClick(mData.getId(), POSITION);
            }
        });

        holder.iv_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ichurchDetailMemberRow!=null)
                ichurchDetailMemberRow.onEditClick(mData.getId(), POSITION);
            }
        });
    }

    private void bindViewListData3(ViewHolder holder, int POSITION) {
        GroupUserDetailsItem mData = mGroupMembers.get(POSITION);
        holder.tv_name.setText(mData.getFirstName() + " " + mData.getLastName());
//        holder.tv_address.setText();
        Glide.with(context)
                .load(mData.getProfileImage())
                .override(60, 60)
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false).centerCrop()
                .into(holder.ivIcon);

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        return mViewListRecord != null ? mViewListRecord.size() :
                mJoinedMembers != null ? mJoinedMembers.size() :
                        mGroupMembers != null ? mGroupMembers.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_address;
        private final ImageView iv_delete;
        private final ImageView iv_disable;
        private final TextView tv_country;
        private final TextView tv_name;
        private ImageView ivIcon = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.iv_icon);
            tv_address = view.findViewById(R.id.tv_address);
            tv_name = view.findViewById(R.id.tv_name);
            tv_country = view.findViewById(R.id.tv_country);
            iv_disable = view.findViewById(R.id.iv_disable);
            iv_delete = view.findViewById(R.id.iv_delete);
        }
    }

    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData2(final ChurchMembersGridAdapter.ViewHolder holder, final int POSITION) {
        com.unitybound.church.beans.churchJoinedMembers.ChurchUserDetailsItem mData = mJoinedMembers.get(POSITION);
        holder.tv_name.setText(mData.getFirstName() + " " + mData.getLastName());
//        holder.tv_address.setText();
        Glide.with(context)
                .load(mData.getProfileImage())
                .override(60, 60)
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false).centerCrop()
                .into(holder.ivIcon);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ichurchDetailMemberRow.onDeleteClick(mData.getId(), POSITION);
            }
        });

        holder.iv_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ichurchDetailMemberRow.onEditClick(mData.getId(), POSITION);
            }
        });
    }

}
