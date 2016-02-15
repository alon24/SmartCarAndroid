package alon24.smartcarandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import alon24.smartcarandroid.joystick.JoystickMovedListener;
import alon24.smartcarandroid.joystick.JoystickView;

public class JoystickActivity extends Activity {

	TextView txtX, txtY;
	JoystickView joystick;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joystick);

		txtX = (TextView)findViewById(R.id.TextViewX);
        txtY = (TextView)findViewById(R.id.TextViewY);
        joystick = (JoystickView)findViewById(R.id.joystickView);
        
        joystick.setOnJostickMovedListener(_listener);
	}

    private JoystickMovedListener _listener = new JoystickMovedListener() {

		@Override
		public void OnMoved(int pan, int tilt) {
			txtX.setText(Integer.toString(pan));
			txtY.setText(Integer.toString(tilt));
		}

		@Override
		public void OnReleased() {
			txtX.setText("stopped");
			txtY.setText("stopped");
		}
	}; 

}
