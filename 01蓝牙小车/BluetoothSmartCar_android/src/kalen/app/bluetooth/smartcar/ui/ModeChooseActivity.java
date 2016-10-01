package kalen.app.bluetooth.smartcar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import kalen.app.bluetooyh.smartcar.R;

public class ModeChooseActivity extends Activity{
	Button btn_gravity;
	Button btn_byhand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mode_choose);
		
		btn_gravity=(Button)findViewById(R.id.btn_gravity);
		btn_byhand=(Button)findViewById(R.id.btn_byhand);
		
		btn_gravity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				MainActivity.tvwMode.setText("重力感应模式");
				MainActivity.rudder.setViewTouchState(false);
				finish();
			}	
		});
		
		
		btn_byhand.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				MainActivity.tvwMode.setText("摇杆控制模式");
				MainActivity.rudder.setViewTouchState(true);
				MainActivity.rudder.setRockerPosition(0, 0);
				finish();
			}	
		});
	}

}
