package com.example.tryanimation.try_huawei_oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R
import com.huawei.hms.common.ApiException
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.hihealth.data.DataCollector
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.HealthDataTypes
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton

class TryHuaweiOauthActivity : AppCompatActivity() {

    private lateinit var btnTryOauth: Button
    private lateinit var btnHuaweiAuth: HuaweiIdAuthButton

    private val authParams: AccountAuthParams =
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode()
            .createParams()
    private lateinit var service: AccountAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_huawei_oauth)

        btnTryOauth = findViewById(R.id.btnTry)
        btnHuaweiAuth = findViewById(R.id.btnHuaweiAuth)

        service = AccountAuthManager.getService(this, authParams)
        val controller = HuaweiHiHealth.getSettingController(this)
        val intent = controller.requestAuthorizationIntent(arrayOf(Scopes.HEALTHKIT_HEARTHEALTH_READ), true)

        startActivityForResult(intent, 200)

        val recorder = HuaweiHiHealth.getAutoRecorderController(this)
        recorder.startRecord(HealthDataTypes.DT_INSTANTANEOUS_SPO2) {

        }

        recorder.startRecord(DataType.DT_INSTANTANEOUS_HEART_RATE) {

        }
        initClick()

    }

    private fun initClick() {
        btnTryOauth.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }

        btnHuaweiAuth.setOnClickListener {
            startActivityForResult(service.signInIntent, 8888)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data)
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