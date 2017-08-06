package com.unitybound.account.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.unitybound.R;
import com.unitybound.account.adapter.AddPostSpinnerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPostActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.sp_drop_down)
    Spinner spDropDown;
    @BindView(R.id.btn_post)
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity_layout);
        ButterKnife.bind(this);

        setUpToolbarLayout();

        /**
         * SetUp Spinner
         */
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("DEVOTIONAL");
        arrayList1.add("PRAISE");
        arrayList1.add("PRAYERS");
        arrayList1.add("TESTIMONIALS");
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        AddPostSpinnerAdapter spAdapter = new AddPostSpinnerAdapter(
                this,
                R.layout.create_post_spinner_item,
                arrayList1, imgs);

        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_white_bg_layout);
        spDropDown.setAdapter(spAdapter);
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Post");
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

    @OnClick(R.id.btn_post)
    public void onViewClicked() {
    }
}
