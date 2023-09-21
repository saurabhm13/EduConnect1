package com.example.educonnect.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.educonnect.R
import com.example.educonnect.databinding.ActivityEditProfileBinding
import com.example.educonnect.ui.viewmodel.EditProfileViewModel
import com.example.educonnect.util.Constants.Companion.EMAIL
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding

    val viewModel: EditProfileViewModel by viewModels()

    private lateinit var name: String
    private lateinit var image: String
    private lateinit var email: String

    private var selectedImageUri: Uri? = null

    private var croppedImageUri: Uri? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            croppedImageUri = result.uriContent!!
            val croppedImageFilePath = result.getUriFilePath(this) // optional usage
            Glide.with(this)
                .load(croppedImageUri)
                .into(binding.profileImage)
            // Process the cropped image URI as needed.
        } else {
            // An error occurred.
            val exception = result.error
            // Handle the error.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setIncomingData()

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.profileImage.setOnClickListener {
            startCrop()
        }

        binding.imagePicker.setOnClickListener {
            startCrop()
        }

        binding.save.setOnClickListener {
            name = binding.editName.editText?.text.toString().trim()
            email = binding.editEmail.editText?.text.toString().trim()

            if (croppedImageUri == null) {
                viewModel.saveUserData(name, email)
            }else {
                croppedImageUri?.let { it1 -> viewModel.saveUserData(name, it1, email) }
            }

            finish()
        }

    }

    private fun setIncomingData() {
        name = intent.getStringExtra(NAME).toString()
        image = intent.getStringExtra(IMAGE).toString()
        email= intent.getStringExtra(EMAIL).toString()

        binding.editName.editText?.setText(name)
        binding.editEmail.editText?.setText(email)

        if (image.isEmpty() || image == "null") {
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImage)
        }else {
            Glide.with(this)
                .load(image)
                .into(binding.profileImage)
        }
    }

//    private fun observeProfileImage() {
//        viewModel.selectedImageUri.observe(this) {
//            if (it != null) {
//                binding.profileImage.setImageURI(it)
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 123 && resultCode == RESULT_OK) {
//            selectedImageUri = data?.data
//            viewModel.onImagePicked(selectedImageUri)
//        }
//    }

    private fun startCrop() {
        cropImage.launch(
            CropImageContractOptions(
                selectedImageUri, CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON_TOUCH,
                    cropShape = CropImageView.CropShape.RECTANGLE,
                    fixAspectRatio = true,
                    aspectRatioY = 1,
                    aspectRatioX = 1,
                    initialCropWindowPaddingRatio = 0f,
                    cropMenuCropButtonTitle = getString(R.string.send),
                    outputRequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_INSIDE,
                )
            )
        )
    }
}