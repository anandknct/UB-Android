package com.unitybound.groups.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.groups.adapter.GroupsPeopleListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.SpacesItemDecoration;
import com.unitybound.utility.customView.CustomDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGroupActivity extends AppCompatActivity implements CustomDialog.IDialogListener, GroupsPeopleListAdapter.IListAdapterCallback {


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
    @BindView(R.id.rv_people_list)
    RecyclerView rvPeoplesList;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    ArrayList<FriendRequestData> datalist = new ArrayList<FriendRequestData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        setUpToolbarLayout();
        setUpPeoplesRV();
    }

    private void setUpPeoplesRV() {
        //add some person to list
        FriendRequestData p1 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p2 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p3 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p4 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p5 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p6 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p7 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");


        datalist.add(p1);
        datalist.add(p2);
        datalist.add(p3);
        datalist.add(p4);
        datalist.add(p5);
        datalist.add(p6);
        datalist.add(p7);

        GroupsPeopleListAdapter adapter =
                new GroupsPeopleListAdapter(AddGroupActivity.this, datalist, AddGroupActivity.this);
        LinearLayoutManager lLayout = new LinearLayoutManager(AddGroupActivity.this);
//        rvPeoplesList.addItemDecoration(new DividerItemDecoration(AddGroupActivity.this, DividerItemDecoration.VERTICAL));
        rvPeoplesList.addItemDecoration(new SpacesItemDecoration(30));
        rvPeoplesList.setLayoutManager(lLayout);
        rvPeoplesList.setHasFixedSize(true);
        rvPeoplesList.setAdapter(adapter);
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Group");
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
                break;
        }
    }

    @OnClick(R.id.btn_create)
    public void onViewClicked() {
        CustomDialog customDialog = new CustomDialog(AddGroupActivity.this,
                "Thank you for creating Group",
                "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                        "industry. " +
                        "Lorem Ipsum has been the industry's standard dummy text ever" +
                        " since the 1500s, when an unknown printer took a galley of" +
                        " type and scrambled it to make a type specimen book.",
                AddGroupActivity.this);
        customDialog.show();
    }

    @OnClick(R.id.btn_cancel)
    public void onViewCanceled() {
        onBackPressed();
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        onBackPressed();
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }
}
