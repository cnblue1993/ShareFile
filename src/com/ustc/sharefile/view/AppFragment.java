package com.ustc.sharefile.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.AppInfo;



public class AppFragment extends Fragment{
	
	private List<AppInfo> list = new ArrayList<AppInfo>();
	private final static int SCAN_OK = 1;
	private ProgressDialog mProgressDialog;
	private GridView mGridView;
	private AppAdapter adapter;
	
	public AppFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_app, container, false);
        
        mGridView = (GridView) view.findViewById(R.id.gv_app);
		getApp();
		
        return view;
    }
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				//关闭进度条
				mProgressDialog.dismiss();
				
				adapter = new AppAdapter(getContext(), list, mGridView);
				mGridView.setAdapter(adapter);
				break;
			}
		}
		
	};
	private void getApp(){
		mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				list = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
				List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
			
				for(int i=0;i<packages.size();i++) { 
					PackageInfo packageInfo = packages.get(i); 
					AppInfo tmpInfo =new AppInfo(); 
					tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(); 
					tmpInfo.packageName = packageInfo.packageName; 
					tmpInfo.versionName = packageInfo.versionName; 
					tmpInfo.versionCode = packageInfo.versionCode; 
					tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
					//Only display the non-system app info
					if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
					{
						list.add(tmpInfo);//如果非系统应用，则添加至appList
					}
			
				}
				
				//通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
				
			}
		}).start();
	}
}
