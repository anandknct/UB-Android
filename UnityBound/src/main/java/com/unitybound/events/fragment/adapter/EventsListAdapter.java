package com.unitybound.events.fragment.adapter;

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
import com.unitybound.events.fragment.beans.EventsItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IListAdapterCallback iListAdapterCallback = null;

    public interface IListAdapterCallback {
        public void onItemClickListner(String s, int position);
        public void onUserNameClickListner(String s, int position);

    }


    private final Context mContext;
    private final List<EventsItem> mViewListRecord;
    public EventsListAdapter(Context context, List<EventsItem> mViewListRecord,
                             IListAdapterCallback iListAdapterCallback) {
        this.mContext = context;
//        this.cellSizeWidth = Utils.getScreenWidth(mContext) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(mContext) / 8;
        this.mViewListRecord = mViewListRecord;
        this.iListAdapterCallback = iListAdapterCallback;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.event_item_row, parent, false);
        return new EventsListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((EventsListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final EventsListAdapter.ViewHolder holder, final int POSITION) {
        EventsItem mEvents = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml(mEvents.getEventName())); // "<B>Andrian Phillips</B> likes he has post that\\nyou shared"
        holder.tvDescription.setText(Html.fromHtml(mEvents.getEventDescription()));
        holder.tvDescription.setText(Html.fromHtml(mEvents.getEventDescription()));
        Glide.with(mContext)
                .load(mEvents.getEventImage())
                .placeholder(R.drawable.profile_def_user_image)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
        holder.tvAddress1.setText(Html.fromHtml(mEvents.getEventLocation()));
        holder.tvAddress2.setText(Html.fromHtml(Util.eventDateFormator(mViewListRecord.get(POSITION).getEventStartDate())));
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
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListAdapterCallback.onItemClickListner("btnRejct", POSITION);
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
        private final LinearLayout llMain;
        private final TextView tvAddress1;
        private TextView tvAddress2 = null;
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
            tvAddress1 = (TextView) view.findViewById(R.id.tv_address_1);
            tvAddress2 = (TextView) view.findViewById(R.id.tv_address_2);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }

}
