package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.aivle.data.AddressProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AddressProtoSerializer : Serializer<AddressProto> {
    override val defaultValue: AddressProto = AddressProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AddressProto {
        try {
            return AddressProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AddressProto, output: OutputStream) = t.writeTo(output)
}

val Context.addressDataStore: DataStore<AddressProto> by dataStore(
    fileName = "address.pb",
    serializer = AddressProtoSerializer
)