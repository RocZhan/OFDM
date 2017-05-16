package com.hust.zp.ofdm;

import android.util.Log;

/**
 * Created by zhanpeng on 2017/5/8.
 */

public class OfdmTransmitter {

    private static final String TAG = "Roc";
    private String msg;
    private byte[] msgByte;
    private String[] bitString;
    private String[] bitGrayString;
    private String bitMsg;
    //private int[] grayByte;

    public OfdmTransmitter(String msg){
        this.msg = msg;
    }

    public void MsgToByte(){
        msgByte = msg.getBytes();
        Log.d(TAG, "MsgToByte.length: " + msgByte.length);
        for (Byte byt: msgByte
             ) {
            Log.d(TAG, "MsgToByte: " + byt);
        }
    }

    public void ByteToBit(){
        bitString = new String[msgByte.length * 2];
        int j = 0;
        for (int i = 0; i < msgByte.length; i++) {
            //bitString[j] = "";
            bitString[j++] = "" +((msgByte[i] >> 7) & 0x1) +
                    ((msgByte[i] >> 6) & 0x1) +
                    ((msgByte[i] >> 5) & 0x1) +
                    ((msgByte[i] >> 4) & 0x1) ;

            bitString[j++] = "" +((msgByte[i] >> 3) & 0x1) +
                    ((msgByte[i] >> 2) & 0x1) +
                    ((msgByte[i] >> 1) & 0x1) +
                    ((msgByte[i]) & 0x1);
            Log.d(TAG, "bitString: " + bitMsg);
        }
    }

    public void ByteToBit(int[] byt){
        bitGrayString = new String[byt.length];
        for (int i = 0; i < byt.length; i++) {
            bitGrayString[i] = "";
            bitGrayString[i] += "" +((byt[i] >> 7) & 0x1) +
                    ((byt[i] >> 6) & 0x1) +
                    ((byt[i] >> 5) & 0x1) +
                    ((byt[i] >> 4) & 0x1) +
                    ((byt[i] >> 3) & 0x1) +
                    ((byt[i] >> 2) & 0x1) +
                    ((byt[i] >> 1) & 0x1) +
                    ((byt[i]) & 0x1);
            Log.d(TAG, "bitGrayString: " + bitGrayString[i]);
        }
    }

    public int[] GrayCode(){
        int[] grayByte = new int[bitString.length];
        for (int i = 0; i < bitString.length; i++) {
            grayByte[i] = (Byte.parseByte(bitString[i]) ^ (Byte.parseByte(bitString[i]) >> 1));
            Log.d(TAG, "GrayCode: " + grayByte[i]);
        }
        return grayByte;
    }

}
