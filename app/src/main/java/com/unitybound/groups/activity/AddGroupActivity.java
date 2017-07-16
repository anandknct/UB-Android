package com.unitybound.groups.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unitybound.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGroupActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.edt_group_name)
    EditText edtGroupName;
    @BindView(R.id.edtprofile_photo)
    EditText edtprofilePhoto;
    @BindView(R.id.sp_group_type)
    Spinner spGroupType;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.edt_search_frnds)
    EditText edtSearchFrnds;
    @BindView(R.id.rv_pepoles_list)
    RecyclerView rvPepolesList;
    @BindView(R.id.btn_create)
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);

        setUpToolbarLayout();
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Group");
    }

    @OnClick({R.id.tv_upload_photo, R.id.edtprofile_photo, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_photo:
                break;
            case R.id.edtprofile_photo:
                break;
            case R.id.btn_create:
                break;
        }
    }
}
