package ua.kpi.comsys.io8205.pms_app.ui;

import android.annotation.SuppressLint;

public class CoordinateIV {

    private final Direction currentDir;
    private String worldLetter;
    private final int degree;
    private final int minute;
    private final int second;

    public CoordinateIV(){
        currentDir = Direction.LATITUDE;
        worldLetter = "N";
        degree = minute = second = 0;
    }

    @SuppressLint("DefaultLocale")
    public CoordinateIV(int deg, int min, int sec, Direction dir) throws Exception {
        currentDir = dir;
        if (dir == Direction.LATITUDE){
            if (deg >= -90 && deg <= 90){
                degree = deg;
                if (deg >= 0)
                    worldLetter = "N";
                else
                    worldLetter = "S";
            }
            else {
                System.err.println(String.format("Found incompatible degree (%d) in latitude coordinate", deg));
                throw new Exception();
            }
        }
        else {
            if (deg >= -180 && deg <= 180){
                degree = deg;
                if (deg >= 0)
                    worldLetter = "E";
                else
                    worldLetter = "W";
            }
            else {
                System.err.println(String.format("Found incompatible degree (%d) in longitude coordinate", deg));
                throw new Exception();
            }
        }

        if (min >= 0 && min <= 59){
            minute = min;
        }
        else {
            System.err.println(String.format("Found incompatible minute (%d) in coordinate", min));
            throw new Exception();
        }

        if (sec >= 0 && sec <= 59){
            second = sec;
        }
        else {
            System.err.println(String.format("Found incompatible second (%d) in coordinate", sec));
            throw new Exception();
        }

        if ((dir == Direction.LATITUDE && Math.abs(getFloatSigned()) > (float) 90) ||
                (dir == Direction.LONGITUDE && Math.abs(getFloatSigned()) > (float) 180)) {
            System.err.println(String.format("The coordinate (%f) is not in bounds", getFloatSigned()));
            throw new Exception();
        }

    }

    @SuppressLint("DefaultLocale")
    public String getIntCoordinate(){
        return String.format("%d°%d'%d\" %s", Math.abs(degree), minute, second, worldLetter);
    }

    private float getFloatSigned(){
        return (Math.abs(degree) + (float)minute/60 + (float)second/3600) * (degree >= 0? 1: -1);
    }

    @SuppressLint("DefaultLocale")
    public String getFloatCoordinate(){
        return String.format("%f° %s", Math.abs(getFloatSigned()), worldLetter);
    }

    public static CoordinateIV getMiddleCoordinate(CoordinateIV a, CoordinateIV b) throws Exception {
        if (a.getCurrentDir() == b.getCurrentDir()){
            float mid = (a.getFloatSigned() + b.getFloatSigned()) / 2;
            int degree = (int) mid;
            mid -= degree;
            mid *= 60;
            int min = (int) mid;
            mid -= min;
            mid *= 60;
            return new CoordinateIV(degree, min, (int) mid, a.getCurrentDir());
        }
        else {
            return null;
        }
    }

    public CoordinateIV getMiddleCoordinate(CoordinateIV second) throws Exception {
        return getMiddleCoordinate(this, second);
    }

    public Direction getCurrentDir() {
        return currentDir;
    }

    public int getDegree() {
        return degree;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}

enum Direction {
    LATITUDE,
    LONGITUDE
}