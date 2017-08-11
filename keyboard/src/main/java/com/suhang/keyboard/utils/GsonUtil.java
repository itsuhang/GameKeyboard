package com.suhang.keyboard.utils;

import android.animation.ValueAnimator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suhang.keyboard.data.ButtonData;

import java.util.ArrayList;

/**
 * Created by 苏杭 on 2017/8/10 17:32.
 */

public class GsonUtil {
    public static ArrayList<ButtonData> getData(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<ArrayList<ButtonData>>() {
        }.getType());
    }
}
