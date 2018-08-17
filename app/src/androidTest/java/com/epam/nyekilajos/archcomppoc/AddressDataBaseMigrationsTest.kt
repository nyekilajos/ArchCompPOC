package com.epam.nyekilajos.archcomppoc

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.epam.nyekilajos.archcomppoc.repository.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressDataBaseMigrationsTest {

    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AddressDataBase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    @Test
    fun migrate1To2() {
        val oldVersion: SupportSQLiteDatabase = helper.createDatabase(TEST_DB, 1)

        oldVersion.run {
            execSQL("INSERT INTO addressItems (ip_address, port) VALUES ('$TEST_IP', $TEST_PORT)")
            close()
        }

        val newVersion = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        newVersion.query("SELECT * FROM addressItems", null).run {
            moveToFirst()
            Assert.assertTrue(getString(getColumnIndex("protocol")) == Protocol.HTTP.name)
            Assert.assertTrue(getString(getColumnIndex("ip_address")) == TEST_IP)
            Assert.assertTrue(getInt(getColumnIndex("port")) == TEST_PORT)
            close()
        }
    }

    @Test
    fun migrate2To3() {
        val oldVersion: SupportSQLiteDatabase = helper.createDatabase(TEST_DB, 2)

        oldVersion.run {
            execSQL("INSERT INTO addressItems (protocol, ip_address, port) VALUES ('${Protocol.HTTP.name}', '$TEST_IP', $TEST_PORT)")
            execSQL("INSERT INTO addressItems (protocol, ip_address, port) VALUES ('${Protocol.HTTPS.name}', '$TEST_IP', $TEST_PORT)")
            close()
        }

        val newVersion = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)

        newVersion.query("SELECT * FROM addressItems", null).run {
            moveToFirst()
            Assert.assertTrue(getString(getColumnIndex("name")) == "${Protocol.HTTP.name} $TEST_IP:$TEST_PORT")
            Assert.assertTrue(getString(getColumnIndex("protocol")) == Protocol.HTTP.name)
            Assert.assertTrue(getString(getColumnIndex("ip_address")) == TEST_IP)
            Assert.assertTrue(getInt(getColumnIndex("port")) == TEST_PORT)

            moveToNext()
            Assert.assertTrue(getString(getColumnIndex("name")) == "${Protocol.HTTPS.name} $TEST_IP:$TEST_PORT")
            Assert.assertTrue(getString(getColumnIndex("protocol")) == Protocol.HTTPS.name)
            Assert.assertTrue(getString(getColumnIndex("ip_address")) == TEST_IP)
            Assert.assertTrue(getInt(getColumnIndex("port")) == TEST_PORT)
            close()
        }
    }

    @Test
    fun migrate3To4() {
        val oldVersion: SupportSQLiteDatabase = helper.createDatabase(TEST_DB, 3)

        oldVersion.run {
            execSQL("INSERT INTO addressItems (name, protocol, ip_address, port) VALUES ('$TEST_NAME', '${Protocol.HTTP.name}', '$TEST_IP', $TEST_PORT)")
            close()
        }

        val newVersion = helper.runMigrationsAndValidate(TEST_DB, 4, true, MIGRATION_3_4)

        newVersion.query("SELECT * FROM addressItems", null).run {
            moveToFirst()
            Assert.assertTrue(getString(getColumnIndex("name")) == TEST_NAME)
            Assert.assertTrue(getString(getColumnIndex("protocol")) == Protocol.HTTP.name)
            Assert.assertTrue(getString(getColumnIndex("ip_address")) == TEST_IP)
            Assert.assertTrue(getInt(getColumnIndex("port")) == TEST_PORT)
            close()
        }
    }
}

private const val TEST_DB = "testDb"
private const val TEST_IP = "test_ip"
private const val TEST_PORT = 1234
private const val TEST_NAME = "my fancy name"
