package com.astra.acan.epart;

class ModelDataEstimasi {
    String HargaPart;
    String NamaPart;
    String NomorPart;
    int id;
    int jmlahItem;
    String namaForeMan;
    String namaSa;
    String namaTeknisi;
    String nomorPolisi;
    String tanggal;
    String totalHargaEstimasi;
    double totalHargaItem;
    String typeKendaraan;
    String userEmail;

    public ModelDataEstimasi(int id, String tanggal, String nomorPart, String namaPart, String hargaPart, int jmlahItem, double totalHargaItem, String totalHargaEstimasi, String namaSa, String namaTeknisi, String namaForeMan, String nomorPolisi, String typeKendaraan, String userEmail) {
        this.id = id;
        this.tanggal = tanggal;
        this.NomorPart = nomorPart;
        this.NamaPart = namaPart;
        this.HargaPart = hargaPart;
        this.jmlahItem = jmlahItem;
        this.totalHargaItem = totalHargaItem;
        this.totalHargaEstimasi = totalHargaEstimasi;
        this.namaSa = namaSa;
        this.namaTeknisi = namaTeknisi;
        this.namaForeMan = namaForeMan;
        this.nomorPolisi = nomorPolisi;
        this.typeKendaraan = typeKendaraan;
        this.userEmail = userEmail;
    }

    public ModelDataEstimasi() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNomorPart() {
        return this.NomorPart;
    }

    public void setNomorPart(String nomorPart) {
        this.NomorPart = nomorPart;
    }

    public String getNamaPart() {
        return this.NamaPart;
    }

    public void setNamaPart(String namaPart) {
        this.NamaPart = namaPart;
    }

    public String getHargaPart() {
        return this.HargaPart;
    }

    public void setHargaPart(String hargaPart) {
        this.HargaPart = hargaPart;
    }

    public int getJmlahItem() {
        return this.jmlahItem;
    }

    public void setJmlahItem(int jmlahItem) {
        this.jmlahItem = jmlahItem;
    }

    public double getTotalHargaItem() {
        return this.totalHargaItem;
    }

    public void setTotalHargaItem(double totalHargaItem) {
        this.totalHargaItem = totalHargaItem;
    }

    public String getTotalHargaEstimasi() {
        return this.totalHargaEstimasi;
    }

    public void setTotalHargaEstimasi(String totalHargaEstimasi) {
        this.totalHargaEstimasi = totalHargaEstimasi;
    }

    public String getNamaSa() {
        return this.namaSa;
    }

    public void setNamaSa(String namaSa) {
        this.namaSa = namaSa;
    }

    public String getNamaTeknisi() {
        return this.namaTeknisi;
    }

    public void setNamaTeknisi(String namaTeknisi) {
        this.namaTeknisi = namaTeknisi;
    }

    public String getNamaForeMan() {
        return this.namaForeMan;
    }

    public void setNamaForeMan(String namaForeMan) {
        this.namaForeMan = namaForeMan;
    }

    public String getNomorPolisi() {
        return this.nomorPolisi;
    }

    public void setNomorPolisi(String nomorPolisi) {
        this.nomorPolisi = nomorPolisi;
    }

    public String getTypeKendaraan() {
        return this.typeKendaraan;
    }

    public void setTypeKendaraan(String typeKendaraan) {
        this.typeKendaraan = typeKendaraan;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
