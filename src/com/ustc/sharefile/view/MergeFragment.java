package com.ustc.sharefile.view;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ustc.sharefile.R;

public class MergeFragment extends Fragment {
	private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private String content;
    private LinearLayout drawLayout;
    
    private ImageView iv_1_1;
    private ImageView iv_1_2;
    private ImageView iv_1_3;
    private ImageView iv_1_4;
    private ImageView iv_1_5;
    private ImageView iv_1_6;
    private ImageView iv_1_7;
    private ImageView iv_1_8;
    private ImageView iv_2_1;
    private ImageView iv_2_2;
    private ImageView iv_2_3;
    private ImageView iv_2_4;
    private ImageView iv_3_1;
    private ImageView iv_3_2;
    private ImageView iv_3_3;
    private ImageView iv_3_4;
    private ImageView iv_4_1;
    private ImageView iv_4_2;
    private ImageView iv_4_3;
    private ImageView iv_4_4;
    private ImageView iv_4_5;

    private ImageView imageView;
    
    public MergeFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = null;
    	switch(content){
    		case "merge1":
    			view = inflater.inflate(R.layout.fg_merge1,container,false);
    			drawLayout = (LinearLayout)view.findViewById(R.id.ly_merge1);
    			init1(view);
    			break;
    		case "merge2":
    			view = inflater.inflate(R.layout.fg_merge2,container,false);
    			drawLayout = (LinearLayout)view.findViewById(R.id.ly_merge2);
    			init2(view);
    			break;
    		case "merge3":
    			view = inflater.inflate(R.layout.fg_merge3,container,false);
    			drawLayout = (LinearLayout)view.findViewById(R.id.ly_merge3);
    			init3(view);
    			break;
    		case "merge4":
    			view = inflater.inflate(R.layout.fg_merge4,container,false);
    			drawLayout = (LinearLayout)view.findViewById(R.id.ly_merge4);
    			init4(view);
    			break;
    			
    	}
        return view;
    }
    private void init1(View v){
    	iv_1_1 = (ImageView) v.findViewById(R.id.iv_1_1);
    	iv_1_2 = (ImageView) v.findViewById(R.id.iv_1_2);
    	iv_1_3 = (ImageView) v.findViewById(R.id.iv_1_3);
    	iv_1_4 = (ImageView) v.findViewById(R.id.iv_1_4);
    	iv_1_5 = (ImageView) v.findViewById(R.id.iv_1_5);
    	iv_1_6 = (ImageView) v.findViewById(R.id.iv_1_6);
    	iv_1_7 = (ImageView) v.findViewById(R.id.iv_1_7);
    	iv_1_8 = (ImageView) v.findViewById(R.id.iv_1_8);
    	
    	iv_1_1.setOnClickListener(new SelectListener());
    	iv_1_2.setOnClickListener(new SelectListener());
    	iv_1_3.setOnClickListener(new SelectListener());
    	iv_1_4.setOnClickListener(new SelectListener());
    	iv_1_5.setOnClickListener(new SelectListener());
    	iv_1_6.setOnClickListener(new SelectListener());
    	iv_1_7.setOnClickListener(new SelectListener());
    	iv_1_8.setOnClickListener(new SelectListener());
    }
    private void init2(View v){
    	iv_2_1 = (ImageView) v.findViewById(R.id.iv_2_1);
    	iv_2_2 = (ImageView) v.findViewById(R.id.iv_2_2);
    	iv_2_3 = (ImageView) v.findViewById(R.id.iv_2_3);
    	iv_2_4 = (ImageView) v.findViewById(R.id.iv_2_4);
    	
    	iv_2_1.setOnClickListener(new SelectListener());
    	iv_2_2.setOnClickListener(new SelectListener());
    	iv_2_3.setOnClickListener(new SelectListener());
    	iv_2_4.setOnClickListener(new SelectListener());
    	
    }
    private void init3(View v){
    	iv_3_1 = (ImageView) v.findViewById(R.id.iv_3_1);
    	iv_3_2 = (ImageView) v.findViewById(R.id.iv_3_2);
    	iv_3_3 = (ImageView) v.findViewById(R.id.iv_3_3);
    	iv_3_4 = (ImageView) v.findViewById(R.id.iv_3_4);
    	
    	iv_3_1.setOnClickListener(new SelectListener());
    	iv_3_2.setOnClickListener(new SelectListener());
    	iv_3_3.setOnClickListener(new SelectListener());
    	iv_3_4.setOnClickListener(new SelectListener());
    }
    private void init4(View v){
    	iv_4_1 = (ImageView) v.findViewById(R.id.iv_4_1);
    	iv_4_2 = (ImageView) v.findViewById(R.id.iv_4_2);
    	iv_4_3 = (ImageView) v.findViewById(R.id.iv_4_3);
    	iv_4_4 = (ImageView) v.findViewById(R.id.iv_4_4);
    	iv_4_5 = (ImageView) v.findViewById(R.id.iv_4_5);
    	
    	iv_4_1.setOnClickListener(new SelectListener());
    	iv_4_2.setOnClickListener(new SelectListener());
    	iv_4_3.setOnClickListener(new SelectListener());
    	iv_4_4.setOnClickListener(new SelectListener());
    	iv_4_5.setOnClickListener(new SelectListener());
    }
    
    public View getDrawView(){
    	return drawLayout;
    }

    class SelectListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");		
			startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			imageView = (ImageView) v;
		}
    	
    }
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == PHOTO_REQUEST_GALLERY) {
    		if(data != null){
    			Uri uri = data.getData();
    			setImage(uri);
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    private void setImage(Uri uri){
    	imageView.setImageURI(uri);
    }
}