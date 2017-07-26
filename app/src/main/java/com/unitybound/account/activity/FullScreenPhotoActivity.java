package com.unitybound.account.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.unitybound.R;

public class FullScreenPhotoActivity extends AppCompatActivity {

    private BottomSheetDialog mBottomSheetDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        initAllViews();
        setUpBottomSheet();
    }

    private void initAllViews() {
        ImageView iv_option_dots = (ImageView) findViewById(R.id.iv_option_dots);
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
        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
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
}
