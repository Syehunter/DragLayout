# DragLayout
RecyclerView with GridLayout that can be dragged.You may use this like the Netease ChannelManager or in some otherwise.

[中文版](https://github.com/Syehunter/MaterialPageStateLayout/blob/master/README_CHS.md)

![](http://7xn4z4.com1.z0.glb.clouddn.com/DragLayout.gif)

The DragLayout implemented with RecyclerView.

Usage:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
	dependencies {
        compile 'com.github.Syehunter:DragLayout:1.0.1'
	}
	
There might be some error occurs when gradle build in Android Studio 2.0 with `"META-INF/maven/com.belerweb/pinyin4j/pom.properties"`(for I imported Pinyin4j.jar into library), just add this in your build.gradle:

	android {
		...
		packagingOptions {
		    exclude 'META-INF/maven/com.belerweb/pinyin4j/pom.properties'
		    exclude 'META-INF/maven/com.belerweb/pinyin4j/pom.xml'
		}
	}

The layout above is `DragRecyclerView`, and `UnsignedRecyclerView` below.

Defaulst is Chinese, and the `UnsignedRecyclerView` is sort by `Pinjin`, Use this in your code if you want to change the item content only:

	mDragView.datas(dragList)
                .onItemClick(new OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                        Toast.makeText(MainActivity.this,
                                "position" + position + "has been clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .onLongPress(new OnLongPressListener() {
                    @Override
                    public void onLongPress() {
                        mQuitBtn.setVisibility(View.VISIBLE);
                    }
                })
                .onItemRemoved(new OnItemRemovedListener<String>() {
                    @Override
                    public void onItemRemoved(int position, String removedItem) {
                        mUnsignedView.addItem(removedItem);
                    }
                })
                .keepItemCount(2)
                .build();
                
 	mUnsignedView = (UnsignedRecyclerView) findViewById(R.id.unsignedView);

    mUnsignedView.hanZiDatas(unsignedList)
                .onItemRemoved(new OnItemRemovedListener<String>() {
                    @Override
                    public void onItemRemoved(int position, String removedItem) {
                        mDragView.addItem(removedItem);
                    }
                })
                .build();
                
 Method `keepItemCount()` that could let you set the unmoved item counts, default is 1.
                
 Define your adapter extends `BaseDragAdapter` or `BaseUnsignedAdapter` if you don't like this, and dragViewHolder extends `BaseDragViewHolder`,  remember to use `DragGridLayoutManager` instead of GridLayoutMananger, which will adjust RecyclerView's height to `wrap_content`(Unfortunately the `DragGridLayoutManager` doesn't support RecyclerView that has set `SpanSizeLookup` now, I will fix it in future).

`mDragView.getDatas()` and `mUnsignedView.getDatas()` will return the transformed datas for you to store them.	
