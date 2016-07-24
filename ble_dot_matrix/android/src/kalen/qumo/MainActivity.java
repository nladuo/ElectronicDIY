package kalen.qumo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kalen.qumo.view.QumoSurfaceView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
	private static final String FINISH_CONNECT_ACTION = "kalen.bluetoothClient.finishConnect";
	private static final int MENU_CONNECT = 0;  
	
	QumoSurfaceView qumoSurfaceView;
	private List<Integer> list;
	public static BluetoothDevice bluetoothDevice = null;     //蓝牙设备
    public static BluetoothSocket bluetoothSocket = null;      //蓝牙通信socket
    
    MenuItem connectItem = null;
    
    public static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    public static ActionBar actionBar = null;
    private ProgressDialog pd = null;
    private ArrayAdapter< String> mDevicesArrayAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qumoSurfaceView = new QumoSurfaceView(this);
        setContentView(qumoSurfaceView);
        list = new ArrayList<Integer>();
        
        //设置标题栏
        actionBar = getActionBar();
        actionBar.setSubtitle("未连接");
        
      //注册广播
        registerMyReciever();

       //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (bluetoothAdapter == null){
        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // 设置设备可以被搜索  
       new Thread(){
    	   public void run(){
    		   if(bluetoothAdapter.isEnabled()==false){
        		bluetoothAdapter.enable();
    		   }
    	   }
       }.start();   
        
    }
    
    private void registerMyReciever(){
    	// 注册接收查找到设备action接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // 注册查找结束action接收器
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        //注册蓝牙断开接收器
      	filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
      	this.registerReceiver(mReceiver, filter);
		//注册用户选择BluetoothDevice的接收器
		filter.addAction(FINISH_CONNECT_ACTION);
		this.registerReceiver(mReceiver, filter);
    }
    
 // 查找到设备和搜索完成action监听器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 查找到设备action
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 得到蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                if (mDevicesArrayAdapter.getCount() == 1) {
                	pd.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    				builder.setTitle(R.string.app_name);
    				builder.setAdapter(mDevicesArrayAdapter, new OnClickListener() {
    					
    					@Override
    					public void onClick(DialogInterface dialog, int which) {
    						bluetoothAdapter.cancelDiscovery();
    			            // 得到mac地址
    						String  info = mDevicesArrayAdapter.getItem(which);
    			            String address = info.substring(info.length() - 17);
    			            // 得到蓝牙设备句柄      
    		                bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
    		                Intent intent = new Intent();
    		    			intent.setAction(FINISH_CONNECT_ACTION);
    		    			MainActivity.this.sendBroadcast(intent);
    					}
    				});
    				builder.create().show();
    				return;
				}
                mDevicesArrayAdapter.notifyDataSetChanged();
            // 搜索完成action
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mDevicesArrayAdapter.getCount() == 0) {
                	pd.dismiss();
                	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    				builder.setTitle(R.string.app_name);
    				builder.setMessage("没有找到新设备");
    				builder.create().show();
                    return;
                }
            }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            	actionBar.setSubtitle("未连接");
            	//关闭连接socket
        	    try{
        	    	bluetoothSocket.close();
        	    	bluetoothSocket = null;
        	    	connectItem.setTitle("连接设备");
        	    }catch(IOException e){}  
        	    Toast.makeText(MainActivity.this, "蓝牙已断开", Toast.LENGTH_LONG).show();
			}else if(FINISH_CONNECT_ACTION.equals(action)) {
            		 
	                // 用服务号得到socket
	                try{
	                	bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
	                }catch(IOException e){
	                	Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
	                }

	                try{
	                	bluetoothSocket.connect();
	                	Toast.makeText(MainActivity.this, "连接"+bluetoothDevice.getName()+"成功！", Toast.LENGTH_SHORT).show();
	                	actionBar.setSubtitle("已连接"+bluetoothDevice.getName());
	                	connectItem.setTitle("断开设备");
	                }catch(IOException e){
	                	try{
	                		Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
	                		actionBar.setSubtitle("未连接");
	                		bluetoothSocket.close();
	                		bluetoothSocket = null;
	                	}catch(IOException ee){
	                		Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
	                		actionBar.setSubtitle("未连接");
	                	}
	                	return;
	                }
	                
			}
        }
    };


    @SuppressLint("InlinedApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        connectItem=menu.add(0,MENU_CONNECT,0,"连接设备"); 
        connectItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//主要是这句话   
        connectItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {  
            @Override  
            public boolean onMenuItemClick(MenuItem item) {  
                switch (item.getItemId()) {  
                case MENU_CONNECT:  
                	if(bluetoothAdapter.isEnabled()==false){
                		Toast.makeText(MainActivity.this, "Open BT......", Toast.LENGTH_LONG).show();
                		return true;
                	}
                	if(bluetoothSocket != null){
                		//先注销接收器
                		unregisterReceiver(mReceiver);
                		actionBar.setSubtitle("未连接");
                		connectItem.setTitle("连接");
                    	//关闭连接socket
                	    try{
                	    	bluetoothSocket.close();
                	    	Thread.sleep(30);
                	    	bluetoothSocket = null;
                	    }
                	    catch(IOException e){} 
                	    catch (InterruptedException e) {}  
                	    break;
                	}
                	mDevicesArrayAdapter = null;
                	// 初使化设备存储数组
                	mDevicesArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.device_name);
                	bluetoothAdapter.startDiscovery();
                	pd = new ProgressDialog(MainActivity.this);
    				pd.setTitle(R.string.app_name);
    				pd.setMessage("正在搜索周围的蓝牙设备...");
    				pd.setIndeterminate(true);
    				pd.setCancelable(false);
    				pd.show();
                    break;  
                default:  
                    break;  
                }  
                return false;  
            }  
    });
        return true;
    }
    
    
    
    class runThread implements Runnable {
		@Override
		public void run() {
			if(bluetoothDevice==null){
	    		Toast.makeText(MainActivity.this, "蓝牙未配对", 
	    				Toast.LENGTH_SHORT).show();
	    		return;
	    	}
			listOperate();
			blueToothSendData();
		}

	}
    
    /**
     * 存放32个8位数据
     */
    private void listOperate(){
    	list.clear();
    	for(int i = 0; i<32 ;i++){
    		list.add(rectArrayOperate(i));
    	}
    }

    /**
     * 取模第几位数据
     * @param row
     * @return
     */
    private int rectArrayOperate(int row){
    	int startRow = 0;
    	int startLine =0;
    	int temp = 0;
    	startLine = row % 16;
    	if(row > 15){
    		startRow = 0;
    	}else{
    		startRow = 8;
    	}

    	for(int i = 7; i >= 0; i--){
    		if(!qumoSurfaceView.rectFlags[startLine][startRow + i]){
    			temp += Math.pow(2, i);
    		}
    	}
    	return temp;
    }
    
    @Override
	protected void onDestroy() {
    	super.onDestroy();
    	if(bluetoothSocket!=null)  //关闭连接socket
    	try{
    		bluetoothSocket.close();
    	}catch(IOException e){}
    	bluetoothAdapter.disable();  //关闭蓝牙服务
	}


	private void blueToothSendData(){
    	if(list.size() == 0){
    		Toast.makeText(this, "internal error", 
    				Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//发送头码
    	/*try {
			OutputStream os = bluetoothSocket.getOutputStream();   //蓝牙连接输出流
			os.write(0XAA);
			os.write(0XAB);
		} catch (IOException e) {
			Toast.makeText(this, "发送数据失败", 
					Toast.LENGTH_LONG).show();
		}*/
    	for(int i = 0; i < 32;i++){
    		try {
    			OutputStream os = bluetoothSocket.getOutputStream();   //蓝牙连接输出流
				os.write(list.get(i).byteValue());
			} catch (IOException e) {
				Toast.makeText(this, "发送数据失败", 
						Toast.LENGTH_LONG).show();
			}
    	}
    }
    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
		case R.id.msg_send:
			new Thread(new runThread()).start();
			break;
			
		case R.id.change_color:
			qumoSurfaceView.paintStyle = !qumoSurfaceView.paintStyle; 
			break;
			
		case R.id.screen_clear:
			qumoSurfaceView.screenClear();
			qumoSurfaceView.rectFlagInit();
			break;
			
		case R.id.exit:
			finish();
			break;
		
		}
		
		return super.onOptionsItemSelected(item);
	}
}