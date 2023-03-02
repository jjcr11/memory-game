package com.jjcr11.memorygame.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentBackdropExportBinding
import com.jjcr11.memorygame.model.AppDatabase
import com.jjcr11.memorygame.model.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader

class BackdropExportFragment : Fragment() {

    private lateinit var binding: FragmentBackdropExportBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var openIntent: Intent
    private var activityResultLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                val inputStream = activity?.contentResolver?.openInputStream(uri)
                val inputStreamReader = InputStreamReader(inputStream)
                val typeToken = object : TypeToken<List<Score>>() {}.type
                val scores: List<Score> = Gson().fromJson(inputStreamReader.readText(), typeToken)
                lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        for (score in scores) {
                            AppDatabase.getDatabase(requireContext()).dao().addScore(score)
                        }
                    }
                    Toast.makeText(requireContext(), "Successfully", Toast.LENGTH_SHORT).show()
                    (parentFragment as SettingsFragment).removeBackdrop()
                }

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackdropExportBinding.inflate(inflater, container, false)

        openIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    activityResultLaunch.launch(openIntent)
                }
            }

        binding.mcvExport.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    val scores =
                        AppDatabase.getDatabase(requireContext()).dao().getAllScoresByScore()
                    val content = Gson().toJson(scores)
                    val file = File(requireContext().filesDir, "memory-game-backup.json")
                    file.writeText(content)
                    val intent: Intent = Intent().let {
                        it.action = Intent.ACTION_SEND
                        it.putExtra(
                            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                                requireContext(),
                                requireContext().applicationContext.packageName + ".provider", file
                            )
                        )
                        it.type = "text/json"
                        it
                    }
                    startActivity(Intent.createChooser(intent, null))
                }
                (parentFragment as SettingsFragment).removeBackdrop()
            }
        }

        binding.mcvImport.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        activityResultLaunch.launch(openIntent)
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            } else {
                activityResultLaunch.launch(openIntent)
            }
        }

        return binding.root
    }
}