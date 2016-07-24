package com.yluo.customseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class YluoSeekBar extends SeekBar implements OnSeekBarChangeListener {

	private static final String TAG = "YluoSeekBar";

	private float mThumbMinRadius = 5;

	private float mThumbMaxRadius = 7;

	private float mThumbRadius;

	private int mThumbColor = 0xff51CCFF;

	private int mDeterminProgressColor = 0xff51CCFF;

	private int mIndeterminProgressBKColor = 0xffD8D8D8;

	private float mProgressBarHeight = 4;

	private float mDrawProgressMaxSpan = 0;

	private float mDrawThumbMaxSpan = 0;

	private Paint paint;

	public YluoSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public YluoSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoSeekBar(Context context) {
		super(context);
		init();
	}

	private void init() {

		mThumbMinRadius = DpTranToPx.dp2px(getContext(), mThumbMinRadius);

		mThumbMaxRadius = DpTranToPx.dp2px(getContext(), mThumbMaxRadius);

		mThumbRadius = mThumbMinRadius;

		setOnSeekBarChangeListener(this);

		paint = PaintUtil.createPaint();

	}

	public void setmDeterminProgressColor(int mDeterminProgressColor) {

		this.mDeterminProgressColor = mDeterminProgressColor;
	}

	public void setmIndeterminProgressColor(int mIndeterminProgressColor) {

		this.mIndeterminProgressBKColor = mIndeterminProgressColor;
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mDrawThumbMaxSpan = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();

		// 这个是背景条单位

		mDrawProgressMaxSpan = mDrawThumbMaxSpan - mThumbRadius * 2;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!isTouchDownOnThumb(event)) {
				return false;
			}
		}
		return super.onTouchEvent(event);
	}

	private boolean isTouchDownOnThumb(MotionEvent event) {
		
		float curMaxTouchProgress = PositionToProgress(event.getX()+ mThumbRadius);
		
		float curMinTouchProgress = PositionToProgress(event.getX()- mThumbRadius);
		
		Log.d(TAG, "curMaxTouchProgress:" + curMaxTouchProgress 
				+ ",curMinTouchProgress:" + curMinTouchProgress);
		
		if(curMaxTouchProgress <= getProgress() && curMinTouchProgress>= getProgress()) {
			return true;
		}
		
//		if (curTouchProgress <= (getProgress() + mThumbRadius)
//				&& curTouchProgress >= (getProgress() - mThumbRadius)) {
//			return true;
//		}
		
//		if (curTouchProgress <= (getProgress() + 1)
//				&& curTouchProgress >= (getProgress() - 1)) {
//			return true;
//		}

		return false;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		drawProgress(canvas);

		drawThumb(canvas);
	}

	private void drawThumb(Canvas canvas) {

		paint.setColor(mThumbColor);

		canvas.drawCircle(getThumDrawPosition(), getMeasuredHeight() / 2,
				mThumbRadius, paint);
	}

	// 获取Thumb的绘制位置
	private float getThumDrawPosition() {
		return getPaddingLeft() + (getProgress() * 1.0f / getMax())
				* mDrawThumbMaxSpan;
	}

	private void drawProgress(Canvas canvas) {
		drawDeterminProgress(canvas);
		drawIndeterminProgress(canvas);
	}

	private void drawDeterminProgress(Canvas canvas) {

		paint.setColor(mDeterminProgressColor);

		canvas.drawRect(getDeterStartDrawPosition(), getDrawPregrossStartY(),
				getThumDrawPosition(), getDrawPregrossStartY()
						+ mProgressBarHeight, paint);

	}

	private void drawIndeterminProgress(Canvas canvas) {
		paint.setColor(mIndeterminProgressBKColor);

		canvas.drawRect(getThumDrawPosition(), getDrawPregrossStartY(),
				getIndeterEndDrawPosition(), getDrawPregrossStartY()
						+ mProgressBarHeight, paint);
	}

	private float getDrawPregrossStartY() {
		return (getMeasuredHeight() - mProgressBarHeight) / 2;
	}

	private float getDeterStartDrawPosition() {

		return getPaddingLeft();
	}

	private float getIndeterEndDrawPosition() {

		return getPaddingLeft() + mThumbRadius * 2 + mDrawProgressMaxSpan;
	}

	private float PositionToProgress(float touchDownX) {
		final int width = getWidth();
		final int available = width - getPaddingLeft() - getPaddingRight();
		final int x = (int) touchDownX;
		float scale;
		float progress = 0;

		if (x < getPaddingLeft()) {
			scale = 0.0f;
		} else if (x > width - getPaddingRight()) {
			scale = 1.0f;
		} else {
			scale = (float) (x - getPaddingLeft()) / (float) available;
		}
		final int max = getMax();
		progress += scale * max;

		return progress;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		Log.d(TAG, "-----------------");

		mThumbRadius = mThumbMaxRadius;

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mThumbRadius = mThumbMinRadius;
	}

}
