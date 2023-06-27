package com.aivle.data.datastore

import android.content.Context
import com.aivle.data.AddressProto
import com.aivle.domain.model.address.Address
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressDatastoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AddressDatastore {

    override val addressFlow: Flow<AddressProto> = context.addressDataStore.data

    override suspend fun setAddress(address: Address) {
        context.addressDataStore.updateData { addressProto ->
            addressProto.toBuilder()
                .setAddressId(address.addressId.toLong())
                .setPostalCode(address.postalCode)
                .setRoadAddress(address.roadAddress)
                .setLandAddress(address.lendAddress)
                .setCity(address.city)
                .setDistrict(address.district)
                .setDong(address.dong)
                .setDisposalLocation(address.disposalLocation)
                .build()
        }
    }
}