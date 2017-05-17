package com.hust.zp.ofdm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btn;
    private OfdmTransmitter ofdmTransmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.text);
        btn = (Button)findViewById(R.id.btnTran);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnTran:
                ofdmTransmitter =new OfdmTransmitter(editText.getText().toString());
                ofdmTransmitter.MsgToByte();
                ofdmTransmitter.ByteToBit();
                ofdmTransmitter.grayCode();
                break;
            default:
                break;
        }
    }

}
