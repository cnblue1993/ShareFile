package com.ustc.sharefile.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.view.MergeFragment;

public class MergeImageActivity extends Activity implements OnClickListener{
	private ImageView iv_merge1;
	private ImageView iv_merge2;
	private ImageView iv_merge3;
	private ImageView iv_merge4;
	private FrameLayout ly_merge;
	
	private MergeFragment fg1,fg2,fg3,fg4;
	private FragmentManager fManager;
	
	private View drawView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_merge);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        fManager = getFragmentManager();
        bindViews();
        iv_merge1.performClick();   //模拟一次点击，既进去后选择第一项
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        iv_merge1 = (ImageView) findViewById(R.id.iv_merge1);
        iv_merge2 = (ImageView) findViewById(R.id.iv_merge2);
        iv_merge3 = (ImageView) findViewById(R.id.iv_merge3);
        iv_merge4 = (ImageView) findViewById(R.id.iv_merge4);

        ly_merge = (FrameLayout) findViewById(R.id.ly_merge);

        iv_merge1.setOnClickListener(this);
        iv_merge2.setOnClickListener(this);
        iv_merge3.setOnClickListener(this);
        iv_merge4.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        iv_merge1.setSelected(false);
        iv_merge2.setSelected(false);
        iv_merge3.setSelected(false);
        iv_merge4.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.iv_merge1:
                setSelected();
                iv_merge1.setSelected(true);
                if(fg1 == null){
                    fg1 = new MergeFragment("merge1");
                    fTransaction.add(R.id.ly_merge,fg1);
                }else{
                    fTransaction.show(fg1);
                }
                drawView = fg1.getDrawView();
                System.out.println("fg1"+drawView);
                break;
            case R.id.iv_merge2:
                setSelected();
                iv_merge2.setSelected(true);
                if(fg2 == null){
                    fg2 = new MergeFragment("merge2");
                    fTransaction.add(R.id.ly_merge,fg2);
                }else{
                    fTransaction.show(fg2);
                }
                drawView = fg2.getDrawView();
                System.out.println("fg2"+drawView);
                break;
            case R.id.iv_merge3:
                setSelected();
                iv_merge3.setSelected(true);
                if(fg3 == null){
                    fg3 = new MergeFragment("merge3");
                    fTransaction.add(R.id.ly_merge,fg3);
                }else{
                    fTransaction.show(fg3);
                }
                drawView = fg3.getDrawView();
                System.out.println("fg3"+drawView);
                break;
            case R.id.iv_merge4:
                setSelected();
                iv_merge4.setSelected(true);
                if(fg4 == null){
                    fg4 = new MergeFragment("merge4");
                    fTransaction.add(R.id.ly_merge,fg4);
                }else{
                    fTransaction.show(fg4);
                }
                drawView = fg4.getDrawView();
                System.out.println("fg4"+drawView);
                break;
        }
        fTransaction.commit();
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.merge, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.merge_finish:
				Bitmap bitmap = createViewBitmap(drawView);
				
				Date date= new Date();//创建一个时间对象，获取到当前的时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间显示格式
				String filename = sdf.format(date);
				
				saveMyBitmap(filename, bitmap);
					
				break;
			case android.R.id.home:// 点击返回图标事件
	            this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
	public void saveMyBitmap(String bitName,Bitmap mBitmap){

		  String path="/sdcard/ShareFiles/Mergepicture";   
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
				Toast.makeText(this, "合成图片已保存", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "合成图片保存失败", Toast.LENGTH_SHORT).show();
			}
	 }
}
