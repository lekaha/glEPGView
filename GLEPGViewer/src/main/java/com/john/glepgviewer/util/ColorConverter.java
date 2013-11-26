package com.john.glepgviewer.util;

import android.graphics.Color;

/**
 * Created by john on 10/28/13.
 */
public class ColorConverter {
    public static GLColor toGLColor(String hexColor){
        int parsedColor = Color.parseColor(hexColor);
        float alpha = Color.alpha(parsedColor) / 255f;
        float red = Color.red(parsedColor) / 255f;
        float green = Color.green(parsedColor) / 255f;
        float blue = Color.blue(parsedColor) / 255f;

        return new GLColor(alpha, red, green, blue);
    }

    public static int toColor(String hexColor){
        return Color.parseColor(hexColor);
    }


    public static class GLColor{
        public float Alpha;
        public float Red;
        public float Green;
        public float Blue;
        public GLColor(float a, float r, float g, float b){
            Alpha = a;
            Red = r;
            Green = g;
            Blue = b;
        }
    }
}
