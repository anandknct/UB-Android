package com.unitybound.groups.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.utility.Util;

import java.util.List;

/**
 * Created by Nikhil.jogdand 14-07-2017.
 */
public class GroupPhotosGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final List<String> mPhotos;
    private onGroupPhotosClickListener onGroupImageClickListener;

    interface onGroupPhotosClickListener {
        void onGroupImageClickListener(String url,int position);
    }


    public GroupPhotosGridAdapter(Context context, List<String> mViewListRecord
            ,onGroupPhotosClickListener onGroupImageClickListener) {
        this.mContext = context;
//        this.cellSizeWidth = Utils.getScreenWidth(mContext) / 2;
//        this.cellSizeHeight = Utils.getScreenHeight(mContext) / 8;
        this.mPhotos = mViewListRecord;
        this.onGroupImageClickListener = onGroupImageClickListener;
    }

    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.group_photos_images_row,
                parent, false);
        return new GroupPhotosGridAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData((GroupPhotosGridAdapter.ViewHolder) holder, position);
    }


    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData(final GroupPhotosGridAdapter.ViewHolder holder, final int POSITION) {
        int displayWidth = Util.getScreenWidth(mContext);

        holder.ivIcon.getLayoutParams().height = (displayWidth / 3);
        holder.ivIcon.getLayoutParams().width = (displayWidth / 3);

        Glide.with(mContext)
                .load(Util.isNull(mPhotos.get(POSITION)))
                .placeholder(R.drawable.ic_photos_placeholder).dontAnimate()
                .skipMemoryCache(false)
                .into(holder.ivIcon);
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGroupImageClickListener.onGroupImageClickListener(mPhotos.get(POSITION),POSITION);
            }
        });
    }

    /**
     * @return the get item count
     */
    @Override
    public int getItemCount() {
        return mPhotos != null ? mPhotos.size() : 0;
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
