package alon24.smartcarandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import alon24.smartcarandroid.utils.CarMoveListener;
import alon24.smartcarandroid.utils.RepeatListener;

/**
 * Created by iklein on 13/02/16.
 */
public class NineAxisController extends Fragment implements View.OnClickListener{
    private static String CAR_COMMAND = "Move xy";

    CarMoveListener mCallback;
    RepeatListener repeatListener;

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

        View view = inflater.inflate(R.layout.table_control_fragment, container, false);

        repeatListener = new RepeatListener(0,100, this);
        ((Button) view.findViewById(R.id.upBtn)).setOnTouchListener(repeatListener);

        ((Button)view.findViewById(R.id.downBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.leftBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.rightBtn)).setOnTouchListener(repeatListener);

        ((Button)view.findViewById(R.id.upLeftBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.upRightBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.downLeftBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.downRightBtn)).setOnTouchListener(repeatListener);
        ((Button)view.findViewById(R.id.stopBtn)).setOnTouchListener(repeatListener);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Move x%,y%
            case R.id.upBtn:
//                Log.d(TAG, "upbtn rpt listener");
                sendMoveXYMessage(0, 100);
                break;
            case R.id.downBtn:
                sendMoveXYMessage(0, -100);
                break;
            case R.id.leftBtn:
                sendMoveXYMessage(-100, 0);
                break;
            case R.id.rightBtn:
                sendMoveXYMessage(100, 0);
                break;
            case R.id.upLeftBtn:
                sendMoveXYMessage(-100, 100);
                break;
            case R.id.upRightBtn:
                sendMoveXYMessage(100, 100);
                break;
            case R.id.downRightBtn:
                sendMoveXYMessage(-100, -100);
                break;
            case R.id.downLeftBtn:
                sendMoveXYMessage(100, -100);
                break;
            case R.id.stopBtn:
                sendMoveXYMessage(0, 0);
                break;
        }
    }

    private void sendMoveXYMessage(int x, int y) {
        String cmd = String.format("%s %d %d", CAR_COMMAND, x, y);
        mCallback.onCarChangeDirectionCommand(cmd);
    }
}
