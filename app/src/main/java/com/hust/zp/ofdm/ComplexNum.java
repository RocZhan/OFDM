package com.hust.zp.ofdm;

/**
 * Created by zhanpeng on 2017/5/17.
 */

public class ComplexNum {
    //定义实部和虚部
    private double rez;
    private double imz;

    //构造函数
    public ComplexNum(){}

    public ComplexNum(double rez,double imz){
        this.rez = rez;
        this.imz = imz;
    }

    public double getRez(){
        return rez;
    }

    public double getImz(){
        return imz;
    }

    //复数加法
    public static ComplexNum Plus(ComplexNum a,ComplexNum b){
        ComplexNum plusResult = new ComplexNum(a.rez + b.rez,a.imz + b.imz);
        return plusResult;
    }

    //复数减法
    public static ComplexNum Minus(ComplexNum a,ComplexNum b){
        ComplexNum minusResult = new ComplexNum(a.rez - b.rez,a.imz - b.imz);
        return minusResult;
    }

    //用字符串显示复数显示
    public static String Display(ComplexNum a){
        StringBuilder sb = new StringBuilder();
        sb.append(a.getRez());
        if (a.getImz() > 0){
            sb.append("+" + a.getImz() + "i");
        }else if(a.imz < 0){
            sb.append(a.getImz() + "i");
        }
        //System.out.println(sb.toString());
        String str = sb.toString();
        return str;
    }
}
