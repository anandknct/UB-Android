package com.unitybound.main.search.beans;

import com.unitybound.main.search.adapter.SearchListAdapter;

/**
 * Created by Admin on 1/20/2018.
 */

public class AllDataTypes {

    public AllDataTypes(AllTypeData data, SearchListAdapter.ViewType viewType, String viewName) {
        this.data = data;
        this.viewType = viewType;
        this.viewName = viewName;
    }

    private AllTypeData data = null;

    private String viewName = null;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    private SearchListAdapter.ViewType viewType;

    public SearchListAdapter.ViewType getViewType() {
        return viewType;
    }

    public void setViewType(SearchListAdapter.ViewType viewType) {
        this.viewType = viewType;
    }

    public AllTypeData getData() {
        return data;
    }

    public void setData(AllTypeData data) {
        this.data = data;
    }

}
