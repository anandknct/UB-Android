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

import com.unitybound.R;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class WeddingsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

    }


    private final Context context;
    private final ArrayList<FriendRequestData> mViewListRecord;
    private IListAdapterCallback iListAdapterCallback = null;
    public WeddingsListAdapter(Context context, ArrayList<FriendRequestData> mViewListRecord,
                               IListAdapterCallback iListAdapterCallback) {
        this.context = context;
//        this.cellSizeWidth = Utils.getScreenWidth(context) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(context) / 8;
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
        final View view = LayoutInflater.from(context).inflate(R.layout.weddings_item_row, parent, false);
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
        holder.tvHeaderTxt.setText(Html.fromHtml("<B>Andrian Phillips</B> likes he has post that\\nyou shared"));
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
