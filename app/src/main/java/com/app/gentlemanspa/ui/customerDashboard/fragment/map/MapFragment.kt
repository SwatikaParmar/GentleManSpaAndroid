package com.app.gentlemanspa.ui.customerDashboard.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentMapBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.AddressFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.ServiceFragmentArgs
import com.app.gentlemanspa.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale


@Suppress("DEPRECATION")
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraIdleListener,
    View.OnClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private var selectedLatLng: LatLng? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val args: MapFragmentArgs by navArgs()

    private var addressType: String = ""
    private var headerTitle: String = ""
    private var customerAddressId: Int=0
    var customerAddress: Address? =null


    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 822
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        getLocation()
        initUI()
    }
    private fun initUI() {
        addressType=args.AddressType
        headerTitle= args.headerTitle.toString()
        customerAddressId=args.addressId
        if (headerTitle.isNotEmpty()){
            binding.tvHeaderTitle.text=headerTitle
        }
        binding.onClick = this
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapClickListener(this)
        googleMap.setOnCameraIdleListener(this) // Set camera idle listener

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            getLocation()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location
                    moveCameraToLocation(location) // Move camera to current location
                } else {
                    requireContext().showToast("Unable to find current location")

                }
            }.addOnFailureListener { exception ->
               requireContext().showToast("exception $exception")
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun moveCameraToLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0F))

    }

    override fun onMapClick(latLng: LatLng) {
        googleMap.clear()
        selectedLatLng = latLng
        fetchAddressFromLatLng(latLng.latitude, latLng.longitude)
    }

    @SuppressLint("SetTextI18n")
    private fun fetchAddressFromLatLng(latitude: Double, longitude: Double) {
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address: Address = addresses[0]
                customerAddress=address
                binding.locationText.text = address.getAddressLine(0)
            } else {
                binding.locationText.text = "No address found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            requireContext().showToast("Unable to get address")
        }
    }



    override fun onCameraIdle() {
        val cameraPosition = googleMap.cameraPosition.target
        val latitude = cameraPosition.latitude
        val longitude = cameraPosition.longitude
        fetchAddressFromLatLng(latitude, longitude)
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed to get the current location
                    getLocation()
                } else {
                    // Permission denied, show a message to the user
                    requireContext().showToast( "Location permission is required to show your current location.")
                }
            }
            else -> {
                // Handle other permission requests if necessary
            }
        }
    }



    override fun onClick(v: View) {
        when(v){
            binding.btnEnterAddress -> {
                val action = MapFragmentDirections.actionMapFragmentToEditAddressFragment(customerAddress!!,"",customerAddressId,addressType)
                findNavController().navigate(action)

            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }

    }
}


