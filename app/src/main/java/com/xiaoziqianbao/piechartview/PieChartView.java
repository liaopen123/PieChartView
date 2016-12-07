package com.xiaoziqianbao.piechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by liaopenghui on 2016/12/1.
 */

public class PieChartView extends View {
    private static final String TAG = "PieChartView";
    private int mWidth;
    private int mHeight;
    private int defaultWidth;
    private int padding = 20;//圆环距离view的padding值
    private Point centerPoint ;//圆心
    private int mRadius;
    private Paint paint;
    private double mIncrease = 0.01d;
    double mCurrentPercent = 0;//初始化百分比
    private RectF rectF = new RectF();//圆所对应的弧
    private ArrayList<PiePartBean> piePartBeanArrayList = new ArrayList<>();
    private static int totalArc1 = 0;
    private boolean Running = true;

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
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //4个参数： 画弧对应的椭圆  起始的角度  划过的弧度，端点连圆心 还是相连  对应的paint


        for(int i = 0;i<piePartBeanArrayList.size();i++){
           // Log.d(TAG,"i="+i+"piePartBeanArrayList.get(i).arc"+piePartBeanArrayList.get(i).arc+"piePartBeanArrayList.get(i).endPoint"+piePartBeanArrayList.get(i).endPointX);
            paint.setColor(piePartBeanArrayList.get(i).color);
            canvas.drawArc(rectF,piePartBeanArrayList.get(i).startArc,piePartBeanArrayList.get(i).moveArc,true,paint);
            Log.d(TAG,i+"..."+piePartBeanArrayList.get(i).startArc+"....."+piePartBeanArrayList.get(i).moveArc);

            //画线
//            if(piePartBeanArrayList.get(i).drawLine) {
//                paint.setStrokeWidth(10);
//                if (i == 0) {
//                    paint.setColor(Color.WHITE);
//
//                    canvas.drawLine(centerPoint.x, centerPoint.y, piePartBeanArrayList.get(i).endPointX, piePartBeanArrayList.get(i).endPointY, paint);
//                } else {
//                    paint.setColor(Color.WHITE);
//                    canvas.drawLine(centerPoint.x, centerPoint.y, piePartBeanArrayList.get(i).endPointX, piePartBeanArrayList.get(i).endPointY, paint);
//                }
//                if (i == piePartBeanArrayList.size() - 1) {
//                    canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x, centerPoint.y - mRadius, paint);
//                }
//            }
        }


if(piePartBeanArrayList.size()!=0) {///防止一进界面未设置数据 就已经画圆
    //画圆心
    paint.setColor(Color.WHITE);
    canvas.drawCircle(centerPoint.x, centerPoint.y, mRadius / 2.5f, paint);
}
    }



    /**
     * 设置进度
     *
     * @param percentMap 各个成分的百分比
     */
    public void initData(ArrayList<PartBean>percentMap) {
        for (int i= 0;i< percentMap.size() ;i++) {    //不能用foreach    它的输出是乱序的
            PiePartBean piePartBean = new PiePartBean();
            piePartBean.percent = percentMap.get(i).part;
            piePartBean.color =  percentMap.get(i).color;

            piePartBean.radius = mRadius;

            piePartBeanArrayList.add(piePartBean);
            //end点是用来不断刷新的
        }



        freshData();

    }

    private void freshData() {

       postDelayed(new Runnable() {
           public float pastTotalArc;

           @Override
           public void run() {
               mCurrentPercent+=mIncrease;
//               totalArc1 = 0;
               for(int i = 0;i<piePartBeanArrayList.size();i++) {
//                   //每个扇形part 划过的角度
//                   if(percent<piePartBeanArrayList.get(i).percent) {
//                       //如果累加百分比还没有到就
//                      Log.d(TAG,"i="+i+"还没有达到最终形态");
//                       piePartBeanArrayList.get(i).moveArc = (int)(percent*360);
//                   }else{
//                       //最终形态：扇形的区块角度 = percent*360.
//                       piePartBeanArrayList.get(i).moveArc = (int)(piePartBeanArrayList.get(i).percent*360);
//                       totalArc1+=piePartBeanArrayList.get(i).moveArc;
//                       Log.d(TAG,"i="+i+"已经达到最终形态，角度和为："+totalArc1);
//                       if(totalArc1>=360){
//                           Running = false;
//                       }else{
//                           Running = true;
//                       }
//                   }
//
//                   //每个扇形的起始角度
//                   if(i==0) {
//                       piePartBeanArrayList.get(i).startArc = -90;
//                   }else{
//                        totalArc=0;
//                       for(int j=0;j<i;j++){
//                           totalArc +=piePartBeanArrayList.get(j).moveArc;
//
//                       }
//                       piePartBeanArrayList.get(i).startArc =-90+totalArc;
//                   }


//                   if(percent<piePartBeanArrayList.get(i).percent)
//                   piePartBeanArrayList.get(i).arc =  (int)(percent*360);//不断增加每部分圆弧的弧度
//                   totalArc+=piePartBeanArrayList.get(i).arc;
//                   piePartBeanArrayList.get(i).endPointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * piePartBeanArrayList.get(i).startArc/ 180));
//                   piePartBeanArrayList.get(i).endPointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * piePartBeanArrayList.get(i).startArc/ 180));
//Log.d(TAG,"1:"+piePartBeanArrayList.get(i).arc);
//Log.d(TAG,"2:"+piePartBeanArrayList.get(i).percent);
//Log.d(TAG,"3:"+piePartBeanArrayList.get(i).radius);
//Log.d(TAG,"1:"+piePartBeanArrayList.get(i).endPointX);
//Log.d(TAG,"1:"+piePartBeanArrayList.get(i).endPointY);
//Log.d(TAG,"1:"+piePartBeanArrayList.get(i).color);
                   /**先判断satarArc  再判断moveArc*/
                   if (i == 0){
                       //第一个arc的起始角度 startArc = -90°
                       piePartBeanArrayList.get(i).startArc = -90;
                       //再判断moveArc
                       if(mCurrentPercent<piePartBeanArrayList.get(i).percent){
                           //还没有达到上限百分比
                           piePartBeanArrayList.get(i).moveArc =(float) (mCurrentPercent*360);
                           Log.d(TAG,"第"+i+"个所划角度："+piePartBeanArrayList.get(i).moveArc);
                       }else{
                           Log.d(TAG,"第"+i+"个达到上限");
                           piePartBeanArrayList.get(i).moveArc =(float) (piePartBeanArrayList.get(i).percent*360);
                           Log.d(TAG,"第"+i+"个所划角度："+piePartBeanArrayList.get(i).moveArc);
                           piePartBeanArrayList.get(i).drawLine = true;
                       }
                       piePartBeanArrayList.get(i).endPointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * piePartBeanArrayList.get(i).percent*360/ 180));
                       piePartBeanArrayList.get(i).endPointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * piePartBeanArrayList.get(i).percent*360/ 180));
                   }else{
                       //第2,3,4....part
                        pastTotalArc = 0;//之前arc所划过的角度
                     for (int j= 0;j<i;j++){
                         pastTotalArc+=piePartBeanArrayList.get(j).moveArc;
                         Log.d(TAG,"第"+i+"个所划角度："+piePartBeanArrayList.get(i).moveArc);
                     }
                       //startArc = -90°+之前arc所划过的角度总和
                       piePartBeanArrayList.get(i).startArc = -90+pastTotalArc;
                       piePartBeanArrayList.get(i).endPointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * pastTotalArc/ 180));
                   piePartBeanArrayList.get(i).endPointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * pastTotalArc/ 180));
                       //求i的moveArc
                       if(mCurrentPercent<piePartBeanArrayList.get(i).percent){
                           //还没有达到上限百分比
                           Log.d(TAG,"第"+i+"个没有达到上限");
                           piePartBeanArrayList.get(i).moveArc =(float) (mCurrentPercent*360);
                       }else{
                           Log.d(TAG,"第"+i+"个达到上限");
                           piePartBeanArrayList.get(i).moveArc =(float) (piePartBeanArrayList.get(i).percent*360);
                           float i1 = piePartBeanArrayList.get(i).moveArc + piePartBeanArrayList.get(i).startArc;
                           Log.d(TAG,"moveArc"+piePartBeanArrayList.get(i).moveArc+"startArc"+piePartBeanArrayList.get(i).startArc);
                           piePartBeanArrayList.get(i).drawLine = true;
                           Log.d(TAG,"I1"+i1);

                           if(i1>=360+(-90)){

                               //此处需要invalidate  不然会出现微小空缺。
                               invalidate();
                               Running = false;
                           }
                       }
                   }


               }

               if(Running) {
                   Log.d(TAG, "invalidate" + totalArc1);
                   invalidateAndFreshData();
               }


           }
       },6);

    }

    private void invalidateAndFreshData() {
        invalidate();
        freshData();

    }
}
