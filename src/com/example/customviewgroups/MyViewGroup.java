package com.example.customviewgroups;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
  private static final String TAG = "CustomLayout";

  // the minimum and maximum zoom
  private float minZoom = .5f;
  private float maxZoom = 5f;

  // the max pannable canvas width and height
  private float maxCanvasWidth = 900;
  private float maxCanvasHeight = 900;

  private volatile float scaleFactor = 1f;
  private ScaleGestureDetector detector;

  // the mode that we're in
  private static final int NONE = 0;
  private static final int DRAG = 1;
  private static final int ZOOM = 2;

  private int mode;

  // the X and Y coordinate of the finger when it first
  // touches the screen
  private float startX = 0f;
  private float startY = 0f;

  // the amount we need to translate the
  // canvas along the X
  // and the Y coordinate
  private float translateX = 0f;
  private float translateY = 0f;

  // the amount we translated the X and Y
  // coordinates, the last time we
  // panned.
  private float previousTranslateX = 0f;
  private float previousTranslateY = 0f;

  private boolean dragged = false;

  private Paint paint;
  
  public MyViewGroup(Context context) {
    super(context);
    init();
  }

  public MyViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    // by default this is true in ViewGroups, set to false so onDraw gets called
    this.setWillNotDraw(false);
    
    detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLUE);
    paint.setStrokeWidth(5f);

  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    // At this time we need to call setMeasuredDimensions(). Lets just call the
    // parent View's method (see
    // https://github.com/android/platform_frameworks_base/blob/master/core/java/android/view/View.java)
    // that does:
    // setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),
    // widthMeasureSpec),
    // getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    //

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int childCount = getChildCount();
    int wspec = 0;
    if (childCount == 0) {
      wspec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
          MeasureSpec.UNSPECIFIED);
    } else {
      wspec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
          MeasureSpec.UNSPECIFIED);
    }
    int hspec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
        MeasureSpec.UNSPECIFIED);

    measureChildren(widthMeasureSpec, heightMeasureSpec);
    //for (int i = 0; i < getChildCount(); i++) {
    //  View v = getChildAt(i);
    //  Log.d(TAG, "Measured Width / Height: " + getMeasuredWidth() + ", "
    //      + getMeasuredHeight());

    //  v.measure(wspec, hspec);
    //}
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int itemWidth = (childCount == 0) ? r - l : (r - l) / childCount;
    for (int i = 0; i < childCount; i++) {
      View v = getChildAt(i);
      // v.layout(itemWidth*i, t, (i+1)*itemWidth, b);
      // v.layout(itemWidth*i, t, v.getMeasuredWidth() + itemWidth*i,
      // t+v.getMeasuredHeight());
      this.adjustTranslationForBounds();
      int coord = (int) ((200 * (i + 1)) * scaleFactor);
      int coordX = (int) ((coord + translateX) );
      int coordY = (int) ((coord + translateY) );
      v.layout(coordX, coordY, v.getMeasuredWidth() + coordX,
          v.getMeasuredHeight() + coordY);
    }

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction() & MotionEvent.ACTION_MASK) {

    case MotionEvent.ACTION_DOWN:
      mode = DRAG;

      // We assign the current X and Y coordinate of the finger to startX and
      // startY minus the previously translated
      startX = event.getX() - previousTranslateX;
      startY = event.getY() - previousTranslateY;
      break;

    case MotionEvent.ACTION_MOVE:
      translateX = event.getX() - startX;
      translateY = event.getY() - startY;

      // We cannot use startX and startY directly because we have adjusted their
      // values using the previous translation values.
      double distance = Math.sqrt(Math.pow(event.getX()
          - (startX + previousTranslateX), 2)
          + Math.pow(event.getY() - (startY + previousTranslateY), 2));

      if (distance > 0) {
        dragged = true;
      }

      break;

    case MotionEvent.ACTION_POINTER_DOWN:
      mode = ZOOM;
      break;

    case MotionEvent.ACTION_UP:
      mode = NONE;
      dragged = false;

      // All fingers went up, so let's save the value of translateX and
      // translateY into previousTranslateX and
      // previousTranslate
      previousTranslateX = translateX;
      previousTranslateY = translateY;
      break;

    case MotionEvent.ACTION_POINTER_UP:
      mode = DRAG;

      // This is not strictly necessary; we save the value of translateX and
      // translateY into previousTranslateX
      // and previousTranslateY when the second finger goes up
      previousTranslateX = translateX;
      previousTranslateY = translateY;
      break;
    }

    detector.onTouchEvent(event);

    // Only redraw if zooming or dragging and zoomed-in
    // if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
    if ((mode == DRAG && dragged) || mode == ZOOM) {
      this.requestLayout();
      invalidate();
    }

    return true;
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);
    
    this.adjustTranslationForBounds();

    // The translation amount also gets scaled according to how much
    // we've zoomed into the canvas.
    canvas.translate(translateX / scaleFactor, translateY / scaleFactor);

    /* put the canvas drawing code here: */

    doCanvasPaint(canvas);

    /* end of drawing code */

    canvas.restore();
  }
  
  private void adjustTranslationForBounds() {
    // these appear to be scaled values:
    int width = getWidth();
    int height = getHeight();

    // make sure we can't scroll the right edge of the "virtual canvas" off the
    // right side of view
    if ((translateX / scaleFactor) < (-maxCanvasWidth + (width / scaleFactor))) {
      translateX = (-maxCanvasWidth + (width / scaleFactor)) * scaleFactor;
    }

    // make sure we can't scroll the bottom edge of the virtual canvas off the
    // bottom of view
    if ((translateY / scaleFactor) < (-maxCanvasHeight + (height / scaleFactor))) {
      translateY = (-maxCanvasHeight + (height / scaleFactor)) * scaleFactor;
    }

    // don't scroll left edge of virtual canvas to right
    if (translateX > 0) {
      translateX = 0;
    }

    // don't scroll top edge of virtual canvas down
    if (translateY > 0) {
      translateY = 0;
    }
  }
  
  public void doCanvasPaint(Canvas canvas) {
    canvas.drawLine(0f, 0f, 900f, 900f, paint);
    canvas.drawLine(900f, 0f, 0f, 900f, paint);    
  }

  public float getTranslateX() {
    return translateX;
  }

  public void setTranslateX(float translateX) {
    this.translateX = translateX;
    previousTranslateX = translateX;
  }

  public float getTranslateY() {
    return translateY;
  }

  public void setTranslateY(float translateY) {
    this.translateY = translateY;
    previousTranslateY = translateY;
  }

  private class ScaleListener extends
      ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      scaleFactor *= detector.getScaleFactor();
      scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom));
      return true;
    }
  }

}
