package com.imericxu.pathfinder.attempt_2;

public class Node
{
    private int row;
    private int col;
    private boolean isWall;
    
    public Node(int row, int col)
    {
        this.row = row;
        this.col = col;
        isWall = Math.random() < 0.3;
    }
    
    /* * * * * * * * * * * * *
    Getters and Setters
    * * * * * * * * * * * * */
    
    public int getRow()
    {
        return row;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public boolean isWall()
    {
        return isWall;
    }
    
    public void setWall(boolean wall)
    {
        isWall = wall;
    }
    
    /* * * * * * * * * * * * *
    Other
    * * * * * * * * * * * * */
    
    @Override
    public String toString()
    {
        return "(" + row + ", " + col + ")";
    }
}
