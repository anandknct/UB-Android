package com.unitybound.groups.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.unitybound.R;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */
public class GroupMembersGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final ArrayList<FriendRequestData> mViewListRecord;


    public GroupMembersGridAdapter(Context context, ArrayList<FriendRequestData> mViewListRecord
            ) {
        this.context = context;
//        this.cellSizeWidth = Utils.getScreenWidth(context) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(context) / 8;
        this.mViewListRecord = mViewListRecord;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.group_members_images_item_row, parent, false);
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
//        holder.tv_name.setText(mViewListRecord.get(POSITION).getUsername());
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
        return mViewListRecord != null ? mViewListRecord.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

}
