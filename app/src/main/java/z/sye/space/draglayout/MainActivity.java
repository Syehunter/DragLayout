package z.sye.space.draglayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import z.sye.space.library.DragGridLayoutManager;
import z.sye.space.library.DragRecyclerView;
import z.sye.space.library.adapters.DragAdapter;
import z.sye.space.library.interfaces.DragItemStartListener;
import z.sye.space.library.interfaces.DragItemTouchHelperCallBack;

public class MainActivity extends AppCompatActivity implements DragItemStartListener {

    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < 30 ; i++) {
            list.add("item " + i);
        }

        DragRecyclerView mDragView = (DragRecyclerView) findViewById(R.id.dragView);
        DragAdapter dragAdapter = new DragAdapter(this);
        mDragView.setAdapter(dragAdapter);
        final DragGridLayoutManager layoutManager = new DragGridLayoutManager(this, 4);
        mDragView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new DragItemTouchHelperCallBack(dragAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mDragView);

        dragAdapter.setItemList(list);
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

    @Override
    public void onDragStart(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
