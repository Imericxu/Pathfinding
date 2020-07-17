package com.imericxu.pathfinder.attempt_2.animation;

import java.awt.*;

public class AnimatedColor
{
    public float[] hsl;
    private float[] mHsl;
    private int progress;
    private float duration;
    private int tick;
    
    public AnimatedColor(Color color, float duration)
    {
        hsl = HSLColor.fromRGB(color);
        mHsl = hsl.clone();
        progress = 0;
        this.duration = duration;
        tick = -1;
    }
    
    public AnimatedColor(float h, float s, float l, float duration)
    {
        hsl = new float[]{h, s, l};
        mHsl = hsl.clone();
        progress = 0;
        this.duration = duration;
        tick = -1;
    }
    
    public void aHue(float change)
    {
        hsl[0] = mHsl[0] + (int) (change * Curves.bezierBlend(progress / duration));
    }
    
    public void aLum(float change)
    {
        hsl[2] = mHsl[2] + (int) (change * Curves.bezierBlend(progress / duration));
    }
    
    public void checkTick()
    {
        if (progress == 0 || progress == duration)
        {
            tick = -tick;
        }
    }
    
    public void tick()
    {
        progress += tick;
    }
}
