package com.ustc.sharefile.activity;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.view.ChildAdapter;

public class ShowImageActivity extends Activity {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		mGridView = (GridView) findViewById(R.id.picture_child_grid);
		list = getIntent().getStringArrayListExtra("data");
		
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		
	
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:// 点击返回图标事件
	            this.finish();
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
}
