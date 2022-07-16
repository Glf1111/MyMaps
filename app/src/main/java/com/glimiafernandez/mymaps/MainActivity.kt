package com.glimiafernandez.mymaps



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimiafernandez.mymaps.models.Place
import com.glimiafernandez.mymaps.models.UserMap
import com.glimiafernandez.mymaps.models.UserPlacesEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils.getActivity
import java.io.*

private const val FILE_NAME = "UserMaps.data"
const val EXTRA_MAP_TITLE ="EXTRA_MAP_TITLE"
const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var userMap: MutableList<UserPlacesEntity>
    private lateinit var mapsAdapter: MapsAdapter


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val userData = data?.getSerializableExtra(EXTRA_USER_MAP) as UserPlacesEntity
                Log.i(TAG, "onActivityResult with new map ${userData.title}")
                userMap.add(userData)
                mapsAdapter.notifyItemInserted(userMap.size - 1)


            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val rvMaps = findViewById<RecyclerView>(R.id.rvMaps)
        val favCreateMap = findViewById<FloatingActionButton>(R.id.fabCreateMap)
        userMap = deserializableUserMaps(this).toMutableList()


        //Set the layout manager on the recycler view
        rvMaps.layoutManager = LinearLayoutManager(this)
        //Set the adapter on the recycler view
        mapsAdapter = MapsAdapter(this, userMap, object : MapsAdapter.OnClickListener {
            override fun onItemCLick(position: Int) {
                Log.i(TAG, "onClickListener $position")
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMap[position])
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left)


            }

        })
        rvMaps.adapter = mapsAdapter
        //when user tap on view , navigate new activity

        favCreateMap.setOnClickListener {
            showAlertDialog()
        }


    }

    @SuppressLint("RestrictedApi")
    fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map, null)


        val dialog = AlertDialog
            .Builder(this)
            .setTitle("Map Title")
            .setView(mapFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

            val title = mapFormView.findViewById<EditText>(R.id.etTitleMap).text.toString()


            if (title.trim().isEmpty()) {
                Toast.makeText(this, "Place must have non-empty Title", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //navigate to create a map activity
            val intent = Intent(getActivity(this), MapsActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            getActivity(this)
            startForResult.launch(intent)
            dialog.dismiss()


        }


    }

    private fun deserializableUserMaps(context: Context): List<UserMap> {
        Log.i(TAG, "deserializableUserMap")
        val dataFile = getDataFile(context)
        if (!dataFile.exists()) {
            Log.i(TAG, "Data file does not exist yet")
            return emptyList()
        }
        ObjectInputStream(FileInputStream(dataFile)).use { return it.readObject() as List<UserMap> }


    }

    private fun serializeUserMaps(context: Context, userMap: List<UserMap>) {
        Log.i(TAG, "SerializeUserMap")
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use { it.writeObject(userMap) }
    }

    //Return the file which other methods can read_from and write_to the file
    private fun getDataFile(context: Context): File {
        Log.i(TAG, "Getting from the directory ${context.filesDir}")
        return File(context.filesDir, FILE_NAME)
    }



}






