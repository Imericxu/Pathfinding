package com.imericxu.pathfinder.attempt_1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel
{
    public static final int CELL_SIZE = 12;
    private final Timer timer;
    private Map map;
    private Spot[][] grid;
    private ArrayList<Spot> openSet;
    private ArrayList<Spot> closedSet;
    private ArrayList<Spot> path;
    
    public Canvas()
    {
        map = new Map();
        grid = map.getGrid();
        openSet = map.getOpenSet();
        closedSet = map.getClosedSet();
        path = map.getPath();
        
        int width = CELL_SIZE * grid[0].length;
        int height = CELL_SIZE * grid.length;
        setSize(width, height);
        
        timer = new Timer(0, (e) ->
        {
            repaint();
        });
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
        boolean pathFound = false;
        if (!map.getOpenSet().isEmpty())
        {
            Spot current = openSet.get(0);
            for (Spot spot : openSet.subList(1, openSet.size()))
            {
                if (spot.getF() < current.getF())
                {
                    current = spot;
                }
            }
    
            if (current == map.getEnd())
            {
                Spot temp = current;
                path.add(temp);
                while (temp.getCameFrom() != null)
                {
                    path.add(temp.getCameFrom());
                    temp = temp.getCameFrom();
                }
                pathFound = true;
                System.out.println("Done!");
                timer.stop();
            }
    
            openSet.remove(current);
            closedSet.add(current);
    
            for (Spot neighbor : current.getNeighbors())
            {
                if (!closedSet.contains(neighbor) && !neighbor.isWall())
                {
                    float tempG = current.getG() + 1;
                    if (tempG < neighbor.getG())
                    {
                        neighbor.setCameFrom(current);
                        neighbor.setG(tempG);
                        neighbor.setF(tempG + map.heuristic(neighbor, map.getEnd()));
                    }
                    if (!openSet.contains(neighbor))
                    {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        else
        {
            timer.stop();
            System.out.println("No solution");
        }
    
        drawGrid(g2);
        colorCode(g2);
        colorPath(g2);
    }
    
    private void colorPath(Graphics2D g2)
    {
        for (Spot spot : path)
        {
            int x = spot.getCol() * CELL_SIZE + 1;
            int y = spot.getRow() * CELL_SIZE + 1;
            g2.setColor(new Color(0x51C2F1));
            g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
        }
    }
    
    private void drawGrid(Graphics2D g2)
    {
        for (int i = 0; i < grid.length; ++i)
        {
            for (int j = 0; j < grid[0].length; ++j)
            {
                int x = grid[i][j].getCol() * CELL_SIZE;
                int y = grid[i][j].getRow() * CELL_SIZE;
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }
    
    private void colorCode(Graphics2D g2)
    {
        g2.setColor(new Color(0xFF6662));
        for (Spot spot : closedSet)
        {
            int x = spot.getCol() * CELL_SIZE + 1;
            int y = spot.getRow() * CELL_SIZE + 1;
            g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
        }
        
        g2.setColor(new Color(0x52FF7F));
        for (Spot spot : openSet)
        {
            int x = spot.getCol() * CELL_SIZE + 1;
            int y = spot.getRow() * CELL_SIZE + 1;
            g2.fillRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2);
        }
        
        g2.setColor(Color.BLACK);
        for (Spot[] row : grid)
        {
            for (Spot spot : row)
            {
                if (spot.isWall())
                {
                    int x = spot.getCol() * CELL_SIZE + 1;
                    int y = spot.getRow() * CELL_SIZE + 1;
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }
}
