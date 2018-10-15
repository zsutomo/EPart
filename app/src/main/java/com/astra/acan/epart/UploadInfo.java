package com.astra.acan.epart;

class UploadInfo {
    public String file_name;
    public String file_url;
    public String date_upload;
    public String user_upload;

    public UploadInfo() {
    }

    public UploadInfo(String filename, String fileUrl, String dateUpload, String userUpload) {
        this.file_name = filename;
        this.file_url = fileUrl;
        this.date_upload = dateUpload;
        this.user_upload = userUpload;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getDate_upload() {
        return date_upload;
    }

    public void setDate_upload(String date_upload) {
        this.date_upload = date_upload;
    }

    public String getUser_upload() {
        return user_upload;
    }

    public void setUser_upload(String user_upload) {
        this.user_upload = user_upload;
    }
}
