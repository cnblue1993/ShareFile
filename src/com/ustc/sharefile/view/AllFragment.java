package com.ustc.sharefile.view;


import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.RippleLayout;

public class AllFragment extends Fragment {
	ImageView imageview;
    RippleLayout layout;
    TextView tip;
    
    private WifiManager wifiManager;   
    
	public AllFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_all, container, false);
        
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE); 
        
       
        layout = (RippleLayout) view.findViewById(R.id.ripple_layout);
        imageview = (ImageView) view.findViewById(R.id.centerImage);
        tip = (TextView) view.findViewById(R.id.tip);
        //subView = inflater.inflate(R.layout.find_user, container, false);
        
        imageview.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) { 
                if (layout.isRippleAnimationRunning()) {
                    layout.stopRippleAnimation();
                    setWifiApEnabled(false); 
                } else {
                    layout.startRippleAnimation();
                    //addSubView();
                    if(setWifiApEnabled(true));
                    	tip.setVisibility(View.GONE);
                    
                }
                
            }
        });
        return view;
    }
    
 // wifi热点开关  
    public boolean setWifiApEnabled(boolean enabled) {  
        if (enabled) { // disable WiFi in any case  
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi  
            wifiManager.setWifiEnabled(false);  
        }  
        try {  
            //热点的配置类  
            WifiConfiguration apConfig = new WifiConfiguration();  
            //配置热点的名称(可以在名字后面加点随机数什么的)  
            apConfig.SSID = "WIFI-OdeTojoy";  
            //配置热点的密码  
            apConfig.preSharedKey="12345678";  
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                //通过反射调用设置热点  
            Method method = wifiManager.getClass().getMethod(  
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);  
            //返回热点打开状态  
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);  
        } catch (Exception e) {  
            return false;  
        }  
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setWifiApEnabled(false);
	}  

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
