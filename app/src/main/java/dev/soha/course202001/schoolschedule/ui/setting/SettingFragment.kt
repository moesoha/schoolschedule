package dev.soha.course202001.schoolschedule.ui.setting

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.soha.course202001.schoolschedule.R

class SettingFragment: PreferenceFragmentCompat() {
	companion object {
		val TAG = SettingFragment::class.qualifiedName
	}

	private lateinit var settingViewModel: SettingViewModel

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
		setPreferencesFromResource(R.xml.setting, rootKey)
		findPreference<Preference>(getString(R.string.key_setting_oa_sync))?.setOnPreferenceClickListener {
			AlertDialog.Builder(context)
				.setTitle(R.string.oa_sync)
				.setMessage(R.string.oa_sync_account_logged_in)
				.setPositiveButton(android.R.string.ok) { _, _ ->
					Log.d(TAG, "Sending Request")
					activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					val progressBar: ProgressBar? = activity?.findViewById(R.id.progress_bar)
					progressBar?.visibility = View.VISIBLE
					settingViewModel.syncWithOa {
						activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
						progressBar?.visibility = View.GONE
					}
				}
				.setNegativeButton(R.string.relogin) { _, _ ->

				}
				.show()
			true
		}
	}
}