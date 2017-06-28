# Overview
Library containing an adapter that exposes data from a Cursor to a RecyclerView
widget.

# How to use
1. Clone the project.
2. Add the following snippets to your `settings.gradle` and `build.gradle` files:

  ```gradle
  // settings.gradle
  include ':app', ':cursorrecycleradapter'
  project (":cursorrecycleradapter").projectDir = new File("PATH_TO_CLONED_PROJECT/CursorRecyclerAdapter/lib")
  ```
  ```gradle
  // build.gradle
  compile project(":cursorrecycleradapter")
  ```
3. Create an adapter that extends from CursorRecyclerAdapter class, as shows below:

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
* To get a better understanding of how to use the library, you can see the
example application that is inside the project.

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
