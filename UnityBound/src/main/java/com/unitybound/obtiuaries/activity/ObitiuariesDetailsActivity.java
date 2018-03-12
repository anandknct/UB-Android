package com.unitybound.obtiuaries.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.obtiuaries.beans.detail.ObituariesDetailResponse;
import com.unitybound.obtiuaries.beans.detail.Obituary;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ObitiuariesDetailsActivity extends ActivityManagePermission implements OnMapReadyCallback {

    private String mOBITUARIES_ID = null;
    private ProgressDialog mProgressDialog = null;
    private Call<ObituariesDetailResponse> callObituariesRequest = null;
    private Obituary mObituaries = null;
    private ImageView iv_obituary_image = null;
    private TextView tv_name = null, tv_description = null, tv_to_from, tv_funeral_services,
            tv_date, tv_time, tv_location;
    private Button btn_direction;
    private double mObituaryLng = 0.0, mObituaryLat = 0.0;
    private GoogleMap mMap = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private Location mLocation = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obitiuaries_details);
        mOBITUARIES_ID = getIntent().getStringExtra("OBITUARIES_ID");
        initView();

        if (Util.checkNetworkAvailablity(ObitiuariesDetailsActivity.this)) {
            getMyObituaries();
        } else {
            CustomDialog customDialog1 = new CustomDialog(ObitiuariesDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Obituaries Details");

        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_obituary_image = (ImageView) findViewById(R.id.iv_obituary_image);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_to_from = (TextView) findViewById(R.id.tv_to_from);
        tv_funeral_services = (TextView) findViewById(R.id.tv_funeral_services);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_direction = (Button) findViewById(R.id.btn_direction);
        btn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentlocationPermission("DIRECTION");
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCurrentlocationPermission(final String commingFrom) {
        askCompactPermissions(new String[]{PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                PermissionUtils.Manifest_ACCESS_FINE_LOCATION}, new PermissionResult() {
            @Override
            public void permissionGranted() {
                getCurrentLocation(commingFrom);
            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                openSettingsApp(ObitiuariesDetailsActivity.this);
            }
        });
    }

    private void getCurrentLocation(final String commingFrom) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLocation = location;
                            try {

                                if (commingFrom.equalsIgnoreCase("DIRECTION")) {
                                    if (mObituaries != null && mLocation != null) {
                                        if (mLocation != null) {
                                            String uri = String.format(Locale.ENGLISH,
                                                    "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                                                    Double.valueOf(mObituaries.getObituaryLat()),
                                                    Double.valueOf(mObituaries.getObituaryLng()),
                                                    "Your Location",
                                                    mLocation.getLatitude(), mLocation.getLongitude(),
                                                    mObituaries.getObituaryName());
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                            intent.setPackage("com.google.android.apps.maps");
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ObitiuariesDetailsActivity.this,
                                                    "Current location not available",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ObitiuariesDetailsActivity.this,
                                                "No contact number found",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
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

    private void getMyObituaries() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callObituariesRequest = apiService.getObituaryDetail(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "",
                        ObitiuariesDetailsActivity.this),
                mOBITUARIES_ID);

        callObituariesRequest.enqueue(new Callback<ObituariesDetailResponse>() {

            @Override
            public void onResponse(Call<ObituariesDetailResponse> call, Response<ObituariesDetailResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ObituariesDetailResponse loginResponse = response.body();
                    if (loginResponse.getObituary() != null) {
                        mObituaries = loginResponse.getObituary();
                        if (mObituaries != null) {

                            Glide.with(ObitiuariesDetailsActivity.this)
                                    .load(mObituaries.getObituaryImage())
                                    .placeholder(R.drawable.ubitauries_def_image)
                                    .skipMemoryCache(false)
                                    .into(iv_obituary_image);
                            tv_name.setText(Util.isNull(mObituaries.getObituaryName()));
                            tv_description.setText(Util.isNull(mObituaries.getObituaryDescription()));
                            String mFromDate = Util.weddingDateFormator(mObituaries.getObituaryBirthDate());
                            String mToDate = Util.weddingDateFormator(mObituaries.getObituaryDeathDate());
                            tv_to_from.setText(Util.isNull(mFromDate + " " + mToDate));
//                            tv_funeral_services.setText();
                            tv_date.setText("Date : " + Util.eventDateFormator(mObituaries.getObituaryFuneralDate()));
                            tv_time.setText("Date : " + Util.isNull(mObituaries.getObituaryFuneralTime()));
                            tv_location.setText(Util.isNull(mObituaries.getObituaryAddress1()) + ", " +
                                    Util.isNull(mObituaries.getObituaryCity()) + ", " +
                                    Util.isNull(mObituaries.getObituaryState()) + ", " +
                                    Util.isNull(mObituaries.getObituaryZip()) + ", " +
                                    Util.isNull(mObituaries.getObituaryCountry()));
                            if (mObituaries.getObituaryLat() != 0.0 && mObituaries.getObituaryLng() != 0.0)
                            {
                                mObituaryLat = mObituaries.getObituaryLat();
                                mObituaryLng = mObituaries.getObituaryLng();
                                addMarkerOnMap(mObituaryLat, mObituaryLng);
                            }
                        }

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(ObitiuariesDetailsActivity.this, null,
                                "", getResources().getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(ObitiuariesDetailsActivity.this, null,
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
            public void onFailure(Call<ObituariesDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(ObitiuariesDetailsActivity.this, null,
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
        if (ActivityCompat.checkSelfPermission(ObitiuariesDetailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ObitiuariesDetailsActivity.this,
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
