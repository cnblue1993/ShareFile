package com.ustc.sharefile.activity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.NativeImageLoader;
import com.ustc.sharefile.model.NativeImageLoader.NativeImageCallBack;
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
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String path = (String) adapter.getItem(position);
				//利用NativeImageLoader类加载本地图片
				Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,null);
				
				Intent cutIntent = new Intent(ShowImageActivity.this,CutImageActivity.class);
				
				ByteArrayOutputStream baos=new ByteArrayOutputStream();  
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
				byte [] bitmapByte =baos.toByteArray();  
				cutIntent.putExtra("bitmap", bitmapByte); 
				cutIntent.putExtra("path", path);
				
				startActivity(cutIntent);
			}
			
		});
		
	
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
