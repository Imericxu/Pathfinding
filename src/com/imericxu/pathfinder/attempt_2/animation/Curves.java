package com.imericxu.pathfinder.attempt_2.animation;

import static java.lang.Math.pow;

public class Curves
{
    /**
     * @param t progress (0 - 1)
     * @return calculated percentage
     */
    public static float bezierBlend(float t)
    {
        return t * t * (3.0f - 2.0f * t);
    }
    
    public static float easeInOutCubic(float x)
    {
        return x < 0.5 ? 4 * x * x * x : (float) (1 - pow(-2 * x + 2, 3) / 2);
    }
}
