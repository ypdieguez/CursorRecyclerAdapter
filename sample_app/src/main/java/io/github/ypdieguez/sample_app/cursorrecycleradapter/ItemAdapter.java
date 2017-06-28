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

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import io.github.ypdieguez.cursorrecycleradapter.CursorRecyclerAdapter;

class ItemAdapter extends CursorRecyclerAdapter<ItemAdapter.ViewHolder> {

    private static final int ID_INDEX = 0;
    private static final int NAME_INDEX = 1;

    private OnItemInteractionListener mListener;

    private ViewHolder mLastHolderInEdition;

    ItemAdapter(Cursor c, OnItemInteractionListener listener) {
        super(c);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        final int id = cursor.getInt(ID_INDEX);
        final String name = cursor.getString(NAME_INDEX);

        holder.restore();
        holder.mNameView.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastHolderInEdition != null) {
                    mLastHolderInEdition.restore();
                }
                mLastHolderInEdition = holder;

                holder.mEditBtnView.setVisibility(View.VISIBLE);
                holder.mDeleteBtnView.setVisibility(View.VISIBLE);

                holder.mEditBtnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mNameView.setVisibility(View.GONE);
                        holder.mEditNameView.setText(name);
                        holder.mEditNameView.setVisibility(View.VISIBLE);
                        holder.mEditBtnView.setVisibility(View.GONE);
                        holder.mDeleteBtnView.setVisibility(View.GONE);
                        holder.mOkBtnView.setVisibility(View.VISIBLE);
                        holder.mCancelBtnView.setVisibility(View.VISIBLE);

                        holder.mOkBtnView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = holder.mEditNameView.getText().toString();
                                mListener.onItemUpdate(id, name);
                                mListener.hideSoftInput(v);

                                holder.restore();
                            }
                        });
                        holder.mCancelBtnView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.hideSoftInput(v);

                                holder.restore();
                            }
                        });
                    }
                });

                holder.mDeleteBtnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemDelete(id);
                    }
                });
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNameView;
        EditText mEditNameView;
        ImageButton mEditBtnView;
        ImageButton mDeleteBtnView;
        ImageButton mOkBtnView;
        ImageButton mCancelBtnView;

        ViewHolder(View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.tvName);
            mEditNameView = (EditText) itemView.findViewById(R.id.etName);
            mEditBtnView = (ImageButton) itemView.findViewById(R.id.ibEdit);
            mDeleteBtnView = (ImageButton) itemView.findViewById(R.id.ibDelete);
            mOkBtnView = (ImageButton) itemView.findViewById(R.id.ibOk);
            mCancelBtnView = (ImageButton) itemView.findViewById(R.id.ibCancel);
        }

        void restore() {
            mNameView.setVisibility(View.VISIBLE);
            mEditNameView.setVisibility(View.GONE);
            mEditBtnView.setVisibility(View.GONE);
            mDeleteBtnView.setVisibility(View.GONE);
            mOkBtnView.setVisibility(View.GONE);
            mCancelBtnView.setVisibility(View.GONE);
        }
    }
}
