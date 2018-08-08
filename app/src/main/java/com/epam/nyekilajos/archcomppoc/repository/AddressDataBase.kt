package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

@Database(entities = [AddressItem::class], version = 1)
abstract class AddressDataBase : RoomDatabase(), AddressRepository {

    private val addressList: MutableList<AddressItem> = mutableListOf()

    private val subject: Subject<List<AddressItem>> = BehaviorSubject.createDefault(emptyList())

    abstract fun addressDao(): AddressDao

    override fun fetchAddressList(): Observable<List<AddressItem>> = subject.hide().also { loadFromDb() }

    private fun loadFromDb() {
        Schedulers.io().scheduleDirect {
            if (addressList.isEmpty()) {
                addressList += addressDao().getAllAddressItems()
                subject.onNext(addressList)
            }
        }
    }

    override fun storeAddress(addressItem: AddressItem) {
        Schedulers.io().scheduleDirect {
            addressList += addressItem
            subject.onNext(addressList)
            addressDao().insert(addressItem)
        }
    }
}

@Dao
abstract class AddressDao {

    @Query("SELECT * FROM addressItems")
    abstract fun getAllAddressItems(): List<AddressItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(addressItem: AddressItem)

    @Delete
    abstract fun delete(addressItem: AddressItem)
}