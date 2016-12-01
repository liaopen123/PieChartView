package com.xiaoziqianbao.piechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

/**
 * Created by liaopenghui on 2016/12/1.
 */

public class PieChartView extends View {
    private int mWidth;
    private int mHeight;
    private int defaultWidth;
    private int padding = 20;//圆环距离view的padding值
    private Point centerPoint ;//圆心
    private int mRadius;
    private Paint paint;
    private RectF rectF = new RectF();//圆所对应的弧

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChartView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        defaultWidth = mHeight>mWidth?mWidth:mHeight;//保证横竖屏时候手势区域以最小的为准。
         mRadius = (defaultWidth - 2 * padding) / 2;//求得圆的半径
        centerPoint = new Point(mWidth/2,mHeight/2);

        rectF.left = centerPoint.x - mRadius;// 左上角x = 圆心X-半径
        rectF.top = centerPoint.y - mRadius;// 左上角y == 圆心y-半径
        rectF.right = centerPoint.x + mRadius; // 左下角x
        rectF.bottom = centerPoint.y + mRadius; // 右下角y
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        //4个参数： 画弧对应的椭圆  起始的角度  划过的弧度，端点连圆心 还是相连  对应的paint
       canvas.drawArc(rectF,-90,90,true,paint);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);
        int pointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * 0.25*360/ 180));
        int pointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * 0.25*360 / 180));
        canvas.drawLine(centerPoint.x,centerPoint.y,pointX,pointY,paint);
    }



    /**
     * 设置进度
     *
     * @param percent 各个成分的百分比
     */
    public void setData(HashMap<Double,Color> percent) {

    }
}
