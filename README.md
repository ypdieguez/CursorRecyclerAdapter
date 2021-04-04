
**Note:** Use cursor is obsolete

# Overview
Library containing an adapter that exposes data from a Cursor to a RecyclerView
widget.

[ ![Download](https://api.bintray.com/packages/ypdieguez/maven/cursor-recycler-adapter/images/download.svg) ](https://bintray.com/ypdieguez/maven/cursor-recycler-adapter/_latestVersion)

# How to use
1. Add the following dependency to your `build.gradle` file:

  ```gradle  
    compile 'io.github.ypdieguez:cursor-recycler-adapter:1.0.0'
  ```

2. Create an adapter that extends from CursorRecyclerAdapter class, as shows below:

  ```java
  import io.github.ypdieguez.cursorrecycleradapter.CursorRecyclerAdapter;

  class ItemAdapter extends CursorRecyclerAdapter<ItemAdapter.ViewHolder> {

      ItemAdapter(Cursor c) {
          super(c);
      }

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
          return new ViewHolder(view);
      }

      @Override
      public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
          //TODO: Bind your holder from cursor.
      }

      static class ViewHolder extends RecyclerView.ViewHolder {
          ViewHolder(View itemView) {
              super(itemView);
          }
      }
  }
  ```

**Notes:**
* The cursor must include a column named "\_id" or this class will not work.
* To get a better understanding of how to use the library, you can see the example application inside this project.

# License
    Copyright (C) 2017 Yordan P. Dieguez <ypdieguez@tuta.io>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
