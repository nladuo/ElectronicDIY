package kalen.qumo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class QumoSurfaceView extends SurfaceView implements 
	SurfaceHolder.Callback,OnTouchListener{
	private float screenWidth;
	private float rectWidth;
	private final float startX = 10;
	private final float startY = 20;
	public static final boolean PAINT_RED = true;
	public static final boolean PAINT_BLACK = true;
	public boolean paintStyle; 
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private RectF[][] rectFs = null;
	public boolean[][] rectFlags = null;
	public QumoSurfaceView(Context context) {		
		this(context, null);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	public QumoSurfaceView(Context context, AttributeSet as) {
		super(context,as);
		setOnTouchListener(this);
		surfaceHolder= this.getHolder();
		surfaceHolder.addCallback(this);
		setFocusable(true);
        setFocusableInTouchMode(true);
	}
	
	void rectInit(){
		rectFs=new RectF[16][16];
		rectFlags = new boolean[16][16];
		float left = 0;
		float top = 0;
		float right = 0;
		float bottom = 0;
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				left = startX + i*rectWidth;
				right = left + rectWidth;
				top = startY + j*rectWidth;
				bottom = top + rectWidth;
				rectFs[i][j] = new RectF(left, top, right, bottom);
				rectFlags[i][j] = false;
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		paintStyle = PAINT_RED;
		screenWidth = this.getWidth();
		rectWidth = (screenWidth-2*startX)/16.0f;
		rectInit();
		screenClear();
		
	}
	
	public void rectFlagInit(){
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				rectFlags[i][j] = false;
			}
		}
	}
	
	/**
	 * 屏幕清屏
	 */
	public void screenClear(){
		canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint();// new一个画笔
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				canvas.drawRect(rectFs[i][j], paint);
			}
		}
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				canvas.drawRect(rectFs[i][j], paint);
			}
		}
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			if(event.getX()>startX 
					&& event.getX()<startX+16*rectWidth
					&& event.getY()>startY
					&& event.getY()<startY+16*rectWidth){
						new Thread(new DrawThread(event.getX(),
								event.getY())).start();
					}
		}
		return true;
	}
	
	class DrawThread implements Runnable{
		private float inputX;
		private float inputY;
		public DrawThread(float inputX,float inputY) {
			this.inputX = inputX;
			this.inputY = inputY;
		}
		@Override
		public void run() {
			rectOperation(inputX, inputY);
			onDrawCallBack();
		}
		
	}
	
	/**
	 * 
	 */
	void onDrawCallBack(){
		canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.WHITE);

		Paint paintBlack = new Paint();// new一个画笔
		paintBlack.setColor(Color.BLACK);
		paintBlack.setStyle(Style.FILL);
		
		Paint paintRed = new Paint();
		paintBlack.setColor(Color.RED);
		paintBlack.setStyle(Style.FILL);
		
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				if(rectFlags[i][j]){
					canvas.drawRect(rectFs[i][j], paintBlack);
				}else{
					canvas.drawRect(rectFs[i][j], paintRed);
				}
				
			}
		}
		paintBlack.setColor(Color.WHITE);
		paintBlack.setStyle(Style.STROKE);
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				canvas.drawRect(rectFs[i][j], paintBlack);
			}
		}
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	/**
	 * 更改数组
	 * @param inputX
	 * @param inputY
	 */
	void rectOperation(float inputX,float inputY){
		int numX = (int) ((int) (inputX - startX)/rectWidth);
		int numY = (int) ((int) (inputY - startY)/rectWidth);
		rectFlags[numX][numY] = paintStyle;
	}

}