package com.unitybound.events.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.events.fragment.beans.eventDetailParticipants.EventMemberDetailsResponse;
import com.unitybound.utility.Util;

import java.util.ArrayList;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */
public class EventParticipantsGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final ArrayList<EventMemberDetailsResponse> mViewListRecord;


    public EventParticipantsGridAdapter(Context context, ArrayList<EventMemberDetailsResponse> mViewListRecord
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
        final View view = LayoutInflater.from(context).inflate(R.layout.event_participants_images_row, parent, false);
        return new EventParticipantsGridAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((EventParticipantsGridAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final EventParticipantsGridAdapter.ViewHolder holder, final int POSITION) {
        Glide.with(context)
                .load(Util.isNull(mViewListRecord.get(POSITION).getProfileImage()))
                .placeholder(R.drawable.user_profile_def)
                .skipMemoryCache(false)
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
