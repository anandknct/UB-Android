package com.unitybound.settings.fragment.notifications.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.unitybound.R
import com.unitybound.settings.fragment.beans.UsersSettingsResponse
import kotlinx.android.synthetic.main.activity_notification_settings.*

class NotificationsSettingsActivity() : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    lateinit var context: NotificationsSettingsActivity
    private lateinit var mSettingsData : UsersSettingsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        context = this
        initToolbar()
        getAllSettingsAndSetonUI()
    }

    private fun getAllSettingsAndSetonUI() {
        mSettingsData = intent.getParcelableExtra<UsersSettingsResponse>("data")
//        tv_notification_want_to.text = mSettingsData.data.userInfo.notificationEmail?.toString()
        tv_notification_address.text = mSettingsData.data.userInfo.notificationEmail?.toString()
    }

    fun initToolbar () {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Notifications settings")
    }

    fun onClick(view: View) {
        when (view?.id) {
            R.id.rr_noti_want_to-> {
//                val activityIntent = Intent(context, NotificationSettingsUpdateActivity::class.java)
//                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                val bundle = Bundle()
//                bundle.putParcelable("data", mSettingsData)
//                intent.putExtras(bundle)
//                context.startActivityForResult(activityIntent,1111)

                val intent = Intent(context, NotificationSettingsUpdateActivity::class.java)
                val bundle2 = Bundle()
                bundle2.putParcelable("data", mSettingsData)
                intent.putExtras(bundle2)
                context.startActivityForResult(intent,1111)
            }
            R.id.rr_notification_email -> {
                val activityIntent = Intent(context, NotificationOnEmailUpdateActivity::class.java)
                activityIntent.putExtra("email",tv_notification_address.text.toString())
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1112)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ResourceType")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1111) {

                finish()
            } else if (requestCode == 1112) {
                finish()
//                val email = data!!.getStringExtra("email")
//                if (email != null && email.length > 0) {
//                    tv_email_label.text = email
//                }
            }
        }
    }
}
