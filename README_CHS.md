# DragLayout
可拖拽的RecyclerView(GridLayout)。可用于类似网易新闻客户端频道管理或其他相似地方。

[English](https://github.com/Syehunter/MaterialPageStateLayout/blob/master/README.md)

![](http://7xn4z4.com1.z0.glb.clouddn.com/DragLayout.gif)

DragLayout由RecyclerView实现。

使用：

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
	dependencies {
        compile 'com.github.Syehunter:DragLayout:1.0.0'
	}

因为在构建项目时添加了`Pinyin4j.jar`，`gradle build`时可能会出现如下错误(主要出现于`Android Studio 2.0`版本)：

	META-INF/maven/com.belerweb/pinyin4j/pom.properties
	
在`build.gradle`中添加如下代码即可：

	android {
		...
		packagingOptions {
		    exclude 'META-INF/maven/com.belerweb/pinyin4j/pom.properties'
		    exclude 'META-INF/maven/com.belerweb/pinyin4j/pom.xml'
		}
	}
	
gif中上半部分控件为`DragRecyclerView`，下半部分为`UnsignedRecycerView`。

默认展示方式为中文形式，且`UnsignedRecyclerView`是按照汉语拼音顺序排序，如果你只想改变item内容而不改变其他部分，添加如下代码即可：

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

`keepItemCount`方法可以让你自行设置希望保留的item数量(即不可移动的item)，默认为1。

如果你希望自己去定义`adapter`或者`viewHolder`，可以选择继承`BaseDragAdapter`或`BaseUnsignedAdapter`，以及`BaseDragViewHolder`，同时记得用`DragGridLayoutManager`替换`GridLayoutManager`。

`DragGridLayoutManager`可以自适应RecyclerView的高度。(暂时还不支持设置了`SpanSizeLookup`的情况，以后会修复这个问题...大雾？)。


