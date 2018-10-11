package com.epam.nyekilajos.archcomppoc.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressDataBaseDaoTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var addressDataBase: AddressDataBase
    private lateinit var addressDao: AddressDao
    private lateinit var widgetPropertiesDao: WidgetPropertiesDao

    @Before
    fun setUp() {
        addressDataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AddressDataBase::class.java).build().also {
            addressDao = it.addressDao()
            widgetPropertiesDao = it.widgetPropertiesDao()
        }
    }

    @After
    fun tearDown() {
        addressDataBase.close()
    }

    @Test
    fun testDatabaseIsEmptyInitially() {
        addressDao.getAllAddressItems().test().assertValue(listOf())
        assertNull(addressDao.getAddressByName(TEST_ADDRESS_ITEM1.name))
    }

    @Test
    fun testInsertAddressItemSuccessfully() {
        addressDao.insert(TEST_ADDRESS_ITEM1)
        addressDao.insert(TEST_ADDRESS_ITEM2)

        addressDao.getAllAddressItems().test().assertValue(listOf(TEST_ADDRESS_ITEM1, TEST_ADDRESS_ITEM2))
        assertThat(addressDao.getAddressByName(TEST_ADDRESS_ITEM1.name), equalTo(TEST_ADDRESS_ITEM1))
        assertThat(addressDao.getAddressByName(TEST_ADDRESS_ITEM2.name), equalTo(TEST_ADDRESS_ITEM2))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testInsertAddressItemFailsByPrimaryKeyConstrains() {
        addressDao.insert(TEST_ADDRESS_ITEM1)
        addressDao.insert(TEST_ADDRESS_ITEM1)
    }

    @Test
    fun testDeleteAddressItemSuccessfully() {
        addressDao.insert(TEST_ADDRESS_ITEM1)
        addressDao.insert(TEST_ADDRESS_ITEM2)

        addressDao.delete(TEST_ADDRESS_ITEM1)

        addressDao.getAllAddressItems().test().assertValues(listOf(TEST_ADDRESS_ITEM2))

    }

    @Test
    fun testSelectNonExistentSelectedAddressItem() {
        assertNull(widgetPropertiesDao.getSelectedAddressItemForId(TEST_APP_WIDGET_ID1))
        assertNull(widgetPropertiesDao.getAddressNameForId(TEST_APP_WIDGET_ID1))
    }

    @Test
    fun testInsertSelectedAddressItemSuccessfully() {
        addressDao.insert(TEST_ADDRESS_ITEM1)

        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM1)

        assertThat(widgetPropertiesDao.getSelectedAddressItemForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM1))
        assertThat(widgetPropertiesDao.getAddressNameForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM1.addressId))
    }

    @Test
    fun testInsertSelectedAddressItemReplacesOldOne() {
        addressDao.insert(TEST_ADDRESS_ITEM1)
        addressDao.insert(TEST_ADDRESS_ITEM2)
        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM1)

        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM1_FOR_2)

        assertThat(widgetPropertiesDao.getSelectedAddressItemForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM1_FOR_2))
        assertThat(widgetPropertiesDao.getAddressNameForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM2.addressId))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testForeignKeyViolationForSelectedAddressItem() {
        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM1)
    }

    @Test
    fun testDeleteSelectedAddressItem() {
        addressDao.insert(TEST_ADDRESS_ITEM1)
        addressDao.insert(TEST_ADDRESS_ITEM2)
        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM1)
        widgetPropertiesDao.insert(TEST_SELECTED_ADDRESS_ITEM2)

        widgetPropertiesDao.delete(TEST_SELECTED_ADDRESS_ITEM2)

        assertThat(widgetPropertiesDao.getSelectedAddressItemForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM1))
        assertThat(widgetPropertiesDao.getAddressNameForId(TEST_APP_WIDGET_ID1), equalTo(TEST_SELECTED_ADDRESS_ITEM1.addressId))

        assertNull(widgetPropertiesDao.getSelectedAddressItemForId(TEST_APP_WIDGET_ID2))
        assertNull(widgetPropertiesDao.getAddressNameForId(TEST_APP_WIDGET_ID2))
    }
}

private const val TEST_APP_WIDGET_ID1 = 100
private const val TEST_APP_WIDGET_ID2 = 101

private val TEST_ADDRESS_ITEM1 = AddressItem("Test item 01", Protocol.HTTP, "www.google.com", 80)
private val TEST_ADDRESS_ITEM2 = AddressItem("Test item 02", Protocol.HTTPS, "www.google.com", 8080)
private val TEST_SELECTED_ADDRESS_ITEM1 = SelectedAddressItem(TEST_APP_WIDGET_ID1, TEST_ADDRESS_ITEM1.name)
private val TEST_SELECTED_ADDRESS_ITEM1_FOR_2 = SelectedAddressItem(TEST_APP_WIDGET_ID1, TEST_ADDRESS_ITEM2.name)
private val TEST_SELECTED_ADDRESS_ITEM2 = SelectedAddressItem(TEST_APP_WIDGET_ID2, TEST_ADDRESS_ITEM2.name)
