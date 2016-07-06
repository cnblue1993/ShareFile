package com.ustc.sharefile.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.ClipImageLayout;
import com.ustc.sharefile.view.MainFragment;
import com.ustc.sharefile.view.PictureFragment;

public class CutImageActivity extends Activity {
	
	private ClipImageLayout mClipImageLayout;
	private String path;
	
	private TextView size1;
	private TextView size2;
	private TextView size3;
	private TextView size4;
	private TextView size5;
	
	private float []cutwidth = {25.0f, 35.0f, 127.0f, 152.4f, 177.8f};
	private float []cutheight = {35.0f, 50.0f, 88.9f, 101.6f, 127.0f};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_cut);
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		
		size1 = (TextView) findViewById(R.id.size1);
		size2 = (TextView) findViewById(R.id.size2);
		size3 = (TextView) findViewById(R.id.size3);
		size4 = (TextView) findViewById(R.id.size4);
		size5 = (TextView) findViewById(R.id.size5);
		
		size1.setOnClickListener(new sizeListener());
		size2.setOnClickListener(new sizeListener());
		size3.setOnClickListener(new sizeListener());
		size4.setOnClickListener(new sizeListener());
		size5.setOnClickListener(new sizeListener());
		
		initCutsize();
		
		
		Intent intent=getIntent();
	    if(intent!=null){
	    	byte [] bis=intent.getByteArrayExtra("bitmap");  
            Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
	    	mClipImageLayout.initImageDrawable(new BitmapDrawable(null, bitmap));
	    	path = intent.getStringExtra("path");
	    }else{
	    	mClipImageLayout.initImageDrawable(getResources().getDrawable(R.drawable.friends_sends_pictures_no,null));
	    	path = "/unknow";
	    }
	    System.out.println(path);
	}
	
	class sizeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.size1:
					mClipImageLayout.initClipBorder(cutwidth[0], cutheight[0]);
					break;
				case R.id.size2:
					mClipImageLayout.initClipBorder(cutwidth[1], cutheight[1]);
					break;
				case R.id.size3:
					mClipImageLayout.initClipBorder(cutwidth[2], cutheight[2]);
					break;
				case R.id.size4:
					mClipImageLayout.initClipBorder(cutwidth[3], cutheight[3]);
					break;
				case R.id.size5:
					mClipImageLayout.initClipBorder(cutwidth[4], cutheight[4]);
					break;
				default:
					break;
			}
			
		}
		
	}
	private void initCutsize(){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		for(int i = 0; i < 5; i++){
			cutheight[i] = cutheight[i] * metrics.density / (metrics.xdpi * (1.0f / 25.4f)) * 40;
			cutwidth[i] = cutwidth[i] * metrics.density / (metrics.xdpi * (1.0f / 25.4f)) * 40;
		}
		mClipImageLayout.initClipBorder(cutwidth[0], cutheight[0]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.picture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.cut:
			Bitmap bitmap = mClipImageLayout.clip();
			//未存储在本地
			saveMyBitmap(path, bitmap);
			break;
		case R.id.merge:
			Intent intent = new Intent(this, MergeImageActivity.class);
			startActivity(intent);
			break;
		case android.R.id.home:// 点击返回图标事件
            this.finish();

			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void saveMyBitmap(String bitName,Bitmap mBitmap){
		  bitName = bitName.substring(bitName.lastIndexOf("/")+1);
		  bitName = bitName.substring(0,bitName.length()-4);

		  String path="/sdcard/ShareFiles/Cutpicture";   
	      File file=new File(path);   
	      if(!file.exists())   
	    	  file.mkdirs();     
	     
		  
		  File f = new File(path +"/" + bitName+".png");
		  try {
			  f.createNewFile();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
			  Toast.makeText(this, "在保存图片时出错：", Toast.LENGTH_LONG).show();
		  }
		  
		  FileOutputStream fOut = null;
		  try {
			  fOut = new FileOutputStream(f);
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  }
		  boolean isSaved = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	
		  try {
			  fOut.flush();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  try {
			  fOut.close();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  
		  if(isSaved){
			  Toast.makeText(this, "裁剪图片已保存", Toast.LENGTH_SHORT).show();
			  Intent backIntent = new Intent(this,MainActivity.class);
			  startActivity(backIntent);
		  }else{
			  Toast.makeText(this, "裁剪图片保存失败", Toast.LENGTH_SHORT).show();
		  }
	 }

}
