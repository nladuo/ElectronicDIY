package kalen.app.bluetooth.smartcar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class Rudder extends SurfaceView implements Runnable,Callback{

	private boolean isTouchable = true;
    private SurfaceHolder mHolder;
    private boolean isStop = false;
    private Thread mThread;
    private Paint  mPaint;
    private Point  mRockerPosition; //摇杆中心的位置
    private Point  mCtrlPoint = new Point(380,380);//中心距离
    private int    mRudderRadius = 80;//摇杆的半径
    private int    mWheelRadius = 300;//摇杆的活动半径
    private RudderListener listener = null; //监听器
    public static final int ACTION_RUDDER = 1 , ACTION_ATTACK = 2; //
    
    public Rudder(Context context) {
        super(context);
    }
    
    public Rudder(Context context, AttributeSet as) {
        super(context, as);
        this.setKeepScreenOn(true);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new Thread(this);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mRockerPosition = new Point(mCtrlPoint);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
    }
    
    public void setRudderListener(RudderListener rockerListener) {
        listener = rockerListener;
    }
    

    @Override
    public void run() {
        Canvas canvas = null;
        while(!isStop) {
            try {
                canvas = mHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
                mPaint.setColor(0x70808080);
                canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, mWheelRadius, mPaint);//���Ʒ�Χ
                mPaint.setColor(0x70ff0000);
                canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRudderRadius, mPaint);//����ҡ��
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isStop = true;
    }
    
    public void setViewTouchState(boolean touch_config){
    	isTouchable = touch_config;
    }
    
    public boolean getViewTouchState(){
    	return isTouchable;
    }
    //设置位置
    public void setRockerPosition(float x,float y){
    	int xPos=mCtrlPoint.x+(int)(x*100);
    	int yPos=mCtrlPoint.y+(int)(y*100);
    	mRockerPosition.set(xPos,yPos);
    }
    
    public boolean isCoincide(){
    	
    	if( (mRockerPosition.x == mCtrlPoint.x) && (mRockerPosition.y == mCtrlPoint.y) ){
    		return true;
    	}else{
    		return false;
    	}    	
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(isTouchable == false)
    		return false;
    	
        int len = MathUtils.getLength(mCtrlPoint.x, mCtrlPoint.y, event.getX(), event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(len >mWheelRadius) {
                return true;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(len <= mWheelRadius) {
                mRockerPosition.set((int)event.getX(), (int)event.getY());
                
            }else{
                mRockerPosition = MathUtils.getBorderPoint(mCtrlPoint,
new Point((int)event.getX(), (int)event.getY()), mWheelRadius);
            }
            if(listener != null) {
                float radian = MathUtils.getRadian(mCtrlPoint, new Point((int)event.getX(), (int)event.getY()));
                listener.onSteeringWheelChanged(ACTION_RUDDER,Rudder.this.getAngleCouvert(radian));
            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            mRockerPosition = new Point(mCtrlPoint);
        }
        return true;
    }
    
    public int getAngleCouvert(float radian) {
        int tmp = (int)Math.round(radian/Math.PI*180);
        if(tmp < 0) {
            return -tmp;
        }else{
            return 180 + (180 - tmp);
        }
    }
    
    public interface RudderListener {
        void onSteeringWheelChanged(int action,int angle);
    }
}