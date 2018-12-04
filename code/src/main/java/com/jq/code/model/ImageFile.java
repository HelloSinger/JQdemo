package com.jq.code.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by hfei on 2016/3/28.
 */
public class ImageFile {
    String formName;
    File file;
    String contentType;
    byte[] data;

    @Override
    public String toString() {
        return "ImageFile{" +
                "formName='" + formName + '\'' +
                ", file=" + file +
                ", contentType='" + contentType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file){
        this.file = file;
    }

    private void getDataFromFile() {
        InputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file.getAbsolutePath());
            data = new byte[fileInput.available()];
            fileInput.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInput != null){
                try {
                    fileInput.close();
                    fileInput = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData(){
        getDataFromFile();
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
