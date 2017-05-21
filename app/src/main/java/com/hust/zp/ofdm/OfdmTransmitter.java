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
    private ComplexNum[][] symbolPadding;
    private float[][] ifftData;
    private FFT fft;
    public int numSeg;

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
    @SuppressWarnings("unchecked")
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
                aList.add((float)-Math.sqrt(2)/2);
            }else {
                aList.add((float)Math.sqrt(2)/2);
            }
        }
        //Log.d(TAG, "aList: " + aList);

        //将b路（即Q相分量）数据变为双极性数据，同样乘上√2/2
        for (int i = 0; i < str2.length(); i++) {
            if (Integer.parseInt(str2.substring(i,i + 1)) == 0){
                bList.add((float)-Math.sqrt(2)/2);
            }else{
                bList.add((float)Math.sqrt(2)/2);
            }
        }
        //Log.d(TAG, "bList: " + bList);

        //将ab两路数据合并形成复数序列
        for (int i = 0; i < aList.size(); i++) {
            ComplexNum a = new ComplexNum((float)aList.get(i),(float)bList.get(i));
            list.add(a);
        }

        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "list: " + ComplexNum.Display((ComplexNum) list.get(i)));
        }
        Log.d(TAG, "list: " + list.size());
    }

    //将qpsk调制的信号分段形成symbol
    float[] QpskToSymbol(){
        //将list分段，每20个复数分成一段
        //int numSeg;//讲bits的数量除以20得到的段数
        int index = 0;
        if (list.size() % 20 == 0){
            numSeg = list.size()/20;
        }else{
            numSeg = list.size()/20 + 1;
            //numLastSeg = list.size() % 20;
        }

        Log.d(TAG, "numSeg: " + numSeg);

        //定义一个二维数组，将复数信息按顺序存储到对应的段中
        ComplexNum symbol[][] = new ComplexNum[numSeg][20];
        for (int i = 0; i < numSeg; i++) {
            for (int j = 0; j < 20; j++) {
                if (list.size() % 20 == 0){
                    symbol[i][j] = (ComplexNum) list.get(index);
                }else{
                    if (index < (numSeg-1) * 20) {
                        symbol[i][j] = (ComplexNum) list.get(index);
                    }else if(index < list.size()){
                        symbol[i][j] = (ComplexNum)list.get(index);
                    }else if (index < numSeg * 20){
                        symbol[i][j] = new ComplexNum(0,0);
                    }
                }
                index++;
            }
        }

        for (int i = 0; i < numSeg; i++) {
            Log.d(TAG, "symbol[][]: " + i);
            for (int j = 0; j < 20; j++) {
                Log.d(TAG, "symbol[][]: " + ComplexNum.Display(symbol[i][j]) + " " + i +" " + j);
            }
        }

        //在每个symbol左侧补充180个零，右侧补充280个零
        symbolPadding = new ComplexNum[numSeg][512];
        for (int i = 0; i < numSeg; i++) {
            for (int j = 0; j < 512; j++) {
                if (j < 180){
                    symbolPadding[i][j] = new ComplexNum(0,0);
                } else if (j < 200){
                    symbolPadding[i][j] = symbol[i][j-180];
                }else {
                    symbolPadding[i][j] = new ComplexNum(0,0);
                }
            }
        }

        for (int i = 0; i < numSeg; i++) {
            Log.d(TAG, "symbolPadding[][]: " + i);
            for (int j = 0; j < 512; j++) {
                //Log.d(TAG, "symbolPadding[][]: " + ComplexNum.Display(symbolPadding[i][j]) + " " + i +" " + j);
                System.out.print(ComplexNum.Display(symbolPadding[i][j]) + " ");
            }
            System.out.println("symbolPadding[]的长度" + symbolPadding[i].length);
        }


        ifftData = new float[numSeg][512];
        float[] audioData = new float[numSeg * 512];
        fft = new FFT(512);
        for (int i = 0; i < numSeg; i++) {
            Log.d(TAG, "IFFT Start" );
            fft.IFFT(symbolPadding[i]);
            //ifftData[i] = fft.magnitude(symbolPadding[i]);
        }

        Log.d(TAG, "symbolPadding: ");
        for (int i = 0; i < numSeg; i++) {
            for (int j = 100; j < 200; j++) {
                System.out.print(ComplexNum.Display(symbolPadding[i][j]) + " ");
            }
            System.out.println();
        }

        int count = 0;
        for (int i = 0; i < numSeg; i++) {
            for (int j = 0; j < 512; j++) {
                ifftData[i][j] = symbolPadding[i][j].real;
            }
        }

        for (int i = 0; i < numSeg; i++) {
            for (int j = 0; j < 512; j++) {
                audioData[count] = ifftData[i][j];
                count++;
            }

        }
        return audioData;

        /*
        Log.d(TAG, "ifftData: ");
        for (int i = 0; i < numSeg; i++) {
            for (int j = 180; j < 200; j++) {
                System.out.print(ifftData[i][j] + " ");
            }
            System.out.println();
        }

        ComplexNum[][] ifftRes = new ComplexNum[numSeg][];
        for (int i = 0; i < numSeg; i++) {
            ifftRes[i] = fft.complexLization(ifftData[i]);
        }
        Log.d(TAG, "ifftRes: " + ComplexNum.Display(ifftRes[0][0]) );


        for (int i = 0; i < numSeg; i++) {
            fft.FFT(ifftRes[i]);
        }

        Log.d(TAG, "ifftRes: ");
        for (int i = 0; i < numSeg; i++) {
            for (int j = 180; j < 200; j++) {
                System.out.print(ComplexNum.Display(ifftRes[i][j]) + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < numSeg; i++) {
            for (int j = 0; j < 512; j++) {
                System.out.print(ifftData[i][j] + " ");
                //Log.d(TAG, "ifftData: " + ifftData[i][j] + " " + j);
            }
            System.out.println();
        }*/
    }
}
