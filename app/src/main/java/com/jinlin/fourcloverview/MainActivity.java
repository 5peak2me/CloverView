package com.jinlin.fourcloverview;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            }
        });
        recyclerView.setAdapter(new BaseRVAdapter<String>(this, Arrays.asList(Images.imageThumbUrls), R.layout.layout_item_square, R.layout.layout_item_rectangle) {

            @Override
            public int getItemViewType(int position) {
                return position % 2;
            }

            @Override
            protected void convert(ViewHolder holder, int position, int type, String item) {
                ((CloverView) holder.getView(R.id.cloverView)).setImageUrls(new String[]{item, item, item, item});
            }
        });
    }
}
