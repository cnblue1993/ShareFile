package com.ustc.sharefile.view;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.RippleLayout;

public class AllFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private final static String TAG = "FileFragment";
	
	private String path = "/";	//当前路径
	
	private ListView itemList;
	private TextView filePath;
	private Button sendButton;
	
	private List<Map<String, Object>> adapterList;
	
	ImageView imageview;
    RippleLayout layout;
    TextView tip;
    
    private WifiManager wifiManager;   
    
	public AllFragment() {

    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_all, container, false);
		findViews(view);
		
		refreshListItems(path);
		return view;
	}
	
	// 刷新ListView
		private void refreshListItems(String path) {
			// TODO Auto-generated method stub
			filePath.setText(path);
			adapterList = buildListForSimpleAdapter(path);
			SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), adapterList, R.layout.file_item, 
					new String[]{"name", "path", "img"}, 
					new int[]{R.id.file_name, R.id.file_path, R.id.file_img});
			
			itemList.setAdapter(listAdapter);
			itemList.setOnItemClickListener(this);
			itemList.setSelection(0);
			
		}
		
		private List<Map<String, Object>> buildListForSimpleAdapter(String path) {
			// TODO Auto-generated method stub
			File nowFile = new File(path);
			
			
			adapterList = new ArrayList<Map<String, Object>>();
			
			//放上根目录
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("name", "/");
			root.put("img", R.drawable.file_root);
			root.put("path", "回根目录");
			adapterList.add(root);
			
			//放上父目录
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("name", "..");
			pMap.put("img", R.drawable.file_parent);
			pMap.put("path", "上一级");
			adapterList.add(pMap);
			
			if(!nowFile.isDirectory()){	//若是当前路径对应的是文件，则返回
				sendButton.setEnabled(true);	//发送按钮可用
				return adapterList;
			}
			
			File[] files = nowFile.listFiles();	//得到path下所有文件
			
			for(File file:files){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", file.getName());
				map.put("path", file.getPath());
				if(file.isDirectory()){
					map.put("img", R.drawable.file_directory);
				}else{
					map.put("img", R.drawable.file_doc);
				}
				adapterList.add(map);
			}
			
			sendButton.setEnabled(false);	//当前路径对应的是文件夹，发送按钮不可用
			return adapterList;
		}

		private void findViews(View view) {
			// TODO Auto-generated method stub
			itemList = (ListView) view.findViewById(R.id.file_detail);
			filePath = (TextView) view.findViewById(R.id.file_path);
			sendButton = (Button) view.findViewById(R.id.file_send);
			sendButton.setOnClickListener(this);
			sendButton.setEnabled(false);	//开始时不可点击，只有选中的路径是文件时才可以点击
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			Log.i(TAG, "位置[" + position + "]上的item被点击");
			if(position == 0){	//回根目录
				path = "/";
				refreshListItems(path);
			}else if(position == 1){	//回到上一级
				goToParent();
			}else{
				path = (String) adapterList.get(position).get("path");
				refreshListItems(path);
			}
		}

		private void goToParent() {
			// TODO Auto-generated method stub
			File file = new File(path);
			File pFile = file.getParentFile();	//得到父文件
			if(pFile == null){
				Toast.makeText(getActivity(), "当前路径已经是根目录，不存在上一级", 
						Toast.LENGTH_SHORT).show();
				refreshListItems(path);
			}else{
				path = pFile.getAbsolutePath();
				refreshListItems(path);
			}
		
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("filePaths", path);
			//发送文件未做
		}

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fg_all, container, false);
//        
//        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE); 
//        
//       
//        layout = (RippleLayout) view.findViewById(R.id.ripple_layout);
//        imageview = (ImageView) view.findViewById(R.id.centerImage);
//        tip = (TextView) view.findViewById(R.id.tip);
//        //subView = inflater.inflate(R.layout.find_user, container, false);
//        
//        imageview.setOnClickListener(new OnClickListener() {
//        	
//            @Override
//            public void onClick(View v) { 
//                if (layout.isRippleAnimationRunning()) {
//                    layout.stopRippleAnimation();
//                    setWifiApEnabled(false); 
//                } else {
//                    layout.startRippleAnimation();
//                    //addSubView();
//                    if(setWifiApEnabled(true));
//                    	tip.setVisibility(View.GONE);
//                    
//                }
//                
//            }
//        });
//        return view;
//    }
//    
// // wifi热点开关  
//    public boolean setWifiApEnabled(boolean enabled) {  
//        if (enabled) { // disable WiFi in any case  
//            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi  
//            wifiManager.setWifiEnabled(false);  
//        }  
//        try {  
//            //热点的配置类  
//            WifiConfiguration apConfig = new WifiConfiguration();  
//            //配置热点的名称(可以在名字后面加点随机数什么的)  
//            apConfig.SSID = "WIFI-OdeTojoy";  
//            //配置热点的密码  
//            apConfig.preSharedKey="12345678";  
//            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                //通过反射调用设置热点  
//            Method method = wifiManager.getClass().getMethod(  
//                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);  
//            //返回热点打开状态  
//            return (Boolean) method.invoke(wifiManager, apConfig, enabled);  
//        } catch (Exception e) {  
//            return false;  
//        }  
//    }
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		setWifiApEnabled(false);
//	}  

//    private void addSubView(){
//    	
//        subRippleLayout sublayout = (subRippleLayout) subView.findViewById(R.id.user_ripple_layout);
//        TextView subtTextView = (TextView) subView.findViewById(R.id.find_user);
//        sublayout.startRippleAnimation();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);   
//        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);   
//        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);     
//        lp.rightMargin=30;
//        lp.topMargin=30;
//        
//        subView.setLayoutParams(lp);
//        
//        layout.addView(subView);
//    }
}
