package com.imericxu.pathfinder.animation;

public abstract class Animated
{
    protected int progress;
    protected float duration;
    private int tick;
    
    protected Animated(float duration)
    {
        progress = 0;
        this.duration = duration;
        tick = -1;
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
