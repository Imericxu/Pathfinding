package com.imericxu.pathfinder.attempt_2.visual;

import com.imericxu.pathfinder.attempt_2.animation.AnimatedColor;
import com.imericxu.pathfinder.attempt_2.animation.HSLColor;
import com.imericxu.pathfinder.attempt_2.essential.HModel;
import com.imericxu.pathfinder.attempt_2.essential.Map;
import com.imericxu.pathfinder.attempt_2.essential.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel
{
    private final int CELL_SIZE;
    private final int ROWS, COLS;
    private final Map MAP;
    private ArrayList<Node> path;
    private Node[] startEnd;
    private boolean finished;
    private AnimatedColor startColor, endColor;
    
    public Canvas()
    {
        finished = false;
        ROWS = 40;
        COLS = 40;
        MAP = new Map(ROWS, COLS);
        
        CELL_SIZE = calculateCellSize();
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        setBackground(Colors.BACKGROUND);
        
        startEnd = generateStartAndEnd();
        path = MAP.aStar(startEnd[0], startEnd[1], HModel.EUCLIDEAN);
        finished = true;
        
        // Animate start and end node
        startColor = new AnimatedColor(Colors.START, 30);
        endColor = new AnimatedColor(Colors.END, 30);
        Timer startEndTimer = getStartEndTimer();
        startEndTimer.start();
    }
    
    /* * * * * * * * * * * * * * * * * * * * *
    Painting
    * * * * * * * * * * * * * * * * * * * * */
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        checkerboard(g2);
        colorNodes(g2);
        if (finished)
        {
            drawPath(g2);
        }
        
        colorSquare(g2, startEnd[0].getRow(), startEnd[0].getCol(), HSLColor.toRGB(startColor.hsl));
        colorSquare(g2, startEnd[1].getRow(), startEnd[1].getCol(), HSLColor.toRGB(endColor.hsl));
    }
    
    private void colorSquare(Graphics2D g2, int row, int col, Color color)
    {
        int x = col * CELL_SIZE + 1;
        int y = row * CELL_SIZE + 1;
        g2.setColor(color);
        g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
    }
    
    private void checkerboard(Graphics2D g2)
    {
        g2.setColor(Colors.CHECKER);
        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                if ((row + col) % 2 == 0)
                {
                    int x = col * CELL_SIZE + 1;
                    int y = row * CELL_SIZE + 1;
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }
    
    private void colorNodes(Graphics2D g2)
    {
        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                Node node = MAP.getNode(row, col);
                int x = node.getCol() * CELL_SIZE + 1;
                int y = node.getRow() * CELL_SIZE + 1;
                
                if (node.isWall())
                {
                    g2.setColor(Colors.WALL);
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (MAP.inOpenList(node))
                {
                    g2.setColor(Colors.OPEN);
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (MAP.inClosedList(node))
                {
                    g2.setColor(Colors.CLOSED);
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }
    
    private void drawPath(Graphics2D g2)
    {
        if (path != null)
        {
            for (Node node : path)
            {
                g2.setColor(Colors.PATH);
                int x = node.getCol() * CELL_SIZE + 1;
                int y = node.getRow() * CELL_SIZE + 1;
                g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
            }
        }
    }
    
    /* * * * * * * * * * * * * * * * * * * * *
    Helper Methods
    * * * * * * * * * * * * * * * * * * * * */
    
    private int calculateCellSize()
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double screenRatio = screen.getWidth() / screen.getHeight();
        double mapRatio = (double) COLS / ROWS;
        
        if (screenRatio > mapRatio)
        {
            return (int) ((screen.getHeight() - 100) / ROWS);
        }
        else
        {
            return (int) (screen.getWidth() / COLS);
        }
    }
    
    private Node[] generateStartAndEnd()
    {
        int randRow = (int) (Math.random() * ROWS);
        int randCol = (int) (Math.random() * COLS);
        Node start = MAP.getNode(randRow, randCol);
        
        Node end;
        do
        {
            randRow = (int) (Math.random() * ROWS);
            randCol = (int) (Math.random() * COLS);
            end = MAP.getNode(randRow, randCol);
        } while (start == end);
        
        return new Node[]{start, end};
    }
    
    private Timer getStartEndTimer()
    {
        return new Timer(1, e ->
        {
            startColor.checkTick();
            startColor.aLum(25);
            startColor.aHue(40);
            startColor.tick();
            repaint(startEnd[0].getCol() * CELL_SIZE, startEnd[0].getRow() * CELL_SIZE, CELL_SIZE
                    , CELL_SIZE);
            
            endColor.checkTick();
            endColor.aLum(25);
            endColor.aHue(-20);
            endColor.tick();
            repaint(startEnd[1].getCol() * CELL_SIZE, startEnd[1].getRow() * CELL_SIZE, CELL_SIZE
                    , CELL_SIZE);
        });
    }
}