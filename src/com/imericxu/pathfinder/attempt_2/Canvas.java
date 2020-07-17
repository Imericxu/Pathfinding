package com.imericxu.pathfinder.attempt_2;

import com.imericxu.pathfinder.attempt_2.animation.AnimatedColor;
import com.imericxu.pathfinder.attempt_2.animation.HSLColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel
{
    private final int CELL_SIZE;
    private final int ROWS;
    private final int COLS;
    private final Map MAP;
    private ArrayList<Node> path = new ArrayList<>();
    private Node[] startEnd;
    private boolean finished;
    private AnimatedColor startColor;
    
    public Canvas() throws InterruptedException
    {
        finished = false;
        ROWS = 20;
        COLS = 20;
        MAP = new Map(ROWS, COLS);
    
        CELL_SIZE = calculateCellSize();
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        setBackground(new Color(0xF4F4F8));
    
        startEnd = generateStartAndEnd();
        path = MAP.aStar(startEnd[0], startEnd[1], HModel.EUCLIDEAN);
        finished = true;
        
        // Animate start and end node
        // Yellow
        startColor = new AnimatedColor(new Color(0x45FF61), 30);
        Timer timer = new Timer(0, e ->
        {
            startColor.checkTick();
            startColor.aLum(20);
            startColor.aHue(40);
            startColor.tick();
            repaint(startEnd[0].getCol() * CELL_SIZE, startEnd[0].getRow() * CELL_SIZE, CELL_SIZE
                    , CELL_SIZE);
        });
        timer.start();
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
        
        int x = startEnd[0].getCol() * CELL_SIZE + 1;
        int y = startEnd[0].getRow() * CELL_SIZE + 1;
        g2.setColor(HSLColor.toRGB(startColor.hsl));
        g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
    }
    
    private void checkerboard(Graphics2D g2)
    {
        g2.setColor(new Color(0xE2E2E2));
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
                    g2.setColor(new Color(0x545464));
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (MAP.inOpenList(node))
                {
                    g2.setColor(new Color(0x77FED766, true));
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (MAP.inClosedList(node))
                {
                    g2.setColor(new Color(0x79FF4653, true));
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }
    
    private void drawPath(Graphics2D g2)
    {
        for (Node node : path)
        {
            g2.setColor(new Color(0x00B1CC));
            int x = node.getCol() * CELL_SIZE + 1;
            int y = node.getRow() * CELL_SIZE + 1;
            g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
            repaint();
        }
    }
}
