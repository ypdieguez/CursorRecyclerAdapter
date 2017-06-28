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

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the {@link AppProvider} and applications. Contains
 * definitions for the supported URIs and columns.
 */
final class AppContract {
    /** The string authority */
    static final String AUTHORITY = "io.github.ypdieguez.sample_app.cursorrecycleradapter";
    /** A content:// style URI authority */
    private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Class Item.
     */
    static final class Item implements BaseColumns {
        static final String TABLE = "items";
        static final String NAME = "name";

        /** The content:// style URI */
        static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE);

        /**
         * MIME TYPES
         */
        static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE;
        static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE;
    }
}
