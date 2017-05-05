package com.waqarahmed.android.vtssystem;

/**
 * Created by waqar on 5/19/2016.
 */
public class safety_model {
    private String engine_rpm;
    private String vehicle_speed;
    private String temperature;
    private String throttle;
    private String date;
    private String time;

    safety_model(String engine_rpm,String vehicle_speed,String temperature,String throttle,String date,String time){
        this.engine_rpm=engine_rpm;
        this.vehicle_speed=vehicle_speed;
        this.temperature=temperature;
        this.throttle=throttle;
        this.date=date;
        this.time=time;

    }
    safety_model(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getEngine_rpm() {
        return engine_rpm;
    }
    public void setEngine_rpm(String engine_rpm) {
        this.engine_rpm = engine_rpm;
    }

    public String getVehicle_speed() {
        return vehicle_speed;
    }

    public void setVehicle_speed(String vehicle_speed) {
        this.vehicle_speed = vehicle_speed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getThrottle() {
        return throttle;
    }

    public void setThrottle(String throttle) {
        this.throttle = throttle;
    }

    @Override
    public String toString() {
        return this.date+"\n"+this.time;
    }
}
