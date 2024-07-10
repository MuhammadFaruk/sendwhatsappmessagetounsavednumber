package com.dev114.wpmsgsender

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCountryCode: Spinner
    private lateinit var etPhoneNumber: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    private val countryCodes = arrayOf(
        "AF +93", "AL +355", "DZ +213", "AS +1-684", "AD +376", "AO +244",
        "AG +1-268", "AR +54", "AM +374", "AU +61", "AT +43", "AZ +994", "BS +1-242",
        "BH +973", "BD +880", "BB +1-246", "BY +375", "BE +32", "BZ +501", "BJ +229",
        "BT +975", "BO +591", "BA +387", "BW +267", "BR +55", "BN +673", "BG +359",
        "BF +226", "BI +257", "KH +855", "CM +237", "CA +1", "CV +238", "CF +236",
        "TD +235", "CL +56", "CN +86", "CO +57", "KM +269", "CG +242", "CD +243",
        "CR +506", "HR +385", "CU +53", "CY +357", "CZ +420", "DK +45", "DJ +253",
        "DM +1-767", "DO +1-809", "EC +593", "EG +20", "SV +503", "GQ +240", "ER +291",
        "EE +372", "ET +251", "FJ +679", "FI +358", "FR +33", "GA +241", "GM +220", "GE +995",
        "DE +49", "GH +233", "GR +30", "GD +1-473", "GT +502", "GN +224", "GW +245", "GY +592",
        "HT +509", "HN +504", "HU +36", "IS +354", "IN +91", "ID +62", "IR +98", "IQ +964", "IE +353",
        "IL +972", "IT +39", "JM +1-876", "JP +81", "JO +962", "KZ +7", "KE +254", "KI +686", "KP +850",
        "KR +82", "KW +965", "KG +996", "LA +856", "LV +371", "LB +961", "LS +266", "LR +231",
        "LY +218", "LI +423", "LT +370", "LU +352", "MG +261", "MW +265", "MY +60", "MV +960",
        "ML +223", "MT +356", "MH +692", "MR +222", "MU +230", "MX +52", "FM +691", "MD +373",
        "MC +377", "MN +976", "ME +382", "MA +212", "MZ +258", "MM +95", "NA +264", "NR +674",
        "NP +977", "NL +31", "NZ +64", "NI +505", "NE +227", "NG +234", "NO +47", "OM +968",
        "PK +92", "PW +680", "PA +507", "PG +675", "PY +595", "PE +51", "PH +63", "PL +48",
        "PT +351", "QA +974", "RO +40", "RU +7", "RW +250", "KN +1-869", "LC +1-758", "VC +1-784",
        "WS +685", "SM +378", "ST +239", "SA +966", "SN +221", "RS +381", "SC +248", "SL +232",
        "SG +65", "SK +421", "SI +386", "SB +677", "SO +252", "ZA +27", "ES +34", "LK +94",
        "SD +249", "SR +597", "SZ +268", "SE +46", "CH +41", "SY +963", "TW +886", "TJ +992",
        "TZ +255", "TH +66", "TL +670", "TG +228", "TO +676", "TT +1-868", "TN +216", "TR +90",
        "TM +993", "TV +688", "UG +256", "UA +380", "AE +971", "GB +44", "US +1", "UY +598",
        "UZ +998", "VU +678", "VA +39-06", "VE +58", "VN +84", "YE +967", "ZM +260", "ZW +263"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCountryCode = findViewById(R.id.spinnerCountryCode)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        // Set up spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountryCode.adapter = adapter

        // Set default country code based on device's country
        val defaultCountryCode = getDefaultCountryCode()
        spinnerCountryCode.setSelection(adapter.getPosition(defaultCountryCode))

        btnSend.setOnClickListener {
            val selectedCountry = spinnerCountryCode.selectedItem.toString()
            val countryCode = selectedCountry.split(" ")[1]
            val phoneNumber = etPhoneNumber.text.toString()
            val message = etMessage.text.toString()

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                sendMessage(countryCode, phoneNumber, message)
            } else {
                Toast.makeText(this, "Please enter both phone number and message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDefaultCountryCode(): String {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryISO = tm.networkCountryIso.uppercase(Locale.getDefault())
        return countryCodes.firstOrNull { it.startsWith(countryISO) } ?: "US +1"
    }

    private fun sendMessage(countryCode: String, phoneNumber: String, message: String) {
        try {
            val fullPhoneNumber = countryCode + phoneNumber
            val uri = Uri.parse("https://wa.me/$fullPhoneNumber?text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp is not installed on your device", Toast.LENGTH_SHORT).show()
        }
    }
}