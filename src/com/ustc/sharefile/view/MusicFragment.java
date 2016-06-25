package com.ustc.sharefile.view;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.sharefile.R;
import com.ustc.sharefile.model.RippleLayout;

public class MusicFragment extends Fragment {
	ImageView imageview;
    RippleLayout layout;
    TextView tip;
    
    private List<ScanResult> wifiList;  
    private WifiManager wifiManager;  
    private List<String> passableHotsPot;  
    private WifiReceiver wifiReceiver;  
    private boolean isConnected=false; 
    
    private static final String TAG="FIND";

	public MusicFragment(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_music, container, false);
        
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);  
        wifiReceiver = new WifiReceiver();
        
        if (!wifiManager.isWifiEnabled()){
        	if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
        		wifiManager.setWifiEnabled(true);
        	}
        }
        getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
       
        layout = (RippleLayout) view.findViewById(R.id.find_ripple_layout);
        imageview = (ImageView) view.findViewById(R.id.find_centerImage);
        tip = (TextView) view.findViewById(R.id.find_tip);
        //subView = inflater.inflate(R.layout.find_user, container, false);
        
        imageview.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) { 
                if (layout.isRippleAnimationRunning()) {
                    layout.stopRippleAnimation();
                    getActivity().unregisterReceiver(wifiReceiver);  
                } else {
                    layout.startRippleAnimation();
                    wifiManager.startScan();
                 
                    //addSubView();
                }
            }
        });
        return view;
    }
	/* 监听热点变化 */  
    private final class WifiReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            wifiList = wifiManager.getScanResults();  
            if (wifiList == null || wifiList.size() == 0 || isConnected)  
                return;  
            onReceiveNewNetworks(wifiList);  
        }  
    }  
      
    /*当搜索到新的wifi热点时判断该热点是否符合规格*/  
    public void onReceiveNewNetworks(List<ScanResult> wifiList){  
        passableHotsPot=new ArrayList<String>();  
        for(ScanResult result:wifiList){  
            System.out.println(result.SSID);
            Log.i(TAG,result.SSID);
            if((result.SSID).contains("WIFI-OdeTojoy"))  
                passableHotsPot.add(result.SSID);  
        }  
        synchronized (this) {  
            connectToHotpot();  
        }  
    }  
      
    /*连接到热点*/  
    public void connectToHotpot(){  
        if(passableHotsPot==null || passableHotsPot.size()==0)  
            return;  
        WifiConfiguration wifiConfig=this.setWifiParams(passableHotsPot.get(0));  
        int wcgID = wifiManager.addNetwork(wifiConfig);  
        boolean flag=wifiManager.enableNetwork(wcgID, true);  
        isConnected=flag;  
        System.out.println("connect success? "+flag);  
    }  
      
    /*设置要连接的热点的参数*/  
    public WifiConfiguration setWifiParams(String ssid){  
        WifiConfiguration apConfig=new WifiConfiguration();  
        apConfig.SSID="\""+ssid+"\"";  
        apConfig.preSharedKey="\"12345678\"";  
        apConfig.hiddenSSID = true;  
        apConfig.status = WifiConfiguration.Status.ENABLED;  
        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
        apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);  
        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  
        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  
        apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  
        return apConfig;  
    }  
      
    @Override
	public void onDestroy() {  
        super.onDestroy();  
        /*销毁时注销广播*/  
        getActivity().unregisterReceiver(wifiReceiver);  
    }  

}
