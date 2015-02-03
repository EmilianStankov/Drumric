package com.emilianstankov.drumric;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class SquareButton extends Button {

	public SquareButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SquareButton(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public SquareButton(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
	}

}
