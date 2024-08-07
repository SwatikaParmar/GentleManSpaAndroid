package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.BottomSheetSpecialityBinding
import com.app.gentlemanspa.databinding.FragmentEditProfileProfessionalBinding
import com.app.gentlemanspa.databinding.ImagePickerBottomBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.register.adapter.GenderAdapter
import com.app.gentlemanspa.ui.auth.fragment.register.model.GenderRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.adapter.SpecialityAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.viewModel.UpdateProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.isValidEmail
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditProfileProfessionalFragment : Fragment(), View.OnClickListener {
    private var commaSeparatedSpecialitiesId: String?= null
    private lateinit var specialityAdapter: SpecialityAdapter
    private var specialityList: ArrayList<SpecialityItem> = ArrayList()
    private var profileMessages: String?= null
    private var profileImage: File?= null
    private var profileData: GetProfessionalDetailResponse? = null
    private var genderItem: Int ? = null
    private var currentPhotoPath: String?= null
    private var onPermissionsGranted: (() -> Unit)? = null
    private var specialityId: List<String>? = listOf()
    private lateinit var binding : FragmentEditProfileProfessionalBinding
    private val viewModel: UpdateProfessionalViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGenderSpinner()
        editTextSpace()
        initUI()

    }

    private fun initObserver() {
        viewModel.resultUpdateProfessional.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        if(profileImage != null){
                            viewModel.profileId.set(CommonFunctions.getTextRequestBodyParams(profileData?.data?.id))
                            viewModel.profilePic.set(
                                CommonFunctions.prepareFilePart(
                                    "profilePic",
                                    profileImage!!
                                )
                            )
                            viewModel.profilePicRegister()
                            profileMessages = it.data?.messages.toString()
                        }else{
                            requireContext().showToast(it.data?.messages.toString())
                        }


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultProfileRegister.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        requireContext().showToast(profileMessages.toString())

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultSpeciality.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        specialityList.clear()
                        it.data?.data?.let { it1 -> specialityList.addAll(it1) }


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }


    private fun editTextSpace() {
        CommonFunctions.startSpaceEditText(binding.etFirstName)
        CommonFunctions.startSpaceEditText(binding.etLastName)
        CommonFunctions.startSpaceEditText(binding.etEmail)
        CommonFunctions.startSpaceEditText(binding.etPhone)
        CommonFunctions.startSpaceEditText(binding.etHouse)
        CommonFunctions.startSpaceEditText(binding.etSpeciality)
        CommonFunctions.startSpaceEditText(binding.etStreet)
        CommonFunctions.startSpaceEditText(binding.etCountry)
        CommonFunctions.startSpaceEditText(binding.etState)
        CommonFunctions.startSpaceEditText(binding.etCity)
        CommonFunctions.startSpaceEditText(binding.etPinCode)
    }


    private fun initUI() {
        profileData = AppPrefs(requireContext()).getProfileProfessionalData("PROFILE_DATA")
        binding.etFirstName.setText(profileData?.data?.firstName)
        binding.etLastName.setText(profileData?.data?.lastName)
        binding.etPhone.setText(profileData?.data?.phoneNumber)
        binding.etEmail.setText(profileData?.data?.email)
        binding.etHouse.setText(profileData?.data?.professionalDetail?.houseNoOrBuildingName)
        binding.etStreet.setText(profileData?.data?.professionalDetail?.streetAddress)
        binding.etCountry.setText(profileData?.data?.professionalDetail?.country)
        binding.etState.setText(profileData?.data?.professionalDetail?.state)
        binding.etCity.setText(profileData?.data?.professionalDetail?.city)
        binding.etPinCode.setText(profileData?.data?.professionalDetail?.pincode)
        Glide.with(requireContext()).load(ApiConstants.BASE_FILE +profileData?.data?.profilepic).into(binding.ivProfile)

        specialityId= profileData?.data?.professionalDetail?.specialityIds?.split(",")
        var specialityName = profileData?.data?.professionalDetail?.speciality?.joinToString(",")

        binding.etSpeciality.setText(specialityName)
        if(profileData?.data?.gender=="Male"){
            genderItem =1
        }else if(profileData?.data?.gender=="Female"){
            genderItem =2
        }else if(profileData?.data?.gender=="Other"){
            genderItem=3
        }
        if (genderItem !=null) {
            binding.spGender.setSelection(genderItem!!)
        }
      //  binding.etFirstName.setText(profileData?.data?.firstName)

        viewModel.getSpeciality()

        binding.onClick = this
    }





    private fun setGenderSpinner() {
        val genderList = ArrayList<GenderRequest>()
        genderList.clear()
        genderList.add(GenderRequest(1, "Male"))
        genderList.add(GenderRequest(2, "Female"))
        genderList.add(GenderRequest(3, "Other"))
        val model = GenderRequest(-1, "Select Gender")
        genderList.add(0, model)

        val adapter = GenderAdapter(requireContext(), genderList)
        binding.spGender.adapter = adapter

        binding.spGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val value = parent!!.getItemAtPosition(position).toString()
                    if (value == "Select Gender") {
                        (view as TextView?)?.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.spinner_color
                            )
                        )

                    } else {
                        if (view != null) {
                            (view as TextView).setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.black
                                )
                            )
                        }
                    }

                    genderItem =position
                }
            }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnUpdate -> {

                if (isValidation()) {
                    viewModel.firstName.set(binding.etFirstName.text.toString())
                    viewModel.lastName.set(binding.etLastName.text.toString())
                    viewModel.phoneNumber.set(binding.etPhone.text.toString())
                    viewModel.gender.set(binding.spGender.selectedItem.toString())
                    viewModel.id.set(profileData?.data?.id)
                    viewModel.email.set(binding.etEmail.text.toString())
                    viewModel.pincode.set(binding.etPinCode.text.toString())
                    viewModel.country.set(binding.etCountry.text.toString())
                    viewModel.award.set("")
                    viewModel.city.set(binding.etCity.text.toString())
                    viewModel.streetAddress.set(binding.etStreet.text.toString())
                    viewModel.houseNoOrBuildingName.set(binding.etHouse.text.toString())
                    viewModel.state.set(binding.etState.text.toString())
                    viewModel.trainingLevel.set("")
                    viewModel.status.set("")
                    viewModel.specialityIds.set(commaSeparatedSpecialitiesId)
                    viewModel.updateProfessional()
                }


            }

            binding.etSpeciality ->{
                setSpecialityBottom()
            }

            binding.ivUpload -> {
                setImagePickerBottomSheet()
            }
        }
    }

    private fun isValidation(): Boolean {
        when {

            checkString(binding.etFirstName) -> requireContext().showToast("Please enter first name")

            checkString(binding.etLastName) -> requireContext().showToast("Please enter last name")

            binding.spGender.selectedItem.toString() == "Select Gender" -> requireContext().showToast("Please select gender")

            checkString(binding.etEmail) -> requireContext().showToast("Please enter email")

            !isValidEmail(checkValidString(binding.etEmail)) -> requireContext().showToast("Please enter a valid email address")

            checkString(binding.etPhone) -> requireContext().showToast("Please enter phone number")

            checkValidString(binding.etPhone).length != 10 -> requireContext().showToast("Please enter a valid 10 digit phone number")

            checkString(binding.etSpeciality) -> requireContext().showToast("Please select speciality")

            checkString(binding.etHouse) -> requireContext().showToast("Please enter house or building number")

            checkString(binding.etStreet) -> requireContext().showToast("Please enter street address")

            checkString(binding.etCountry) -> requireContext().showToast("Please enter country")

            checkString(binding.etState) -> requireContext().showToast("Please enter state")

            checkString(binding.etCity) -> requireContext().showToast("Please enter city")

            checkString(binding.etPinCode) -> requireContext().showToast("Please enter pin code")
            else -> return true
        }

        return false
    }


    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the camera result
            // val imageUri = result.data?.data
            profileImage = File(currentPhotoPath)

            binding.ivProfile.setImageURI(Uri.fromFile(profileImage))
            // Use the imageUri
        }
    }

    private var  galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the gallery result
            val imageUri = result.data?.data
            profileImage = File(imageUri?.path.toString())
            binding.ivProfile.setImageURI(imageUri)
            // Use the imageUri
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                currentPhotoPath = photoFile.absolutePath
            } catch (ex: IOException) {
                // Handle the error
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.app.gentlemanspa.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraLauncher.launch(intent)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }



    private fun checkAndRequestPermissionsForCamera(onPermissionsGranted: () -> Unit) {
        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (permissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }) {
            onPermissionsGranted()
        } else {
            this.onPermissionsGranted = onPermissionsGranted
            requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSIONS)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_CODE_CAMERA_PERMISSIONS || requestCode == REQUEST_CODE_GALLERY_PERMISSIONS) &&
            grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Permissions are granted, proceed with the action
            //openCamera()
            onPermissionsGranted?.invoke()
        } else {


                CommonFunctions.goToAppSettings(requireContext())

        }

    }

    private fun checkAndRequestPermissionsForGallery(onPermissionsGranted: () -> Unit) {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        if (permissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }) {
            // Permissions are already granted, proceed with the action
            //onPermissionsGranted()
            // openCamera()
            openGallery()
        } else {
            // Request permissions
            this.onPermissionsGranted = onPermissionsGranted
            requestPermissions(permissions,REQUEST_CODE_GALLERY_PERMISSIONS)
        }
    }



    private fun setImagePickerBottomSheet() {

        val bottomSheet = BottomSheetDialog(requireContext(),R.style.DialogTheme_transparent)
        val bottomSheetLayout = ImagePickerBottomBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetLayout.root)

        // Adjust the layout parameters for full screen
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout.root.parent as View)
        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheet.setCancelable(false)
        bottomSheet.show()

        bottomSheetLayout.tvCancel.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheetLayout.clCamera.setOnClickListener {
            checkAndRequestPermissionsForCamera { openCamera() }
            bottomSheet.dismiss()
        }
        bottomSheetLayout.clGallery.setOnClickListener {
            checkAndRequestPermissionsForGallery { openGallery() }
            bottomSheet.dismiss()

        }
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun setSpecialityBottom() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        val bottomSheetLayout = BottomSheetSpecialityBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetLayout.root)
        bottomSheetLayout.tvCategory.text ="Select Category"
        specialityAdapter =SpecialityAdapter(specialityList,specialityId)
        bottomSheetLayout.rvSpeciality.adapter = specialityAdapter



        bottomSheet.behavior.maxWidth = Resources.getSystem().displayMetrics.widthPixels
        bottomSheet.setCancelable(true)
        bottomSheet.show()

        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetLayout.ivCross.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheetLayout.btnSubmit.setOnClickListener {
            val selectedItems = specialityAdapter.getSelectedItems()
            if (selectedItems.size>0){
                val selectedSpecialities = selectedItems.map { it.speciality }
                val selectedSpecialitiesId = selectedItems.map { it.specialityId }
                val specialityName = selectedSpecialities.joinToString(",")
                binding.etSpeciality.setText(specialityName)
                commaSeparatedSpecialitiesId = selectedSpecialitiesId.joinToString(",")
                bottomSheet.dismiss()
            }else{
                Toast.makeText(requireContext(), "Please atleast one speciality checked", Toast.LENGTH_LONG).show()
            }

        }




    }

    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 101
        private const val REQUEST_CODE_GALLERY_PERMISSIONS = 102
    }

}