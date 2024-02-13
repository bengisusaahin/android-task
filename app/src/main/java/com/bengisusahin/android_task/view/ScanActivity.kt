package com.bengisusahin.android_task.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bengisusahin.android_task.R
import com.google.zxing.integration.android.IntentIntegrator

class ScanActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted:Boolean ->
            if (isGranted){
                startQRScanner()
            }else{
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissionCamera(applicationContext)
    }
    private fun checkPermissionCamera(context: Context){
        if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED){
            startQRScanner()
        }else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_LONG).show()
        }else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }
    private fun startQRScanner() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                result.contents?.let { scannedText ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("scanned_text",scannedText)
                    startActivity(intent)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}