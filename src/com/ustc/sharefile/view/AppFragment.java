package com.ustc.sharefile.view;

import com.ustc.sharefile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AppFragment extends Fragment {
	public AppFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_all, container, false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        txt_content.setText("app");
        return view;
    }
}
