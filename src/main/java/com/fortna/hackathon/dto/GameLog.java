package com.fortna.hackathon.dto;

import java.util.List;

public class GameLog {
    private String name0;
    private String name1;
    private float time0;
    private float time1;
    private List<PlayerLog> log0;
    private List<PlayerLog> log1;

    public String getName0() {
        return name0;
    }

    public void setName0(String name0) {
        this.name0 = name0;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public float getTime0() {
        return time0;
    }

    public void setTime0(Float time0) {
        this.time0 = time0;
    }

    public float getTime1() {
        return time1;
    }

    public void setTime1(Float time1) {
        this.time1 = time1;
    }

    public List<PlayerLog> getLog0() {
        return log0;
    }

    public void setLog0(List<PlayerLog> log0) {
        this.log0 = log0;
    }

    public List<PlayerLog> getLog1() {
        return log1;
    }

    public void setLog1(List<PlayerLog> log1) {
        this.log1 = log1;
    }

    public static class PlayerLog {
        private int step;
        private Coordination before;
        private Coordination velocity;
        private Coordination acceleration;
        private int result;
        private Coordination after;

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public Coordination getBefore() {
            return before;
        }

        public void setBefore(Coordination before) {
            this.before = before;
        }

        public Coordination getVelocity() {
            return velocity;
        }

        public void setVelocity(Coordination velocity) {
            this.velocity = velocity;
        }

        public Coordination getAcceleration() {
            return acceleration;
        }

        public void setAcceleration(Coordination acceleration) {
            this.acceleration = acceleration;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public Coordination getAfter() {
            return after;
        }

        public void setAfter(Coordination after) {
            this.after = after;
        }
    }

    public static class Coordination {
        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}