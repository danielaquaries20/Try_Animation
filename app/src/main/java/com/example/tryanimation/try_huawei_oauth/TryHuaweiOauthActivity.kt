package com.example.tryanimation.try_huawei_oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R
import com.huawei.hms.common.ApiException
import com.huawei.hms.hihealth.AutoRecorderController
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.HealthDataTypes
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton

class TryHuaweiOauthActivity : AppCompatActivity() {

    private val REQUEST_AUTH = 1002
    private val tagHMS = "HealthKit-Huawei"

    private lateinit var btnTryOauth: Button
    private lateinit var btnHuaweiAuth: HuaweiIdAuthButton

    private val authParams: AccountAuthParams =
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode()
            .createParams()
    private lateinit var service: AccountAuthService
    private lateinit var controller: SettingController
    private lateinit var recorder: AutoRecorderController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_huawei_oauth)

        btnTryOauth = findViewById(R.id.btnTry)
        btnHuaweiAuth = findViewById(R.id.btnHuaweiAuth)

        service = AccountAuthManager.getService(this, authParams)

        controller = HuaweiHiHealth.getSettingController(this)
        recorder = HuaweiHiHealth.getAutoRecorderController(this)
        initClick()

    }

    private fun initClick() {
        btnTryOauth.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }

        btnHuaweiAuth.setOnClickListener {
            requestAuthorization()
//            startActivityForResult(service.signInIntent, 8888)
        }
    }

    private fun requestAuthorization() {
//        val listScope = arrayOf(Scopes.HEALTHKIT_HEARTHEALTH_READ)
        val listScope = arrayOf(Scopes.HEALTHKIT_HEARTHEALTH_READ,
            Scopes.HEALTHKIT_HEARTHEALTH_WRITE,
            Scopes.HEALTHKIT_HEARTRATE_READ,
            Scopes.HEALTHKIT_HEARTRATE_WRITE)
        val intent = controller.requestAuthorizationIntent(listScope, true)
        startActivityForResult(intent, REQUEST_AUTH)
    }

    private fun startRecord() {
        recorder.startRecord(HealthDataTypes.DT_INSTANTANEOUS_SPO2) {}
        recorder.startRecord(DataType.DT_INSTANTANEOUS_HEART_RATE) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Process the authorization result to obtain the authorization code from AuthAccount.
        if (requestCode == REQUEST_AUTH) {
            Log.d(tagHMS, "Testing 1: $resultCode")
            Log.d(tagHMS, "Data: $data")
            val resultData = controller.parseHealthKitAuthResultFromIntent(data)

            if (resultData == null) {
                Log.d(tagHMS, "Testing 2")
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT).show()
                return
            }

            if (resultData.isSuccess) {
                Log.d(tagHMS, "Testing 3")
                val authAccount = resultData.authAccount
                Log.d(tagHMS, "AuthAccount: $authAccount")
                Log.d(tagHMS, "AuthorizedScopes: ${authAccount.authorizedScopes}")
                if (authAccount != null && authAccount.authorizedScopes != null) {
                    Log.d(tagHMS, "Testing 4")
                    val authorizedScopes = authAccount.authorizedScopes
                    Toast.makeText(this, "Data: ${authorizedScopes.size}", Toast.LENGTH_LONG).show()
                    Log.d(tagHMS, "Data: ${authorizedScopes.size}")
                }
            } else {
                Log.d(tagHMS, "Authorization Failed - Error Code: ${resultData.errorCode}")
                Toast.makeText(this,
                    "Authorization Failed - Error Code: ${resultData.errorCode}",
                    Toast.LENGTH_SHORT).show()

            }
            Log.d(tagHMS, "Testing 5")
        }
        if (requestCode == 8888) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                val authAccount = authAccountTask.result
                Log.d("ResultHuawei", "serverAuthCode:" + authAccount.authorizationCode)
                Log.d("ResultHuawei", "idToken:" + authAccount.idToken)
                // Obtain the ID type (0: HUAWEI ID; 1: AppTouch ID).
                Log.d("ResultHuawei", "accountFlag:" + authAccount.accountFlag)
                val theData =
                    "${authAccount.authorizationCode}\n${authAccount.idToken}\n${authAccount.accountFlag}"
                Toast.makeText(this, "ResultData: $theData", Toast.LENGTH_SHORT).show()
            } else {
                // The sign-in failed.
                val statusCode = (authAccountTask.exception as ApiException).statusCode
                Log.e("ResultHuawei", "sign in failed: $statusCode")
                Log.e("ResultHuawei", "sign in failed: ${authAccountTask.exception}")
                Toast.makeText(this, "sign in failed: $statusCode", Toast.LENGTH_SHORT).show()
            }
        }
    }
}