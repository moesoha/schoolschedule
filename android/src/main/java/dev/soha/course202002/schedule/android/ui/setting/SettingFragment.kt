package dev.soha.course202002.schedule.android.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.soha.course202002.schedule.android.R
import dev.soha.course202002.schedule.android.ui.oalogin.OaLoginActivity
import java.util.*

class SettingFragment: PreferenceFragmentCompat() {
	companion object {
		val TAG = SettingFragment::class.qualifiedName
		const val REQUEST_OA_LOGIN_ACTIVITY = 2333
	}

	private lateinit var settingViewModel: SettingViewModel
	private var loginActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { when (it.resultCode) {
		OaLoginActivity.RESULT_LOGIN_SUCCESS -> settingViewModel.syncWithOa()
		OaLoginActivity.RESULT_LOGIN_FAIL -> Unit
	} }

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.setting, rootKey)

		settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java).apply {
			loading.observe(this@SettingFragment) {
				val progressBar: ProgressBar? = activity?.findViewById(R.id.progress_bar)
				val window = activity?.window
				if (it) {
					window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
					progressBar?.visibility = View.VISIBLE
				} else {
					window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
					progressBar?.visibility = View.GONE
				}
			}
			loggedInUsername.observe(this@SettingFragment) {
				findPreference<Preference>(getString(R.string.key_setting_oa_sync))?.summary =
					it?.let { getString(R.string.oa_sync_summary_username, it) }
			}
			currentWeekNumber.observe(this@SettingFragment) {
				findPreference<Preference>(getString(R.string.key_setting_current_week))?.summary =
					it?.let { getString(R.string.current_week_summary, it) }
			}
		}

		findPreference<Preference>(getString(R.string.key_setting_oa_sync))?.setOnPreferenceClickListener {
			if (settingViewModel.loggedInUsername.value == null) { loginActivityLauncher.launch(Intent(activity, OaLoginActivity::class.java)) }
			else {
				AlertDialog.Builder(context)
					.setTitle(R.string.oa_sync)
					.setMessage(R.string.oa_sync_account_logged_in)
					.setPositiveButton(android.R.string.ok) { _, _ ->
						settingViewModel.syncWithOa()
					}
					.setNegativeButton(R.string.relogin) { _, _ -> loginActivityLauncher.launch(Intent(activity, OaLoginActivity::class.java)) }
					.show()
			}
			true
		}
		findPreference<EditTextPreference>(getString(R.string.key_setting_current_week))?.apply {
			setOnBindEditTextListener {
				it.inputType = InputType.TYPE_CLASS_NUMBER
				settingViewModel.currentWeekNumber.value?.let { v -> it.setText(v.toString()) }
			}
			setOnPreferenceChangeListener { _, newValue ->
				val currentWeek = (newValue as String).toInt()
				settingViewModel.setCurrentWeekNumber(currentWeek)
				true
			}
		}
	}

	override fun onResume() {
		Log.v(TAG, "onResume invoked")
		super.onResume()
		settingViewModel.loadLoggedInUsername()
		settingViewModel.loadSettings()
	}
}