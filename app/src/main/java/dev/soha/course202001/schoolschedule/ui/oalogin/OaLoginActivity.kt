package dev.soha.course202001.schoolschedule.ui.oalogin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import dev.soha.course202001.schoolschedule.R

class OaLoginActivity: AppCompatActivity() {
	companion object {
		const val RESULT_LOGIN_SUCCESS = 10001
		const val RESULT_LOGIN_FAIL = 10000
	}

	private lateinit var oaLoginViewModel: OaLoginViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_oalogin)
		setResult(RESULT_LOGIN_FAIL)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val username: EditText = findViewById(R.id.username)
		val password: EditText = findViewById(R.id.password)
		val captcha: EditText = findViewById(R.id.captcha)
		val captchaContainer: ImageView = findViewById(R.id.image_captcha)
		val captchaRefresh: Button = findViewById(R.id.refresh_captcha)
		val login: Button = findViewById(R.id.login)
		val loading: ProgressBar = findViewById(R.id.loading)

		oaLoginViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(OaLoginViewModel::class.java).apply {
			captchaBitmap.observe(this@OaLoginActivity) {
				if (it == null) captchaRefresh.apply {
					setText(R.string.refreshing_captcha)
					isEnabled = false
				} else captchaRefresh.apply {
					setText(R.string.refresh_captcha)
					isEnabled = true
				}
				captchaContainer.setImageBitmap(it)
			}
			loginSuccess.observe(this@OaLoginActivity) { success ->
				if (success == true) {
					Toast.makeText(this@OaLoginActivity, R.string.oa_login_success, Toast.LENGTH_LONG).show()
					setResult(RESULT_LOGIN_SUCCESS)
					finish()
				} else if (success == false) {
					Toast.makeText(this@OaLoginActivity, R.string.oa_login_fail, Toast.LENGTH_LONG).show()
					oaLoginViewModel.loginStart()
				}
			}
			submitting.observe(this@OaLoginActivity) {
				loading.visibility = if (it) View.VISIBLE else View.GONE
			}
			loginCredentials.observe(this@OaLoginActivity) { (u, p) ->
				username.setText(u)
				password.setText(p)
			}
			loginStart()
			loadCredentials()
		}

		captchaRefresh.setOnClickListener { oaLoginViewModel.loginStart() }

		password.apply {
			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE -> oaLoginViewModel.login(username.text.toString(), password.text.toString(), captcha.text.toString())
				}
				false
			}

			login.setOnClickListener { oaLoginViewModel.login(username.text.toString(), password.text.toString(), captcha.text.toString()) }
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> finish()
		}
		return super.onOptionsItemSelected(item)
	}
}