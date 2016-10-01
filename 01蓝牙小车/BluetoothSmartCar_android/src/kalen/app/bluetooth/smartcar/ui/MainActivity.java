package kalen.app.bluetooth.smartcar.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import kalen.app.bluetooth.smartcar.view.MathUtils;
import kalen.app.bluetooth.smartcar.view.Rudder;
import kalen.app.bluetooth.smartcar.view.Rudder.RudderListener;
import kalen.app.bluetooyh.smartcar.R;



public class MainActivity extends Activity implements SensorEventListener{
    /** Called when the activity is first created. */

	
	private final static int REQUEST_CONNECT_DEVICE = 1;
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //蓝牙SPP的UUID
	public static TextView tvwMode;
	public static Rudder rudder;	
	private SensorManager mSensorMgr; 
    private Sensor mSensor;
    private BluetoothDevice _device = null;     //蓝牙设备
    private BluetoothSocket _socket = null;      //蓝牙的socket
    boolean _discoveryFinished = false;    
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    
    private boolean isStop = true;//小车是否停止
    private int AngleDeviation = 1;//小车的角度
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //设置横屏
        if(this.getResources().getConfiguration().orientation ==
        		Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       }
        
        tvwMode=(TextView)findViewById(R.id.tvw_mode);
        tvwMode.setText("摇杆控制模式");
        //获取传感器
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);  
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY  
        mSensorMgr.registerListener(this, mSensor,  
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME  
        //获取摇杆
        rudder=(Rudder)findViewById(R.id.rudder);
        rudder.setRudderListener(new RudderListener() {
            @Override
            public void onSteeringWheelChanged(int action, int angle) {
                if(action == Rudder.ACTION_RUDDER) {
                	AngleDeviation=angle;//设置角度
                }
            }
        });

        if (_bluetooth == null){
        	Toast.makeText(this, "你的设备不支持蓝牙", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // 开启蓝牙 
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start();      
    }
    
    //发送数据
    public void startSendDataThread(){
    	updateHandler.post(updateThread);
    }
    
    Handler updateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			updateHandler.post(updateThread);
		}
	};
	
	Runnable updateThread = new Runnable(){
		@Override
		public void run() {
			if(_device!=null){
				isStop = rudder.isCoincide();
				if(isStop == true){//让小车停止, 发送0
					sendDataToBluetooth(0);
				}else{ //根据旋转角度来改变小车的方向
					OutputDataByAngleChange(AngleDeviation);
				}
			}
			
			try{
				Thread.sleep(100);
			}catch(Exception e){}
			Message msg = updateHandler.obtainMessage();
			updateHandler.sendMessage(msg);
		}		
	};
	
	//发送数据给小车
	private void  sendDataToBluetooth(int data) {
		try{
    		OutputStream os = _socket.getOutputStream();
    		os.write( (byte)data );
    	}catch(IOException ex){}  
	}
	
	//根据角度发送数据给小车
	private void OutputDataByAngleChange(int angel){
		//向前
		if(AngleDeviation>45 && AngleDeviation<135){
			sendDataToBluetooth(1);
		}
		//向后
		if(AngleDeviation>225 && AngleDeviation<315){
			sendDataToBluetooth(2); 
		}
		//向左
		if(AngleDeviation<225 && AngleDeviation>135){
			sendDataToBluetooth(3);
		}
		//向右
		if(AngleDeviation<=45 || AngleDeviation >=315){
			sendDataToBluetooth(4);
		}
	}
    

    //选择了连接的蓝牙后
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     
            if (resultCode == Activity.RESULT_OK) {//连接成功
                // 获取地址
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // 获取蓝牙设备   
                _device = _bluetooth.getRemoteDevice(address);
 
                // 获取socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();
                }
                //建立socket连接
            	Button btn = (Button) findViewById(R.id.btn_connect);
                try{
                	_socket.connect();
                	Toast.makeText(this, "连接"+_device.getName()+"成功", Toast.LENGTH_SHORT).show();
                	startSendDataThread();//开始发送数据
                	btn.setText("断开链接");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
            }
    		break;
    	default:break;
    	}
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null) 
	    	try{
	    		_socket.close();
	    	}catch(IOException e){}
    	_bluetooth.disable();  //关闭蓝牙
    }
    
    
    //点击连接或断开蓝牙
    public void onConnectButtonClicked(View v){   	
    	
    	if(_bluetooth.isEnabled()==false){ 
    		Toast.makeText(this, " 蓝牙未开启...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	Button btn = (Button) findViewById(R.id.btn_connect);
    	if(_socket==null){ //点击打开蓝牙
    		Intent serverIntent = new Intent(this, DeviceListActivity.class);
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    	}else{//点击断开蓝牙
    	    try{
    	    	_socket.close();
    	    	_socket = null;
    	    	btn.setText("打开蓝牙");
    	    }catch(IOException e){}   
    	}
    	
    	return;
    }
    
    //点击模式选择
    public void onModeChooseButtonClicked(View v){
    	Intent intent = new Intent(this, ModeChooseActivity.class); 
		this.startActivity(intent);
    }
    
    //点击退出
    public void onQuitButtonClicked(View v){
    	try{
    		updateHandler.removeCallbacks(updateThread);
    	}catch(Exception ex){}
    	finish();
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	//根据传感器的值计算角度
	@Override
	public void onSensorChanged(SensorEvent e) {
		if(rudder.getViewTouchState() == true)
			return;
		
		// TODO Auto-generated method stub
		float x = e.values[SensorManager.DATA_X];        
        float y = e.values[SensorManager.DATA_Y];    
//        float z = e.values[SensorManager.DATA_Z]; 
      //设置x不要太敏感
        if(y>5)
        	y=(float) (5-2.0);
        else if(y<-5)
        	y=(float) (-5+2.0);
        else if(y<2 && y>-2)
        	y=0;
        else if(y>2)
        	y-=2.0;
        else if(y<-2)
        	y+=2.0;
        //设置x不要太敏感
        if(x>5)
        	x=(float) (5-2.0);
        else if(x<-5)
        	x=(float) (-5+2.0);
        else if(x<2 && x>-2)
        	x=0;
        else if(x>2)
        	x-=2.0;
        else if(x<-2)
        	x+=2.0;
        float temp=(float) (3.0/Math.sqrt(x*x+y*y));
        rudder.setRockerPosition(y*temp, x*temp);//设置位置
        
        if(!(x == 0 && y == 0)){
        	int xPos=(int) (y*100);
        	int yPos=(int) (x*100);
        	int angle = getAngle(xPos,yPos);
        	AngleDeviation=angle;
        }else{
        }      
        
	}
	
	 public int getAngle(int x,int y) {
	    float radian = MathUtils.getRadian(new Point(0,0), new Point(x,y));
	    int tmp = (int)Math.round(radian/Math.PI*180);
	    if(tmp < 0) {
	        return -tmp;
	    }else{
	    	return 180 + (180 - tmp);
	    }
	 }
    
}