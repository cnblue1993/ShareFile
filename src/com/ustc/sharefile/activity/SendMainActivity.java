package com.ustc.sharefile.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ustc.sharefile.R;
import com.ustc.sharefile.transfer.model.FileTcpServer;
import com.ustc.sharefile.transfer.model.Msg;
import com.ustc.sharefile.transfer.model.Tools;
import com.ustc.sharefile.transfer.model.User;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SendMainActivity extends Activity {
	ListView listView;
	
	public List<User> userList = null;
	public List<Map<String, Object>> adapterList = null;
	SimpleAdapter adapter=null;

	Tools tools=null;
	Msg m=null;
	ProgressDialog proDia = null;
	Double fileSize=0.0;
	
	User person = null;
	public String choosePath = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        
      //初始化布局
      		Tools.State=Tools.SENDACTIVITY;//状态
      		Tools.send=this;
      		init();
      		
      		tools=new Tools(this,Tools.ACTIVITY_SEND);
      		
      		//广播上线(包括自己)
    		reBroad();
    		
    		// 开启接收端 时时更新在线列表
    		tools.receiveMsg();
    		
    		Intent pathIntent = getIntent();
    		choosePath = pathIntent.getStringExtra("filePaths");
      		
      		
    }

 // 初始化布局
 	public void init()
 	{
 
 		listView = (ListView) super.findViewById(R.id.friend_listView);
 		// 列表项监听
 		listView.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 					long arg3) {
 				
 				person = userList.get(arg2);
	 			//发送请求传送文件
	 			Msg msg=new Msg(Tools.me.getHeadIcon(),Tools.me.getName(), Tools.me.getIp(), person.getName(), person.getIp(),Tools.CMD_FILEREQUEST, 
	 					(new File(choosePath)).getName()+Tools.sign+(new File(choosePath)).length());

	 			tools.sendMsg(msg);
	 			Tools.out("发送请求传送文件");
 				
 			}
 		});
 		// 初始化自己
 		userList = new ArrayList<User>();
 		
 		//Tools.me = new User(Build.MODEL,Tools.getLocalHostIp(),0);

 		userList.add(Tools.me.getCopy());
 		
 		adapterList = new ArrayList<Map<String, Object>>();
 		Map map = new HashMap<String, Object>();
 		map.put("headicon", Tools.headIconIds[Tools.me.getHeadIcon()]);
 		map.put("name", Tools.me.getName());
 		map.put("ip", " IP:"+Tools.me.getIp());
 		map.put("UnReadMsgCount", "");
 		
 		adapterList.add(map);
 		
 		//初始化view适配器
 		adapter = new SimpleAdapter(this, adapterList, R.layout.friend_item,
 				new String[] {"headicon","name", "ip", "UnReadMsgCount" }, new int[] {
 						R.id.headicon,R.id.name, R.id.ip, R.id.UnReadMsgCount });
 		listView.setAdapter(adapter);
 		

 		
 		proDia = new ProgressDialog(this);
 		proDia.setTitle("文件发送");// 设置标题
 		proDia.setMessage("文件");// 设置显示信息
 		proDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平进度条
 		proDia.setMax(100);// 设置最大进度指
 		proDia.setProgress(10);// 开始点
 		
 	}



    
    public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Tools.SHOW:
				Toast.makeText(SendMainActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
				case Tools.FLUSH:
					adapter.notifyDataSetChanged();
					break;
				case Tools.ADDUSER:
					adapterList.add((Map)msg.obj);
					adapter.notifyDataSetChanged();
					break;
				case Tools.DESTROYUSER:
					int i=(Integer) msg.obj;
					userList.remove(i);
					adapterList.remove(i);
					adapter.notifyDataSetChanged();

				case Tools.CMD_FILEREQUEST:
					//文件请求
					receiveFile((Msg)msg.obj);
					break;
				case Tools.FILE_JINDU:
					String[] pi = ((String) msg.obj).split(Tools.sign);
					fileSize = Double.parseDouble(pi[2]);
					proDia.setTitle(pi[0]);// 设置标题
					proDia.setMessage(pi[1] + " 大小："
							+ getFormatSize(fileSize));// 设置显示信息
					proDia.onStart();
					proDia.show();
					break;
				case Tools.PROGRESS_FLUSH:
					System.out.println("main  progress flush");
					int i0 = (int) ((Tools.sendProgress / (fileSize)) * 100);
					proDia.setProgress(i0);
					break;
				case Tools.PROGRESS_COL:// 关闭进度条
					
					proDia.dismiss();
					Intent mainintent = new Intent(SendMainActivity.this,MainActivity.class);
					startActivity(mainintent);
					break;
			}
		}
	};
	// 收到传送文件请求  创建文件接收对话框
	private void receiveFile(Msg mes)
	{
		System.out.println("send receiveFile()");
		this.m=mes;
		String str=mes.getBody().toString();
		new AlertDialog.Builder(SendMainActivity.this)
		//FileActivity.getFormatSize(Double.parseDouble(str.split(Tools.sign)[1]))
		.setTitle("是否接收文件：" + str.split(Tools.sign)[0] +" 大小："+"0")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("接受", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 接收文件 返回提示接受 建立tcp 服务器 接收文件
				FileTcpServer ts = new FileTcpServer(SendMainActivity.this);
				ts.start();
				Tools.sendProgress = 0;
				Message m1 = new Message();
				m1.what = Tools.FILE_JINDU;
				m1.obj = "接收文件" + Tools.sign + "正在接收：" + Tools.newfileName
						+ Tools.sign + Tools.newfileSize;
				
				handler.sendMessage(m1);
				fileProgress();// 启动进度条线程
				
				// 发送消息 让对方开始发送文件
				Msg msg=new Msg(0,Tools.me.getName(), Tools.me.getIp(), m.getSendUser(), m.getSendUserIp(),Tools.CMD_FILEACCEPT, null);
				
				tools.sendMsg(msg);
				
				return;
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 不接受 返回提示不接受
				Msg msg=new Msg(0,Tools.me.getName(), Tools.me.getIp(), m.getSendUser(), m.getSendUserIp(),Tools.CMD_FILEREFUSE, null);
				tools.sendMsg(msg);
				return;
			}
		}).show();
	}
	
	// 文件传送进度条
		public void fileProgress() {
			new Thread() {
				public void run() {
					while (Tools.sendProgress != -1) {
						System.out.println("file fileprogress");
						Message m = new Message();
						m.what = Tools.PROGRESS_FLUSH;
						handler.sendMessage(m);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 关闭进度条
					Message m1 = new Message();
					m1.what = Tools.PROGRESS_COL;
					handler.sendMessage(m1);
				}
			}.start();
		}
		@Override
	    protected void onResume() {
	    	super.onResume();
	    	//isPaused = false;
	    	Tools.out("Resume");
	    	reBroad();
			Tools.State=Tools.SENDACTIVITY;
	    }
	    @Override
	    protected void onPause() {
	    	super.onPause();
	    	//isPaused = false;
	    	Tools.out("PAUSE");
	    }
	    @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	//isPaused=true;
	    	Tools.out("Destroy");
	    }
		//广播自己
	    public void reBroad()
	    {
	    	//广播上线(包括自己)
			Msg msg=new Msg();
			msg.setSendUser(Tools.me.getName());//昵称默认为自己的机器号
			msg.setHeadIconPos(Tools.me.getHeadIcon());
			msg.setSendUserIp(Tools.me.getIp());
			msg.setReceiveUserIp(Tools.getBroadCastIP());
			msg.setMsgType(Tools.CMD_ONLINE);//通知上线命令
			// 发送广播通知上线
			tools.sendMsg(msg);
	    }
		// 计算文件大小
		public static String getFormatSize(double size) {
			double kiloByte = size / 1024;
			if (kiloByte < 1) {
				return size + "Byte(s)";
			}

			double megaByte = kiloByte / 1024;
			if (megaByte < 1) {
				BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
				return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
						.toPlainString() + "KB";
			}

			double gigaByte = megaByte / 1024;
			if (gigaByte < 1) {
				BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
				return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
						.toPlainString() + "MB";
			}

			double teraBytes = gigaByte / 1024;
			if (teraBytes < 1) {
				BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
				return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
						.toPlainString() + "GB";
			}
			BigDecimal result4 = new BigDecimal(teraBytes);
			return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
					+ "TB";
		}
}

