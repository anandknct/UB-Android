package com.unitybound.church.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.church.beans.libraryListMode.ChurchDocumentItem;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Nikhil.Jogdand 14-07-2017.
 */
public class ChurchLibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<ChurchDocumentItem> allChurchDocuments;
    private AdapterClickListener listner = null;
    private List<com.unitybound.church.beans.churchJoinedMembers.ChurchUserDetailsItem> mJoinedMembers = null;

    public ChurchLibraryAdapter(Context churchDetailsActivity,
                                List<ChurchDocumentItem> allChurchDocuments,
                                AdapterClickListener churchDetailsActivity1) {

        this.context = churchDetailsActivity;
        this.allChurchDocuments = allChurchDocuments;
        this.listner = churchDetailsActivity1;
    }

    public interface AdapterClickListener {
        void onEditLibraryClick(String blockedUserId, String positionToRemove, String type);

        void onDeleteDocumentClick(String deletedUserId, int positionToRemove);
    }


    /**
     * @param parent   the view group
     * @param viewType the view type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.library_layout_row, parent, false);
        return new ChurchLibraryAdapter.ViewHolder(view);
    }

    /**
     * @param holder   the view holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewListData2((ViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return allChurchDocuments!=null ? allChurchDocuments.size() : 0;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_doc;
        private ImageView iv_delete;
        private TextView edt_doc_tittle1;
        private ImageView ivIcon = null;

        /**
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            edt_doc_tittle1 = (TextView) view.findViewById(R.id.edt_doc_tittle1);
            iv_doc = (ImageView) view.findViewById(R.id.iv_edit_doc);
            iv_delete = (ImageView) view.findViewById(R.id.iv_edit_delete);
        }
    }

    /**
     * @param holder   the view holder
     * @param POSITION the adapter position
     */
    private void bindViewListData2(ChurchLibraryAdapter.ViewHolder holder, final int POSITION) {
        ChurchDocumentItem mData = allChurchDocuments.get(POSITION);
        holder.edt_doc_tittle1.setText(mData.getDocumentName());

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onDeleteDocumentClick(mData.getId(), POSITION);
            }
        });

        holder.iv_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] separated;
//                separated = (mData.getDocumentFile().trim()).split(".");
                StringTokenizer tokens = new StringTokenizer(mData.getDocumentFile().trim(), ".");
                String first = tokens.nextToken();// this will contain "Fruit"
                String second = tokens.nextToken();
//                String type = separated[1];
                listner.onEditLibraryClick(mData.getDocumentFile().trim(), mData.getDocumentName(),second);
            }
        });
    }

}
