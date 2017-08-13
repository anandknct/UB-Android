package com.unitybound.utility.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unitybound.R;

/**
 * Created by nikhil.jogdand on 15-7-2017
 */
public class CustomDialog extends Dialog {
    public static final String NO_PARAM = "No_param";
    public static final String REQUEST_ACCESS = "REQUEST_ACCESS";
    private RadioButton dropRadioButton = null;
    private RadioButton pickUpRadioButton = null;
    private FragmentActivity fragmentActivity;
    private IDialogListener dialogListener;
    private String param;
    private int btnWidth;

    public interface IDialogListener {
        /**
         * Callback for dialog Cancel event
         *
         * @param param
         */
        void onCancelPress(String param);

        /**
         * Callback for dialog Yes event
         *
         * @param param
         */
        void onYesPress(String param, String message);
    }

    public CustomDialog(FragmentActivity activity,
                        IDialogListener dialog,
                        final String tittleStr, String msg, final String params) {

        super(activity, R.style.newDialog);
        this.fragmentActivity = activity;
//        btnWidth = (int) (Util.getScreenWidth(getContext()) * .2f);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.custom_dailog);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        this.param = params;
        this.dialogListener = dialog;
        final RelativeLayout rr_main = (RelativeLayout) findViewById(R.id.ll_1);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                circularRevealEnter(rr_main);
            }
        });
        TextView _msg = (TextView) findViewById(R.id.msg);
        TextView _cancel = (TextView) findViewById(R.id.cancel);
        TextView _titel = (TextView) findViewById(R.id.title);

        TextView _ok = (TextView) findViewById(R.id.ok);

        if (params.equalsIgnoreCase("ONFAILED") || params.equalsIgnoreCase("ONEXCEPTION")) {
            _cancel.setVisibility(View.GONE);
            _titel.setVisibility(View.GONE);
            this.setCancelable(false);
        } else if (params.equalsIgnoreCase("NO_NETWORK")) {
            _cancel.setVisibility(View.GONE);

            _titel.setVisibility(View.GONE);
            this.setCancelable(false);
        }

        if (tittleStr != null && !tittleStr.equals("")) {
            _titel.setText(tittleStr);
        } else {
            _titel.setVisibility(View.GONE);
        }

        if (msg != null && !msg.equals("")) {
            _msg.setText(msg);
        }

        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onCancelPress(params);
                }
            }
        });

        _ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onYesPress(params, "" + tittleStr);
                }
            }
        });
    }

    /*Join Church POP up dialog*/
    public CustomDialog(Context activity,
                        IDialogListener dialog) {

        super(activity, R.style.newDialog);
        final Context context = activity;
//        btnWidth = (int) (Util.getScreenWidth(getContext()) * .2f);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.join_church_custom_dailog);
        this.setCancelable(false);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        this.dialogListener = dialog;
        final RelativeLayout rr_main = (RelativeLayout) findViewById(R.id.ll_1);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                circularRevealEnter(rr_main);
            }
        });
        final EditText edtAccessCode = (EditText) findViewById(R.id.edt_access_code);
        Button btnJoin = (Button) findViewById(R.id.btn_join);
        TextView tvClickHere = (TextView) findViewById(R.id.tv_click_here);
        TextView tvClickHere2 = (TextView) findViewById(R.id.tv_click_here2);
        ImageView iv_cross = (ImageView) findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onCancelPress(NO_PARAM);
                }
            }
        });

        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onYesPress(REQUEST_ACCESS, "");
                }
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edtAccessCode.getText().toString().isEmpty()) {
                    edtAccessCode.setError(context.getString(R.string.str_enter_access_code));
                    edtAccessCode.requestFocus();
                    return;
                } else {
                    cancel();
                    if (dialogListener != null) {
                        dialogListener.onYesPress("JOIN", edtAccessCode.getText().toString());
                    }
                }
            }
        });
    }

    /*Leave Church POP up dialog*/
    public CustomDialog(
            IDialogListener dialog, Context activity, String tittleMessage) {

        super(activity, R.style.newDialog);
        final Context context = activity;
//        btnWidth = (int) (Util.getScreenWidth(getContext()) * .2f);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.leave_church_custom_dailog);
        this.setCancelable(false);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        this.dialogListener = dialog;
        final RelativeLayout rr_main = (RelativeLayout) findViewById(R.id.ll_1);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                circularRevealEnter(rr_main);
            }
        });
        final EditText edtAccessCode = (EditText) findViewById(R.id.edt_access_code);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        TextView title = (TextView) findViewById(R.id.title);
        if (tittleMessage != null && !tittleMessage.isEmpty()) {
            title.setText(tittleMessage);
        } else {
            title.setVisibility(View.GONE);
        }
        ImageView iv_cross = (ImageView) findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onCancelPress(NO_PARAM);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onYesPress(REQUEST_ACCESS, "");
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cancel();
                if (dialogListener != null) {
                    dialogListener.onYesPress("JOIN", edtAccessCode.getText().toString());
                }

            }
        });
    }


    /*Thanks Add Church POP up dialog*/
    public CustomDialog(IDialogListener dialog, String tittleMessage,String messageString, Context activity) {

        super(activity, R.style.newDialog);
        final Context context = activity;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.thanks_custom_dailog);
        this.setCancelable(false);
        this.dialogListener = dialog;
        final RelativeLayout rr_main = (RelativeLayout) findViewById(R.id.ll_1);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                circularRevealEnter(rr_main);
            }
        });
        final EditText edtAccessCode = (EditText) findViewById(R.id.edt_access_code);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        final TextView edtTitle = (TextView) findViewById(R.id.edt_title);
        TextView edtMessage = (TextView) findViewById(R.id.edt_message);
        if (tittleMessage != null && !tittleMessage.isEmpty()) {
            edtTitle.setText(tittleMessage);
        } else {
            edtTitle.setVisibility(View.GONE);
        }
        if (edtMessage != null && !messageString.isEmpty()) {
            edtMessage.setText(messageString);
        } else {
            edtMessage.setVisibility(View.GONE);
        }

        ImageView iv_cross = (ImageView) findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (dialogListener != null) {
                    dialogListener.onCancelPress(NO_PARAM);
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cancel();
                if (dialogListener != null) {
                    dialogListener.onYesPress("CLOSE", edtTitle.getText().toString());
                }

            }
        });
    }

    private static void circularRevealEnter(RelativeLayout rr_main) {

        int cx = rr_main.getWidth() / 2;
        int cy = rr_main.getHeight() / 2;

        float finalRadius = Math.max(rr_main.getWidth(), rr_main.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(rr_main, cx, cy, 0, finalRadius);
        }
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rr_main.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    /**
     * @param rr_main        Root layout
     * @param param          params from activity return after user event
     * @param dialogListener listner to send callback to activity
     * @param forYes         On yes press
     * @param dialog         Dialog context
     * @param extraString    If Any extra input need to be send with listner arguments
     */
    private static void circularRevealExit(final RelativeLayout rr_main, final String param,
                                           final IDialogListener dialogListener, final boolean forYes,
                                           final CustomDialog dialog, final String extraString) {

        int cx = rr_main.getWidth() / 2;
        int cy = rr_main.getHeight() / 2;

        float finalRadius = Math.max(rr_main.getWidth(), rr_main.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(rr_main, cx, cy, finalRadius, 0);
        }
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rr_main.setVisibility(View.VISIBLE);
        circularReveal.start();
        // make the view invisible when the animation is done
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (forYes) {
                    rr_main.setVisibility(View.GONE);
                    dialogListener.onYesPress(param, extraString.trim());
                    dialog.cancel();
                } else {
                    dialogListener.onCancelPress(param);
                    dialog.cancel();
                }

            }
        });
    }

}


