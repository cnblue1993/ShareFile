package com.ustc.sharefile.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TypeFragmentPagerAdapter extends FragmentPagerAdapter {

	private final int PAGER_COUNT = 4;
	private AllFragment allFragment = null;
	private AppFragment appFragment = null;
	private PictureFragment pictureFragment = null;
	private MusicFragment musicFragment = null;
	
	
	public TypeFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		allFragment = new AllFragment();
		appFragment = new AppFragment();
		pictureFragment = new PictureFragment();
		musicFragment = new MusicFragment();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (arg0) {
			case MainFragment.PAGE_ONE:
				fragment = allFragment;
				break;
			case MainFragment.PAGE_TWO:
				fragment = appFragment;
				break;
			case MainFragment.PAGE_THREE:
				fragment = pictureFragment;
				break;
			case MainFragment.PAGE_FOUR:
				fragment = musicFragment;
				break;
			default:
				break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGER_COUNT;
	}
	

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

}
