package com.lucasmellof.bioforge.utils;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ColorUtils {

    // Taken from: https://stackoverflow.com/questions/19398238/how-to-mix-two-int-colors-correctly
    public static int blend(int color, int secondColor, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int a1 = (color >> 24 & 0xff);
        int r1 = ((color & 0xff0000) >> 16);
        int g1 = ((color & 0xff00) >> 8);
        int b1 = (color & 0xff);

        int a2 = (secondColor >> 24 & 0xff);
        int r2 = ((secondColor & 0xff0000) >> 16);
        int g2 = ((secondColor & 0xff00) >> 8);
        int b2 = (secondColor & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return (a << 24 | r << 16 | g << 8 | b);
    }
}
