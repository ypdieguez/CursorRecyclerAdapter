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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import io.github.ypdieguez.sample_app.cursorrecycleradapter.AppContract.Item;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, OnItemInteractionListener, View.OnTouchListener {

    private static final int LOADER_ID = 385;
    private static final String KEY_QUERY = "QUERY";
    private static final String[] PROJECTION = new String[]{Item._ID, Item.NAME};
    private static final String SELECTION = Item.NAME + " LIKE ?";

    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnTouchListener(this);
        mRecyclerView.setOnTouchListener(this);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Bundle bundle = new Bundle();

            @Override
            public boolean onQueryTextSubmit(String query) {
                query(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query(newText);
                return true;
            }

            private void query(String text) {
                bundle.putString(KEY_QUERY, text);
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mFab.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                mFab.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            String[] selectionArgs = new String[]{"%" + args.getString(KEY_QUERY) + "%"};
            return new CursorLoader(this, Item.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null);
        }

        return new CursorLoader(this, Item.CONTENT_URI, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    public void onItemInsert(View v) {
        ContentValues values = new ContentValues();
        values.put(Item.NAME, "Item " + (mAdapter.getItemCount() + 1));

        Uri uri = getContentResolver().insert(Item.CONTENT_URI, values);
        mRecyclerView.smoothScrollToPosition((int) ContentUris.parseId(uri));
    }

    @Override
    public void onItemUpdate(int id, String name) {
        if (!name.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(Item.NAME, name);

            Uri uri = ContentUris.withAppendedId(Item.CONTENT_URI, id);
            getContentResolver().update(uri, values, null, null);
        }
    }

    @Override
    public void onItemDelete(int id) {
        Uri uri = ContentUris.withAppendedId(Item.CONTENT_URI, id);
        getContentResolver().delete(uri, null, null);
    }

    @Override
    public void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideSoftInput(v);

        return false;
    }
}
