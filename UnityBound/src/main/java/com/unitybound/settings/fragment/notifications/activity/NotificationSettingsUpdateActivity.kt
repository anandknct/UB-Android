package com.unitybound.settings.fragment.notifications.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.unitybound.BuildConfig
import com.unitybound.R
import com.unitybound.settings.fragment.beans.UsersSettingsResponse
import com.unitybound.settings.fragment.general.bean.NameUpdatedResponse
import com.unitybound.utility.ErrorResponse
import com.unitybound.utility.Util
import com.unitybound.utility.customView.CustomDialog
import com.unitybound.utility.network.ApiClient
import com.unitybound.utility.network.ApiInterface
import kotlinx.android.synthetic.main.notification_settings_update_layout.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by Admin on 12/3/2017.
 */
class NotificationSettingsUpdateActivity() : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var callPhoneUpdate: Call<NameUpdatedResponse>
    private lateinit var mSettingsData: UsersSettingsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_settings_update_layout)
        initToolbar()
        initOnClicks()
        getAllSettingsAndSetonUI()
    }

    private fun getAllSettingsAndSetonUI() {
        mSettingsData = intent.getParcelableExtra<UsersSettingsResponse>("data")
        /**
         * Friends
         */
        cb_devotional.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.devotionals==1) true else false
        cb_praises.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.praises==1) true else false
        cb_prayer_request.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.prayerrequest==1) true else false
        cb_testimonials.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.testimonials==1) true else false
        cb_comments.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.comments==1) true else false
        cb_likes.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.likes==1) true else false
//        cb_reply.isChecked = if (mSettingsData.data!!.notificationSetting.friends.reply==1) true else false

        /**
         * Church
         */
        cb_devotional_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.devotionals==1) true else false
        cb_praises_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.praises==1) true else false
        cb_prayer_request_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.prayerrequest==1) true else false
        cb_testimonials_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.testimonials==1) true else false
        cb_comments_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.comments==1) true else false
        cb_likes_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.likes==1) true else false
//        cb_reply_church.isChecked = if (mSettingsData?.data!!.notificationSetting.church.reply==1) true else false

        /**
         * Groups
         */
        cb_devotional_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.devotionals==1) true else false
        cb_praises_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.praises==1) true else false
        cb_prayer_request_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.prayerrequest==1) true else false
        cb_testimonials_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.testimonials==1) true else false
        cb_comments_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.comments==1) true else false
        cb_likes_groups.isChecked = if (mSettingsData?.data!!.notificationSetting.groups.likes==1) true else false
//        cb_reply.isChecked = if (mSettingsData?.data!!.notificationSetting.friends.reply==1) true else false

        /**
         * Events
         */
        cb_devotional_events.isChecked = if (mSettingsData?.data.notificationSetting.events.devotionals==1) true else false
        cb_praises_events.isChecked = if (mSettingsData?.data.notificationSetting.events.praises==1) true else false
        cb_prayer_request_events.isChecked = if (mSettingsData?.data.notificationSetting.events.prayerrequest==1) true else false
        cb_testimonials_events.isChecked = if (mSettingsData?.data.notificationSetting.events.testimonials==1) true else false
        cb_comments_events.isChecked = if (mSettingsData?.data.notificationSetting.events.comments==1) true else false
        cb_likes_events.isChecked = if (mSettingsData?.data.notificationSetting.events.likes==1) true else false
//        cb_reply_events.isChecked = if (mSettingsData?.data.notificationSetting.friends.events==1) true else false

    }

    fun initToolbar() {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Privacy Update")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initOnClicks() {
        tv_cancel.setOnClickListener({
            finish()
        })

        tv_update.setOnClickListener({
            if (Util.checkNetworkAvailablity(this)) {
                updateNotificationSettings()
            } else run {

                val customDialog1 = CustomDialog(this, null,
                        "", resources.getString(R.string.alt_checknet),
                        "ONFAILED")
                if (customDialog1.isShowing) {
                    customDialog1.dismiss()
                }
                customDialog1.show()
            }
        })

    }

    private fun updateNotificationSettings() {
        showProgressDialog()

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        callPhoneUpdate = apiService.updateNotificationSettings(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", this)
                , if (cb_devotional.isChecked) 1 else 0
                , if (cb_praises.isChecked) 1 else 0
                , if (cb_prayer_request.isChecked) 1 else 0
                , if (cb_testimonials.isChecked) 1 else 0
                , if (cb_comments.isChecked) 1 else 0
                , if (cb_likes.isChecked) 1 else 0
                , if (cb_reply.isChecked) 1 else 0

                , if (cb_devotional_church.isChecked) 1 else 0
                , if (cb_praises_church.isChecked) 1 else 0
                , if (cb_prayer_request_church.isChecked) 1 else 0
                , if (cb_testimonials_church.isChecked) 1 else 0
                , if (cb_comments_church.isChecked) 1 else 0
                , if (cb_likes_church.isChecked) 1 else 0
                , if (cb_reply_church.isChecked) 1 else 0

                , if (cb_devotional_groups.isChecked) 1 else 0
                , if (cb_praises_groups.isChecked) 1 else 0
                , if (cb_prayer_request_groups.isChecked) 1 else 0
                , if (cb_testimonials_groups.isChecked) 1 else 0
                , if (cb_comments_groups.isChecked) 1 else 0
                , if (cb_likes_groups.isChecked) 1 else 0
                , if (cb_reply_groups.isChecked) 1 else 0

                , if (cb_devotional_events.isChecked) 1 else 0
                , if (cb_praises_events.isChecked) 1 else 0
                , if (cb_prayer_request_events.isChecked) 1 else 0
                , if (cb_testimonials_events.isChecked) 1 else 0
                , if (cb_comments_events.isChecked) 1 else 0
                , if (cb_likes_events.isChecked) 1 else 0
                , if (cb_reply_events.isChecked) 1 else 0);

        callPhoneUpdate.enqueue(object : Callback<NameUpdatedResponse> {

            override fun onResponse(call: Call<NameUpdatedResponse>, response: Response<NameUpdatedResponse>) {
                hideProgressDialog()
                if (response.body() != null) {
                    Log.d("nik", response.body().toString())
                }
                val sCode = response.code().toString() + ""
                val c = sCode[0].toString()

                if (sCode.equals("200", ignoreCase = true)) {
                    val mResponse = response.body()
                    if (mResponse.statuscode.equals("200", ignoreCase = true)) {
                        //                        tv_description.setText(Util.isNull(mResponse.getData().getUserInfo().get));
                        Toast.makeText(this@NotificationSettingsUpdateActivity,
                                mResponse.msg, Toast.LENGTH_SHORT).show()
                        val intent: Intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                } else {

                    if (response.body() == null) {
                        try {
                            val jObjError = JSONObject(response.errorBody().string())
                            val gson = Gson()
                            val error = gson.fromJson(jObjError.toString(),
                                    ErrorResponse::class.java)
                            var msg: String? = null
                            if (error != null) {
                                msg = error.msg
                            } else {
                                msg = "Something went wrong"
                            }
                            CustomDialog(this@NotificationSettingsUpdateActivity, null, "",
                                    msg,
                                    "ONFAILED").show()
                            hideProgressDialog()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }

            }

            override fun onFailure(call: Call<NameUpdatedResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("nik", t.toString())
                hideProgressDialog()
            }
        })
    }

    fun showProgressDialog() {
        mProgressDialog.show()
        if (mProgressDialog != null) {
            //  mProgressDialog = Utils.createProgressDialog(BaseActivity.this, getString(R.string.str_logging_in));
            mProgressDialog.setCancelable(false)
            mProgressDialog.setMessage("Loading...")
        }
        mProgressDialog.setContentView(R.layout.custom_progressdialog)
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }


}