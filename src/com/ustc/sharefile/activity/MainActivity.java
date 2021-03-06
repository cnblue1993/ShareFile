package com.ustc.sharefile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.fraction;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.sharefile.R;
import com.ustc.sharefile.transfer.model.FileTcpServer;
import com.ustc.sharefile.transfer.model.Msg;
import com.ustc.sharefile.transfer.model.Tools;
import com.ustc.sharefile.transfer.model.User;
import com.ustc.sharefile.view.AllFragment;
import com.ustc.sharefile.view.MainFragment;

public class MainActivity extends FragmentActivity {

	private Fragment mainFragment;
	
	private DrawerLayout drawerLayout;
	private RelativeLayout leftDrawer;
	private ListView leftList;
	private SimpleAdapter contentLeftAdapter;
	private TextView username;
	
	private boolean isopen = false;
	
	Tools tools = null;
	Msg m=null;
	ProgressDialog proDia = null;
	Double fileSize=0.0;
	
	SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		username = (TextView) findViewById(R.id.tv_userName);
		 
		mainFragment = new MainFragment();

		getSupportFragmentManager().beginTransaction().add(R.id.root_framelayout,mainFragment).commit();
		
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		/* 左抽屉 */
        leftDrawer = (RelativeLayout) findViewById(R.id.left_relative_drawer);
        /* 左列表在左抽屉里 */
        leftList = (ListView) findViewById(R.id.lv_left);
        

        /* 适配器装载数据;即初始化导航列表；这里使用SimpleAdapter，加载自定义的LinearLayout作为按钮 */
        contentLeftAdapter = new SimpleAdapter(this, leftDrawerListData(),
                R.layout.left_item_view, new String[]{"image","text"},
                new int[]{R.id.iv_left,R.id.tv_left});

        leftList.setAdapter(contentLeftAdapter);

        /* 为list设置ClickListener;DrawerOnItemClickListener定义在下面*/
        leftList.setOnItemClickListener(new DrawerOnItemClickListener());
        
        java.util.Random random=new java.util.Random();// 定义随机类
        int head=random.nextInt(10);
        
        Tools.me = new User(Build.MODEL,Tools.getLocalHostIp(),0);
        Tools.me.setHeadIcon(head);
        
        tools=new Tools(this,Tools.ACTIVITY_MAIN);
        tools.receiveMsg();
        
        proDia = new ProgressDialog(this);
 		proDia.setTitle("文件发送");// 设置标题
 		proDia.setMessage("文件");// 设置显示信息
 		proDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平进度条
 		proDia.setMax(100);// 设置最大进度指
 		proDia.setProgress(10);// 开始点
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	   switch (item.getItemId()) 
	   {        
	      case android.R.id.home:            
	         isopen = !isopen;
	         if(isopen)
	        	 drawerLayout.openDrawer(leftDrawer);
	         else 
	        	 drawerLayout.closeDrawer(leftDrawer);
	         return true; 
	      case R.id.menu_receive:
	    	  reBroad();
	         
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	}
	
	private List<Map<String, Object>> leftDrawerListData(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("image", R.drawable.login);
        map.put("text", "登录/注册");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("image", R.drawable.home);
        map.put("text", "主页");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.modify);
        map.put("text", "个人信息");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.out);
        map.put("text", "注销");
        list.add(map);
        


        return list;
    }
	
	//---------------------------------------------------------------------
	private class DrawerOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            seleteItem(position);/* 按钮选择 */
        }
    }

    private void seleteItem(int position){
        leftList.setItemChecked(position, true);
        
        switch (position) {
			case 0:
				Intent loginIntent = new Intent(MainActivity.this, LoginOrRegister.class);
				startActivity(loginIntent);
				break;
			case 1:
				drawerLayout.closeDrawer(leftDrawer);
				break;
			case 3:
				clearLogin();
				drawerLayout.closeDrawer(leftDrawer);
				break;
			default:
				break;
			}
    }
	
    public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Tools.SHOW:
				Toast.makeText(MainActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				
				case Tools.CMD_FILEREQUEST:
					//文件请求
					receiveFile((Msg)(msg.obj));
					break;
				case Tools.FILE_JINDU:
					String[] pi = ((String) msg.obj).split(Tools.sign);
					fileSize = Double.parseDouble(pi[2]);
					proDia.setTitle(pi[0]);// 设置标题
					proDia.setMessage(pi[1] + " 大小："
							+ SendMainActivity.getFormatSize(fileSize));// 设置显示信息
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
					break;
			}
		}
	};
	// 收到传送文件请求  创建文件接收对话框
	private void receiveFile(Msg mes)
	{
		this.m=mes;
		String str=mes.getBody().toString();
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("是否接收文件：" + str.split(Tools.sign)[0] +" 大小："+SendMainActivity.getFormatSize(Double.parseDouble(str.split(Tools.sign)[1])))
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("接受", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 接收文件 返回提示接受 建立tcp 服务器 接收文件
				FileTcpServer ts = new FileTcpServer(MainActivity.this);
				ts.start();
				Tools.sendProgress = 0;
				Message m1 = new Message();
				m1.what = Tools.FILE_JINDU;
				m1.obj = "接收文件" + Tools.sign + "正在接收：" + Tools.newfileName
						+ Tools.sign + Tools.newfileSize;
				
				handler.sendMessage(m1);
				
				
				// 发送消息 让对方开始发送文件
				Msg msg=new Msg(0,Tools.me.getName(), Tools.me.getIp(), m.getSendUser(), m.getSendUserIp(),Tools.CMD_FILEACCEPT, null);
				
				tools.sendMsg(msg);
				fileProgress();// 启动接收进度条线程
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
					System.out.println("main progress");
					while (Tools.sendProgress != -1) {
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
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			
			preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
			username.setText(preferences.getString("userName", "userName"));
			
		}
		@Override
	    protected void onResume() {
	    	super.onResume();
	    	//isPaused = false;
	    	Tools.out("Resume");
	    	reBroad();
			Tools.State=Tools.MAINACTIVITY;
	    }
	    @Override
	    protected void onPause() {
	    	super.onPause();
	    	//isPaused = false;
	    	Tools.out("PAUSE");
	    	Tools.State=Tools.MAINACTIVITY;
	    }
	    @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	//isPaused=true;
	    	Tools.out("Destroy");
	    	Tools.State=Tools.MAINACTIVITY;
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
	    public void clearLogin() { 
	    	  preferences.edit().clear().commit(); 
	    	  preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
			  username.setText(preferences.getString("userName", "userName"));
	    } 
}
