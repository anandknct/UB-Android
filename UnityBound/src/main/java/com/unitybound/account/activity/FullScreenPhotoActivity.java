package com.unitybound.account.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.unitybound.R;
import com.unitybound.utility.Util;

public class FullScreenPhotoActivity extends AppCompatActivity {

    private BottomSheetDialog mBottomSheetDialog = null;
    private ImageView iv_image_prev = null;
    private String mIMAGE_URL = null;
    private String mPROFILE_USER_USER_ID = null;
    private String mLIKES = null;
    private String mCOMMENTS = null;
    private RelativeLayout rr_bottom_menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        initAllViews();
        setUpBottomSheet();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("URL")) {
                mIMAGE_URL = bundle.getString("URL");

                Glide.with(this)
                        .load(mIMAGE_URL.trim())    // you can pass url too
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                // you can do something with loaded bitmap here
                                if (resource!=null) {
                                    setImage(resource,iv_image_prev);
                                }
                            }
                        });
            }
            if (bundle.containsKey("PROFILE_USER_USER_ID")) {
                mPROFILE_USER_USER_ID = bundle.getString("PROFILE_USER_USER_ID");
            }

            if (bundle.containsKey("LIKES")) {
                mLIKES = bundle.getString("LIKES");
                rr_bottom_menu.setVisibility(View.VISIBLE);
            } else {
                rr_bottom_menu.setVisibility(View.GONE);
            }

            if (bundle.containsKey("COMMENTS")) {
                mCOMMENTS = bundle.getString("COMMENTS");
            } else {
                rr_bottom_menu.setVisibility(View.GONE);
            }
        }
    }

    private void initAllViews() {
        iv_image_prev = (ImageView) findViewById(R.id.iv_image_prev);
        ImageView iv_option_dots = (ImageView) findViewById(R.id.iv_option_dots);
        rr_bottom_menu = (RelativeLayout) findViewById(R.id.rr_bottom_menu);
        iv_option_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.findViewById(R.id.fragment_history_menu_bottom).setVisibility(View.VISIBLE);
                mBottomSheetDialog.show();
            }
        });
    }

    private void setUpBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheets_main_layout, null);
        mBottomSheetDialog.setContentView(sheetView);

        LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_del);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
    }

    private void setImage(Bitmap resource, ImageView iv_image_prev) {
        if (resource!=null) {
            int mWidth = Util.getScreenWidth(this);
            int mHeight = Util.getScreenHeight(this);
            int width = resource.getWidth();
            int height = resource.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) mWidth / (float) mHeight;

            int finalWidth = 0;
            int finalHeight = 0;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)mWidth * ratioBitmap);
            } else {
                finalHeight = (int) ((float)mWidth / ratioBitmap);
            }

            iv_image_prev.setMinimumWidth(finalWidth);
            iv_image_prev.setMinimumHeight(finalHeight);
            iv_image_prev.setMaxHeight(finalHeight);
            iv_image_prev.setImageBitmap(resource);
        }
    }

}
