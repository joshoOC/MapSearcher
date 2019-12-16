package com.angiie.mapsearcher

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException

open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var bd = Ubicaciones(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        bd.openBD()
        misLugares()

        btn_buscar.setOnClickListener{vista ->
            buscarLugar(vista)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    fun buscarLugar(view: View) {
        lateinit var lugar: String
        lugar = et_buscarLugar.text.toString()
        var addressList: List<Address>? = null

        if (lugar == null || lugar == "") {
            Toast.makeText(applicationContext,"RELLENE EL CAMPO",Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(lugar, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                var lugares = bd.consultarLugar(lugar)
                if (lugares==""){
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(lugar))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f))
                    Toast.makeText(applicationContext, address.latitude.toString() + " " + address.longitude, Toast.LENGTH_LONG).show()
                }else{
                    var partes = lugares.split(",")
                    var latitud = partes[0].toDouble()
                    var longitud = partes[1].toDouble()
                    val latLng = LatLng(latitud, longitud)
                    mMap.addMarker(MarkerOptions().position(latLng).title(lugar))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f))
                    Toast.makeText(this,lugares,Toast.LENGTH_SHORT).show()
                }


            }catch ( e: Exception){
                Toast.makeText(this, "Lugar no encontrado :C",Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun misLugares(){
        bd.registrarLugar("casita", 18.879836 ,-99.230097)
        bd.registrarLugar("Lugar de mis penas", 18.8496,-99.200242)
    }




}
