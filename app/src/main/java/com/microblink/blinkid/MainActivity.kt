package com.microblink.blinkid

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.imageoptions.FaceImageOptions
import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult
import com.microblink.uisettings.ActivityRunner
import com.microblink.uisettings.BlinkIdUISettings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListAdapter
    private lateinit var list: MutableList<String>
    private var mRecognizer: BlinkIdCombinedRecognizer? = null
    private var mRecognizerBundle: RecognizerBundle? = null


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)
        // setup views, as you would normally do in onCreate callback

        // create BlinkIdCombinedRecognizer
        mRecognizer = BlinkIdCombinedRecognizer()
        if (mRecognizer is FullDocumentImageOptions) {
            val options = mRecognizer as FullDocumentImageOptions
            options.setReturnFullDocumentImage(true)
        }
        if (mRecognizer is FaceImageOptions) {
            val options = mRecognizer as FaceImageOptions
            options.setReturnFaceImage(true)
        }
        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = RecognizerBundle(mRecognizer)


        setupListAdapter()

    }

    private fun setupListAdapter() {
        list = ArrayList<String>()
        // Create adapter which will be used to populate ListView.
        adapter = ListAdapter(list)
        list_view.adapter = adapter
    }

    // method within MyActivity from previous step
    fun startScanning(v: View) {
        // Settings for BlinkIdActivity
        val settings = BlinkIdUISettings(mRecognizerBundle)

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, 1, settings)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle
                mRecognizerBundle!!.loadFromIntent(data)

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                val result = mRecognizer!!.result
                if (result.resultState == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                    profile_image.setImageBitmap(result.faceImage?.convertToBitmap())
                    list.clear()
                    list.add("ID Number :\n" + result.personalIdNumber)
                    list.add("Full name :\n"+ result.fullName)
                    list.add("Date of Birth :\n" + result.dateOfBirth.originalDateString + " age: " + result.age.toString())
                    list.add("Nationality :\n" + result.mrzResult.nationality)
                    list.add("Nationality name :\n" + result.mrzResult.nationalityName)
                    list.add("Issuing Date :\n" + result.dateOfIssue.originalDateString)
                    list.add("Expiry Date :\n" + result.dateOfExpiry.originalDateString)
                    list.add("Sex :\n" + result.sex)
                    list.add("Occupation :\n" + result.profession)
                    list.add("Employer :\n" + result.employer)
                    list.add("Issuing Place :\n" + result.issuingAuthority)
                    list.add("Document Number :\n" + result.mrzResult.documentNumber)
                    list.add("Document code :\n"+ result.mrzResult.documentCode)
                    list.add("Document type name :\n"+ result.mrzResult.documentType.name)
                    list.add("Document Additional Number : " + result.documentAdditionalNumber)
                    list.add("Document Optional Additional Number : " + result.documentOptionalAdditionalNumber)
                    list.add("result.driverLicenseDetailedInfo.vehicleClassesInfo : "
                            + result.driverLicenseDetailedInfo.vehicleClassesInfo.size)
                    adapter.addList(list)

                    Toast.makeText(this, result.fullName, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}