@file:Suppress("MaxLineLength", "MagicNumber")

package com.epam.nyekilajos.archcomppoc.repository

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.run {
            execSQL("ALTER TABLE addressItems ADD COLUMN protocol TEXT NOT NULL DEFAULT '${Protocol.HTTP.name}'")
            execSQL("CREATE TABLE updated_addressItems (protocol TEXT NOT NULL, ip_address TEXT NOT NULL, port INTEGER NOT NULL, PRIMARY KEY(protocol, ip_address, port)) ")
            execSQL("INSERT INTO updated_addressItems (protocol, ip_address, port) SELECT protocol, ip_address, port FROM addressItems")
            execSQL("DROP TABLE addressItems")
            execSQL("ALTER TABLE updated_addressItems RENAME TO addressItems")
        }
    }
}

val MIGRATION_2_3: Migration = object : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.run {
            execSQL("ALTER TABLE addressItems ADD COLUMN name TEXT")
            execSQL("UPDATE addressItems SET name =  (protocol || ' ' || ip_address || ':' || port)")

            execSQL("CREATE TABLE updated_addressItems (name TEXT NOT NULL, protocol TEXT NOT NULL, ip_address TEXT NOT NULL, port INTEGER NOT NULL, PRIMARY KEY(name)) ")
            execSQL("INSERT INTO updated_addressItems (name, protocol, ip_address, port) SELECT name, protocol, ip_address, port FROM addressItems")
            execSQL("DROP TABLE addressItems")
            execSQL("ALTER TABLE updated_addressItems RENAME TO addressItems")
        }
    }
}

val MIGRATION_3_4: Migration = object : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.run {
            execSQL("CREATE TABLE IF NOT EXISTS selectedAddressItems (id INTEGER NOT NULL, address_id TEXT NOT NULL, PRIMARY KEY(id), FOREIGN KEY(address_id) REFERENCES addressItems(name) ON UPDATE NO ACTION ON DELETE CASCADE )")
            execSQL("CREATE  INDEX index_selectedAddressItems_address_id ON selectedAddressItems (address_id)")
        }
    }
}
