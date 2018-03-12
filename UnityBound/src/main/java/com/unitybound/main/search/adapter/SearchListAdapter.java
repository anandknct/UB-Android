package com.unitybound.main.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unitybound.R;
import com.unitybound.main.search.beans.AllDataTypes;
import com.unitybound.main.search.beans.AllTypeData;
import com.unitybound.main.search.beans.ChurchesItem;
import com.unitybound.main.search.beans.Data;
import com.unitybound.main.search.beans.EventsItem;
import com.unitybound.main.search.beans.GroupsItem;
import com.unitybound.main.search.beans.PeoplesItem;
import com.unitybound.main.search.viewHolder.AllViewHolder;
import com.unitybound.main.search.viewHolder.ChurchViewHolder;
import com.unitybound.main.search.viewHolder.EventsViewHolder;
import com.unitybound.main.search.viewHolder.GroupsViewHolder;
import com.unitybound.main.search.viewHolder.PeoplesViewHolder;

import java.util.ArrayList;

/**
 * Created by Admin on 1/19/2018.
 */

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PeoplesItem> mPeoplesArrayList = null;
    private ArrayList<ChurchesItem> mChurchesArrayList;
    private ArrayList<GroupsItem> mGroupsArrayList;
    private ArrayList<EventsItem> mEventsArrayList;
    private ArrayList<AllDataTypes> mAllTypesArrayList = null;
    private static ViewType mViewType = null;
    private Context context = null;
    private SearchRowItemListener listener = null;
    private int i;

    public interface SearchRowItemListener {
        void onSearchRowItemSelected(String Id,String type);
    }

    public SearchListAdapter(Context context, Data searchResponses,
                             SearchRowItemListener listener, ViewType viewType) {
        this.context = context;
        this.listener = listener;
        if (ViewType.PEOPLE == viewType) {
            this.mPeoplesArrayList = searchResponses.getPeoples();
        } else if (ViewType.CHURCH == viewType) {
            this.mChurchesArrayList = searchResponses.getChurches();
        } else if (ViewType.GROUPS == viewType) {
            this.mGroupsArrayList = searchResponses.getGroups();
        } else if (ViewType.EVENTS == viewType) {
            this.mEventsArrayList = searchResponses.getEvents();
        } else if (ViewType.ALL == viewType) {
            this.mPeoplesArrayList = searchResponses.getPeoples();
            this.mChurchesArrayList = searchResponses.getChurches();
            this.mGroupsArrayList = searchResponses.getGroups();
            this.mEventsArrayList = searchResponses.getEvents();
            prepareAllTypeDataList();
        }

        this.mViewType = viewType;
    }

    private void prepareAllTypeDataList() {
        this.mAllTypesArrayList = new ArrayList<>();
        for (int i = 0; i < mPeoplesArrayList.size(); i++) {
            PeoplesItem item = mPeoplesArrayList.get(i);
            AllTypeData data = new AllTypeData(item.getFirstName() + " " + item.getLastName(),
                    item.getEmail() != null ? item.getEmail() : "Not available!", item.getId(),
                    item.getProfileImage());
            if (i == 0) {
                this.mAllTypesArrayList.add(new AllDataTypes(data, ViewType.PEOPLE,ViewType.PEOPLE.toString()));
            } else {
                this.mAllTypesArrayList.add(new AllDataTypes(data, null,ViewType.PEOPLE.toString()));
            }
        }
        for (int i = 0; i < mGroupsArrayList.size(); i++) {
            GroupsItem item = mGroupsArrayList.get(i);
            AllTypeData data = new AllTypeData(item.getGroupName(),
                    item.getGroupMembers() + " Members", item.getId(),
                    item.getGroup_image());
            if (i == 0) {
                this.mAllTypesArrayList.add(new AllDataTypes(data, ViewType.GROUPS,ViewType.GROUPS.toString()));
            } else {
                this.mAllTypesArrayList.add(new AllDataTypes(data, null,ViewType.GROUPS.toString()));
            }

        }
        for (int i = 0; i < mChurchesArrayList.size(); i++) {
            ChurchesItem item = mChurchesArrayList.get(i);
            AllTypeData data = new AllTypeData(item.getChurchName(),
                    item.getChurchCity() + item.getChurchState(), item.getId(),
                    item.getChurchImage());
            if (i == 0) {
                this.mAllTypesArrayList.add(new AllDataTypes(data, ViewType.CHURCH,ViewType.CHURCH.toString()));
            } else {
                this.mAllTypesArrayList.add(new AllDataTypes(data, null,ViewType.CHURCH.toString()));
            }

        }
        for (int i = 0; i < mEventsArrayList.size(); i++) {
            EventsItem item = mEventsArrayList.get(i);
            AllTypeData data = new AllTypeData(item.getEventName(), item.getEventLocation(), item.getId(),
                    item.getEventImage());
            if (i == 0) {
                this.mAllTypesArrayList.add(new AllDataTypes(data, ViewType.EVENTS,ViewType.EVENTS.toString()));
            } else {
                this.mAllTypesArrayList.add(new AllDataTypes(data, null,ViewType.EVENTS.toString()));
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.mViewType == ViewType.ALL) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_row_layout, parent, false);
//            View itemView1 = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.search_row_layout, parent, false);
//            View itemView2 = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.search_row_layout, parent, false);
//            View itemView4 = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.search_row_layout, parent, false);
//            switch (viewType) {
//                case 0:
            return new AllViewHolder(itemView, this.listener);

//                case 1:
//                    return new GroupsViewHolder(itemView1, this.listener);
//
//                case 2:
//                    return new ChurchViewHolder(itemView2, this.listener);
//
//                case 3:
//                    return new EventsViewHolder(itemView4, this.listener);
//            }
        } else if (this.mViewType == ViewType.PEOPLE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_row_layout, parent, false);
            return new PeoplesViewHolder(itemView, this.listener);
        } else if (this.mViewType == ViewType.GROUPS) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_row_layout, parent, false);
            return new GroupsViewHolder(itemView, this.listener);
        } else if (this.mViewType == ViewType.CHURCH) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_row_layout, parent, false);
            return new ChurchViewHolder(itemView, this.listener);
        } else if (this.mViewType == ViewType.EVENTS) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_row_layout, parent, false);
            return new EventsViewHolder(itemView, this.listener);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position < 3) {
            return 0; // PEOPLE
        } else if (position >= 3 && position < 6) {
            return 1; // CHURCH
        } else if (position >= 6 && position < 9) {
            return 2; // GROUPS
        } else if (position >= 9 && position < 12) {
            return 3; // EVENTS
        } else {
            return 4;
        }
    }

    public Object getAdapterArrayList() {
        if (ViewType.PEOPLE == mViewType) {
            return mPeoplesArrayList;
        } else if (ViewType.CHURCH == mViewType) {
            return this.mChurchesArrayList;
        } else if (ViewType.GROUPS == mViewType) {
            return this.mGroupsArrayList;
        } else if (ViewType.EVENTS == mViewType) {
            return this.mEventsArrayList;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mViewType == ViewType.PEOPLE) {
            if (mPeoplesArrayList != null && mPeoplesArrayList.size() > 0) {
                PeoplesItem peoplesItem = mPeoplesArrayList.get(position);
                PeoplesViewHolder peopleViewholder = ((PeoplesViewHolder) holder);
                peopleViewholder.name.setText(peoplesItem.getFirstName() + " " + peoplesItem.getLastName());
                peopleViewholder.phone.setText(peoplesItem.getEmail() != null ? peoplesItem.getEmail() : "Not available!");
                Glide.with(context).load(peoplesItem.getProfileImage())
                        .thumbnail(0.5f)
                        .crossFade()
                        .override(R.dimen._50sdp,R.dimen._50sdp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(peopleViewholder.thumbnail);
                if (position == 0) {
                    peopleViewholder.tv_my_label.setVisibility(View.VISIBLE);
                    peopleViewholder.rr_top.setVisibility(View.VISIBLE);
                    peopleViewholder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_people, 0, 0, 0);
                    peopleViewholder.tv_my_label.setText(R.string.str_peoples);
                } else {
                    peopleViewholder.tv_my_label.setVisibility(View.GONE);
                    peopleViewholder.rr_top.setVisibility(View.GONE);
                }

                peopleViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onSearchRowItemSelected(peoplesItem.getId(),ViewType.PEOPLE.toString());
                    }
                });
            } else {
                Toast.makeText(context, "Not data found!", Toast.LENGTH_SHORT).show();
            }
        } else if (mViewType == ViewType.GROUPS) {
            if (mGroupsArrayList != null && mGroupsArrayList.size() > 0) {
                GroupsItem groupsItem = mGroupsArrayList.get(position);
                GroupsViewHolder groupsViewHolder = ((GroupsViewHolder) holder);
                groupsViewHolder.name.setText(groupsItem.getGroupName());
                groupsViewHolder.phone.setText(groupsItem.getGroupMembers() + " Members");
                Glide.with(context).load(groupsItem.getGroup_image())
                        .thumbnail(0.5f)
                        .crossFade()
                        .override(R.dimen._50sdp,R.dimen._50sdp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(groupsViewHolder.thumbnail);
                if (position == 0) {
                    groupsViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                    groupsViewHolder.rr_top.setVisibility(View.VISIBLE);
                    groupsViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_groups, 0, 0, 0);
                    groupsViewHolder.tv_my_label.setText(R.string.str_my_groups);
                } else {
                    groupsViewHolder.tv_my_label.setVisibility(View.GONE);
                    groupsViewHolder.rr_top.setVisibility(View.GONE);
                }

                groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onSearchRowItemSelected(groupsItem.getId(),ViewType.GROUPS.toString());
                    }
                });
            } else {
                Toast.makeText(context, "Not data found!", Toast.LENGTH_SHORT).show();
            }
        } else if (mViewType == ViewType.CHURCH) {
            if (mChurchesArrayList != null && mChurchesArrayList.size() > 0) {
                ChurchesItem churchesItem = mChurchesArrayList.get(position);
                ChurchViewHolder churchViewHolder = ((ChurchViewHolder) holder);
                churchViewHolder.name.setText(churchesItem.getChurchName());
                churchViewHolder.phone.setText(churchesItem.getChurchCity() + churchesItem.getChurchState());
                Glide.with(context).load(churchesItem.getChurchImage())
                        .thumbnail(0.5f)
                        .crossFade()
                        .override(R.dimen._50sdp,R.dimen._50sdp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(churchViewHolder.thumbnail);
                if (position == 0) {
                    churchViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                    churchViewHolder.rr_top.setVisibility(View.VISIBLE);
                    churchViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mychurch, 0, 0, 0);
                    churchViewHolder.tv_my_label.setText(R.string.str_my_church);
                } else {
                    churchViewHolder.tv_my_label.setVisibility(View.GONE);
                    churchViewHolder.rr_top.setVisibility(View.GONE);
                }
                churchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onSearchRowItemSelected(churchesItem.getId(),ViewType.CHURCH.toString());
                    }
                });
            } else {
                Toast.makeText(context, "Not data found!", Toast.LENGTH_SHORT).show();
            }
        } else if (mViewType == ViewType.EVENTS) {
            if (mEventsArrayList != null && mEventsArrayList.size() > 0) {

                EventsItem eventsItem = mEventsArrayList.get(position);
                EventsViewHolder eventsViewHolder = ((EventsViewHolder) holder);
                eventsViewHolder.name.setText(eventsItem.getEventName());
                eventsViewHolder.phone.setText(eventsItem.getEventLocation());
                Glide.with(context).load(eventsItem.getEventImage())
                        .thumbnail(0.5f)
                        .crossFade()
                        .override(R.dimen._50sdp,R.dimen._50sdp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(eventsViewHolder.thumbnail);
                if (position == 0) {
                    eventsViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                    eventsViewHolder.rr_top.setVisibility(View.VISIBLE);
                    eventsViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_events, 0, 0, 0);
                    eventsViewHolder.tv_my_label.setText(R.string.str_my_events);
                } else {
                    eventsViewHolder.tv_my_label.setVisibility(View.GONE);
                    eventsViewHolder.rr_top.setVisibility(View.GONE);
                }

                eventsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onSearchRowItemSelected(eventsItem.getId(),ViewType.EVENTS.toString());
                    }
                });
            } else {
                Toast.makeText(context, "Not data found!", Toast.LENGTH_SHORT).show();
            }
        } else if (mViewType == ViewType.ALL) {

            if (this.mAllTypesArrayList != null && this.mAllTypesArrayList.size() > 0) {
                AllViewHolder allViewHolder = ((AllViewHolder) holder);
                AllDataTypes item = this.mAllTypesArrayList.get(position);
                allViewHolder.name.setText(item.getData().getName());
                allViewHolder.phone.setText(item.getData().getPhone() != null ? item.getData().getPhone() : "Not available!");// ALL

                switchAllTypeHeaderVisibleAsDataChange(allViewHolder, item);
                Glide.with(context).load(item.getData().getImage())
                        .thumbnail(0.5f)
                        .override(R.dimen._50sdp,R.dimen._50sdp)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(allViewHolder.thumbnail);

                allViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item!=null) {
                            if (item.getViewName().equalsIgnoreCase(ViewType.PEOPLE.toString())) {
                                listener.onSearchRowItemSelected(item.getData().getId(), ViewType.PEOPLE.toString());
                            } else if (item.getViewName().equalsIgnoreCase(ViewType.CHURCH.toString())) {
                                listener.onSearchRowItemSelected(item.getData().getId(), ViewType.CHURCH.toString());
                            } else if (item.getViewName().equalsIgnoreCase(ViewType.EVENTS.toString())) {
                                listener.onSearchRowItemSelected(item.getData().getId(), ViewType.EVENTS.toString());
                            } else if (item.getViewName().equalsIgnoreCase(ViewType.GROUPS.toString())) {
                                listener.onSearchRowItemSelected(item.getData().getId(), ViewType.GROUPS.toString());
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(context, "Not data found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Not view type found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchAllTypeHeaderVisibleAsDataChange(AllViewHolder allViewHolder, AllDataTypes item) {
        if (item.getViewType() != null) {
            if (item.getViewType().equals(ViewType.PEOPLE)) {
                allViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                allViewHolder.rr_top.setVisibility(View.VISIBLE);
                allViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_people, 0, 0, 0);
                allViewHolder.tv_my_label.setText(context.getString(R.string.str_peoples));

            } else if (item.getViewType().equals(ViewType.GROUPS)) {
                allViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                allViewHolder.rr_top.setVisibility(View.VISIBLE);
                allViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_groups, 0, 0, 0);
                allViewHolder.tv_my_label.setText(context.getString(R.string.str_my_groups));

            } else if (item.getViewType().equals(ViewType.CHURCH)) {
                allViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                allViewHolder.rr_top.setVisibility(View.VISIBLE);
                allViewHolder.tv_my_label.setText(context.getString(R.string.str_my_church));
                allViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mychurch, 0, 0, 0);
            } else if (item.getViewType().equals(ViewType.EVENTS)) {
                allViewHolder.tv_my_label.setVisibility(View.VISIBLE);
                allViewHolder.rr_top.setVisibility(View.VISIBLE);
                allViewHolder.tv_my_label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_events, 0, 0, 0);
                allViewHolder.tv_my_label.setText(context.getString(R.string.str_my_events));
            } else {
                allViewHolder.tv_my_label.setVisibility(View.GONE);
                allViewHolder.rr_top.setVisibility(View.GONE);
            }
        }else {
            allViewHolder.tv_my_label.setVisibility(View.GONE);
            allViewHolder.rr_top.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (mViewType == ViewType.PEOPLE) {
            return mPeoplesArrayList != null ? mPeoplesArrayList.size() : 0;
        } else if (mViewType == ViewType.GROUPS) {
            return mGroupsArrayList != null ? mGroupsArrayList.size() : 0;
        } else if (mViewType == ViewType.CHURCH) {
            return mChurchesArrayList != null ? mChurchesArrayList.size() : 0;
        } else if (mViewType == ViewType.EVENTS) {
            return mEventsArrayList != null ? mEventsArrayList.size() : 0;
        } else if (mViewType == ViewType.ALL) {
//            int i = 0;
//            if (mEventsArrayList != null && mEventsArrayList.size()>0){
//                i = i + mEventsArrayList.size();
//            }
//            if (mPeoplesArrayList != null && mPeoplesArrayList.size()>0){
//                i = i + mPeoplesArrayList.size();
//            }
//            if (mChurchesArrayList != null && mChurchesArrayList.size()>0){
//                i = i + mChurchesArrayList.size();
//            }
//            if (mEventsArrayList != null && mEventsArrayList.size()>0){
//                i = i + mEventsArrayList.size();
//            }
            return this.mAllTypesArrayList.size();
        } else {
            return 0;
        }
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (mViewType == ViewType.PEOPLE) {
//                    if (charString.isEmpty()) {
//                        searchResponsesFiltered = searchResponses;
//                    } else {
//                        List<Data> filteredList = new ArrayList<>();
//                        for (PeoplesItem row : searchResponses.getData().getPeoples()) {
//
//                            // name match condition. this might differ depending on your requirement
//                            // here we are looking for name or phone number match
//                            if (row.getFirstName().toLowerCase().contains(charString.toLowerCase())
//                                    || row.getLastName().contains(charSequence)) {
//                                filteredList.add(row);
//                            }
//                        }
//
//                        contactListFiltered = filteredList;
//                    }
//
//                    FilterResults filterResults = new FilterResults();
//                    filterResults.values = contactListFiltered;
//                    return filterResults;
//                } else {
//                    return null;
//                }
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                contactListFiltered = (ArrayList<Data>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

    public enum ViewType {
        ALL("all", 0),
        EVENTS("events", 1),
        GROUPS("groups", 2),
        CHURCH("church", 3),
        PEOPLE("peoples", 4);

        private String stringValue;
        private int intValue;

        private ViewType(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

    }
}