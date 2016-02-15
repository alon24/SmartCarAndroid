package alon24.smartcarandroid;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import alon24.smartcarandroid.joystick.JoystickMovedListener;
import alon24.smartcarandroid.joystick.JoystickView;
import alon24.smartcarandroid.utils.CarMoveListener;

/**
 * Created by iklein on 13/02/16.
 */
public class JoystickFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private String CAR_COMMAND = "Move xyJoystick";
    private String FREQ_COMMAND = "Move freq";
    CarMoveListener mCallback;
    JoystickView joystick;
    TextView txtX, txtY, freqPwrLab;
    SeekBar freqBar, pwrBar;
    Button stopBtn;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (CarMoveListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CarMoveListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.joystick_fragment, container, false);
        joystick = (JoystickView) view.findViewById(R.id.joystickView);
        txtX = (TextView) view.findViewById(R.id.TextViewX);
        txtY = (TextView) view.findViewById(R.id.TextViewY);

        joystick.setOnJostickMovedListener(_listener);

        pwrBar = (SeekBar) view.findViewById(R.id.PWRSeekBar);
        freqBar = (SeekBar) view.findViewById(R.id.freqSeekeBar);
        freqPwrLab = (TextView) view.findViewById(R.id.freqPwrLab);
        pwrBar.setOnSeekBarChangeListener(this);
        freqBar.setOnSeekBarChangeListener(this);
        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(this);
        updateUI();
        return view;
    }

    private JoystickMovedListener _listener = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            txtX.setText(Integer.toString(pan));
            txtY.setText(Integer.toString(tilt));
            String cmd = String.format("%s %d %d", CAR_COMMAND, pan, tilt);
            mCallback.onCarChangeDirectionCommand(cmd);
        }

        @Override
        public void OnReleased() {
            txtX.setText("stopped");
            txtY.setText("stopped");
            String cmd = String.format("%s %d %d", CAR_COMMAND, 0, 0);
            mCallback.onCarChangeDirectionCommand(cmd);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateUI();
        String cmd = String.format("%s 4 %d %d", FREQ_COMMAND, freqBar.getProgress(), pwrBar.getProgress());
        mCallback.onCarChangeDirectionCommand(cmd);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freqPwrLab.setText(String.format("Freq is %dHz, power is %d", freqBar.getProgress(), pwrBar.getProgress()));
            }
        });

    }

    @Override
    public void onClick(View v) {
        String cmd = String.format("%s %d %d", CAR_COMMAND, 0, 0);
        mCallback.onCarChangeDirectionCommand(cmd);
    }
}