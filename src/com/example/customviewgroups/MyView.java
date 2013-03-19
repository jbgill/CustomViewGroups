package com.example.customviewgroups;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
  
  private float scaleFactor = 1f;
  private String text = "Testing";
  
  private static final int width = 100;
  private static final int height = 75;
  
  private Paint rectPaint;
  private Paint textPaint;
  private RectF rect;

  public MyView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public MyView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyView(Context context) {
    super(context);
    init();
  }
  
  public float getScaleFactor() {
    return scaleFactor;
  }

  public void setScaleFactor(float scaleFactor) {
    this.scaleFactor = scaleFactor;
  }
  
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  private void init() {
    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Style.FILL);
    rectPaint.setColor(Color.YELLOW);
    
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setStyle(Style.STROKE);
    textPaint.setTextSize(20);
    
    rect = new RectF(0,0,width,height);
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);

    // draw it:
    canvas.drawRoundRect(rect, 8, 8, rectPaint);
    canvas.drawText(text, 5, 25, textPaint);
    
    canvas.restore();
  }
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int wspec = MeasureSpec.makeMeasureSpec((int) (width * scaleFactor), MeasureSpec.UNSPECIFIED);
    int hspec = MeasureSpec.makeMeasureSpec((int) (height * scaleFactor), MeasureSpec.UNSPECIFIED);

    this.setMeasuredDimension(wspec, hspec);
  }

}
