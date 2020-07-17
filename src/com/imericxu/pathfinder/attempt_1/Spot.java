package com.imericxu.pathfinder.attempt_1;

import java.util.ArrayList;

public class Spot
{
    private Spot[][] grid;
    private float f;
    private float g;
    private int row, col;
    private ArrayList<Spot> neighbors = new ArrayList<>();
    private Spot cameFrom;
    private boolean isWall;
    
    public Spot(Spot[][] grid, int row, int col)
    {
        this.grid = grid;
        f = Float.POSITIVE_INFINITY;
        g = Float.POSITIVE_INFINITY;
        this.row = row;
        this.col = col;
        isWall = Math.random() < 0.25;
    }
    
    public boolean isWall()
    {
        return isWall;
    }
    
    public void setWall(boolean wall)
    {
        isWall = wall;
    }
    
    public Spot getCameFrom()
    {
        return cameFrom;
    }
    
    public void setCameFrom(Spot cameFrom)
    {
        this.cameFrom = cameFrom;
    }
    
    public void findNeighbors()
    {
        if (row > 0)
        {
            neighbors.add(grid[row - 1][col]);
        }
        if (row < grid.length - 1)
        {
            neighbors.add(grid[row + 1][col]);
        }
        if (col > 0)
        {
            neighbors.add(grid[row][col - 1]);
        }
        if (col < grid[0].length - 1)
        {
            neighbors.add(grid[row][col + 1]);
        }
    }
    
    public int getRow()
    {
        return row;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public float getF()
    {
        return f;
    }
    
    public void setF(float f)
    {
        this.f = f;
    }
    
    public float getG()
    {
        return g;
    }
    
    public void setG(float g)
    {
        this.g = g;
    }
    
    public ArrayList<Spot> getNeighbors()
    {
        return neighbors;
    }
    
    @Override
    public String toString()
    {
        return "(" + row + ", " + col + ")";
    }
}
