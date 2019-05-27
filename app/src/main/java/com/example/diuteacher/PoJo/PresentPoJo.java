package com.example.diuteacher.PoJo;

import java.util.ArrayList;

public class PresentPoJo {

    ArrayList<StudentPoJo> presentPoJos;
    ArrayList<StudentPoJo> totalPoJos;

    public PresentPoJo(ArrayList<StudentPoJo> presentPoJos, ArrayList<StudentPoJo> totalPoJos) {
        this.presentPoJos = presentPoJos;
        this.totalPoJos = totalPoJos;
    }

    public ArrayList<StudentPoJo> getPresentPoJos() {
        return presentPoJos;
    }

    public void setPresentPoJos(ArrayList<StudentPoJo> presentPoJos) {
        this.presentPoJos = presentPoJos;
    }

    public ArrayList<StudentPoJo> getTotalPoJos() {
        return totalPoJos;
    }

    public void setTotalPoJos(ArrayList<StudentPoJo> totalPoJos) {
        this.totalPoJos = totalPoJos;
    }
}
