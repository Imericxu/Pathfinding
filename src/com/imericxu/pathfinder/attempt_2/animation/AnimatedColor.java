package com.imericxu.pathfinder.attempt_2.animation;

import java.awt.*;

public class AnimatedColor extends Animated
{
    public float[] hsl;
    private float[] mHsl;
    private int alpha, mAlpha;
    
    public AnimatedColor(Color color, float duration)
    {
        super(duration);
        hsl = HSLColor.fromRGB(color);
        mHsl = hsl.clone();
        this.duration = duration;
        alpha = color.getAlpha();
        mAlpha = alpha;
    }
    
    public AnimatedColor(float h, float s, float l, float duration)
    {
        super(duration);
        hsl = new float[]{h, s, l};
        mHsl = hsl.clone();
        this.duration = duration;
        alpha = 255;
    }
    
    public void aHue(float change)
    {
        hsl[0] = mHsl[0] + (int) (change * Curves.bezierBlend(progress / duration));
    }
    
    public void aLum(float change)
    {
        hsl[2] = mHsl[2] + (int) (change * Curves.bezierBlend(progress / duration));
    }
    
    /**
     * @param newAlpha between 0-1
     */
    public void aAlpha(float newAlpha)
    {
        int change = (int) (newAlpha * 255 - mAlpha);
        alpha = mAlpha + (int) (change * (progress / duration));
    }
}
