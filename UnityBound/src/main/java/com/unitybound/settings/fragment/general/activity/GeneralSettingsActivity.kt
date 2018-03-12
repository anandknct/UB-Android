package com.unitybound.settings.fragment.general.activity

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
import kotlinx.android.synthetic.main.activity_general_settings.*

class GeneralSettingsActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    lateinit var context: GeneralSettingsActivity

//    private var mSettingsData: UsersSettingsResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)
        context = this
        initToolbar()

        getAllSettingsAndSetonUI()

    }

    private fun getAllSettingsAndSetonUI() {
        val mSettingsData = intent.getParcelableExtra<UsersSettingsResponse>("data")
        tv_name.text = mSettingsData.data.userInfo.firstName?.toString()  + " " +mSettingsData.data.userInfo.lastName?.toString()
        tv_email.text = mSettingsData.data.userInfo.email?.toString()
        tv_phone.text = mSettingsData.data.userInfo.profile.mobileNo?.toString()
        tv_gender.text = mSettingsData.data.userInfo.gender?.toString()
        tv_relationship_status.text = mSettingsData.data.userInfo.relationshipStatus?.toString()
        tv_about.text = mSettingsData.data.userInfo.profile.aboutMe?.toString()
    }

    fun initToolbar () {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Settings")
    }

    fun onClick(view: View) {
        when (view?.id) {

            R.id.rr_name -> {
                val activityIntent = Intent(context, NameUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activityIntent.putExtra("name",tv_name.text)
                context.startActivityForResult(activityIntent,1111)
            }
            R.id.rr_email -> {
                val activityIntent = Intent(context, EmailUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activityIntent.putExtra("email",tv_email.text)
                context.startActivityForResult(activityIntent,1112)
            }
            R.id.rr_phone -> {
                val activityIntent = Intent(context, PhoneUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activityIntent.putExtra("phone",tv_phone.text)
                context.startActivityForResult(activityIntent,1113)
            }
            R.id.rr_password -> {
                val activityIntent = Intent(context, PasswordUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1114)
            }
            R.id.rr_gender -> {
                val activityIntent = Intent(context, GenderUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1115)
            }
            R.id.rr_relationship_status -> {
                val activityIntent = Intent(context, RelationshipUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1116)
            }
            R.id.rr_about -> {
                val activityIntent = Intent(context, AboutMeUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activityIntent.putExtra("about",tv_about.text)
                context.startActivityForResult(activityIntent,1117)
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
            } else if (requestCode == 1113) {
                val phone = data!!.getStringExtra("phone")
//                val phoneType = data!!.getStringExtra("phoneType")
//                val mobile = data!!.getStringExtra("mobile")
//                val mobileType = data!!.getStringExtra("mobileType")
                if (phone != null && phone.length > 0) {
                    tv_phone.text = phone
                }
            } else if (requestCode == 1114) {
                val password = data!!.getStringExtra("password")

            } else if (requestCode == 1115) {
                val gender = data!!.getStringExtra("gender")
                if (gender != null && gender.length > 0) {
                    tv_gender.text = gender
                }
            } else if (requestCode == 1116) {
                val relationship = data!!.getStringExtra("relationship")
                if (relationship != null && relationship.length > 0) {
                    tv_relationship_status.text = relationship
                }
            } else if (requestCode == 1117) {
                val aboutme = data!!.getStringExtra("aboutme")
                if (aboutme != null && aboutme.length > 0) {
                    tv_about.text = aboutme;
                }
            }
        }
    }
}
