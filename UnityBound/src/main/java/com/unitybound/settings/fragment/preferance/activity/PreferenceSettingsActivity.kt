package com.unitybound.settings.fragment.preferance.activity

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
import kotlinx.android.synthetic.main.activity_prefrance_settings.*

class PreferenceSettingsActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    lateinit var context: PreferenceSettingsActivity
    private lateinit var mSettingsData : UsersSettingsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefrance_settings)
        context = this
        initToolbar()
    }

    fun initToolbar () {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Preference settings")
    }

    private fun getAllSettingsAndSetonUI() {
        mSettingsData = intent.getParcelableExtra<UsersSettingsResponse>("data")
    }

    fun onClick(view: View) {
        when (view?.id) {
            R.id.rr_def_view-> {
                val activityIntent = Intent(context, DefaultViewUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1111)
            }
            R.id.rr_def_post_creation_type -> {
                val activityIntent = Intent(context, DefaultPostCreationTypeUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1112)
            }
            R.id.rr_def_filter -> {
                val activityIntent = Intent(context, DefaultFilterUpdateActivity::class.java)
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivityForResult(activityIntent,1113)
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
            if (requestCode == 1112) {

                val default_post_creation_type = data!!.getStringExtra("default_post_creation_type")
                if (default_post_creation_type != null && default_post_creation_type.length > 0) {
                    tv_def_post_creation_type.text = default_post_creation_type
                }

            } else if (requestCode == 1111) {
                val default_view = data!!.getStringExtra("default_view")
                if (default_view != null && default_view.length > 0) {
                    tv_default_view.text = default_view
                }
            } else if (requestCode == 1113) {
                val default_filter_selected = data!!.getStringExtra("default_filter_selected")
                if (default_filter_selected != null && default_filter_selected.length > 0) {
                    tv_def_filter.text = default_filter_selected
                }
            }
        }
    }
}
