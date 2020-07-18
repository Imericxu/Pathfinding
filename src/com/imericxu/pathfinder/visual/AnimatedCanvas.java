package com.imericxu.pathfinder.visual;

import com.imericxu.pathfinder.animation.AnimatedColor;
import com.imericxu.pathfinder.animation.HSLColor;
import com.imericxu.pathfinder.essential.HModel;
import com.imericxu.pathfinder.essential.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AnimatedCanvas extends JPanel
{
    private static final HModel HMODEL = HModel.EUCLIDEAN;
    private static final int DELAY = 0;
    private final int CELL_SIZE;
    private final int ROWS, COLS;
    private Node[][] grid;
    private Node start, end;
    private ArrayList<Node> openList = new ArrayList<>();
    private ArrayList<Node> closedList = new ArrayList<>();
    private HashMap<Node, Node> cameFrom = new HashMap<>();
    private HashMap<Node, Double> fScores = new HashMap<>();
    private HashMap<Node, Double> gScores = new HashMap<>();
    private ArrayList<Node> path;
    private AnimatedColor startColor, endColor;
    private boolean pathFound;
    private boolean begin;
    private int ticks;
    private ArrayList<Node> nodes;
    private final Timer delayTimer;
    private int ticksToStart;
    
    public AnimatedCanvas(int rows, int cols)
    {
        ROWS = rows;
        COLS = cols;
        grid = new Node[rows][cols];
        
        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                grid[i][j] = new Node(i, j);
            }
        }
        
        pathFound = false;
        begin = false;
        ticks = 0;
        nodes = new ArrayList<>();
        nodes.ensureCapacity(grid.length * grid[0].length);
    
        for (Node[] arr : grid)
        {
            nodes.addAll(Arrays.asList(arr));
        }
        
        // Begin A*
        generateEndpoints();
        initializeScores(fScores, gScores);
        openList.add(start);
        gScores.replace(start, 0.0);
        fScores.replace(start, heuristic(start, end, HMODEL));
        
        // Animate start and end node
        startColor = new AnimatedColor(Colors.START, 30);
        endColor = new AnimatedColor(Colors.END, 30);
        Timer startEndTimer = getStartEndTimer();
        startEndTimer.start();
        
        // Delay pathfinding
        ticksToStart = 10;
        delayTimer = new Timer(100, e ->
        {
            --ticksToStart;
            if (ticksToStart <= 0)
            {
                begin = true;
            }
        });
        delayTimer.start();
        
        // JPanel
        CELL_SIZE = calculateCellSize();
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        setBackground(Colors.BACKGROUND);
    }
    
    /* * * * * * * * * * * * * * * * * * * * *
    Canvas
    * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (begin)
        {
            delayTimer.stop();
            ticksToStart = 10;
            aStarStep();
        }
        
        if (pathFound)
        {
            blackout(g2);
        }
        else
        {
            checkerboard(g2);
            colorNodes(g2);
        }
        
        drawPath(g2);
        
        colorSquare(g2, start.getRow(), start.getCol(), HSLColor.toRGB(startColor.hsl));
        colorSquare(g2, end.getRow(), end.getCol(), HSLColor.toRGB(endColor.hsl));
    }
    
    private void aStarStep()
    {
        if (!openList.isEmpty() && !pathFound && ticks >= DELAY)
        {
            ticks = 0;
            
            // Get lowest f score
            Node current = openList.get(0);
            for (Node node : openList.subList(1, openList.size()))
            {
                if (fScores.get(node) < fScores.get(current))
                {
                    current = node;
                }
            }
            
            path = reconstructPath(cameFrom, current);
            
            if (current == end)
            {
                pathFound = true;
                System.out.println("Path found!");
            }
            
            openList.remove(current);
            closedList.add(current);
            
            ArrayList<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors)
            {
                if (!closedList.contains(neighbor))
                {
                    // Current g + cost of traversal (just 1 for now)
                    double tempG = gScores.get(current) + 1;
                    if (tempG < gScores.get(neighbor))
                    {
                        cameFrom.put(neighbor, current);
                        gScores.replace(neighbor, tempG);
                        fScores.replace(neighbor, tempG + heuristic(neighbor, end,
                                HModel.EUCLIDEAN));
                    }
                    
                    if (!openList.contains(neighbor))
                    {
                        openList.add(neighbor);
                    }
                }
            }
        }
    }
    
    private void greedySearch()
    {
        if (!openList.isEmpty() && !pathFound && ticks >= DELAY)
        {
            ticks = 0;
            
            // Get lowest f score
            Node current = openList.get(0);
            for (Node node : openList.subList(1, openList.size()))
            {
                if (fScores.get(node) < fScores.get(current))
                {
                    current = node;
                }
            }
            
            path = reconstructPath(cameFrom, current);
            
            if (current == end)
            {
                pathFound = true;
                System.out.println("Path found!");
            }
            
            openList.remove(current);
            closedList.add(current);
            
            ArrayList<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors)
            {
                if (!closedList.contains(neighbor))
                {
                    // Current g + cost of traversal (just 1 for now)
                    double tempG = gScores.get(current) + 1;
                    if (tempG < gScores.get(neighbor))
                    {
                        cameFrom.put(neighbor, current);
                        gScores.replace(neighbor, tempG);
                        fScores.replace(neighbor, heuristic(neighbor, end, HModel.EUCLIDEAN));
                    }
                    
                    if (!openList.contains(neighbor))
                    {
                        openList.add(neighbor);
                    }
                }
            }
        }
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
    
    private void colorSquare(Graphics2D g2, int row, int col, Color color)
    {
        int x = col * CELL_SIZE + 1;
        int y = row * CELL_SIZE + 1;
        g2.setColor(color);
        g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
    }
    
    private void colorNodes(Graphics2D g2)
    {
        for (Node[] row : grid)
        {
            for (Node node : row)
            {
                int x = node.getCol() * CELL_SIZE + 1;
                int y = node.getRow() * CELL_SIZE + 1;
                
                if (node.isWall())
                {
                    g2.setColor(Colors.WALL);
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (openList.contains(node))
                {
                    g2.setColor(Colors.OPEN);
                    g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
                }
                else if (closedList.contains(node))
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
            if (pathFound)
            {
                g2.setColor(Colors.PATH);
            }
            else
            {
                g2.setColor(Colors.UNFINISHED_PATH);
            }
            for (Node node : path)
            {
                int x = node.getCol() * CELL_SIZE + 1;
                int y = node.getRow() * CELL_SIZE + 1;
                g2.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
            }
        }
    }
    
    private void blackout(Graphics2D g2)
    {
        if (!nodes.isEmpty())
        {
            for (int times = 0; times < nodes.size() / 30 + 1; ++times)
            {
                int i = (int) (Math.random() * nodes.size());
                Node node = nodes.get(i);
//                if (node.isWall())
//                {
//                    colorSquare(g2, node.getRow(), node.getCol(), Color.BLACK);
//                    continue;
//                }
                
                Color randomColor = HSLColor.toRGB((int) (Math.random() * 360), 90, 20);
                colorSquare(g2, node.getRow(), node.getCol(), randomColor);
                nodes.remove(i);
            }
        }
    }
    
    /* * * * * * * * * * * * * * * * * * * * *
    Timers
    * * * * * * * * * * * * * * * * * * * * */
    private Timer getStartEndTimer()
    {
        return new Timer(1, e ->
        {
            ++ticks;
            
            startColor.checkTick();
            startColor.aLum(25);
            startColor.aHue(40);
            startColor.tick();
            
            endColor.checkTick();
            endColor.aLum(25);
            endColor.aHue(-20);
            endColor.tick();
            
            repaint();
        });
    }
    
    /* * * * * * * * * * * * * * * * * * * * *
    Map Methods
    * * * * * * * * * * * * * * * * * * * * */
    
    private ArrayList<Node> getNeighbors(Node node)
    {
        ArrayList<Node> neighbors = new ArrayList<>();
        int row = node.getRow();
        int col = node.getCol();
        
        Node north = row > 0 ? grid[row - 1][col] : null;
        if (north != null && !north.isWall())
        {
            neighbors.add(north);
        }
        
        Node east = col < grid[0].length - 1 ? grid[row][col + 1] : null;
        if (east != null && !east.isWall())
        {
            neighbors.add(east);
        }
        
        Node south = row < grid.length - 1 ? grid[row + 1][col] : null;
        if (south != null && !south.isWall())
        {
            neighbors.add(south);
        }
        
        Node west = col > 0 ? grid[row][col - 1] : null;
        if (west != null && !west.isWall())
        {
            neighbors.add(west);
        }
        
        return neighbors;
    }
    
    private ArrayList<Node> reconstructPath(HashMap<Node, Node> cameFrom, Node current)
    {
        ArrayList<Node> totalPath = new ArrayList<>();
        totalPath.add(current);
        
        while (cameFrom.containsKey(current))
        {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        
        return totalPath;
    }
    
    private void initializeScores(HashMap<Node, Double> fScores, HashMap<Node, Double> gScores)
    {
        for (Node[] row : grid)
        {
            for (Node node : row)
            {
                if (!node.isWall())
                {
                    fScores.put(node, Double.POSITIVE_INFINITY);
                    gScores.put(node, Double.POSITIVE_INFINITY);
                }
            }
        }
    }
    
    private double heuristic(Node p1, Node p2, HModel model)
    {
        switch (model)
        {
        case EUCLIDEAN:
            return Math.hypot(Math.abs(p1.getRow() - p2.getRow()),
                    Math.abs(p1.getCol() - p2.getCol()));
        case MANHATTAN:
            return Math.abs(p1.getRow() - p2.getRow()) + Math.abs(p1.getCol() - p2.getCol());
        default:
            throw new IllegalArgumentException("Invalid model");
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
    
    private void generateEndpoints()
    {
        int randRow = (int) (Math.random() * ROWS);
        int randCol = (int) (Math.random() * COLS);
        start = grid[randRow][randCol];
        start.setWall(false);
        
        do
        {
            randRow = (int) (Math.random() * ROWS);
            randCol = (int) (Math.random() * COLS);
        } while (grid[randRow][randCol] == start);
        end = grid[randRow][randCol];
        end.setWall(false);
    }
}