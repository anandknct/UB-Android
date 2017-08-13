package com.unitybound.church.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.church.adapter.DenominationSpinnerAdapter;
import com.unitybound.utility.customView.CustomDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddChurchActivity extends AppCompatActivity implements CustomDialog.IDialogListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.spinner_denomination)
    Spinner spinnerDenomination;
    @BindView(R.id.ll_0)
    LinearLayout ll0;
    @BindView(R.id.edt_group_name)
    EditText edtGroupName;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.edtprofile_photo)
    EditText edtprofilePhoto;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.edt_address1)
    EditText edtAddress1;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.edt_address2)
    EditText edtAddress2;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.edt_mailing_address1)
    EditText edtMailingAddress1;
    @BindView(R.id.edt_mailing_address2)
    EditText edtMailingAddress2;
    @BindView(R.id.ll_7)
    LinearLayout ll7;
    @BindView(R.id.edt_address3)
    EditText edtAddress3;
    @BindView(R.id.ll_8)
    LinearLayout ll8;
    @BindView(R.id.ll_9)
    LinearLayout ll9;
    @BindView(R.id.ll_10)
    LinearLayout ll10;
    @BindView(R.id.edt_church_phone)
    EditText edtChurchPhone;
    @BindView(R.id.ll_11)
    LinearLayout ll11;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ll_12)
    LinearLayout ll12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_church);
        ButterKnife.bind(this);

        setUpToolbarLayout();
        setUpChurchDenomination();
    }

    private void setUpChurchDenomination() {
        /* Spinner Feeds Adapter*/
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Anglican");
        arrayList1.add("Baptist");
        arrayList1.add("Catholic");
        arrayList1.add("Eastern Orthodox");
        arrayList1.add("Lutheran");
        arrayList1.add("Methodist");
        arrayList1.add("Nondenominational");
        arrayList1.add("Oriental Orthodoxy");
        arrayList1.add("Pentecostal");
        arrayList1.add("Presbyterian");
        arrayList1.add("Seventh-Day adventist");
        arrayList1.add("Other");
//        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        DenominationSpinnerAdapter spAdapter = new DenominationSpinnerAdapter(
                AddChurchActivity.this,
                R.layout.spinner_item_white,
                arrayList1, null);

        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_white);
        spinnerDenomination.setAdapter(spAdapter);
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Church");
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


    @OnClick({R.id.tv_upload_photo, R.id.edtprofile_photo, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_photo:
                break;
            case R.id.edtprofile_photo:
                break;
            case R.id.btn_create:
                CustomDialog customDialog = new CustomDialog(AddChurchActivity.this,
                        "Thank you for creating Church",
                        "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                                "industry. " +
                                "Lorem Ipsum has been the industry's standard dummy text ever" +
                                " since the 1500s, when an unknown printer took a galley of" +
                                " type and scrambled it to make a type specimen book.",
                        AddChurchActivity.this);
                customDialog.show();
                break;
        }
    }

    @OnClick(R.id.btn_create)
    public void onViewClicked() {
    }
    @OnClick(R.id.btn_cancel)
    public void onViewCancelClicked() {
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        onBackPressed();
    }
}
