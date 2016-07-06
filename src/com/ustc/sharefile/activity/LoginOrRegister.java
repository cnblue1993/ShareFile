package com.ustc.sharefile.activity;


import com.ustc.sharefile.R;

import android.R.bool;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginOrRegister extends Activity {
	private EditText et_account;
	private EditText et_pwd;
	private Button btn_login;
	private Button btn_register;
	
	private String userName;
	private int loginState;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		et_account = (EditText)findViewById(R.id.et_account);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_register = (Button)findViewById(R.id.btn_register);
	
		btn_login.setOnClickListener(clickListener);
		btn_register.setOnClickListener(clickListener);
		
	}
	
	public OnClickListener clickListener = new OnClickListener() {  
		  
        public void onClick(View v) {
        	boolean isLogin = true;
        	
        	Intent intent = new Intent(LoginOrRegister.this, MainActivity.class);
			boolean flag1 = checkFormat(et_account.getText().toString());
			boolean flag2 = checkFormat(et_pwd.getText().toString());
			
			SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
        	Editor editor=preferences.edit();
        	editor.putString("userName", et_account.getText().toString());

            if(flag1 && flag2){
            	
            	editor.commit();
            	startActivity(intent);
            }
            else{
            	if(!flag1)
            		et_account.setError("输入内容 不符合规则");
            	if(!flag2)
            		et_pwd.setError("输入内容 不符合规则");
            }
        }
    };
	
    boolean checkFormat(String string){
    	String regex = "^[a-zA-Z0-9]+$";
    	return string.length()>0 && string.matches(regex);
    }
	
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:// 点击返回图标事件
	            this.finish();
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

}
