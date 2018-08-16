package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

@Database(entities = [AddressItem::class], version = 3)
@TypeConverters(ProtocolConverter::class)
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

    override fun storeAddress(addressItem: AddressItem): Completable {
        return Completable.fromCallable {
            addressList += addressItem
            subject.onNext(addressList)
            addressDao().insert(addressItem)
        }.subscribeOn(Schedulers.io())
    }

    override fun removeAddress(addressItem: AddressItem): Completable {
        return Completable.fromCallable {
            addressList -= addressItem
            subject.onNext(addressList)
            addressDao().delete(addressItem)
        }.subscribeOn(Schedulers.io())
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

object ProtocolConverter {

    @JvmStatic
    @TypeConverter
    fun toProtocol(protocolName: String): Protocol? = Protocol.valueOf(protocolName)

    @JvmStatic
    @TypeConverter
    fun toString(protocol: Protocol): String? = protocol.name
}
