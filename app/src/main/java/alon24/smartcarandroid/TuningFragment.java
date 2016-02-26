package alon24.smartcarandroid;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import alon24.smartcarandroid.utils.CarMoveListener;

/**
 * Created by iklein on 24/02/16.
 */
public class TuningFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private static String CAR_COMMAND = "Move tune";
    private String FREQ_COMMAND = "Move freq";
    CarMoveListener mCallback;

    SeekBar freqBar, pwrBar;
    TextView freqText, pwrText;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tune_fragment, container, false);

        pwrBar = (SeekBar) view.findViewById(R.id.pwrSeekeBar);
        freqBar = (SeekBar) view.findViewById(R.id.freqSeekeBar);
        freqText = (TextView) view.findViewById(R.id.freqText);
        pwrText = (TextView) view.findViewById(R.id.pwrText);
        pwrBar.setOnSeekBarChangeListener(this);
        freqBar.setOnSeekBarChangeListener(this);
        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(this);
        updateUI();

        return view;
    }

    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freqText.setText(freqBar.getProgress());
                pwrText.setText(pwrBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stopBtn) {
            String cmd = String.format("%s %d %d", CAR_COMMAND, 0, 0);
            mCallback.onCarChangeDirectionCommand(cmd);
        } else  if (v.getId() == R.id.resetBtn) {
//            String cmd = String.format("%s %d %d", CAR_COMMAND, 0, 0);
//            mCallback.onCarChangeDirectionCommand(cmd);
        } else  if (v.getId() == R.id.currentCarParamsBtn) {
//            String cmd = String.format("%s %d %d", CAR_COMMAND, 0, 0);
//            mCallback.onCarChangeDirectionCommand(cmd);
        }
    }

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
}
