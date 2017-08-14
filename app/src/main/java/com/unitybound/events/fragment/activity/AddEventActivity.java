package com.unitybound.events.fragment.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.utility.customView.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEventActivity extends AppCompatActivity implements CustomDialog.IDialogListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.edt_event_name)
    EditText edtEventName;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.edt_event_venue)
    EditText edtEventVenue;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.edt_address1)
    EditText edtAddress1;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.edt_address2)
    EditText edtAddress2;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.ll_7)
    LinearLayout ll7;
    @BindView(R.id.ll_8)
    LinearLayout ll8;
    @BindView(R.id.ll_9)
    LinearLayout ll9;
    @BindView(R.id.ll_10)
    LinearLayout ll10;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ll_12)
    LinearLayout ll12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);


        setUpToolbarLayout();
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Event");
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


    @OnClick({R.id.tv_upload_photo, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_create:
                CustomDialog customDialog = new CustomDialog(AddEventActivity.this,
                        "Thank you for creating Event",
                        "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                                "industry. " +
                                "Lorem Ipsum has been the industry's standard dummy text ever" +
                                " since the 1500s, when an unknown printer took a galley of" +
                                " type and scrambled it to make a type specimen book.",
                        AddEventActivity.this);
                customDialog.show();
                break;
            case R.id.btn_cancel:
                break;
        }
    }

    @OnClick(R.id.btn_create)
    public void onViewClicked() {
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        onBackPressed();
    }

}
