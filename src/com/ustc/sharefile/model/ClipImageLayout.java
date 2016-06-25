package com.ustc.sharefile.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.ustc.sharefile.R;

public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private float mHorizontalPadding;
	private float mVerticalPadding;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
		//mZoomImageView.setImageDrawable(getResources().getDrawable(R.drawable.user));
		mZoomImageView.setImageDrawable(null);
		
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 计算padding的px
//		mHorizontalPadding = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
//						.getDisplayMetrics());
		
		
//		mZoomImageView.setHorizontalPadding(460.625f);
//		mZoomImageView.setVerticalPadding(739.375f);
//		mClipImageView.setHorizontalPadding(460.625f);
//		mClipImageView.setVerticalPadding(739.375f);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(float mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}
	public void setVerticalPadding(float mVerticalPadding){
		this.mVerticalPadding = mVerticalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}
	
	public void initImageDrawable(Drawable picture){
		mZoomImageView.setImageDrawable(picture);
	}
	
	public void initClipBorder(float width, float height){
		mClipImageView.setmWidth(width);
		mClipImageView.setmHeight(height);
		//初始化时getwidth()为0 ，未解决
		mZoomImageView.setHorizontalPadding((getWidth() - width)/2.0f);
		mClipImageView.setHorizontalPadding((getWidth() - width)/2.0f);
		mZoomImageView.setVerticalPadding((getHeight() - height)/2.0f);
		mClipImageView.setVerticalPadding((getHeight() - height)/2.0f);
		
		System.out.println("layout:"+getWidth());
		System.out.println("layout:"+getHeight());
		
		mClipImageView.postInvalidate();
	}
	

}
