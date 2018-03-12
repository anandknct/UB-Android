package com.unitybound.events.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.account.beans.FriendsItem;
import com.unitybound.utility.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */
public class ProfileFriendsAddEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<FriendsItem> mViewListRecord;
    private RecyclerView.ViewHolder mHolder = null;
    ArrayList<String> stringArrayList = new ArrayList<>();

    public ProfileFriendsAddEventAdapter(Context context, List<FriendsItem> mViewListRecord
    ) {
        this.context = context;
        this.mViewListRecord = mViewListRecord;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.profile_friends_add_event_list_row, parent, false);
        return new ProfileFriendsAddEventAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mHolder = holder;
        bindViewListData((ProfileFriendsAddEventAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final ProfileFriendsAddEventAdapter.ViewHolder holder, final int POSITION) {
        Glide.with(context)
                .load(Util.isNull(mViewListRecord.get(POSITION).getProfileImage()))
                .placeholder(R.drawable.ic_me)
                .skipMemoryCache(false)
                .into(holder.ivIcon);
        holder.tv_user_name.setText(Util.isNull(mViewListRecord.get(POSITION).getName()));
        holder.cbUserSelection.setVisibility(View.VISIBLE);
        holder.cbUserSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedId = mViewListRecord.get(POSITION).getId();
                if (holder.cbUserSelection.isChecked()) {
                    if (stringArrayList != null && !stringArrayList.contains(selectedId)) {
                        stringArrayList.add(selectedId);
                    }
                } else {
                    if (stringArrayList != null && stringArrayList.contains(selectedId)) {
                        stringArrayList.remove(selectedId);
                    }
                }
            }
        });
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public ArrayList<String> getAllCheckedBox() {

        return stringArrayList;
    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        return mViewListRecord != null ? mViewListRecord.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_user_name = null;
        private ImageView ivIcon = null;
        private CheckBox cbUserSelection = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            cbUserSelection = (CheckBox) view.findViewById(R.id.cb_user_selection);
        }
    }

}
