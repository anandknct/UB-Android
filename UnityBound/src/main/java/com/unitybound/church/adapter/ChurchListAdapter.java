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
import com.unitybound.church.beans.ChurchesItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class ChurchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);
        public void onUserNameClickListener(String s, int position);
    }


    private final Context mContext;
    private final List<ChurchesItem> mViewListRecord;
    private IListAdapterCallback iListAdapterCallback = null;
    public ChurchListAdapter(Context context, List<ChurchesItem> mViewListRecord,
                             IListAdapterCallback iListAdapterCallback) {
        this.mContext = context;
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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.churches_item_row, parent, false);
        return new ChurchListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((ChurchListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final ChurchListAdapter.ViewHolder holder, final int POSITION) {
        ChurchesItem mChurch = mViewListRecord.get(POSITION);
        holder.tvHeaderTxt.setText(Html.fromHtml(Util.isNull(mChurch.getChurchName()))); // "<B><I>Andrian Phillips</I></B>"
        holder.tvDescription.setText(Html.fromHtml(Util.isNull(mChurch.getChurchState())));
        holder.tv_address_1.setText(Html.fromHtml(Util.isNull(mChurch.getChurchCountry())));// "<I> likes he has post that\\nyou shared </I>"
        Glide.with(mContext)
                .load(Util.isNull(mChurch.getChurchImage()))
                .placeholder(R.drawable.profile_def_user_image)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
//        String mobileNumber = String.valueOf(mViewListRecord.get(POSITION).getMobileNumber());
//        holder.tv_number.setText(Util.getContactNumberEnc(mobileNumber));
//        holder.flRoot.setMinimumHeight(cellSizeHeight - 20);
//        holder.flRoot.setMinimumWidth(cellSizeWidth - 20);
        holder.tvHeaderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListAdapterCallback.onUserNameClickListener("btnUser", POSITION);
            }
        });
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
        private ImageView ivUserIcons = null;
        TextView tvHeaderTxt = null,tv_address_1 = null;
        TextView tvDescription = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tv_address_1 = (TextView) view.findViewById(R.id.tv_address_1);
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }

}
