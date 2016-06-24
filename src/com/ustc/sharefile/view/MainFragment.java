package com.ustc.sharefile.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ustc.sharefile.R;


public class MainFragment extends Fragment {
	
	 public static final int PAGE_ONE = 0;
	 public static final int PAGE_TWO = 1;
	 public static final int PAGE_THREE = 2;
	 public static final int PAGE_FOUR = 3;
	 
	 private RadioGroup rg_type;
	 private RadioButton rb_all;
	 private RadioButton rb_app;
	 private RadioButton rb_picture;
	 private RadioButton rb_music;
	 private ViewPager vpager;

	 private TypeFragmentPagerAdapter mAdapter;
	 

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.home, container, false);
		
		findById(rootView);
		
		rb_all.setChecked(true);
		
		return rootView;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.home);
		mAdapter = new TypeFragmentPagerAdapter(getFragmentManager());
	}
	
	private void findById(View view){
		rg_type = (RadioGroup) view.findViewById(R.id.rg_type);
		
		rb_all = (RadioButton) view.findViewById(R.id.rb_all);
		rb_app = (RadioButton) view.findViewById(R.id.rb_app);
		rb_picture = (RadioButton) view.findViewById(R.id.rb_picture);
		rb_music = (RadioButton) view.findViewById(R.id.rb_music);
		
		rg_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
	            case R.id.rb_all:
	                vpager.setCurrentItem(PAGE_ONE);
	                break;
	            case R.id.rb_app:
	                vpager.setCurrentItem(PAGE_TWO);
	                break;
	            case R.id.rb_picture:
	                vpager.setCurrentItem(PAGE_THREE);
	                break;
	            case R.id.rb_music:
	                vpager.setCurrentItem(PAGE_FOUR);
	                break;
				}
				
			}
		});
		
		vpager = (ViewPager) view.findViewById(R.id.vpager);
		vpager.setAdapter(mAdapter);
	    vpager.setCurrentItem(0);
	    vpager.addOnPageChangeListener(new OnPageChangeListener(){
	        @Override
	        public void onPageScrollStateChanged(int state) {
	            //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
	            if (state == 2) {
	                switch (vpager.getCurrentItem()) {
	                    case PAGE_ONE:
	                        rb_all.setChecked(true);
	                        
	                        break;
	                    case PAGE_TWO:
	                        rb_app.setChecked(true);
	                        break;
	                    case PAGE_THREE:
	                        rb_picture.setChecked(true);
	                        break;
	                    case PAGE_FOUR:
	                        rb_music.setChecked(true);
	                        break;
	                }
	            }
	        }
	        
	        //重写ViewPager页面切换的处理方法
	        @Override
	        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	        }

	        @Override
	        public void onPageSelected(int position) {
	        }
		});
	}
	
	
}
