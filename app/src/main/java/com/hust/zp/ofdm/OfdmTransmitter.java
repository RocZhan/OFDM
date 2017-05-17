package com.hust.zp.ofdm;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    private List list = new ArrayList();

    OfdmTransmitter(String msg){
        this.msg = msg;
    }

    //转换成字节流
    void MsgToByte(){
        msgByte = msg.getBytes();
        Log.d(TAG, "MsgToByte.length: " + msgByte.length);
        for (Byte byt: msgByte
             ) {
            Log.d(TAG, "MsgToByte: " + byt);
        }
    }

    //转换成bit流
    void ByteToBit(){
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
    void grayCode(){
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

    //进行qpsk调制
    void GrayToQpsk(){
        //将grayCode分为两路，a路为str1，b路为str2
        String str1 = "";
        String str2 = "";
        char[] ch = grayCode.toCharArray();
        List aList = new ArrayList();
        List bList = new ArrayList();

        /*
        for (char cha: ch
             ) {
            Log.d(TAG, "ch: " + cha);
        }
        */
        for (int i = 0; i < ch.length-1; i+=2) {
            str1 += ch[i];
            str2 += ch[i+1];
        }
        Log.d(TAG, "str1: " + str1);
        Log.d(TAG, "str2: " + str2);

        //将a路（即I相分量）数据变为双极性数据（1不变，0变为1），然后乘上√2/2
        for (int i = 0; i < str1.length(); i++) {
            if (Integer.parseInt(str1.substring(i,i + 1)) == 0){
                aList.add(-Math.sqrt(2)/2);
            }else {
                aList.add(Math.sqrt(2)/2);
            }
        }
        Log.d(TAG, "aList: " + aList);

        //将b路（即Q相分量）数据变为双极性数据，同样乘上√2/2
        for (int i = 0; i < str2.length(); i++) {
            if (Integer.parseInt(str2.substring(i,i + 1)) == 0){
                bList.add(-Math.sqrt(2)/2);
            }else{
                bList.add(Math.sqrt(2)/2);
            }
        }
        Log.d(TAG, "bList: " + bList);

        //将ab两路数据合并形成复数序列
        for (int i = 0; i < aList.size(); i++) {
            ComplexNum a = new ComplexNum((double)aList.get(i),(double)bList.get(i));
            list.add(a);
        }

        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "list: " + ComplexNum.Display((ComplexNum) list.get(i)));
        }
        Log.d(TAG, "list: " + list.size());
    }
}
