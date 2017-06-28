/*
 * Copyright (C) 2017 Yordan P. Dieguez <ypdieguez@tuta.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ypdieguez.sample_app.cursorrecycleradapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.ypdieguez.sample_app.cursorrecycleradapter.AppContract.Item;

class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sample.db";

    // SQL Entries
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Item.TABLE + " (" +
                    Item._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Item.NAME + " TEXT NOT NULL" +
                    ")";

    DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(
                "INSERT INTO " + Item.TABLE + " (" + Item.NAME + ") VALUES " +
                        "('Item 1'), ('Item 2'), ('Item 3'), ('Item 4'), ('Item 5'), ('Item 6')," +
                        " ('Item 7'), ('Item 8'), ('Item 9')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE);
        onCreate(db);
    }
}
