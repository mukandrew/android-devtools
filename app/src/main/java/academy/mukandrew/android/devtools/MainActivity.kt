package academy.mukandrew.android.devtools

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.provider.Settings
import android.provider.Settings.Global
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        val dialog =
            MaterialAlertDialogBuilder(this)
                .setTitle("DevTools")
        if (isDeveloperModeEnabled()) {
            dialog.setMessage("Deseja abrir o modo desenvolvedor?")
            dialog.setPositiveButton("Sim") { _, _ ->
                openDeveloperMode()
            }
        } else {
            dialog.setMessage("Modo Dev desabilitado. Deseja abrir as configurações?")
            dialog.setPositiveButton("Sim") { _, _ ->
                openSettings()
            }
        }
        dialog.setNegativeButton("Não") { _, _ -> }
        dialog.show()
    }

    private fun openDeveloperMode() {
        try {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Não foi possível abrir o modo desenvolvedor", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openSettings() {
        try {
            val intent =
                Intent(Settings.ACTION_DEVICE_INFO_SETTINGS)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Não foi possível abrir as configurações", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isDeveloperModeEnabled(): Boolean {
        return Global.getInt(
            applicationContext.contentResolver,
            Global.DEVELOPMENT_SETTINGS_ENABLED,
            0,
        ) != 0
    }
}
