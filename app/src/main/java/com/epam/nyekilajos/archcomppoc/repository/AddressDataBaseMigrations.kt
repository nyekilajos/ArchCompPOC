@file:Suppress("MaxLineLength")

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
