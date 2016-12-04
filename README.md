# PieChartView
饼状图View
![image](https://github.com/liaopen123/PieChartView/blob/master/app/src/main/res/raw/img1.png?raw=true)
<h1>缘由</h1>
3.1.0版本的开发当中涉及到了饼状体，小组有人认为可以用<strong><a href="https://github.com/PhilJay/MPAndroidChart" data-pjax="#js-repo-pjax-container">MPAndroidChart</a></strong>实现这个功能，但是我的内心自始至终是拒绝的，因为MPAndroidChart的确很强大，但是项目中并不需要太多功能，不能因为只需要十分之一的功能就导入一个包，于是在星期四就开始构思这个事情。对于动态处理扇形起始角度这个东西有点迷糊，重写了3次扇形部分的代码，改了几个bug才将这个功能做好。

<strong>遇到复杂的问题，先把复杂的问题分解成一个一个小问题，然后一个一个的解决，复杂问题也就显得不再复杂。</strong>

先看看最后的=实现的效果图：

<img class="alignnone" src="https://github.com/liaopen123/ImageRepo/blob/master/app/src/main/res/raw/444444.gif?raw=true" width="267" height="480" />

&nbsp;
<h1>功能实现：</h1>
对于这个功能的实现，可以分为3个部分 就可以搞定。

part1：

<img class="alignnone wp-image-113 aligncenter" src="http://www.doyouevershine.com/wp-content/uploads/2016/12/20161204111555.png" alt="20161204111555" width="423" height="747" />

part2&amp;part3：<img class="alignnone size-full wp-image-114 aligncenter" src="http://www.doyouevershine.com/wp-content/uploads/2016/12/20161204112014.png" alt="20161204112014" width="426" height="756" />

&nbsp;

3个部分中 最难分析的是part1：因为android提供的api中画扇形代码如下，
<pre>public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)</pre>
四个参数分别为:
<ul>
 	<li>oval: 圆弧所在的椭圆。</li>
 	<li>startAngle: 圆弧起始角度，单位为度。</li>
 	<li>sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。</li>
 	<li>useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。</li>
 	<li>paint: 绘制圆弧的画板属性，如颜色，是否填充等。</li>
</ul>
其中难点在于startAngle,因为它是一直再变化,并且依赖于相邻圆弧的结束边,就是在这个地方翻了一点点迷糊,该用手写的还是得做做草稿啊,最后一个版本的代码还是在草稿纸完成的...

<strong>注意:通过面向对象写圆弧的javaBean的时候,要根据onDraw中画图的参数(startAngle,sweepAngle)去确定,不能凭空想象(自己凭想象犯了错).</strong>

javaBean:
<pre>public class PiePartBean {
    int color;//扇形区域的颜色
    double percent;//百分比  每个部分占总的百分比 如0.2
    
    int endPointX;//扇形末端的点 用来链接圆心画直线
    int endPointY;//扇形末端的点 用来链接圆心画直线
    int radius;//扇形所对圆的半径
    public  int startArc;//
    public int moveArc;//划过的角度
    public boolean drawLine = false;
}</pre>
实现逻辑:

这里需要区分2个大情况,①:是否是第一个扇形,因为第一个扇形的固定从-90°(也就是12点方向开始的),而其他圆弧都是依赖于第一个扇形和之前的扇形。②：在扇形百分比递增的时候，考虑有没有超过最大百分比。先确定基准扇形的位置和大小,这里也就是i=0的情况,i=1的扇形的起始位置就是i=0的结束位置(startArc = -90+moveArc),i=1的扇形moveArc 也为递增percent*360°,当超过上限的percent的时候就为上限percent*360°;同理i=2的扇形的起始角度(startArc = -90+(i=0的moveArc)+(i=1的moveArc)),划过的扇形范围和i=1的情况相同.

可得结论:每个扇形的起始位置(startArc)都与之前部分的扇形的moveArc相关.
<pre>/**先判断satarArc  再判断moveArc*/
if (i == 0){
    //第一个arc的起始角度 startArc = -90°
   <span style="color: #008000;"><strong> piePartBeanArrayList.get(i).startArc = -90;</strong></span>
    //再判断moveArc
    if(mCurrentPercent&lt;piePartBeanArrayList.get(i).percent){
        //还没有达到上限百分比
//
<span style="color: #0000ff;">        piePartBeanArrayList.get(i).moveArc =(int) (mCurrentPercent*360);</span>
    }else{
        Log.d(TAG,"第"+i+"个达到上限");
 <span style="color: #0000ff;">       piePartBeanArrayList.get(i).moveArc =(int) (piePartBeanArrayList.get(i).percent*360);</span>
        piePartBeanArrayList.get(i).drawLine = true;
    }
    piePartBeanArrayList.get(i).endPointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * piePartBeanArrayList.get(i).percent*360/ 180));
    piePartBeanArrayList.get(i).endPointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * piePartBeanArrayList.get(i).percent*360/ 180));
}else{
    //第2,3,4....part
     pastTotalArc = 0;//之前arc所划过的角度
 <span style="color: #008000;"><strong> for (int j= 0;j&lt;i;j++){ //1+2...个的扇形总角度
      pastTotalArc+=piePartBeanArrayList.get(j).moveArc;
  }</strong></span>
    //startArc = -90°+之前arc所划过的角度总和
<strong><span style="color: #008000;">    piePartBeanArrayList.get(i).startArc = -90+pastTotalArc;
</span></strong>//确定直线的位置要放在这里 因为这里可以得到每个部分的角度总和
<span style="color: #993366;">    piePartBeanArrayList.get(i).endPointX = (int) (centerPoint.x + mRadius * Math.sin(Math.PI * pastTotalArc/ 180));
piePartBeanArrayList.get(i).endPointY = (int) (centerPoint.y - mRadius * Math.cos(Math.PI * pastTotalArc/ 180));</span>
    //求i的moveArc
    if(mCurrentPercent&lt;piePartBeanArrayList.get(i).percent){
        //还没有达到上限百分比
    <span style="color: #0000ff;">    piePartBeanArrayList.get(i).moveArc =(int) (mCurrentPercent*360);</span>
    }else{
        Log.d(TAG,"第"+i+"个达到上限");
 <span style="color: #0000ff;">       piePartBeanArrayList.get(i).moveArc =(int) (piePartBeanArrayList.get(i).percent*360);

</span>//<span style="color: #ff0000;">当最后一个扇形区域+起始角度+(--90°) = 360的时候 就说明已经绘制完整  Running = false;跳出递归</span>
        int i1 = piePartBeanArrayList.get(i).moveArc + piePartBeanArrayList.get(i).startArc;
        piePartBeanArrayList.get(i).drawLine = true;
        if(i1&gt;=360+(-90)){
            //此处需要invalidate  不然会出现微小空缺。
            invalidate();
            Running = false;
        }
    }
}</pre>
&nbsp;

part2的实现:

刚开始是这样想的:区分的直线和扇形一起移动,但是代码实现很麻烦,不知道哪个地方出了问题,奇奇怪怪的样子,后来通过另外一种方式实现了这个功能:就假设直线的位置固定不动,为扇形走完360°区域后的位置,当每个part走到最大值的时候,绘制出来就可以了,这里需要<strong>给PiePartBean添加一个drawLine的boolean值,当每个扇形区域当达到最大值时候,把drawLine=<span style="color: #ff0000;">ture</span>;同时invalidate()即可实现:</strong>
<pre>//画线
if(piePartBeanArrayList.get(i).drawLine) {
    paint.setStrokeWidth(10);
    if (i == 0) {
        paint.setColor(Color.WHITE);

        canvas.drawLine(centerPoint.x, centerPoint.y, piePartBeanArrayList.get(i).endPointX, piePartBeanArrayList.get(i).endPointY, paint);
    } else {
        paint.setColor(Color.WHITE);
        canvas.drawLine(centerPoint.x, centerPoint.y, piePartBeanArrayList.get(i).endPointX, piePartBeanArrayList.get(i).endPointY, paint);
    }
    if (i == piePartBeanArrayList.size() - 1) {
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x, centerPoint.y - mRadius, paint);
    }
}</pre>
&nbsp;

part3的实现：

最后通过画圆的API就可以实现。不过，为了防止一进入界面调用ondraw方法就去画圆，还行添加数据非空判断。
<pre>if(piePartBeanArrayList.size()!=0) {///防止一进界面未设置数据 就已经画圆
    //画圆心
    paint.setColor(Color.WHITE);
    canvas.drawCircle(centerPoint.x, centerPoint.y, mRadius / 2.5f, paint);
}</pre>
最后附上最后一版的草稿(我已经看不懂我在写什么。。)<img class="alignnone size-large wp-image-120" src="http://www.doyouevershine.com/wp-content/uploads/2016/12/826453987486950133-768x1024.jpg" alt="826453987486950133" width="660" height="880" />

gitHub地址：<strong><a href="https://github.com/liaopen123/PieChartView" data-pjax="#js-repo-pjax-container">PieChartView</a></strong>
