package com.hust.zp.ofdm;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText editText;
    private Button btn,btnPlay,btnStop;
    private float[] audioData;
    private OfdmTransmitter ofdmTransmitter;

    private int frequence = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_FLOAT;

    private boolean isRecording,isPlaying;

    private PlayTask playTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.text);
        btn = (Button)findViewById(R.id.btnTran);
        btn.setOnClickListener(this);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnTran:
                ofdmTransmitter =new OfdmTransmitter(editText.getText().toString());
                ofdmTransmitter.MsgToByte();
                ofdmTransmitter.ByteToBit();
                ofdmTransmitter.grayCode();
                ofdmTransmitter.GrayToQpsk();
                audioData = new float[ofdmTransmitter.numSeg * 512];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        audioData=ofdmTransmitter.QpskToSymbol();
                    }
                }).start();
                break;
            case R.id.btnPlay:
                playTask = new PlayTask();
                playTask.execute();
                break;
            case R.id.btnStop:
                isPlaying = false;
            default:
                break;
        }
    }

    class PlayTask extends AsyncTask<Void, Integer, Void> {
        private String fileName;
        public PlayTask(){}

        public PlayTask(String fileName){
            this.fileName = fileName;
            //initParam();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequence, channelConfig, audioEncoding);
            float[] buffer = new float[bufferSize/4];
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                //DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                //实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, frequence, channelConfig, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                track.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                //while(isPlaying && dis.available()>0){
                    //int i = 0;
                    //while(dis.available()>0 && i<buffer.length){
                        //buffer[i] = dis.readShort();
                        //i++;
                    //}

                int num = 0;
               while (isPlaying){
                    num = track.write(audioData, 0, audioData.length,AudioTrack.WRITE_NON_BLOCKING);
                   }
                    //然后将数据写入到AudioTrack中

                Log.d(TAG, "write: " + num);
                Log.d(TAG, "playState: " + track.getPlayState());

                //}

                //播放结束
                track.stop();
                //dis.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            return null;
        }
    }

}
