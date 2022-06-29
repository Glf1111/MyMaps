package com.glimiafernandez.mymaps

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.glimiafernandez.mymaps.databinding.ActivityMapsBinding
import com.glimiafernandez.mymaps.models.Place
import com.glimiafernandez.mymaps.models.UserMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var markers: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Message for add a marker
        mapFragment.view?.let {
            Snackbar.make(it, "Long press to add a marker!!",Snackbar.LENGTH_INDEFINITE)
                .setAction("Don't show"){}
                .setActionTextColor(ContextCompat.getColor(this,android.R.color.white))
                .show()


        }

    }
        override  fun onCreateOptionsMenu(menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.menu,menu )
            return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        //Check that `item` is the save menu option
       if(markers.isEmpty()) {
           Toast.makeText(this, "There must be at least one marker on the map", Toast.LENGTH_LONG)
               .show()
           return true
       }
        //Create a list with a tittle and place (place is generated for each marker)
        val places = markers.map {marker -> marker.title?.let { marker.snippet?.let { it1 ->
            Place(it,
                it1, marker.position.latitude , marker.position.longitude)
        } } }
        val userMap = intent.getStringExtra(EXTRA_MAP_TITLE)?.let { UserMap(it,
            places as List<Place>
        ) }

        val data = Intent()
        data.putExtra(EXTRA_USER_MAP,userMap)
        setResult(Activity.RESULT_OK,data)
        finish()
        return super.onOptionsItemSelected(item)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Delete Marker
        mMap.setOnInfoWindowClickListener {
            markers.remove(it)
            it.remove()
        }


        //Add new Marker and storage in mutable List
        mMap.setOnMapLongClickListener {
            showAlertDialog(it)

        }


        // Add a marker in Sydney and move the camera
        val madrid = LatLng(40.416775, -3.703790)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid,10f))
    }

    private fun showAlertDialog(LatLng: LatLng) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place, null)


        val dialog = AlertDialog
            .Builder(this)
            .setTitle("Create a Marker")
            .setView(placeFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

            val title = placeFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            val description = placeFormView.findViewById<EditText>(R.id.etDescription).text.toString()

            if (title.trim().isEmpty()) {
                Toast.makeText(this ,"Place must have non-empty Title",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions().position(LatLng).title(title).snippet(description))
            if (marker != null) {
                markers.add(marker)
            }
            dialog.dismiss()





        }
    }

}