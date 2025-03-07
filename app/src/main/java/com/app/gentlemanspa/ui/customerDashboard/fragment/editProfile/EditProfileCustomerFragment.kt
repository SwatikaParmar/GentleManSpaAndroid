package com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentEditProfileCustomerBinding
import com.app.gentlemanspa.databinding.ImagePickerBottomBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.auth.fragment.register.adapter.GenderAdapter
import com.app.gentlemanspa.ui.auth.fragment.register.model.GenderRequest
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.viewModel.UpdateCustomerViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.PROFILE_CUSTOMER_DATA
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.isValidEmail
import com.app.gentlemanspa.utils.showDeleteAccountDialog
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION")
class EditProfileCustomerFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditProfileCustomerBinding
    private var currentPhotoPath: String? = null
    private var profileImage: File? = null
    private var genderItem: Int? = null
    private var onPermissionsGranted: (() -> Unit)? = null
    private var profileCustomerData: GetProfessionalDetailResponse? = null
    private val viewModel: UpdateCustomerViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileCustomerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initObserver()
        initUI()
    }

    private fun initObserver() {

        viewModel.resultUpdateCustomer.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        if (profileImage != null) {
                            viewModel.profileId.set(
                                CommonFunctions.getTextRequestBodyParams(
                                    profileCustomerData?.data?.id
                                )
                            )
                            viewModel.profilePic.set(
                                CommonFunctions.prepareFilePart(
                                    "profilePic",
                                    profileImage!!
                                )
                            )
                            viewModel.updateCustomerProfilePic()
                            //   requireContext().showToast( it.data?.messages.toString())
                        } else {
                            requireContext().showToast(it.data?.messages.toString())
                        }


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultUpdateCustomerProfilePic.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultDeleteAccount.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireActivity())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("deleteAccount", "dashboard data ->${it.data?.messages}")
                        requireContext().showToast(it.data?.messages.toString())
                        AppPrefs(requireContext()).clearAllPrefs()
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        intent.putExtra("LOG_OUT","logout")
                        startActivity(intent)
                        requireActivity().finish()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun initUI() {
        binding.onClick = this
        binding.countryCode.isEnabled = false
        setGenderSpinner()
        editTextSpace()
        setProfileData()
    }
    @SuppressLint("SetTextI18n")
    private fun setProfileData() {
        profileCustomerData=AppPrefs(requireContext()).getProfileCustomerData(PROFILE_CUSTOMER_DATA)
        binding.etFirstName.setText(profileCustomerData?.data?.firstName.toString())
        binding.etLastName.setText(profileCustomerData?.data?.lastName)
        //binding.spGender.setText(profileCustomerData?.data?.professionalDetail?.speciality.)
        binding.etEmail.setText(profileCustomerData?.data?.email)
        binding.etPhone.setText("${profileCustomerData?.data?.dialCode} ${profileCustomerData?.data?.phoneNumber}")
        Glide.with(requireContext())
            .load(ApiConstants.BASE_FILE + profileCustomerData?.data?.profilepic).error(R.drawable.profile_placeholder).placeholder(R.drawable.profile_placeholder)
            .into(binding.ivProfile)
        val specialityName = profileCustomerData?.data?.professionalDetail?.speciality?.joinToString(",")
        binding.etSpeciality.setText(specialityName)
        when (profileCustomerData?.data?.gender) {
            "Male" -> {
                genderItem = 1
            }

            "Female" -> {
                genderItem = 2
            }

            "Other" -> {
                genderItem = 3
            }
        }
        if (genderItem != null) {
            binding.spGender.setSelection(genderItem!!)
        }
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

                    genderItem = position
                }
            }
    }
    private fun editTextSpace() {
        CommonFunctions.startSpaceEditText(binding.etFirstName)
        CommonFunctions.startSpaceEditText(binding.etLastName)
        CommonFunctions.startSpaceEditText(binding.etEmail)
        CommonFunctions.startSpaceEditText(binding.etPhone)
        CommonFunctions.startSpaceEditText(binding.etSpeciality)
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

            binding.etSpeciality -> {
                //setSpecialityBottom()
            }

            binding.ivUpload -> {
                setImagePickerBottomSheet()
            }
            binding.btnUpdate -> {
                if (isValidation()) {
                    viewModel.firstName.set(binding.etFirstName.text.toString())
                    viewModel.lastName.set(binding.etLastName.text.toString())
                    viewModel.gender.set(binding.spGender.selectedItem.toString())
                    viewModel.dialCode.set(profileCustomerData?.data?.dialCode)
                    viewModel.phoneNumber.set(profileCustomerData?.data?.phoneNumber)
                    viewModel.email.set(binding.etEmail.text.toString())
                    viewModel.id.set(profileCustomerData?.data?.id)
                    viewModel.updateCustomerProfile()
                }

            }
            binding.btnDeleteAccount->{
                proceedToDeleteAccount()
            }
        }
    }
    private fun proceedToDeleteAccount() {
        showDeleteAccountDialog(requireContext(),"Delete Account!","Are you sure you want to delete your account?"){
            viewModel.id.set(AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID))
            viewModel.deleteAccountApi()
        }
    }
    private fun isValidation(): Boolean {
        when {
            checkString(binding.etFirstName) -> requireContext().showToast("Please enter first name")
            checkString(binding.etLastName) -> requireContext().showToast("Please enter last name")
            binding.spGender.selectedItem.toString() == "Select Gender" -> requireContext().showToast(
                "Please select gender"
            )
            //  checkString(binding.etEmail) -> requireContext().showToast("Please enter email")
            //    !isValidEmail(checkValidString(binding.etEmail)) -> requireContext().showToast("Please enter a valid email address")
            //   checkString(binding.etPhone) -> requireContext().showToast("Please enter phone number")
            //   checkValidString(binding.etPhone).length != 10 -> requireContext().showToast("Please enter a valid 10 digit phone number")
            //    checkString(binding.etSpeciality) -> requireContext().showToast("Please enter VIP Clients")
            else -> return true
        }
        return false
    }
    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // val imageUri = result.data?.data
                profileImage = currentPhotoPath?.let { File(it) }
                binding.ivProfile.setImageURI(Uri.fromFile(profileImage))
            }
        }

    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                Log.d("GalleryImage", "imageUri->$imageUri")
            /*  profileImage = File(imageUri?.path.toString())
                Log.d("GalleryImage","profileImage->$profileImage")
                binding.ivProfile.setImageURI(imageUri) */
                imageUri?.let {
                    val file = getFileFromUri(it)
                    if (file != null) {
                        Log.d("GalleryImage", "profileImage->$file")
                        // binding.ivProfile.setImageURI(it)
                        profileImage = file
                        Glide.with(this)
                            .load(profileImage)
                            .into(binding.ivProfile)
                    }
                }
            }
        }

    private fun getFileFromUri(uri: Uri): File? {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val filePath = columnIndex?.let { cursor?.getString(it) }
            return if (filePath != null) File(filePath) else null
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
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
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
    private fun checkAndRequestPermissionsForCamera(onPermissionsGranted: () -> Unit) {
        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            onPermissionsGranted()
        } else {
            this.onPermissionsGranted = onPermissionsGranted
            requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSIONS)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_CODE_CAMERA_PERMISSIONS || requestCode == REQUEST_CODE_GALLERY_PERMISSIONS) &&
            grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            // Permissions are granted, proceed with the action
            // openCamera()
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

        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            openGallery()
        } else {
            // Request permissions
            this.onPermissionsGranted = onPermissionsGranted
            requestPermissions(permissions, REQUEST_CODE_GALLERY_PERMISSIONS)
        }
    }
    private fun setImagePickerBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
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
    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 101
        private const val REQUEST_CODE_GALLERY_PERMISSIONS = 102
    }

}