package kalen.app.bluetooth.smartcar.view;

import android.graphics.Point;

public class MathUtils {

	public static int getLength(float x1,float y1,float x2,float y2) {
        return (int)Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public static Point getBorderPoint(Point a, Point b,int cutRadius) {
        float radian = getRadian(a, b);
        return new Point(a.x + (int)(cutRadius * Math.cos(radian)), a.x + (int)(cutRadius * Math.sin(radian)));
    }
    
    public static float getRadian (Point a, Point b) {
        float lenA = b.x-a.x;
        float lenB = b.y-a.y;
        float lenC = (float)Math.sqrt(lenA*lenA+lenB*lenB);
        float ang = (float)Math.acos(lenA/lenC);
        ang = ang * (b.y < a.y ? -1 : 1); 
        return ang;
    }
}