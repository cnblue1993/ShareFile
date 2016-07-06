package com.ustc.sharefile.view;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ustc.sharefile.R;


public class LoginFragment extends Fragment {
	public static final String ARG_SHOW_FRAGMENT = "login";

	private EditText et_account;
	private EditText et_pwd;
	private Button btn_login;
	private Button btn_register;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.login, container, false);
		ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		findById(view);
		
		return view;
	}
	
	private void findById(View view){
		et_account = (EditText) view.findViewById(R.id.et_account);
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_register = (Button) view.findViewById(R.id.btn_register);
	
		btn_login.setOnClickListener(clickListener);
		btn_register.setOnClickListener(clickListener);
	}
	
	public OnClickListener clickListener = new OnClickListener() {  
		  
        public void onClick(View v) {
        	boolean isLogin = true;
			
			boolean flag1 = checkFormat(et_account.getText().toString());
			boolean flag2 = checkFormat(et_pwd.getText().toString());
			
			SharedPreferences preferences = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        	Editor editor=preferences.edit();
        	editor.putString("userName", et_account.getText().toString());
			editor.putString("pwd", et_pwd.getText().toString());
            switch (v.getId()) {
				case R.id.btn_login:
					editor.putInt("loginState", 1);
					break;

				case R.id.btn_register:
					editor.putInt("loginState", 1);
					isLogin = false;
					break;
            } 
            if(flag1 && flag2){
            	
            	editor.commit();
            }
            else{
            	if(!flag1)
            		et_account.setError("输入内容 不符合规则");
            	if(!flag2)
            		et_pwd.setError("输入内容 不符合规则");
            }
        }
    };
    private boolean checkFormat(String string){
    	String regex = "^[a-zA-Z0-9]+$";
    	return string.length()>0 && string.matches(regex);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:// 点击返回图标事件
            getActivity().finish();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
