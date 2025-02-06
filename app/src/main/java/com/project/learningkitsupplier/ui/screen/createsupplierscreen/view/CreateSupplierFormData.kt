package com.project.learningkitsupplier.ui.screen.createsupplierscreen.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierCallback
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.AddSuppliedItemLogic.SuppliedItemBaseField
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.Button
import com.tagsamurai.tscomponents.button.SingleSelector
import com.tagsamurai.tscomponents.model.TypeButton
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.textfield.PhoneNumberTextField
import com.tagsamurai.tscomponents.textfield.TextField
import com.tagsamurai.tscomponents.utils.Spacer.heightBox

@Composable
fun CreateSupplierFormData(
    uiState: CreateSupplierUiState,
    onUpdateForm: (CreateSupplierFormData) -> Unit,
    supplierCallback: CreateSupplierCallback
){

    Log.d("TAG", "CreateSupplierFormData: ${uiState.formData}")

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            TextField(
                value = uiState.formData.companyName,
                onValueChange = { result ->
                    onUpdateForm(
                        uiState.formData.copy(companyName = result)
                    )
                },
                placeholder = "Enter company name",
                maxLines = 1,
                maxChar = 30,
                isError = uiState.formError.companyName != null,
                title = "Company name",
                required = true,
                textError = uiState.formError.companyName
            )

            10.heightBox()

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = supplierCallback.addSupplierButton,
                type = TypeButton.OUTLINED,
                text = "Supplied item",
                leadingIcon = R.drawable.ic_add_line_24dp
            )

            10.heightBox()

            uiState.formData.items.forEachIndexed { index, item ->
                SuppliedItemBaseField(
                    isRemovable = uiState.formData.items.size > 1 ,
                    onRemove = { supplierCallback.removeSupplierButton(item) },
                    uiState = uiState,
                    item = item,
                    getCallback = supplierCallback
                )
                10.heightBox()
            }

            10.heightBox()

            Column {

                SingleSelector(
                    onValueChange = { result ->
                        onUpdateForm(
                            uiState.formData.copy(country = result)
                        )
                    },
                    value = uiState.formData.country,
                    items = uiState.formOption.country,
                    placeHolder = "Select country",
                    title = "Country"
                )
            }

            10.heightBox()

            Column {

                SingleSelector(
                    onValueChange = { result ->
                        onUpdateForm(
                            uiState.formData.copy(state = result)
                        )
                    },
                    items = uiState.formOption.state,
                    value = uiState.formData.state,
                    placeHolder = "Select state",
                    title = "State"
                )
            }

            10.heightBox()

            Column {

                SingleSelector(
                    onValueChange = { result ->
                        onUpdateForm(
                            uiState.formData.copy(city = result)
                        )
                    },
                    items = uiState.formOption.city,
                    value = uiState.formData.city,
                    placeHolder = "Select city",
                    title = "City"
                )
            }

            10.heightBox()

            TextField(
                value = uiState.formData.zipCode,
                onValueChange = { result ->
                    if (result.matches(Regex("^\\d*\$"))) {
                        onUpdateForm(
                            uiState.formData.copy(zipCode = result)
                        )
                    }
                },
                placeholder = "Enter ZIP code",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                title = "ZIP Code",
                maxChar = 15,
            )

            10.heightBox()

            TextField(
                value = uiState.formData.companyLocation,
                onValueChange = { result ->
                    onUpdateForm(
                        uiState.formData.copy(companyLocation = result)
                    )
                },
                placeholder = "Enter company address",
                maxLines = 3,
                title = "Company address",
                maxChar = 120,
            )

            10.heightBox()

            val numberCompany = uiState.formData.companyPhoneNumber?.split(" ")

            PhoneNumberTextField(
                dialCode = numberCompany?.first(),
                value = numberCompany?.last(),
                placeholder = "Enter company phone number",
                onValueChange = { countryCode, companyPhoneNumber ->

                    Log.d("PhoneNumberTextField", "Dial Code: $countryCode, Phone Number: $companyPhoneNumber")

                    val cleanedPhoneNumber = if (companyPhoneNumber.startsWith(countryCode)) {
                        companyPhoneNumber.substring(countryCode.length).trim()
                    } else {
                        companyPhoneNumber
                    }

                    val formattedPhoneNumber = "$countryCode $cleanedPhoneNumber"

                    onUpdateForm(
                        uiState.formData.copy(companyPhoneNumber = formattedPhoneNumber)
                    )
                },
                title = "Company Phone Number",
                isError = uiState.formError.companyPhoneNumber != null,
                textError = uiState.formError.companyPhoneNumber,
            )

            10.heightBox()

            TextField(
                value = uiState.formData.picName,
                onValueChange = { result ->
                    onUpdateForm(
                        uiState.formData.copy(picName = result)
                    )
                },
                placeholder = "Enter PIC name",
                title = "PIC Name",
                maxChar = 30
            )

            10.heightBox()

            val numberPic = uiState.formData.picPhoneNumber?.split(" ")

            PhoneNumberTextField(
                dialCode = numberPic?.first(),
                value = numberPic?.last(),
                placeholder = "Enter company phone number",
                onValueChange = { countryCode, picPhoneNumber ->

                    Log.d("PhoneNumberTextField", "Dial Code: $countryCode, Phone Number: $picPhoneNumber")


                    val formattedPhoneNumber = "$countryCode $picPhoneNumber"

                    onUpdateForm(
                        uiState.formData.copy(picPhoneNumber = formattedPhoneNumber)
                    )
                },
                title = "Company Phone Number",
                isError = uiState.formError.picPhoneNumber != null,
                textError = uiState.formError.picPhoneNumber
            )


            10.heightBox()


            TextField(
                value = uiState.formData.picEmail,
                onValueChange = { result ->
                    onUpdateForm(
                        uiState.formData.copy(picEmail = result)
                    )
                },
                placeholder = "Enter PIC email",
                title = "PIC Email",
                isError = uiState.formError.picEmail != null,
                textError = uiState.formError.picEmail
            )
        }
    }
}