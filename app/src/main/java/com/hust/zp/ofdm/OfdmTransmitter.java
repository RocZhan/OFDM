package com.hust.zp.ofdm;

import android.util.Log;

/**
 * Created by zhanpeng on 2017/5/8.
 */

public class OfdmTransmitter {

    private static final String TAG = "Roc";
    private String msg;
    private byte[] msgByte;
    private String bitString = "";
    //private String[] bitGrayString;
    private String grayCode = "";
    //private int[] grayByte;

    public OfdmTransmitter(String msg){
        this.msg = msg;
    }

    //转换成字节流
    public void MsgToByte(){
        msgByte = msg.getBytes();
        Log.d(TAG, "MsgToByte.length: " + msgByte.length);
        for (Byte byt: msgByte
             ) {
            Log.d(TAG, "MsgToByte: " + byt);
        }
    }

    //转换成bit流
    public void ByteToBit(){
        for (int i = 0; i < msgByte.length; i++) {
            bitString += ""  +((msgByte[i] >> 7) & 0x1) +
                    ((msgByte[i] >> 6) & 0x1) +
                    ((msgByte[i] >> 5) & 0x1) +
                    ((msgByte[i] >> 4) & 0x1) +
                    ((msgByte[i] >> 3) & 0x1) +
                    ((msgByte[i] >> 2) & 0x1) +
                    ((msgByte[i] >> 1) & 0x1) +
                    ((msgByte[i]) & 0x1);
        }
        Log.d(TAG, "ByteToBit: " + bitString);
        Log.d(TAG, "bitStringLength: " + bitString.length());
    }
/*
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
 */

    //转换成格雷码
    public void grayCode(){
        //int[] grayByte = new int[bitString.length];
        //char[] bitMsg = bitString.toCharArray();
        StringBuilder str = new StringBuilder(bitString);
        str.deleteCharAt(bitString.length() - 1);
        str.insert(0,'0');
        for (int i = 0; i < bitString.length(); i++) {
            grayCode += "" + (Integer.parseInt(bitString.substring(i, i + 1)) ^ Integer.parseInt(str.substring(i,i + 1)));
        }
        Log.d(TAG, "GrayCode: " + grayCode);
        Log.d(TAG, "grayCodeLength: " + grayCode.length());
    }

}
