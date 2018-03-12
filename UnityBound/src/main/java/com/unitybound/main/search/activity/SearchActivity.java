package com.unitybound.main.search.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.church.fragment.ChurchDetailsActivity;
import com.unitybound.events.fragment.fragment.EventsDetailsActivity;
import com.unitybound.groups.fragment.GroupsDetailsActivity;
import com.unitybound.main.MainActivity;
import com.unitybound.main.search.adapter.SearchListAdapter;
import com.unitybound.main.search.beans.Data;
import com.unitybound.main.search.beans.SearchResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchListAdapter.SearchRowItemListener {

    private RecyclerView recyclerView = null;
    private Data searchData = null;
    private SearchView searchView = null;
    private ProgressDialog mProgressDialog = null;
    private Call<SearchResponse> callSearchAPI = null;
    private TextView tv_all = null, tv_peoples = null, tv_groups = null, tv_church_all = null, tv_events_all = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initAllViews();
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("Search Here");
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(SearchActivity.this)) {
                    String query = searchView.getQuery().toString().trim();
                    if (query != null && query.length() > 0) {
                        sendSearchRequest(query, SearchListAdapter.ViewType.ALL);
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(SearchActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });
    }

    private void initAllViews() {
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.str_search_here));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Util.checkNetworkAvailablity(SearchActivity.this)) {
                    sendSearchRequest(query, SearchListAdapter.ViewType.ALL);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(SearchActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    recyclerView.setAdapter(null);
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchData = null;
                recyclerView.setAdapter(null);

                return false;
            }
        });

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_peoples = (TextView) findViewById(R.id.tv_peoples);
        tv_groups = (TextView) findViewById(R.id.tv_groups);
        tv_church_all = (TextView) findViewById(R.id.tv_church_all);
        tv_events_all = (TextView) findViewById(R.id.tv_events_all);
        setOnClickListners();
    }

    private void setOnClickListners() {
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTvSelection(1);
                if (searchData != null) {
                    setAdapter(searchData, SearchListAdapter.ViewType.ALL);
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        });
        tv_peoples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTvSelection(2);
                if (searchData != null) {
                    setAdapter(searchData, SearchListAdapter.ViewType.PEOPLE);
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        });
        tv_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTvSelection(3);
                if (searchData != null) {
                    setAdapter(searchData, SearchListAdapter.ViewType.GROUPS);
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        });
        tv_church_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTvSelection(4);
                if (searchData != null) {
                    setAdapter(searchData, SearchListAdapter.ViewType.CHURCH);
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        });
        tv_events_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTvSelection(5);
                if (searchData != null) {
                    setAdapter(searchData, SearchListAdapter.ViewType.EVENTS);
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        });
    }

    private void changeTvSelection(int i) {
        switch (i) {
            case 1:
                tv_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.color_orange));
                tv_peoples.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_groups.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_church_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_events_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                break;
            case 2:
                tv_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_peoples.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.color_orange));
                tv_groups.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_church_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_events_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                break;
            case 3:
                tv_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_peoples.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_groups.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.color_orange));
                tv_church_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_events_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                break;
            case 4:
                tv_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_peoples.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_groups.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_church_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.color_orange));
                tv_events_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                break;
            case 5:
                tv_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_peoples.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_groups.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_church_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.white));
                tv_events_all.setTextColor(ContextCompat.getColor(SearchActivity.this,
                        R.color.color_orange));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        } else
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendSearchRequest(String searchKey, SearchListAdapter.ViewType listViewType) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callSearchAPI = apiService.getAllSearch(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", SearchActivity.this),
                searchKey);

        callSearchAPI.enqueue(new Callback<SearchResponse>() {

            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    SearchResponse searchResponse = response.body();
                    if (searchResponse.getData() != null) {
                        searchData = searchResponse.getData();
                        if (searchData != null) {
                            setAdapter(searchData, listViewType);
                            changeTvSelection(1);
                        } else {
                            recyclerView.setAdapter(null);
                        }
                    } else {
                        new CustomDialog(SearchActivity.this, null, "",
                                "No data found...",
                                "ONFAILED").show();
                    }

                } else {

                    if (response.body() == null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Gson gson = new Gson();
                            ErrorResponse error = gson.fromJson(jObjError.toString(),
                                    ErrorResponse.class);
                            String msg = null;
                            if (error != null) {
                                msg = error.getMsg();
                            } else {
                                msg = "Something went wrong";
                            }
                            new CustomDialog(SearchActivity.this, null, "",
                                    msg,
                                    "ONFAILED").show();
                            hideProgressDialog();

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(SearchActivity.this, null,
                        "", "" + t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void setAdapter(Data searchData, SearchListAdapter.ViewType listViewType) {
        SearchListAdapter mAdapter = new SearchListAdapter(SearchActivity.this, searchData,
                SearchActivity.this, listViewType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(SearchActivity.this));
        recyclerView.setAdapter(mAdapter);
    }

    public void showProgressDialog() {
        mProgressDialog.show();
        if (mProgressDialog != null) {
            //  mProgressDialog = Utils.createProgressDialog(BaseActivity.this, getString(R.string.str_logging_in));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.setContentView(R.layout.custom_progressdialog);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (callSearchAPI != null && callSearchAPI.isExecuted()) {
            callSearchAPI.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onSearchRowItemSelected(String id,String type) {
        if (SearchListAdapter.ViewType.CHURCH.toString().equals(type)) {
//            Intent intent = new Intent(SearchActivity.this , MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//            Bundle bundle = new Bundle();
//            bundle.putString("CHURCH_ID",id);
//            bundle.putInt("POSITION", 1);
//            intent.putExtras(bundle);
//            startActivity(intent);
            Intent intent = new Intent(SearchActivity.this , ChurchDetailsActivity.class);
            intent.putExtra(Util.CHURCH_ID, id);
            startActivity(intent);
        } else if (SearchListAdapter.ViewType.EVENTS.toString().equals(type)) {
//            Intent intent = new Intent(SearchActivity.this , MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//            Bundle bundle = new Bundle();
//            bundle.putInt("POSITION", 2);
//            intent.putExtras(bundle);
//            startActivity(intent);
            Intent intent = new Intent(SearchActivity.this, EventsDetailsActivity.class);
            intent.putExtra("EVENT_ID",id);
            startActivity(intent);
        } else if (SearchListAdapter.ViewType.GROUPS.toString().equals(type)) {
//            Intent intent = new Intent(SearchActivity.this , MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//            Bundle bundle = new Bundle();
//            bundle.putInt("POSITION", 3);
//            intent.putExtras(bundle);
//            startActivity(intent);
            Intent i = new Intent(SearchActivity.this , GroupsDetailsActivity.class);
            i.putExtra(Util.GROUP_ID,id);
            startActivity(i);
        } else if (SearchListAdapter.ViewType.PEOPLE.toString().equals(type)) {
            Intent intent = new Intent(SearchActivity.this , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            Bundle bundle = new Bundle();
            bundle.putString("PROFILE_ID",id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
