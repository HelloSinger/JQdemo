package com.jq.btc.kitchenscale.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jq.btc.app.R;
import com.jq.code.model.PieChartEntity;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends View {
	private float assistLine1 = 10 ;
	private float assistLine2 = 50 ;
	private float pathWidth = 30 ;
	private float textSize  = 10;
	private float width ,height ;
	private float textHeight ;
	private List<PieChartEntity> entities ;
	private float total;
	private float[] itemsAngle ;
	private float[] itemsBeginAngle;
	private float[] itemsRate;
	private float radius = 100;
	private Paint picPaint = new Paint();
	private Paint linPaint = new Paint();
	private Paint textPaint = new Paint();
	private int mTextColor;
	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
		textSize = a.getInteger(R.styleable.PieChart_pie_text_size,10) ;
		assistLine1 = a.getInteger(R.styleable.PieChart_pie_assist_line1,10) ;
		assistLine2 = a.getInteger(R.styleable.PieChart_pie_assist_line2,50) ;
		pathWidth = a.getInteger(R.styleable.PieChart_pie_edge_width,30) ;
		mTextColor = a.getColor(R.styleable.PieChart_pie_text_color, 0xFFFFFFFF);
		a.recycle();

		radius = radius * Resources.getSystem().getDisplayMetrics().density ;
		pathWidth = pathWidth * Resources.getSystem().getDisplayMetrics().density;
		assistLine1 = assistLine1 * Resources.getSystem().getDisplayMetrics().density;
		assistLine2 = assistLine2 * Resources.getSystem().getDisplayMetrics().density;
		textSize = textSize* Resources.getSystem().getDisplayMetrics().density ;
		picPaint.setAntiAlias(true);
		picPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		picPaint.setStyle(Paint.Style.STROKE);
		picPaint.setDither(true);
		picPaint.setStrokeWidth(pathWidth);

		linPaint = new Paint();
		linPaint.setStyle(Paint.Style.STROKE);
		linPaint.setStrokeWidth(2);

		textPaint.setTextSize(10 * Resources.getSystem().getDisplayMetrics().density);
		textPaint.setColor(mTextColor);
		String textStr = "24242424.7" ;
		Rect rect = new Rect() ;
		textPaint.getTextBounds(textStr, 0, textStr.length(), rect);
		textHeight = rect.height() ;
		assistLine2 = rect.width() ;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}
	public void setEntites(List<PieChartEntity> entities){
		this.entities = entities ;
		jsTotal();
		refreshItemsAngs();
		notifyDraw();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (entities == null || entities.size() == 0) {
			return;
		} else {
			RectF oval = new RectF(width/2 -  radius + pathWidth/2 , height/2 + textHeight / 2 - radius + pathWidth/2 ,
					width/2  + radius  - pathWidth/2 ,  height/2 + textHeight / 2  +  radius -  pathWidth/2);
			for (int i = 0; i < entities.size(); i++) {
				picPaint.setColor(getResources().getColor(entities.get(i).getColor()));
				linPaint.setColor(getResources().getColor(entities.get(i).getColor()));
				canvas.drawArc(oval, itemsBeginAngle[i], itemsAngle[i] + 1, false, picPaint);
				if(entities.size()!= 1){
					List<PointF> pointFs = getItemPointF(itemsBeginAngle[i] + itemsAngle[i]/2) ;
					drawLine(canvas,pointFs);
					drawText(entities.get(i),canvas,pointFs);
				}
			}
		}
	}
	public void drawText(PieChartEntity entity, Canvas canvas, List<PointF> pointFs){
		if(pointFs.size() != 3)return;
		float yuanxinX = width / 2 ;
		PointF pointF2 = pointFs.get(1) ;
		PointF pointF3 = pointFs.get(2) ;
		if(pointF2.x > yuanxinX){
			canvas.drawText(entity.getContent(),pointF2.x,pointF2.y - 10,textPaint);
		}else {
			canvas.drawText(entity.getContent(),pointF3.x,pointF3.y - 10,textPaint);
		}

	}
	public void drawLine(Canvas canvas, List<PointF> pointFs){
		if(pointFs.size() != 3)return;
		Path path = new Path() ;
		path.moveTo(pointFs.get(0).x,pointFs.get(0).y);
		path.lineTo(pointFs.get(1).x,pointFs.get(1).y);
		path.lineTo(pointFs.get(2).x,pointFs.get(2).y);
		canvas.drawPath(path,linPaint);
	}
	public List<PointF> getItemPointF(float minddlAngle){
		List<PointF> pointFs = new ArrayList<PointF>() ;
		float yuanxinX = width / 2 ;
		float yuanxinY = height /2 + textHeight /2   ;
		minddlAngle = (float) ((2* Math.PI/360)*minddlAngle);

		PointF point1 = new PointF() ;
		point1.x  = (float) (yuanxinX + radius * Math.cos(minddlAngle));
		point1.y  = (float) (yuanxinY + radius * Math.sin(minddlAngle));

		PointF point2 = new PointF() ;
		point2.x  = (float) (yuanxinX + (radius + assistLine1 ) * Math.cos(minddlAngle));
		point2.y  = (float) (yuanxinY + (radius + assistLine1 ) * Math.sin(minddlAngle));

		PointF point3 = new PointF() ;
		if(minddlAngle > Math.PI/2 && minddlAngle < Math.PI * 3/2){
			point3.x = point2.x - assistLine2 ;
		}else {
			point3.x = point2.x + assistLine2 ;
		}
		point3.y = point2.y ;


		pointFs.add(point1);
		pointFs.add(point2);
		pointFs.add(point3);
		return pointFs ;
	}

	private void jsTotal() {
		total = 0;
		for (PieChartEntity entity : entities) {
			total += entity.getValue();
		}
	}

	private void refreshItemsAngs() {
		if (entities != null && entities.size() > 0) {
			itemsRate = new float[entities.size()];
			itemsBeginAngle = new float[entities.size()];
			itemsAngle = new float[entities.size()];
			float beginAngle = 0;
			for (int i = 0; i < entities.size(); i++) {
				itemsRate[i] = (float) (entities.get(i).getValue() * 1.0 / total * 1.0);
			}
			for (int i = 0; i < itemsRate.length; i++) {
				if(i >0){
					beginAngle = 360 * itemsRate[i - 1] + beginAngle;
				}
				itemsBeginAngle[i] = beginAngle;
				itemsAngle[i] = 360 * itemsRate[i];
			}
		}
	}

	public void notifyDraw() {
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth() ;
		height = getMeasuredHeight() ;
		float heightRadius = (height - 2 * assistLine1 - textHeight - 6 * Resources.getSystem().getDisplayMetrics().density) / 2 ;
		float weightRadius = (width - 2 * (assistLine1 + assistLine2)) / 2 ;
		radius = Math.min(heightRadius,weightRadius) ;
	}
}
