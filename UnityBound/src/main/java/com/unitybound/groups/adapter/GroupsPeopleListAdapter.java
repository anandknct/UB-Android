package com.unitybound.groups.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */

public class GroupsPeopleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IListAdapterCallback {

        public void onItemClickListner(String s, int position);

    }


    private final Context context;
    private final ArrayList<FriendRequestData> mViewListRecord;
    private IListAdapterCallback iListAdapterCallback = null;
    public GroupsPeopleListAdapter(Context context, ArrayList<FriendRequestData> mViewListRecord,
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
        final View view = LayoutInflater.from(context).inflate(R.layout.groups_people_item_row, parent, false);
        return new GroupsPeopleListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((GroupsPeopleListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final GroupsPeopleListAdapter.ViewHolder holder, final int POSITION) {
        holder.tvHeaderTxt.setText("Adrian Phillip");
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
                if (holder.cbSelection.isChecked()){
                    holder.cbSelection.setChecked(false);
                    iListAdapterCallback.onItemClickListner("btnUnChecked", POSITION);
                }else {
                    holder.cbSelection.setChecked(true);
                    iListAdapterCallback.onItemClickListner("btnChecked", POSITION);
                }

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
        TextView tvHeaderTxt = null;
        CheckBox cbSelection = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            cbSelection = (CheckBox) view.findViewById(R.id.cb_user_selection);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }

}
