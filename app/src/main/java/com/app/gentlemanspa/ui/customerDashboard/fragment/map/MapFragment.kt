package com.app.gentlemanspa.ui.customerDashboard.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentMapBinding

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.io.IOException
import java.util.Locale


class MapFragment:Fragment() ,OnMapReadyCallback,LocationListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraMoveStartedListener,GoogleMap.OnCameraIdleListener {
    private var addressScreen: String=""
    private var customerAddressId: Int=0
    private var addressType: String = ""
    var binding : FragmentMapBinding? =null

    private lateinit var googleMap: GoogleMap

    lateinit var markerCurrentLocation: Marker
    lateinit var markerLastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    val LOCATION_PERMISSION_REQUEST_CODE = 822
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    var myAddressLocation: Address? =null

    lateinit var locationManager : LocationManager


    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel
/*    override fun getRootView(): View? {
        return binding?.root
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /*    mHomeActivity?.bottomNavigation(false)
        mHomeActivity?.bottomCart(false)
        mHomeActivity?.toolbar(false)*/
        onClick()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

/*        val token = sharedPrefs?.read(SharedPrefsHelper.KEY_ACCESS_TOKEN,"")
        val hashMap =HashMap<String,String>()
        hashMap[ApiConstants.KEY_AUTHORIZATION] ="Bearer $token"
        Log.d(ContentValues.TAG, "BearerEditAddress: $token")
        viewModel.headers =hashMap*/
/*
        if (requireArguments().containsKey("AddType")){
            val addType =requireArguments().getInt("AddType")
            customerAddressId =requireArguments().getInt("CustomerAddressId")
            addressType =requireArguments().getString("AddressType").toString()
            addressScreen = "BottomAddress"

            if (addType ==1){
                binding?.headerTitle?.text = "Add Address"
            }else{
                binding?.headerTitle?.text = "Edit Address"
            }
        }else{
            val args = MapFragmentArgs.fromBundle(requireArguments())
            binding?.headerTitle?.text = args.headerTitle
            customerAddressId =args.addressId
            addressType = args.addressType
            addressScreen = "address"
        }*/


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkGps(requireContext())
        checkLocationPermission()
       /* if(Edit_Address.idForAddressStatus != null){
            viewModel.setCustomerAddressStatus(Edit_Address.idForAddressStatus!!,true)
            initObserver()
        }*/



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    currentLocation = location
                }
            }
        }
    }

    private fun initObserver() {
   /*     viewModel.responseSetCustomerAddressStatus.observe(viewLifecycleOwner, Observer {
            if (it.status == 200) {
                if (it.data?.isSuccess == true) {
                    Edit_Address.idForAddressStatus =null
                    Toast.makeText(requireContext(), "${it.data?.messages}", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), "${it.data?.messages}", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                Toast.makeText(requireContext(), "${it.errorMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
        })*/

    }

    private fun onClick() {
     /*   binding?.ivBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }*/

        binding?.btnEnterAddress?.isEnabled =false
        binding?.btnEnterAddress?.setOnClickListener {
         /*   val action = MapFragmentDirections.actionMapFragmentToEditAddress(myAddressLocation!!,addressScreen,customerAddressId,addressType)
            findNavController().navigate(action)*/
        }
    }


    override fun onResume() {
        super.onResume()
        // checkGps(requireContext())
        checkLocationPermission()
    }



    private fun checkGps(context: Context):Boolean {
        val gpsStatus: Boolean
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus =locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(gpsStatus){
            //Toast.makeText(requireContext(), "Gps Enabled!!", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        return gpsStatus
    }


    private fun checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            // when permission granted
            getLocation()
        } else {
            // when permission denied

            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf<String?>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            getLocation()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf<String?>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            // initialise location
            val location = task.result
            if (location != null) {
                // initiate address list
                try {
                    geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val senderAddress = addresses?.get(0)?.getAddressLine(0)
                    // currLat = location.latitude
                    //  currLong = location.longitude
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
                        location.longitude), 18.0F))
                    val cameraPosition: CameraPosition = CameraPosition.Builder()
                        .target(LatLng(location.latitude,
                            location.longitude)) // Sets the center of the map to location user
                        .zoom(18.0f) // Sets the zoom
                        //  .bearing(90f) // Sets the orientation of the camera to east
                    //    .tilt(20f) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))



                    googleMap.isMyLocationEnabled = true
                    googleMap.isTrafficEnabled = false
                    googleMap.isBuildingsEnabled = false

                    googleMap.resetMinMaxZoomPreference()
                    googleMap.uiSettings.isMyLocationButtonEnabled = true

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else{
                getLocation()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.setOnCameraMoveListener(this)
        googleMap.setOnCameraMoveStartedListener(this)
        googleMap.setOnCameraIdleListener(this)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

        }


    }

    override fun onLocationChanged(location: Location) {
        currentLocation =location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude,
            currentLocation.longitude), 18.0F))

    }

    private fun setAddress(address: Address) {
        if(address.getAddressLine(0) != null){
            Log.d(TAG, "setAddressFromMap: ${address.countryCode}  ${address.countryName}")

            if(address.countryName =="India"){
                binding?.locationText?.text = address.getAddressLine(0)
                myAddressLocation =address
                binding?.btnEnterAddress?.isEnabled =true
            }else{
                binding?.btnEnterAddress?.isEnabled =false
                binding?.locationText?.text = "Zigy Kart is not available at this location at the moment. please select a different location. "
            }
        }
        if(address.getAddressLine(1) != null){
            if(address.countryName =="India"){
                myAddressLocation =address
                binding?.locationText?.text =binding?.locationText?.text.toString() +address.getAddressLine(1)
                binding?.btnEnterAddress?.isEnabled =true
            }else{
                binding?.btnEnterAddress?.isEnabled =false
                binding?.locationText?.text = "Zigy Kart is not available at this location at the moment. please select a different location. "
            }

        }
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {
        var addresses :List<Address>? =null
        val geo =
            Geocoder(requireContext(), Locale.getDefault())
        try {
            addresses =geo.getFromLocation(googleMap.cameraPosition.target.latitude,googleMap.cameraPosition.target.longitude,1)

            setAddress(addresses!![0])
        }catch (e: Exception) {
            e.printStackTrace() // getFromLocation() may sometimes fail
        }
    }


}