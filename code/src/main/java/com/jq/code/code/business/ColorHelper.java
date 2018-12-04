package com.jq.code.code.business;

import android.graphics.Color;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/3.
 */

public class ColorHelper {
    private static ArrayList<Integer> colors = new ArrayList<Integer>();

    private static final RandomColor randomColor;

    static {
        randomColor = new RandomColor();
        colors.add(Color.parseColor("#d76496"));
        colors.add(Color.parseColor("#f0dc78"));
        colors.add(Color.parseColor("#d2b47d"));
        colors.add(Color.parseColor("#82d7dc"));
        colors.add(Color.parseColor("#fa8c8c"));
        colors.add(Color.parseColor("#96aad2"));
        colors.add(Color.parseColor("#91c8dc"));
        colors.add(Color.parseColor("#ebaa82"));
        colors.add(Color.parseColor("#b4dc96"));
        colors.add(Color.parseColor("#e1aaa5"));
        colors.add(Color.parseColor("#87dcaa"));
        colors.add(Color.parseColor("#c8a5d2"));
        colors.add(Color.parseColor("#82afd7"));
    }

    public static int getColor(int position) {
        if (position > colors.size() - 1) {
            colors.add(randomColor.randomColor());
        }
        return colors.get(position);
    }
}
