package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.BottomProductCategoryBinding
import com.app.gentlemanspa.databinding.FragmentAddProductBinding
import com.app.gentlemanspa.databinding.ImagePickerBottomBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.adapter.ProductCategoryAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.adapter.ProductPhotoAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddPhotoRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.viewModel.AddProductViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.CommonFunctions.getTextRequestBodyParams
import com.app.gentlemanspa.utils.CommonFunctions.prepareFilePart
import com.app.gentlemanspa.utils.DecimalDigitsInputFilter
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MultipartBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddProductFragment : Fragment(), View.OnClickListener {

    private var addType: Int=0
    private var mainCategoryId: Int =0
    private var messagesProduct: String=""
    private var productsPhoto: ArrayList<AddPhotoRequest> = ArrayList()
    private var profileImage: File?= null
    private lateinit var binding : FragmentAddProductBinding
    private var productCategoriesList: ArrayList<ProductCategoriesItem> = ArrayList()
    private var currentPhotoPath: String?= null
    private var onPermissionsGranted: (() -> Unit)? = null
    private val args : AddProductFragmentArgs by navArgs()
    private val viewModel: AddProductViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addType =args.AddType
        initObserver()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)

        if (addType ==0) {
            binding.tvTitle.text ="Add Product"
            binding.btnAddProduct.text ="Add Product"
        }else {
            val data = args.ProductListItem
            binding.tvTitle.text ="Update Product"
            binding.btnAddProduct.text ="Update Product"
            binding.etProductName.setText(data?.name)
            binding.etBasePrice.setText(data?.basePrice.toString())
            binding.etListingPrice.setText(data?.listingPrice.toString())
            binding.etDescription.setText(data?.description)

        }
         productsPhoto.add(AddPhotoRequest(profileImage,R.drawable.plus))
        setProductPhotoAdapter()
        binding.onClick = this
        viewModel.getProductCategories()

        binding.etBasePrice.filters = arrayOf(DecimalDigitsInputFilter(2))
        binding.etListingPrice.filters = arrayOf(DecimalDigitsInputFilter(2))
    }

    private fun initObserver() {
        viewModel.resultProductCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //  MyApplication.showProgress(requireContext())


                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        productCategoriesList.clear()

                        it.data?.data?.let { it1 -> productCategoriesList.addAll(it1) }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }

        }

            viewModel.resultAddProduct.observe(this) {
                it?.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            showProgress(requireActivity())

                        }

                        Status.SUCCESS -> {
                           // MyApplication.hideProgress()
                            val productId = getTextRequestBodyParams(it.data?.data?.productId.toString())
                            val listOfImages = ArrayList<MultipartBody.Part>()
                            for (i in 0 until productsPhoto.size) {
                                listOfImages.add(
                                    prepareFilePart(
                                        "Images",
                                        productsPhoto[i].image!!
                                    )
                                )
                            }

                            messagesProduct = it.data?.messages.toString()


                            viewModel.productId.set(productId)
                            viewModel.productImages.set(listOfImages)

                            viewModel.uploadProductImage()

                        }

                        Status.ERROR -> {
                            requireContext().showToast(it.message.toString())
                            hideProgress()
                        }
                    }
                }
            }


            viewModel.resultUploadProductImage.observe(this) {
                it?.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                          //  MyApplication.showProgress(requireContext())



                        }

                        Status.SUCCESS -> {
                            hideProgress()
                            requireContext().showToast(messagesProduct)
                            findNavController().popBackStack()
                        }

                        Status.ERROR -> {
                            requireContext().showToast(it.message.toString())
                            hideProgress()
                        }
                    }
                }
            }

            viewModel.resultUpdateProduct.observe(this) {
                it?.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            showProgress(requireActivity())

                        }

                        Status.SUCCESS -> {
                            hideProgress()
                           /* val productId = getTextRequestBodyParams(it.data?.data?.productId.toString())
                            val listOfImages = ArrayList<MultipartBody.Part>()
                            for (i in 0 until productsPhoto.size) {
                                listOfImages.add(
                                    prepareFilePart(
                                        "Images",
                                        productsPhoto[i]
                                    )
                                )
                            }

                            messagesProduct = it.data?.messages.toString()


                            viewModel.productId.set(productId)
                            viewModel.productImages.set(listOfImages)

                            viewModel.uploadProductImage()*/
                            requireContext().showToast(it.data?.messages.toString())
                            findNavController().popBackStack()


                        }

                        Status.ERROR -> {
                            requireContext().showToast(it.message.toString())
                            hideProgress()
                        }
                    }
                }
            }

    }




    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the camera result
            // val imageUri = result.data?.data
            profileImage = File(currentPhotoPath)
            compressImageFile(profileImage!!)
            productsPhoto.add(AddPhotoRequest(profileImage!!,0))
            setProductPhotoAdapter()

           // binding.ivProfile.setImageURI(Uri.fromFile(profileImage))
            // Use the imageUri
        }
    }

    private fun setProductPhotoAdapter() {
        val productPhotoAdapter = ProductPhotoAdapter(productsPhoto)
        binding.rvProductsPhoto.adapter = productPhotoAdapter

        productPhotoAdapter.setOnClickUploadProduct(object : ProductPhotoAdapter.UploadProductCallback{
            override fun rootUploadProduct() {
                setImagePickerBottomSheet()
            }

        })
    }

    private var  galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the gallery result
            val imageUri = result.data?.data
            profileImage = uriToFile(imageUri!!)
            compressImageFile(profileImage!!)
            productsPhoto.add(AddPhotoRequest(profileImage!!,0))
            setProductPhotoAdapter()
            // Use the imageUri
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val name = cursor.getString(index)
            val file = File(requireContext().cacheDir, name)
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            cursor.close()
            inputStream?.close()
            outputStream.close()
            file
        } else {
            null
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

    private fun compressImageFile(imageFile: File) {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80 is the compression quality
        outputStream.close()
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

    override fun onClick(v: View?) {
        when(v) {
            binding.tvProductCategory -> {
                setProductCategoryBottom()
            }

            binding.ivArrowBack->{
                findNavController().popBackStack()
            }

            binding.btnAddProduct -> {

                if (isValidation()) {
                    val createdBy = AppPrefs(requireContext()).getString("CREATED_BY")

                    if (addType ==0){
                        viewModel.listingPrice.set(binding.etListingPrice.text.toString().toInt())
                        viewModel.createdBy.set(createdBy)
                        viewModel.name.set(binding.etProductName.text.toString())
                        viewModel.description.set(binding.etDescription.text.toString())
                        viewModel.subCategoryId.set(0)
                        viewModel.spaDetailId.set(21)
                        viewModel.mainCategoryId.set(mainCategoryId)
                        viewModel.basePrice.set(binding.etBasePrice.text.toString().toInt())
                        viewModel.stock.set(binding.etInstock.text.toString().toInt())
                        viewModel.addProduct()
                    }else{
                        viewModel.productUpdateId.set(args.ProductListItem?.productId)
                        viewModel.listingPrice.set(binding.etListingPrice.text.toString().toInt())
                        viewModel.createdBy.set(createdBy)
                        viewModel.name.set(binding.etProductName.text.toString())
                        viewModel.description.set(binding.etDescription.text.toString())
                        viewModel.subCategoryId.set(0)
                        viewModel.spaDetailId.set(21)
                        viewModel.mainCategoryId.set(mainCategoryId)
                        viewModel.basePrice.set(binding.etBasePrice.text.toString().toInt())
                        viewModel.stock.set(binding.etInstock.text.toString().toInt())
                        viewModel.updateProduct()
                    }




                }

            }
        }

    }



    private fun setProductCategoryBottom() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        val bottomSheetLayout = BottomProductCategoryBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetLayout.root)

        val productCategoryAdapter = ProductCategoryAdapter(productCategoriesList)
        bottomSheetLayout.rvProductCategory.adapter = productCategoryAdapter

        productCategoryAdapter.setOnClickUploadProduct(object : ProductCategoryAdapter.ProductCategoryCallback{
            override fun rootProductCategory(item: ProductCategoriesItem) {
                binding.tvProductCategory.text= item.categoryName
                mainCategoryId = item.mainCategoryId
                bottomSheet.dismiss()
            }

        })



        bottomSheet.behavior.maxWidth = Resources.getSystem().displayMetrics.widthPixels
        bottomSheet.setCancelable(true)
        bottomSheet.show()

        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetLayout.ivCross.setOnClickListener {
            bottomSheet.dismiss()
        }


    }


    private fun isValidation(): Boolean {
        when {


            (addType == 0 && productsPhoto.size == 0) -> {
                    requireContext().showToast("Please upload atleast one image")
                }


            binding.etProductName.text.toString().trim().isEmpty() -> {
                requireContext().showToast("Enter product name")
            }

            binding.tvProductCategory.text.toString().isEmpty()  -> {
                requireContext().showToast("Please select product category")
            }

            binding.etBasePrice.text.toString().trim().isEmpty() -> {
                requireContext().showToast("Enter base price")
            }

            binding.etListingPrice.text.toString().trim().isEmpty() -> {
                requireContext().showToast("Enter listing price")
            }

            binding.etListingPrice.text.toString().toInt() > binding.etBasePrice.text.toString()
                .toInt() -> {
                requireContext().showToast(
                    "Please do not enter Base Price more than Listing Price"
                )
            }

            binding.etInstock.text.toString().trim().isEmpty() -> {
                requireContext().showToast("Enter in stock")
            }

            binding.etDescription.text.toString().isEmpty() -> {
                requireContext().showToast("Enter description")
            }

            else -> return true
        }
        return false
    }


}