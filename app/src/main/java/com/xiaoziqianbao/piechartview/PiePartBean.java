package com.xiaoziqianbao.piechartview;

/**
 * Created by liaopenghui on 2016/12/1.
 */

public class PiePartBean {
    int color;//扇形区域的颜色
    double percent;//百分比  每个部分占总的百分比

    double totalPercent;//总的百分比  比如i= 1的part 占2%  1的totalPercent = 2%，i= 2的part 占4% ,2的totalPercent = 6%，就是当前part加上后之前part的百分数    最后一个part累加为100%   这行可以得到每个条状的动态位置
    int endPointX;//扇形末端的点 用来链接圆心画直线
    int endPointY;//扇形末端的点 用来链接圆心画直线
    int radius;//扇形所对圆的半径
    public  int startArc;//
    public int moveArc;//划过的角度
    public boolean drawLine = false;
}
