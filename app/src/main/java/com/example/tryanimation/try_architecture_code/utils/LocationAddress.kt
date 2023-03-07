package com.example.tryanimation.try_architecture_code.utils

import android.content.Context
import android.location.Geocoder
import java.util.*

class LocationAddress {

    private val tag = "LocationAddress"

    fun getAddressFromLocation(
        latitude: Double,
        longitude: Double, context: Context,
    ): String? {
        val geoCoder = Geocoder(context, Locale.getDefault())
        var result: String? = ""

        try {
            val addressList = geoCoder.getFromLocation(latitude, longitude, 1) // from LatLng
//            val addressList = geoCoder.getFromLocationName("Crocodic", 5) // from Location Name

            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]

                result = "Alamat :"
//                for (i in 0 until address.maxAddressLineIndex) {
//                    val getLine = " ${address.getAddressLine(i)},"
//                    result += getLine
//                }

                val countryNameAddress = "\nCountry Name: ${address.countryName}" // Negara
                result += countryNameAddress

                val localityAddress = "\nLocality: ${address.locality}" // Kecamatan
                result += localityAddress

                val subLocality = "\nSub-Locality: ${address.subLocality}" // Kelurahan
                result += subLocality

                val postalCodeAddress = "\nPostal Code: ${address.postalCode}" // Kode Post
                result += postalCodeAddress

                val adminArea = "\nAdmin Area: ${address.adminArea}" // Provinsi
                result += adminArea

                val subAdminArea = "\nSub-Admin Area: ${address.subAdminArea}" // Kabupaten /Kota
                result += subAdminArea

                val thoroughfare = "\nThoroughfare: ${address.thoroughfare}" //Jalan
                result += thoroughfare

                val subThoroughfare = "\nSub-Thoroughfare: ${address.subThoroughfare}" //Blok
                result += subThoroughfare

                val featureName = "\nFeature Name: ${address.featureName}" //Blok
                result += featureName

                val phone = "\nPhone: ${address.phone}"
                result += phone

                val premises = "\nPremises: ${address.premises}"
                result += premises

                return result
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error: $e"
        }

    }
}