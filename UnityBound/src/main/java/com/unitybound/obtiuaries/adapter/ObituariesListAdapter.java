package com.unitybound.obtiuaries.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.obtiuaries.beans.DataItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class ObituariesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

    }


    private final Context context;
    private final List<DataItem> mViewListRecord;
    private IListAdapterCallback iListAdapterCallback = null;
    public ObituariesListAdapter(Context context, List<DataItem> mViewListRecord,
                                 IListAdapterCallback iListAdapterCallback) {
        this.context = context;
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
        final View view = LayoutInflater.from(context).inflate(R.layout.obituaries_item_row, parent, false);
        return new ObituariesListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((ObituariesListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    @SuppressLint("SetTextI18n")
    private void bindViewListData(final ObituariesListAdapter.ViewHolder holder, final int POSITION) {
        DataItem mData = mViewListRecord.get(POSITION);
        Glide.with(context)
                .load(mData.getObituaryImage())
                .placeholder(R.drawable.profile_def_user_image)
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
        holder.tvHeaderTxt.setText(Html.fromHtml(mData.getObituaryName()));
        String mFromDate  =Util.weddingDateFormator(mData.getObituaryBirthDate());
        String mToDate  =Util.weddingDateFormator(mData.getObituaryDeathDate());
        holder.tvDescription.setText(mFromDate+" "+mToDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListAdapterCallback.onItemClickListner("btnReject", POSITION);
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
        private final RelativeLayout llMain;
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
            llMain = (RelativeLayout) view.findViewById(R.id.ll_main);
        }
    }

}
