package com.xiaoziqianbao.piechartview;

/**
 * Created by liaopenghui on 2016/12/1.
 */

public class PiePartBean {
    int color;//扇形区域的颜色
    double percent;//百分比  每个部分占总的百分比 如0.2

    int endPointX;//扇形末端的点 用来链接圆心画直线
    int endPointY;//扇形末端的点 用来链接圆心画直线
    int radius;//扇形所对圆的半径
    public  float startArc;//
    public float moveArc;//划过的角度
    public boolean drawLine = false;
}
