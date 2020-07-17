package com.imericxu.pathfinder.attempt_1;

import java.util.ArrayList;

public class Map
{
    private static final int COLS = 50;
    private static final int ROWS = 50;
    private Spot[][] grid;
    private ArrayList<Spot> openSet = new ArrayList<>();
    private ArrayList<Spot> closedSet = new ArrayList<>();
    private Spot start;
    private Spot end;
    private ArrayList<Spot> path = new ArrayList<>();
    
    public Map()
    {
        grid = new Spot[ROWS][COLS];
        
        for (int i = 0; i < grid.length; ++i)
        {
            for (int j = 0; j < grid[0].length; ++j)
            {
                grid[i][j] = new Spot(grid, i, j);
            }
        }
        
        for (Spot[] row : grid)
        {
            for (Spot spot : row)
            {
                spot.findNeighbors();
            }
        }
        
        start = grid[0][0];
        end = grid[ROWS - 1][COLS - 1];
        start.setWall(false);
        end.setWall(false);
        openSet.add(start);
        start.setG(0);
        start.setF(heuristic(start, end));
    }
    
    public ArrayList<Spot> getPath()
    {
        return path;
    }
    
    public float heuristic(Spot p1, Spot p2)
    {
//        return (float) Math.hypot(p1.getCol() - p2.getCol(), p1.getRow() - p2.getRow());
        return Math.abs(p1.getCol() - p2.getCol()) + Math.abs(p1.getRow() - p2.getRow());
    }
    
    public Spot[][] getGrid()
    {
        return grid;
    }
    
    public ArrayList<Spot> getOpenSet()
    {
        return openSet;
    }
    
    public ArrayList<Spot> getClosedSet()
    {
        return closedSet;
    }
    
    public Spot getStart()
    {
        return start;
    }
    
    public Spot getEnd()
    {
        return end;
    }
}
