package com.unitybound.settings.fragment.privacy

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
import com.unitybound.settings.fragment.privacy.activity.WhoCanLookStuffUpdateActivity
import com.unitybound.settings.fragment.privacy.activity.WhoCanLookUpUpdateActivity
import kotlinx.android.synthetic.main.activity_general_settings.*

class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    lateinit var context: PrivacySettingsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)
        context = this
        initToolbar()
        getAllSettingsAndSetonUI()
    }

    private fun getAllSettingsAndSetonUI() {
        /*val mSettingsData = intent.getParcelableExtra<UsersSettingsResponse>("data")
        tv_name.tv_look_up = mSettingsData.data.userInfo.firstName?.toString()
        tv_email.text = mSettingsData.data.userInfo.email?.toString()*/
    }

    fun initToolbar () {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Privacy settings")
    }

    fun onClick(view: View) {
        when (view?.id) {
            R.id.rr_look_up-> {
                val activityIntent = Intent(context, WhoCanLookUpUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1111)
            }
            R.id.rr_look_up_stuff -> {
                val activityIntent = Intent(context, WhoCanLookStuffUpdateActivity::class.java)
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

                val firstName = data!!.getStringExtra("firstName")
                if (firstName != null && firstName.length > 0) {
                    tv_name.text = firstName
                }

                val middleName = data!!.getStringExtra("middleName")
                if (middleName != null && middleName.length > 0) {
                    tv_name.text = firstName + " " + middleName
                }

                val lastName = data!!.getStringExtra("lastName")
                if (lastName != null && lastName.length > 0) {
                    tv_name.text = firstName + " " + middleName + " " + lastName
                }
            } else if (requestCode == 1112) {
                val email = data!!.getStringExtra("email")
                if (email != null && email.length > 0) {
                    tv_email_label.text = email
                }
            }
        }
    }
}
