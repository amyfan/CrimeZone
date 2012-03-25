package com.crimezone.sd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CircleMapOverlay extends Overlay {

  Context context;
  double mLat;
  double mLon;
  float radius;

  public static int metersToRadius(float meters, MapView map, double latitude) {
    return (int) (map.getProjection().metersToEquatorPixels(meters) * (1 / Math.cos(Math
        .toRadians(latitude))));
  }

  public CircleMapOverlay(Context _context, double _lat, double _lon, float _radius) {
    context = _context;
    mLat = _lat;
    mLon = _lon;
    radius = _radius;
  }

  public void draw(Canvas canvas, MapView mapView, boolean shadow) {

    super.draw(canvas, mapView, shadow);

    Projection projection = mapView.getProjection();

    Point pt = new Point();

    GeoPoint geo = new GeoPoint((int) (mLat * 1e6), (int) (mLon * 1e6));

    projection.toPixels(geo, pt);

    float circleRadius = metersToRadius(radius * 1600, mapView, mLat);

    Paint innerCirclePaint;

    innerCirclePaint = new Paint();
    innerCirclePaint.setARGB(100, 150, 150, 180);
    innerCirclePaint.setAntiAlias(true);
    innerCirclePaint.setStyle(Style.FILL);
    
    Paint outerCirclePaint = new Paint();
    outerCirclePaint.setColor(0xFF000000);
    outerCirclePaint.setStyle(Style.STROKE);
    outerCirclePaint.setAntiAlias(true);
    outerCirclePaint.setStrokeWidth((float) 5.0);


    canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, innerCirclePaint);
    canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, outerCirclePaint);
  }
}
