package com.example.compass

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exit_dialog.view.*
import kotlinx.android.synthetic.main.send_message_sheet.view.*
import kotlin.math.roundToInt


private var currentDegree = 0f
private var mSensorManager: SensorManager? = null
private var sensor = Sensor.TYPE_ORIENTATION

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()



        open_nav.setOnClickListener {
            drawable_layout.openDrawer(Gravity.LEFT)
        }



        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> {
                    val alertDialog = AlertDialog.Builder(this)
                    val dialog = alertDialog.create()
                    val dialogView =
                            layoutInflater.inflate(R.layout.custom_dialog_about, null, false)
                    dialog.setView(dialogView)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()
                }
                R.id.share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Extra text or Link that you want to add"
                    )
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Technical Speaks")
                    startActivity(Intent.createChooser(sharingIntent, "Share"))
                }
                R.id.for_ideas -> {
                    val sheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
                    val view = layoutInflater.inflate(R.layout.send_message_sheet, null, false)
                    sheetDialog.setContentView(view)
                    view.cancel_button.setOnClickListener {
                        sheetDialog.cancel()
                    }
                    view.btn_instagramm.setOnClickListener {
                        startActivity(
                                Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.instagram.com/shokirov_ollohberdi/")
                                )
                        )
                    }
                    view.btn_telegram.setOnClickListener {
                        startActivity(
                                Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://t.me/It_blog_adm1n")
                                )
                        )
                    }

                    sheetDialog.show()
                }
            }
            true
        }

        @Suppress("DEPRECATION")
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensor = Sensor.TYPE_ORIENTATION
        @Suppress("DEPRECATION")
        if (sensor != null) {
            mSensorManager?.registerListener(
                    this,
                    mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_FASTEST
            )
        } else {
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val degree = event!!.values[0].roundToInt()
        var rotateAnimation = RotateAnimation(
                currentDegree,
                (-degree).toFloat(),
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        )
        rotateAnimation.duration = 80
        rotateAnimation.fillAfter = true
        first.startAnimation(rotateAnimation)
        currentDegree = (-degree).toFloat()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        super.onBackPressed()

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}