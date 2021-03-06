package com.example.pocketgardener

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*



private const val REQUEST_CAMERA = 110
private const val REQUEST_GALLERY = 111
private const val TAG = "GardenPhoto"

class GardenActivity : PermittedActivity(), TimePickerDialog.OnTimeSetListener {
    private lateinit var name : String
    private lateinit var photosList: RecyclerView
    private lateinit var dialogView : View

    private lateinit var daySpinner : Spinner
    private lateinit var monthSpinner : Spinner
    private var month = 1
    private var day = 1
    private var notifications = true
    private var file_name = ""

    private lateinit var prefs: SharedPreferences

    private val photoDirectory
        get() = File(Environment.getExternalStorageDirectory(), "pocketgardener")

    private fun setUpUI() {
        name = intent.getStringExtra("name")
        val planted = intent.getStringExtra("planted")
        val comments = intent.getStringExtra("comments")
        notifications = intent.getBooleanExtra("notifications", true)

        val nameTextView : TextView = findViewById(R.id.plant_header)
        val plantedTextView : TextView = findViewById(R.id.plantedText)
        val commentTextView : TextView = findViewById(R.id.commentText)

        nameTextView.text = name
        plantedTextView.text = planted
        commentTextView.text = comments

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val inflater = LayoutInflater.from(this)
        dialogView = inflater.inflate(R.layout.alert_photo, null)
        setContentView(R.layout.individual_plant)
        setUpUI()

        photosList = findViewById(R.id.photosList)
        photosList.layoutManager = GridLayoutManager(this, 3)

        createNotificationChannel()
        setUpSpinners()

        sortOutPermissions()
        loadDayPhotos()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sortOutPermissions() {
        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions, 100, {
        }, {
            Toast.makeText(this, "Unable to store photos.", Toast.LENGTH_LONG).show()
        })
    }

    private fun loadDayPhotos() {
        val photos :ArrayList<PlantPhoto> = arrayListOf()
        if (photoDirectory.exists()) {
            Log.d(TAG, "Photo directory exists")
            for (file_dir in photoDirectory.listFiles()) {
                if (name in file_dir.toString()) {
                    for (file in file_dir.listFiles()) {
                        Log.d(TAG, "File: $file")
                        val start_index = (file.toString()).length - 9
                        file_name = file.toString().substring(start_index)
                        photos.add(PlantPhoto(File(file_dir, file_name)))
                    }
                }
            }
            photosList.adapter = PhotoAdapter(photos)
            val result = Intent()
            result.putExtra("name", name)
            if (photos.size > 0) {
                val lastPhoto = photos[photos.size-1].file.absolutePath
                result.putExtra("image", lastPhoto)
            } else {
                result.putExtra("image", "")
            }
            setResult(Activity.RESULT_OK, result)
        } else {
            if (!photoDirectory.mkdirs()) {
                Log.d(TAG, "directory not made")
            }
            val result = Intent()
            result.putExtra("name", name)
            result.putExtra("image", "")
        }
    }

    private fun dayFile(name : String, month : Int, day : Int) : File{
        val file = File(photoDirectory, String.format("$name/%02d_%02d.jpg", month, day))
        file.parentFile.mkdirs()
        Log.d(TAG, "File: $file")
        return file

    }

    private fun dayUri(name : String, month : Int, day : Int) : Uri {
        val file = dayFile(name, month, day)
        return getUriForFile(this, "com.example.pocketgardener.fileprovider", file)

    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTimeSet(picker: TimePicker, hour: Int, minute: Int) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().apply {
            putInt("hour", hour)
            putInt("minute", minute)
            apply()
        }

        Utilities.scheduleReminder(applicationContext, hour, minute)

        val receiver = ComponentName(this, BootReceiver::class.java)
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

    }

    private fun setReminderTime() {
        val fragment = TimePickerFragment()
        fragment.listener = this
        fragment.hour = prefs.getInt("hour", 6)
        fragment.minute = prefs.getInt("minute", 0)
        fragment.show(supportFragmentManager, null)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Notification.CATEGORY_REMINDER, "Daily Reminder", importance).apply {
            description = "Remember to water your $name"
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.garden_action_bar, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.shareButton -> {
            share()
            true
        }
        R.id.reminderButton -> {
            setReminder()
            true
        }
        R.id.addButton -> {

            promptForAdd()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun promptForAdd() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Choose source")
            setMessage("Where is the photo?")
            setPositiveButton("Camera") { _, _ ->
                takePictureFromCamera()
            }
            setNegativeButton("Gallery") { _, _ ->
                createTakenDialog("Gallery")
            }
        }
        builder.show()
    }


    private fun share() {
        Log.d("ActionBar", "Share Progress")
        if (file_name == "") {
            val toast = Toast.makeText(this, "Cannot share latest photo, no photos have been taken", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
        } else {
            var month_int = file_name.substring(0,2).toInt()
            var day_int = file_name.substring(3,5).toInt()
            Log.d(TAG, "Month: $month_int")
            Log.d(TAG, "Day: $day_int")
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, dayUri(name, month_int, day_int))
            shareIntent.setPackage("com.instagram.android")
            try {
                startActivity(shareIntent)
            } catch(e: ActivityNotFoundException) {
                Toast.makeText(this, "You do not have instagram installed", Toast.LENGTH_LONG).show()

            }

        }

    }

    private fun setReminder() {
        Log.d("ActionBar", "Set Reminder")
        if (notifications) {
            setReminderTime()
        } else {
            Toast.makeText(this, "Cannot set reminder, turn this on in app settings.", Toast.LENGTH_LONG).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun takePictureFromCamera() {
        val today = Calendar.getInstance()
        month = today.get(Calendar.MONTH)
        day = today.get(Calendar.DAY_OF_MONTH)
        Log.d("Camera", "from camera")

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)?.let {
            val uri = dayUri(name, month, day)
            Log.d(TAG, "Uri $uri")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpSpinners() {
        val today = Calendar.getInstance()
        month = today.get(Calendar.MONTH)
        day = today.get(Calendar.DAY_OF_MONTH)

        monthSpinner  = dialogView.findViewById(R.id.month_spinner)
        daySpinner = dialogView.findViewById(R.id.day_spinner)
        ArrayAdapter.createFromResource(
            this, R.array.month_array, android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthSpinner.adapter = adapter
        }

        monthSpinner.setSelection(today.get(Calendar.MONTH), false)
        syncDays()
        daySpinner.setSelection(today.get(Calendar.DAY_OF_MONTH) - 1, false)

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, i: Int, id: Long) {
                syncDays()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTakenDialog(goto : String) {
        val builder = this.let { AlertDialog.Builder(it) }
        builder.setView(dialogView)
        builder.setPositiveButton("Ok") { _, _ ->
            month = monthSpinner.selectedItemPosition + 1
            day = daySpinner.selectedItemPosition + 1
            if (goto == "Camera") {
                takePictureFromCamera()
            } else {
                takePictureFromGallery()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.create()?.show()

    }
    private fun daysInMonth(month: Int) = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        2 -> 29
        else -> 30
    }

    private fun syncDays() {
        val nDays = daysInMonth(month)
        if (daySpinner.adapter != null && nDays == daySpinner.adapter.count) {
            return
        }

        val days = (1..nDays).map { it.toString() }
        daySpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, days.toTypedArray())
    }

    private fun takePictureFromGallery() {
        Log.d("Camera", "from gallery")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun copyUriToUri(from: Uri, to: Uri) {
        Log.d(TAG, "From URI: $from")
        Log.d(TAG, "To URI: $to")
        contentResolver.openInputStream(from).use{input ->
            contentResolver.openOutputStream(to).use {output ->
                try {
                    input?.copyTo(output!!)
                } catch (e: NullPointerException) { }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "Finished get photo activity")
        when(requestCode) {

            REQUEST_CAMERA -> {
                Log.d(TAG, "Finished getting photo from camera")
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Got it OK")
                    loadDayPhotos()
                }
            }
            REQUEST_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Finished getting photo from gallery")
                    data?.data?.let {uri ->
                        copyUriToUri(uri, dayUri(name, month, day))
                        loadDayPhotos()
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }



}
