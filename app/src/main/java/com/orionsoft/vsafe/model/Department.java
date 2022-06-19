package com.orionsoft.vsafe.model;

import java.io.Serializable;

public class Department implements Serializable {

    private int police = 0;
    private int hospital = 0;
    private int fireBrigade = 0;
    private int dmc = 0;
    private int mwca = 0;

    public Department(int police, int hospital, int fireBrigade, int dmc, int mwca) {
        this.police = police;
        this.hospital = hospital;
        this.fireBrigade = fireBrigade;
        this.dmc = dmc;
        this.mwca = mwca;
    }

    public Department() {

    }

    public int getPolice() {
        return police;
    }

    public void setPolice(int police) {
        this.police = police;
    }

    public int getHospital() {
        return hospital;
    }

    public void setHospital(int hospital) {
        this.hospital = hospital;
    }

    public int getFireBrigade() {
        return fireBrigade;
    }

    public void setFireBrigade(int fireBrigade) {
        this.fireBrigade = fireBrigade;
    }

    public int getDmc() {
        return dmc;
    }

    public void setDmc(int dmc) {
        this.dmc = dmc;
    }

    public int getMwca() {
        return mwca;
    }

    public void setMwca(int mwca) {
        this.mwca = mwca;
    }
}
