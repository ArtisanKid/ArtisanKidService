package com.artisankid.elementwar.controller.utils;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by LiXiangYu on 2017/5/10.
 */
public class Error {
    static public String ErrorToJSON(ErrorEnum errorEnum) throws IOException {
        return ErrorToJSON(errorEnum.getCode(), errorEnum.getMessage());
    }

    static public String ErrorToJSON(Integer code, String message) throws IOException {
        ResponseClass<Object> responseContent = new ResponseClass<>();
        responseContent.setCode(code);
        responseContent.setMessage(message);
        String json = new Gson().toJson(responseContent);
        return json;
    }
}
