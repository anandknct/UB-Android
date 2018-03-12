package com.unitybound.account.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.account.beans.FriendsItem;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 22-02-2017.
 */

public class ProfileFriendRequestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private profileListClickListener listener = null;
    private String mSelectedUserProfileId;

    public interface profileListClickListener {
        void onUnfriendClickListner(int position,String id);
        void onUSerNameClickClickListner(int position,String id);
    }


    private final Context context;
    private final List<FriendsItem> mViewListRecord;

    public ProfileFriendRequestListAdapter(Context context, List<FriendsItem> mViewListRecord,
                                           profileListClickListener listner, String mSelectedUserProfileId) {
        this.context = context;
        this.mViewListRecord = mViewListRecord;
        this.listener = listner;
        this.mSelectedUserProfileId = mSelectedUserProfileId;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.friend_request_item_row, parent, false);
        return new ProfileFriendRequestListAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((ProfileFriendRequestListAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final ProfileFriendRequestListAdapter.ViewHolder holder, final int POSITION) {
        holder.tvHeaderTxt.setText(Html.fromHtml(mViewListRecord.get(POSITION).getName()));
        Glide.with(context)
                .load( Util.isNull(mViewListRecord.get(POSITION).getProfileImage()))
                .placeholder(R.drawable.user_profile_def) // ic_me
                .skipMemoryCache(false)
                .into(holder.ivUserIcons);
        holder.tv_mutuals.setText(mViewListRecord.get(POSITION).getMutualFriends() + " Mutual friends");
        String state = Util.isNull(mViewListRecord.get(POSITION).getState());
        String country = Util.isNull(mViewListRecord.get(POSITION).getCountry());
        holder.tv_address.setText((state.length() > 0 ? state + "," : "") +", "+ (country.length() > 0 ? country : ""));
        if (this.mSelectedUserProfileId!=null) {
            holder.ll_accepted_layout.setVisibility(View.GONE);
        }else{
            holder.ll_accepted_layout.setVisibility(View.VISIBLE);
        }
        holder.tv_accepted.setText(context.getString(R.string.str_unfriend));
        holder.tv_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                iAdaperClickListener.OnNavigationItemSelectedListener("btnRejct", mViewListRecord.get(POSITION), POSITION);
                Toast.makeText(context, context.getString(R.string.str_under_progress)
                        , Toast.LENGTH_SHORT).show();
                listener.onUnfriendClickListner(POSITION,mViewListRecord.get(POSITION).getId());
            }
        });
        holder.tvHeaderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUSerNameClickClickListner(POSITION,mViewListRecord.get(POSITION).getId());
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
        private TextView tv_accepted = null;
        private TextView tv_accept = null;
        private ImageView ivUserIcons = null;
        TextView tvHeaderTxt = null;
        TextView tv_mutuals = null,tv_address;
        private LinearLayout ll_accepted_layout;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            tvHeaderTxt = (TextView) view.findViewById(R.id.tv_header_txt);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_mutuals = (TextView) view.findViewById(R.id.tv_mutuals);
            ivUserIcons = (ImageView) view.findViewById(R.id.iv_user_icons);
            tv_accept = (TextView) view.findViewById(R.id.tv_accept);
            ll_accepted_layout = (LinearLayout) view.findViewById(R.id.ll_accepted_layout);
            tv_accepted = (TextView) view.findViewById(R.id.tv_accepted);
        }
    }

}
