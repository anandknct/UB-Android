package com.unitybound.settings.fragment.general.activity

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
import com.unitybound.settings.fragment.general.bean.NameUpdatedResponse
import com.unitybound.utility.ErrorResponse
import com.unitybound.utility.Util
import com.unitybound.utility.customView.CustomDialog
import com.unitybound.utility.network.ApiClient
import com.unitybound.utility.network.ApiInterface
import kotlinx.android.synthetic.main.name_update_layout.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by Admin on 12/3/2017.
 */
class NameUpdateActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var callNameUpdate: Call<NameUpdatedResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.name_update_layout)
        initToolbar()
        initOnClicks()
        val bundle=intent.extras
        var samplename: String? = null
        if(bundle!=null && bundle.containsKey("name"))
        {
            samplename = bundle.getString("name")
        }
        edt_first_name.setText(samplename)
    }

    fun initToolbar () {
        mProgressDialog = ProgressDialog(this, R.style.newDialog)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("Name Update")
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
            if (edt_first_name.text.toString().length==0){
                edt_first_name.setError("Please update first name")
                return@setOnClickListener
            } else if (edt_middle_name.text.toString().length==0){
                edt_middle_name.setError("Please update middle name")
                return@setOnClickListener
            } else if (edt_last_name.text.toString().length==0){
                edt_last_name.setError("Please update last name")
                return@setOnClickListener
            } else if (Util.checkNetworkAvailablity(this)) {
                edt_first_name.setError(null)
                edt_middle_name.setError(null)
                edt_last_name.setError(null)
                updateName(Util.isNull(edt_first_name.text.toString()),
                        Util.isNull(edt_middle_name.text.toString()),
                        Util.isNull(edt_last_name.text.toString()))
            } else run {
                edt_first_name.setError(null)
                edt_middle_name.setError(null)
                edt_last_name.setError(null)
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

    private fun updateName(firstName: String, middleName: String, lastName: String) {
        showProgressDialog()

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        callNameUpdate = apiService.updateName(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", this@NameUpdateActivity),
                firstName,middleName,lastName);

        callNameUpdate.enqueue(object : Callback<NameUpdatedResponse> {

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
                        Toast.makeText(this@NameUpdateActivity,
                                mResponse.msg, Toast.LENGTH_SHORT).show()
                        val intent: Intent = Intent()
                        intent.putExtra("firstName",firstName)
                        intent.putExtra("middleName",middleName)
                        intent.putExtra("lastName",lastName)
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
                            CustomDialog(this@NameUpdateActivity, null, "",
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