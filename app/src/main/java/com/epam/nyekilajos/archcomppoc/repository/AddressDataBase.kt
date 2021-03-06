package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

@Database(entities = [AddressItem::class, SelectedAddressItem::class], version = 4)
@TypeConverters(ProtocolConverter::class)
abstract class AddressDataBase : RoomDatabase(), AddressRepository, WidgetProperties {

    abstract fun addressDao(): AddressDao

    abstract fun widgetPropertiesDao(): WidgetPropertiesDao

    override fun fetchAddressList(): Flowable<List<AddressItem>> {
        return addressDao().getAllAddressItems()

    }

    override fun storeAddress(addressItem: AddressItem): Completable {
        return Completable.fromCallable { addressDao().insert(addressItem) }
    }

    override fun removeAddress(addressItem: AddressItem): Completable {
        return Completable.fromCallable { addressDao().delete(addressItem) }
    }

    override fun getWidgetSettings(appWidgetId: Int): Maybe<AddressItem> {
        return Maybe.fromCallable {
            addressDao().getAddressByName(
                    widgetPropertiesDao().getAddressNameForId(appWidgetId)
                            ?: throw createExceptionForMissingWidgetProperty(appWidgetId)
            )
        }
    }

    override fun insert(selectedAddressItem: SelectedAddressItem): Completable {
        return Completable.fromCallable { widgetPropertiesDao().insert(selectedAddressItem) }
    }

    override fun delete(appWidgetId: Int): Completable {
        return Completable.fromCallable {
            widgetPropertiesDao().run {
                delete(getSelectedAddressItemForId(appWidgetId)
                        ?: throw createExceptionForMissingWidgetProperty(appWidgetId))
            }
        }
    }

    private fun createExceptionForMissingWidgetProperty(appWidgetId: Int) = NoSuchElementException("No such element exists for id: $appWidgetId ")
}

@Dao
abstract class AddressDao {

    @Query("SELECT * FROM addressItems")
    abstract fun getAllAddressItems(): Flowable<List<AddressItem>>

    @Query("SELECT * FROM addressItems WHERE name = :name")
    abstract fun getAddressByName(name: String): AddressItem?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract fun insert(addressItem: AddressItem)

    @Delete
    abstract fun delete(addressItem: AddressItem)
}

@Dao
abstract class WidgetPropertiesDao {

    @Query("SELECT address_id FROM selectedAddressItems WHERE id = :appWidgetId")
    abstract fun getAddressNameForId(appWidgetId: Int): String?

    @Query("SELECT * FROM selectedAddressItems WHERE id = :appWidgetId")
    abstract fun getSelectedAddressItemForId(appWidgetId: Int): SelectedAddressItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(selectedAddressItem: SelectedAddressItem)

    @Delete
    abstract fun delete(selectedAddressItem: SelectedAddressItem)
}

object ProtocolConverter {

    @JvmStatic
    @TypeConverter
    fun toProtocol(protocolName: String): Protocol? = Protocol.valueOf(protocolName)

    @JvmStatic
    @TypeConverter
    fun toString(protocol: Protocol): String? = protocol.name
}
