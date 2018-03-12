package com.unitybound.account.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.account.beans.photos.PhotosItem;
import com.unitybound.account.listner.IAdapterClickListner;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by nikhil.jogdand on 04-07-2017.
 */
public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.RecyclerViewHolders> {

    private final IAdapterClickListner dashBoardFragment;
    private List<PhotosItem> itemList;
    private Context context;

    public GridRecyclerViewAdapter(Context context, List<PhotosItem> itemList, IAdapterClickListner dashBoardFragment) {
        this.itemList = itemList;
        this.context = context;
        this.dashBoardFragment = dashBoardFragment;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_grid_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        int displayWidth = Util.getScreenWidth(context);

        holder.countryPhoto.getLayoutParams().height = (displayWidth / 3);
        holder.countryPhoto.getLayoutParams().width = (displayWidth / 3);
        Glide.with(context).load(itemList.get(position).getPath()) // "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRopQ6yW-qzfQpeKPFisdcpisOvnj3FOHVpipmLQ5aKaaTFglyu7w"
                .thumbnail(0.5f)
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_photos_placeholder)
                .into(holder.countryPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashBoardFragment.onAdapterItemClick("", position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView countryName;
        public ImageView countryPhoto;

        public RecyclerViewHolders(View itemView) {
            super(itemView);

//            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
        }


    }

}