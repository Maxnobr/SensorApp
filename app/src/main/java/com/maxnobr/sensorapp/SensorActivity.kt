package com.maxnobr.sensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent

import kotlinx.android.synthetic.main.activity_sensor.*
import kotlinx.android.synthetic.main.content_sensor.*

class SensorActivity : AppCompatActivity() , SensorEventListener {

    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null

    private var gravity:MutableList<Float> = mutableListOf()
    private var linearAcceleration:MutableList<Float> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if(gravity.size == 0) {
            gravity.add(0F)
            gravity.add(0F)
            gravity.add(0F)

            linearAcceleration.add(0F)
            linearAcceleration.add(0F)
            linearAcceleration.add(0F)
        }
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_sensor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //Log.i("sensor","X:${event.x} Y: ${event.y}")
        textView4.text = "X:${event.x}"
        textView5.text = "Y:${event.y}"
        return true
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    override fun onSensorChanged(event: SensorEvent?) {

        if (event != null) {
            //Log.i("sensor","${event.values[0]} , ${event.values[1]} , ${event.values[2]}")
            //ground!!.updateMe(event.values[1] , event.values[0])

            val alpha = 0.8F

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

            linearAcceleration[0] = event.values[0] - gravity[0]
            linearAcceleration[1] = event.values[1] - gravity[1]
            linearAcceleration[2] = event.values[2] - gravity[2]

            textView1.text = "X:${linearAcceleration[0]}"
            textView2.text = "Y:${linearAcceleration[1]}"
            textView3.text = "Z:${linearAcceleration[2]}"

        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this,mAccelerometer,
                SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }
}
