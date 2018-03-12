package com.unitybound.groups.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.groups.beans.groupMembers.GroupUserDetailsItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */
public class GroupMembersGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final List<GroupUserDetailsItem> mViewListRecord;
    private onMemberGridClickListener clickListner = null;

        public interface onMemberGridClickListener {
        void onDisableMemberButton(String id,int position);
        void onDeleteMemberButton(String id,int position);
    }


    public GroupMembersGridAdapter(Context context, List<GroupUserDetailsItem> mViewListRecord
    ,onMemberGridClickListener clickListener) {
        this.mContext = context;
        this.mViewListRecord = mViewListRecord;
        this.clickListner = clickListener;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.group_members_images_item_row, parent, false);
        return new GroupMembersGridAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((GroupMembersGridAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final GroupMembersGridAdapter.ViewHolder holder, final int POSITION) {
        GroupUserDetailsItem mData = mViewListRecord.get(POSITION);
        holder.tv_name.setText(mData.getFirstName() + " " + mData.getLastName());

        if (mData.getId().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", mContext))) {
//            holder.tv_accept.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_disable.setVisibility(View.GONE);
        }else{
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_disable.setVisibility(View.VISIBLE);
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onDeleteMemberButton(mData.getId(),POSITION);
            }
        });

        holder.iv_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onDisableMemberButton(mData.getId(),POSITION);
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
        private TextView tv_address = null;
        private TextView tv_country = null;
        private TextView tv_name = null;
        private ImageView ivIcon = null;
        private ImageView iv_disable = null,iv_delete = null;
        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_country = (TextView) view.findViewById(R.id.tv_country);
            iv_disable = (ImageView) view.findViewById(R.id.iv_disable);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }

}
