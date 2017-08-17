package com.cheng315.nfc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class ListViewForScrollView extends ListView {
	public ListViewForScrollView(Context context) {
		super(context);
	}
	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ListViewForScrollView(Context context, AttributeSet attrs,
								 int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST );
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	/**
	 * 切记
	 */
	/*//最重要的代码 也就是我们主类中的代码了 实现onTouch事件的监听
	list_view.setOnTouchListener(new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction()==MotionEvent.ACTION_UP) {
				scroll_view.requestDisallowInterceptTouchEvent(false);
			}else {
				scroll_view.requestDisallowInterceptTouchEvent(false);
			}
			return false;
		}
	});*/
}