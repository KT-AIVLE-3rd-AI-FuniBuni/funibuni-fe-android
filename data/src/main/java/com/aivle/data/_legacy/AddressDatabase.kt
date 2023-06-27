//package com.aivle.data._legacy
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.aivle.data.entity.address.AddressRoomEntity
//
//@Database(entities = [AddressRoomEntity::class], version = 1, exportSchema = false)
//
//abstract class AddressDatabase : RoomDatabase() {
//
//    abstract fun addressDao() : AddressDao
//}