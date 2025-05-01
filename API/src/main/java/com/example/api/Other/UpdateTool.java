package com.example.api.Other;

public class UpdateTool {
    public static <T> T updateIfNotNull(T oldData, T newData){
        return newData == null? oldData : newData;
    }

    public static String updateIfNotBlank(String oldData, String newData){
        return (newData == null || newData.isBlank())? oldData : newData;
    }
}
