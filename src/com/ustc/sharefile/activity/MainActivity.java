package com.ustc.sharefile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.ustc.sharefile.R;
import com.ustc.sharefile.view.LoginFragment;
import com.ustc.sharefile.view.MainFragment;

public class MainActivity extends FragmentActivity {

	private Fragment mainFragment;
	
	private DrawerLayout drawerLayout;
	private RelativeLayout leftDrawer;
	private ListView leftList;
	private SimpleAdapter contentLeftAdapter;
	
	private boolean isopen = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
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
        
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
        
        list.add(null);


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
        Fragment newFragment = null;
        Bundle args = new Bundle();
        switch (position) {
		case 0:
			newFragment = new LoginFragment();/* new 一个子fragment */
	        args.putInt(LoginFragment.ARG_SHOW_FRAGMENT,position);
			break;
		case 1:
			newFragment = new MainFragment();/* new 一个子fragment */
	        args.putInt(LoginFragment.ARG_SHOW_FRAGMENT,position);
			break;
			
		default:
			break;
		}
        
        newFragment.setArguments(args);/* 装载数据 */
        /* 替换当前fragment */
        getSupportFragmentManager().beginTransaction().replace(R.id.root_framelayout,newFragment).commit();

        /* 最后关闭左侧抽屉 */
        drawerLayout.closeDrawer(leftDrawer);
    }
}
