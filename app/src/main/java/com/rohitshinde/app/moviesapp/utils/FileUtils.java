package com.rohitshinde.app.moviesapp.utils;

public class FileUtils {

    public static String selectImageSize(int dpi){
        String type;
        if(dpi<92){
            type="w92";
        }else if(dpi<154){
            type="w154";
        }else if(dpi<185){
            type="w185";
        }else if(dpi<342){
            type="w342";
        }else if(dpi<500){
            type="w500";
        }else if(dpi<780){
            type="w780";
        }else {
            type="original";
        }
        return type;
    }
}


