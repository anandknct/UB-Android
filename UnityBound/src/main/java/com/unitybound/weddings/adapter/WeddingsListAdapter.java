package com.unitybound.weddings.adapter;

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
import com.unitybound.utility.Util;
import com.unitybound.weddings.beans.allWeddings.DataItem;
import com.unitybound.weddings.fragment.WeddingsFragment;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class WeddingsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int LIST_TYPE = 0;
    private List<com.unitybound.weddings.beans.myWeddings.DataItem> mAllMyWeadding = null;

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

    }


    private final Context context;
    private List<DataItem> mViewListRecord;
    private IListAdapterCallback iListAdapterCallback = null;

    public WeddingsListAdapter(Context context, List<DataItem> mViewListRecord,
                               IListAdapterCallback iListAdapterCallback) {
        this.context = context;
        this.mViewListRecord = mViewListRecord;
        this.iListAdapterCallback = iListAdapterCallback;
        LIST_TYPE = 0;
    }

    public WeddingsListAdapter(Context context,
                               List<com.unitybound.weddings.beans.myWeddings.DataItem>
                                       allMyWeadding,
                               WeddingsFragment iListAdapterCallback) {
        this.context = context;
        this.mAllMyWeadding = allMyWeadding;
        this.iListAdapterCallback = iListAdapterCallback;
        LIST_TYPE = 1;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.weddings_item_row, parent,
                false);
        return new WeddingsListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((WeddingsListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final WeddingsListAdapter.ViewHolder holder, final int POSITION) {
        if (LIST_TYPE == 0) {
            DataItem mDataItem = mViewListRecord.get(POSITION);
            holder.tvHeaderTxt.setText(
                    mDataItem.getWeddingGroomName() + " & " + mDataItem.getWeddingBrideName());
            Glide.with(context)
                    .load(mDataItem.getWeddingImage())
                    .placeholder(R.drawable.profile_def_user_image)
                    .skipMemoryCache(false)
                    .override(R.dimen._96sdp,R.dimen._96sdp)
                    .into(holder.ivUserIcons);
            holder.tv_wedding_time.setText(Html.fromHtml(Util.weddingDetailFormator(mDataItem.getWeddingDate())));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner("btnRejct", POSITION);
                }
            });
        } else if (LIST_TYPE == 1) {
            com.unitybound.weddings.beans.myWeddings.DataItem mDataItem = mAllMyWeadding.get(POSITION);
            holder.tvHeaderTxt.setText(
                    mDataItem.getWeddingGroomName() + " & " + mDataItem.getWeddingBrideName());
            Glide.with(context)
                    .load(mDataItem.getWeddingImageS())
                    .placeholder(R.drawable.mobile_churches_screen)
                    .skipMemoryCache(false)
                    .override(R.dimen._96sdp,R.dimen._96sdp)
                    .into(holder.ivUserIcons);
            holder.tv_wedding_time.setText(Html.fromHtml(Util.weddingDetailFormator(mDataItem.getWeddingDate())));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListAdapterCallback.onItemClickListner("btnRejct", POSITION);
                }
            });
        }
    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        if (LIST_TYPE == 0) {
            return mViewListRecord != null ? mViewListRecord.size() : 0;
        } else if (LIST_TYPE == 1) {
            return mAllMyWeadding != null ? mAllMyWeadding.size() : 0;
        } else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout llMain;
        private ImageView ivUserIcons = null;
        TextView tvHeaderTxt = null;
        TextView tv_wedding_time = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tv_wedding_time = (TextView) view.findViewById(R.id.tv_wedding_time);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            llMain = (RelativeLayout) view.findViewById(R.id.ll_main);
        }
    }

}
