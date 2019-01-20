package com.astra.acan.epart;

public class ModelDataPart {
    String nomorPart;
    String namaPart;
    String hargaPart;
    int jmlahItem;
    String stStok;


    public ModelDataPart(String nomorPart, String namaPart, String hargaPart, int jmlahItem, String stStok) {
        this.nomorPart = nomorPart;
        this.namaPart = namaPart;
        this.hargaPart = hargaPart;
        this.jmlahItem = jmlahItem;
    }

    public ModelDataPart() {

    }

    public String getNomorPart() {
        return nomorPart;
    }

    public void setNomorPart(String nomorPart) {
        this.nomorPart = nomorPart;
    }

    public String getNamaPart() {
        return namaPart;
    }

    public void setNamaPart(String namaPart) {
        this.namaPart = namaPart;
    }

    public String getHargaPart() {
        return hargaPart;
    }

    public void setHargaPart(String hargaPart) {
        this.hargaPart = hargaPart;
    }

    public int getJmlahItem() {
        return jmlahItem;
    }

    public void setJmlahItem(int jmlahItem) {
        this.jmlahItem = jmlahItem;
    }

    public String getStStok() {
        return stStok;
    }

    public void setStStok(String stStok) {
        this.stStok = stStok;
    }
}
