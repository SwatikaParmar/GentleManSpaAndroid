package com.app.gentlemanspa.ui.auth.fragment.register


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentRegisterBinding
import com.app.gentlemanspa.databinding.ImagePickerBottomBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.register.adapter.GenderAdapter
import com.app.gentlemanspa.ui.auth.fragment.register.model.GenderRequest
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.viewModel.RegisterViewModel
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.CommonFunctions.goToAppSettings
import com.app.gentlemanspa.utils.CommonFunctions.togglePasswordVisibility
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.isValidEmail
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION")
class RegisterFragment : Fragment(), View.OnClickListener {
    private var messagesRegister: String? = ""
    private var cameraPermission = false
    private var currentPhotoPath: String? = null
    private lateinit var binding: FragmentRegisterBinding
    private var isPasswordVisible: Boolean = false
    private var isConfirmPasswordVisible: Boolean = false
    private var profileImage: File? = null
    private var onPermissionsGranted: (() -> Unit)? = null
    private var emailOtp=""
    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory(InitialRepository()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initUI()
    }


    private fun initUI() {
        setGenderSpinner()
        editTextSpace()
        binding.onClick = this
    }

    private fun editTextSpace() {
        CommonFunctions.startSpaceEditText(binding.etFirstName)
        CommonFunctions.startSpaceEditText(binding.etLastName)
        CommonFunctions.startSpaceEditText(binding.etEmail)
        CommonFunctions.startSpaceEditText(binding.etPhone)
        CommonFunctions.startSpaceEditText(binding.etPassword)
        CommonFunctions.startSpaceEditText(binding.etConfirmPassword)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSignup -> {
                if (isValidation()) {
                    viewModel.dialCode.set(binding.countryCode.defaultCountryCode)
                    viewModel.phoneNumber.set(binding.etPhone.text.toString())
                    viewModel.email.set(binding.etEmail.text.toString())
                    viewModel.emailOtp()
                }

            }

            binding.ivTogglePassword -> {
                isPasswordVisible = !isPasswordVisible
                togglePasswordVisibility(
                    binding.etPassword, binding.ivTogglePassword, isPasswordVisible
                )
            }

            binding.ivToggleConfirmPassword -> {
                isConfirmPasswordVisible = !isConfirmPasswordVisible
                togglePasswordVisibility(
                    binding.etConfirmPassword,
                    binding.ivToggleConfirmPassword,
                    isConfirmPasswordVisible
                )
            }

            binding.ivUpload -> {
                setImagePickerBottomSheet()
            }

        }
    }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the camera result
                // val imageUri = result.data?.data
                profileImage = File(currentPhotoPath)
                binding.ivProfile.setImageURI(Uri.fromFile(profileImage))
                // Use the imageUri
            }
        }

    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the gallery result
                val imageUri = result.data?.data
                currentPhotoPath=imageUri?.path.toString()
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
                    requireContext(), "com.app.gentlemanspa.fileprovider", photoFile
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
                    requireContext(), it
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_CODE_CAMERA_PERMISSIONS || requestCode == REQUEST_CODE_GALLERY_PERMISSIONS) && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Permissions are granted, proceed with the action
            //openCamera()
            onPermissionsGranted?.invoke()
        } else {

            if (cameraPermission) {
                goToAppSettings(requireContext())
            } else {
                cameraPermission = true
            }

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
                    requireContext(), it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            // Permissions are already granted, proceed with the action
            //onPermissionsGranted()
            // openCamera()
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

    private fun initObserver() {
        viewModel.resultEmailOtp.observe(viewLifecycleOwner) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        messagesRegister = it.data?.messages.toString()
                        if (it.data!!.isSuccess){
                            Log.d("apiEmail", "Otp is ->${it.data.data.otp}")
                            emailOtp=it.data.data.otp.toString()
                            requireContext().showToast(messagesRegister.toString())
                            viewModel.phoneUnique()
                        }else{
                            requireContext().showToast(messagesRegister.toString())
                        }

                    }

                    Status.ERROR -> {
                        hideProgress()
                        Log.d("apiEmail", "error->${it.message}")

                        if (it.message != null) {
                            requireContext().showToast(it.message.toString())
                        }
                    }
                }
            }
        }
        viewModel.resultPhoneUnique.observe(viewLifecycleOwner) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        messagesRegister = it.data?.messages.toString()
                        Log.d("apiEmail", "inside phoneUnique emailOtp->${emailOtp} currentPhotoPath->$currentPhotoPath")

                        if (it.data!!.isSuccess) {
                            requireContext().showToast(messagesRegister.toString())
                            val signUpRequest= SignUpRequest(binding.etFirstName.text.toString(),binding.etLastName.text.toString(),binding.etPassword.text.toString(),
                                binding.etPhone.text.toString(),"Professional",binding.spGender.selectedItem.toString(),binding.etEmail.text.toString(),
                                binding.countryCode.defaultCountryCode)
                            val action =
                                RegisterFragmentDirections.actionRegisterFragmentToOtpFragment(emailOtp,currentPhotoPath.toString(),signUpRequest,0)
                            findNavController().navigate(action)
                        }else{
                            requireContext().showToast(messagesRegister.toString())
                        }
                    }



                    Status.ERROR -> {
                        hideProgress()
                        if (it.message != null) {
                            requireContext().showToast(it.message.toString())
                        }
                    }
                }
            }
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
        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                            requireContext(), R.color.spinner_color
                        )
                    )

                } else {
                    if (view != null) {
                        (view as TextView).setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                    }
                }
            }
        }
    }


    private fun isValidation(): Boolean {
        when {
            //   profileImage == null -> requireContext().showToast("Please select profile picture")
            checkString(binding.etFirstName) -> requireContext().showToast("Please enter first name")
            checkString(binding.etLastName) -> requireContext().showToast("Please enter last name")
            binding.spGender.selectedItem.toString() == "Select Gender" -> requireContext().showToast(
                "Please select gender"
            )

            checkString(binding.etEmail) -> requireContext().showToast("Please enter email")
            !isValidEmail(checkValidString(binding.etEmail)) -> requireContext().showToast("Please enter a valid email address")
            checkString(binding.etPhone) -> requireContext().showToast("Please enter phone number")
            checkValidString(binding.etPhone).length != 10 -> requireContext().showToast("Please enter a valid 10 digit phone number")
            checkString(binding.etPassword) -> requireContext().showToast("Please enter password")
            checkValidString(binding.etPassword).length < 6 -> requireContext().showToast("Password should be 6 characters or more")
            checkString(binding.etConfirmPassword) -> requireContext().showToast("Please enter confirm password")
            (checkValidString(binding.etPassword) != checkValidString(binding.etConfirmPassword)) -> requireContext().showToast(
                "Password and confirm password mismatched"
            )

            else -> return true
        }
        return false
    }


    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 101
        private const val REQUEST_CODE_GALLERY_PERMISSIONS = 102
    }

}