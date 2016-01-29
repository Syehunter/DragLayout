package z.sye.space.draglayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import z.sye.space.library.DragRecyclerView;
import z.sye.space.library.UnsignedRecyclerView;
import z.sye.space.library.interfaces.OnItemClickListener;
import z.sye.space.library.interfaces.OnItemRemovedListener;
import z.sye.space.library.interfaces.OnLongPressListener;

public class MainActivity extends AppCompatActivity {

    private Button mQuitBtn;
    private UnsignedRecyclerView mUnsignedView;
    private DragRecyclerView mDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> dragList = new ArrayList<>();
        dragList.add("头条");
        dragList.add("热点");
        dragList.add("体育");
        dragList.add("军事");
        dragList.add("足球");
        dragList.add("订阅");
        dragList.add("NBA");
        dragList.add("国足");
        dragList.add("科技");
        dragList.add("娱乐");
        dragList.add("本地");
        dragList.add("财经");
        dragList.add("图片");
        dragList.add("跟帖");
        dragList.add("直播");
        dragList.add("时尚");
        dragList.add("段子");
        dragList.add("漫画");
        dragList.add("游戏");
        dragList.add("影视");

        List<String> unsignedList = new ArrayList<>();
        unsignedList.add("原创");
        unsignedList.add("历史");
        unsignedList.add("彩票");
        unsignedList.add("数码");
        unsignedList.add("汽车");
        unsignedList.add("家居");
        unsignedList.add("旅游");
        unsignedList.add("健康");
        unsignedList.add("读书");
        unsignedList.add("教育");
        unsignedList.add("艺术");
        unsignedList.add("论坛");
        unsignedList.add("博客");
        unsignedList.add("情感");

        mDragView = (DragRecyclerView) findViewById(R.id.dragView);
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

        mQuitBtn = (Button) findViewById(R.id.btn_quit);
        mQuitBtn.setVisibility(View.GONE);
        mQuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragView.quitLongPressMode();
                mQuitBtn.setVisibility(View.GONE);
            }
        });

        mUnsignedView = (UnsignedRecyclerView) findViewById(R.id.unsignedView);

        mUnsignedView.hanZiDatas(unsignedList)
                .onItemRemoved(new OnItemRemovedListener<String>() {
                    @Override
                    public void onItemRemoved(int position, String removedItem) {
                        mDragView.addItem(removedItem);
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
