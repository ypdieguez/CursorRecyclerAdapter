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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.github.ypdieguez.sample_app.cursorrecycleradapter.AppContract.Item;

import static io.github.ypdieguez.sample_app.cursorrecycleradapter.AppContract.AUTHORITY;

/**
 * Content provider. The contract between this provider and applications
 * is defined in {@link AppContract}.
 */
public class AppProvider extends ContentProvider {

    private static final int ITEMS = 1;
    private static final int ITEMS_ID = 2;

    private static final String ACTION_UPDATE = "UPDATE";
    private static final String ACTION_DELETE = "DELETE";

    private DbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, Item.TABLE, ITEMS);
        sUriMatcher.addURI(AUTHORITY, Item.TABLE + "/#", ITEMS_ID);
    }

    private ContentResolver mResolver;

    public AppProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context == null) {
            return false;
        }

        mResolver = context.getContentResolver();
        if (mResolver == null) {
            return false;
        }

        mDbHelper = new DbHelper(context);

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                break;
            case ITEMS_ID:
                selection = appendIdToSelection(uri.getLastPathSegment(), selection);
                break;
            default:
                return null;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Item.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(mResolver, Item.CONTENT_URI);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                break;
            default:
                return null;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(Item.TABLE, null, values);
        if (newRowId != -1) {
            Uri newUri = ContentUris.withAppendedId(Item.CONTENT_URI, newRowId);
            mResolver.notifyChange(newUri, null);
            return newUri;
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return doAction(uri, values, selection, selectionArgs, ACTION_UPDATE);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return doAction(uri, null, selection, selectionArgs, ACTION_DELETE);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                return Item.DIR_MIME_TYPE;
            case ITEMS_ID:
                return Item.ITEM_MIME_TYPE;
            default:
                return null;
        }
    }

    private String appendIdToSelection(String id, String selection) {
        return Item._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }

    private int doAction(Uri uri, ContentValues values, String selection,
                         String[] selectionArgs, String action) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                break;
            case ITEMS_ID:
                selection = appendIdToSelection(uri.getLastPathSegment(), selection);
                break;
            default:
                return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int count;
        switch (action) {
            case ACTION_UPDATE:
                count = db.update(Item.TABLE, values, selection, selectionArgs);
                break;
            case ACTION_DELETE:
                count = db.delete(Item.TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Action not supported: " + action);
        }

        mResolver.notifyChange(uri, null);
        return count;
    }
}
