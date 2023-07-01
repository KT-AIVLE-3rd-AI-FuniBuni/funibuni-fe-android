//package com.aivle.data._legacy
//
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.aivle.data._legacy.AddressRoomEntity
//import kotlinx.coroutines.flow.Flow
//
//interface AddressDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(address: AddressRoomEntity)
//
//    @Query("SELECT * FROM Address LIMIT 1")
//    suspend fun getAddressOnlyOne(): Flow<AddressRoomEntity>
//
//    @Query("DELETE FROM Address")
//    suspend fun deleteAll()
//}