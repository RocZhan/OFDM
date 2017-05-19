package com.hust.zp.ofdm;

/**
 * Created by zhanpeng on 2017/5/17.
 */

public class ComplexNum {
    //定义实部和虚部
    public float real;
    public float imag;

    //构造函数
    public ComplexNum(){}

    public ComplexNum(float rez,float imz){
        this.real = rez;
        this.imag = imz;
    }

    public float getRez(){
        return real;
    }

    public float getImz(){
        return imag;
    }

    //复数加法
    public static ComplexNum Plus(ComplexNum a,ComplexNum b){
        ComplexNum plusResult = new ComplexNum(a.real + b.real,a.imag + b.imag);
        return plusResult;
    }

    //复数减法
    public static ComplexNum Minus(ComplexNum a,ComplexNum b){
        ComplexNum minusResult = new ComplexNum(a.real - b.real,a.imag - b.imag);
        return minusResult;
    }

    //用字符串显示复数显示
    public static String Display(ComplexNum a){
        StringBuilder sb = new StringBuilder();
        sb.append(a.getRez());
        if (a.getImz() > 0){
            sb.append("+" + a.getImz() + "i");
        }else if(a.imag < 0){
            sb.append(a.getImz() + "i");
        }
        //System.out.println(sb.toString());
        String str = sb.toString();
        return str;
    }
}
