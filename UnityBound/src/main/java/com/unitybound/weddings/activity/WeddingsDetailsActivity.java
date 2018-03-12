package com.unitybound.weddings.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;
import com.unitybound.weddings.beans.WeadingDetailResponse.WeddingDetailResponse;
import com.unitybound.weddings.beans.WeadingDetailResponse.WeddingDetails;
import com.unitybound.weddings.beans.WeadingDetailResponse.WeddingImagesItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeddingsDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String mWEDDING_ID = null;
    private ProgressDialog mProgressDialog = null;
    private Call<WeddingDetailResponse> callWeedingDetailRequest = null;
    private WeddingDetails mWeddingDetails = null;
    private ImageView iv_wedding_image = null;
    private TextView tv_wedding_description = null;
    private TextView tv_date,tv_edit;
    private TextView tv_time,tv_name=  null;
    private TextView tv_location,tv_bridemf = null,tv_groommf = null;
    private GoogleMap mMap;
    private LinearLayout ll_image_view_list = null;
    private ImageView iv_1 = null,iv_2 = null,iv_3 = null,iv_4 = null,iv_5 = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weddings_details);
        mWEDDING_ID = getIntent().getStringExtra("WEDDING_ID");
        initView();

        if (Util.checkNetworkAvailablity(WeddingsDetailsActivity.this)) {
            getWeaddingDetail();
        } else {
            CustomDialog customDialog1 = new CustomDialog(WeddingsDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    RecyclerView recyclerView;
    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    EventsListAdapter adapter;

    private void initView() {
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Wedding Details");

        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_edit.setVisibility(View.GONE);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeddingsDetailsActivity.this, AddWeddingActivity.class);
                intent.putExtra("WEDDING_ID",mWEDDING_ID);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data",mWeddingDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_wedding_description = (TextView) findViewById(R.id.tv_description);
        iv_wedding_image = (ImageView) findViewById(R.id.iv_wedding_image);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_bridemf = (TextView) findViewById(R.id.tv_bridemf);
        tv_groommf = (TextView) findViewById(R.id.tv_groommf);
        ll_image_view_list = (LinearLayout) findViewById(R.id.ll_image_view_list);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        iv_4 = (ImageView) findViewById(R.id.iv_4);
        iv_5 = (ImageView) findViewById(R.id.iv_5);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getWeaddingDetail() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callWeedingDetailRequest = apiService.getWeaddingDetail(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "",
                        WeddingsDetailsActivity.this),
                mWEDDING_ID);

        callWeedingDetailRequest.enqueue(new Callback<WeddingDetailResponse>() {

            @Override
            public void onResponse(Call<WeddingDetailResponse> call, Response<WeddingDetailResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    WeddingDetailResponse loginResponse = response.body();
                    if (loginResponse.getWeddingDetails() != null) {
                        mWeddingDetails = loginResponse.getWeddingDetails();
                        if (mWeddingDetails != null) {

                            Glide.with(WeddingsDetailsActivity.this)
                                    .load(mWeddingDetails.getWeddingImage())
                                    .placeholder(R.drawable.ubitauries_def_image)
                                    .skipMemoryCache(false)
                                    .into(iv_wedding_image);
                            tv_name.setText(Util.isNull("Name : "
                                    + mWeddingDetails.getWeddingGroomName()+" &amp; "+mWeddingDetails.getWeddingBrideName()));
                            tv_wedding_description.setText("Description : " + Util.isNull(mWeddingDetails.getWeddingDescription()));
                            tv_date.setText("Date : " + Util.weddingDetailFormator(mWeddingDetails.getWeddingDate()));
                            tv_time.setText("Date : " + Util.isNull(mWeddingDetails.getWeddingTime()));
                            tv_location.setText(Util.isNull(mWeddingDetails.getWeddingLocation()));
                            tv_bridemf.setText("Mother & Father of Bride : "+Util.isNull(mWeddingDetails.getWeddingBrideMotherName()+ " and "+
                                    Util.isNull(mWeddingDetails.getWeddingBrideMotherName())));
                            tv_groommf.setText("Mother & Father of Bride : "+Util.isNull(mWeddingDetails.getWeddingGroomFatherName()+ " and "+
                                    Util.isNull(mWeddingDetails.getWeddingGroomMotherName())));
                            if (mWeddingDetails.getAddedBy()!=null) {
                                if (mWeddingDetails.getAddedBy().equalsIgnoreCase(
                                        Util.loadPrefrence(Util.PREF_USER_ID, "",
                                                WeddingsDetailsActivity.this))) {
                                    tv_edit.setVisibility(View.VISIBLE);
                                } else {
                                    tv_edit.setVisibility(View.GONE);
                                }
                            }

                            if (mWeddingDetails.getLatitude()!=null && mWeddingDetails.getLatitude()!=0.0 &&
                                    mWeddingDetails.getLongitude()!=null && mWeddingDetails.getLongitude()!=0.0){
                                addMarkerOnMap(mWeddingDetails.getLatitude(),mWeddingDetails.getLongitude());
                            } else {
                                findViewById(R.id.map).setVisibility(View.GONE);
//                                Toast.makeText(WeddingsDetailsActivity.this,
//                                        "In API lat lng is not comming please make " +
//                                                "them 0.0 by default from server end issue ;)", Toast.LENGTH_SHORT).show();
                            }

                            List<WeddingImagesItem> mWeddingImages = loginResponse.getWeddingImages();
                            if (mWeddingImages!=null && mWeddingImages.size()>0){
                                ll_image_view_list.setVisibility(View.VISIBLE);
                                if (mWeddingImages.size()>0){
                                    Glide.with(WeddingsDetailsActivity.this)
                                            .load(mWeddingImages.get(0).getImage())
                                            .placeholder(R.drawable.ic_photos_placeholder)
                                            .skipMemoryCache(false)
                                            .into(iv_wedding_image);
                                }
                                if (mWeddingImages.size()>1){
                                    Glide.with(WeddingsDetailsActivity.this)
                                            .load(mWeddingImages.get(1).getImage())
                                            .placeholder(R.drawable.ic_photos_placeholder)
                                            .skipMemoryCache(false)
                                            .into(iv_wedding_image);
                                }
                                if (mWeddingImages.size()>2){
                                    Glide.with(WeddingsDetailsActivity.this)
                                            .load(mWeddingImages.get(2).getImage())
                                            .placeholder(R.drawable.ic_photos_placeholder)
                                            .skipMemoryCache(false)
                                            .into(iv_wedding_image);
                                }
                                if (mWeddingImages.size()>3){
                                    Glide.with(WeddingsDetailsActivity.this)
                                            .load(mWeddingImages.get(3).getImage())
                                            .placeholder(R.drawable.ic_photos_placeholder)
                                            .skipMemoryCache(false)
                                            .into(iv_wedding_image);
                                }
                                if (mWeddingImages.size()>4){
                                    Glide.with(WeddingsDetailsActivity.this)
                                            .load(mWeddingImages.get(5).getImage())
                                            .placeholder(R.drawable.ic_photos_placeholder)
                                            .skipMemoryCache(false)
                                            .into(iv_wedding_image);
                                }
                            }else{
                                ll_image_view_list.setVisibility(View.GONE);
                            }
//                            addMarkerOnMap(mObituaryLat, mObituaryLng);
                        }

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(WeddingsDetailsActivity.this, null,
                                "", getResources().getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(WeddingsDetailsActivity.this, null,
                            "", getResources().getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            private void addMarkerOnMap(double mObituaryLat, double mObituaryLng) {
                LatLng position = new LatLng(mObituaryLat, mObituaryLng);

                // Instantiating MarkerOptions class
                MarkerOptions options = new MarkerOptions();

                // Setting position for the MarkerOptions
                options.position(position);

                // Setting title for the MarkerOptions
                options.title("Position");

                // Setting snippet for the MarkerOptions
                options.snippet("Latitude:" + mObituaryLat + ",Longitude:" + mObituaryLng);

                // Adding Marker on the Google Map
                mMap.addMarker(options);

                // Creating CameraUpdate object for position
                CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);

                // Creating CameraUpdate object for zoom
                CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

                // Updating the camera position to the user input latitude and longitude
                mMap.moveCamera(updatePosition);

                // Applying zoom to the marker position
                mMap.animateCamera(updateZoom);
            }

            @Override
            public void onFailure(Call<WeddingDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(WeddingsDetailsActivity.this, null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(WeddingsDetailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(WeddingsDetailsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Disable Map Toolbar:
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
//        mMap.setOnMarkerClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
    }
}
