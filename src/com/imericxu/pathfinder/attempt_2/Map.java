package com.imericxu.pathfinder.attempt_2;

import com.imericxu.pathfinder.attempt_2.maps.MapList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Map
{
    private Node[][] grid;
    private Node start;
    private Node end;
    private ArrayList<Node> openList = new ArrayList<>();
    private ArrayList<Node> closedList = new ArrayList<>();
    
    public Map(MapList mapSelection)
    {
        readMapFile(mapSelection);
    }
    
    public Map(int rows, int cols)
    {
        grid = new Node[rows][cols];
        
        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                grid[i][j] = new Node(i, j);
            }
        }
        
        /*int randRow = (int) (Math.random() * rows);
        int randCol = (int) (Math.random() * cols);
        start = grid[randRow][randCol];
        start.setWall(false);
        
        do
        {
            randRow = (int) (Math.random() * rows);
            randCol = (int) (Math.random() * cols);
        } while (grid[randRow][randCol] != start);
        end = grid[randRow][randCol];
        end.setWall(false);*/
    }
    
    public Node getNode(int row, int col)
    {
        return grid[row][col];
    }
    
    public boolean inOpenList(Node node)
    {
        return openList.contains(node);
    }
    
    public boolean inClosedList(Node node)
    {
        return closedList.contains(node);
    }
    
    public ArrayList<Node> aStar(Node start, Node end, HModel model)
    {
        start.setWall(false);
        end.setWall(false);
        
        openList.add(start);
        HashMap<Node, Node> cameFrom = new HashMap<>();
        /**
         * Map of total cost of node
         */
        HashMap<Node, Double> fScores = new HashMap<>();
        /**
         * Map of cost from start to node
         */
        HashMap<Node, Double> gScores = new HashMap<>();
        
        initializeScores(gScores, fScores);
        
        gScores.replace(start, 0.0);
        fScores.replace(start, heuristic(start, end, model));
        
        while (!openList.isEmpty())
        {
            // Get lowest f score
            Node current = openList.get(0);
            for (Node node : openList.subList(1, openList.size()))
            {
                if (fScores.get(node) < fScores.get(current))
                {
                    current = node;
                }
            }
            
            if (current == end)
            {
                return reconstructPath(cameFrom, current);
            }
            
            openList.remove(current);
            closedList.add(current);
            
            ArrayList<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors)
            {
                if (!inClosedList(neighbor))
                {
                    // Current g + weight of edge (just 1 for now)
                    double tempG = gScores.get(current) + 1; // + d(current, neighbor)
                    if (tempG < gScores.get(neighbor))
                    {
                        cameFrom.put(neighbor, current);
                        gScores.replace(neighbor, tempG);
                        fScores.replace(neighbor, tempG + heuristic(neighbor, end,
                                HModel.EUCLIDEAN));
                        
                        if (!openList.contains(neighbor))
                        {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    public ArrayList<Node> slowedAStar(Node start, Node end, HModel model) throws InterruptedException
    {
        start.setWall(false);
        end.setWall(false);
        
        openList.add(start);
        // Map of nodes pointing to the previous node
        HashMap<Node, Node> cameFrom = new HashMap<>();
        // Map of total cost of nodes
        HashMap<Node, Double> fScores = new HashMap<>();
        // Map of cost from start to node
        HashMap<Node, Double> gScores = new HashMap<>();
        
        initializeScores(gScores, fScores);
        
        gScores.replace(start, 0.0);
        fScores.replace(start, heuristic(start, end, model));
        
        while (!openList.isEmpty())
        {
            // Get lowest f score
            Node current = openList.get(0);
            for (Node node : openList.subList(1, openList.size()))
            {
                if (fScores.get(node) < fScores.get(current))
                {
                    current = node;
                }
            }
            
            if (current == end)
            {
                return reconstructPath(cameFrom, current);
            }
            
            openList.remove(current);
            closedList.add(current);
            
            ArrayList<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors)
            {
                if (!inClosedList(neighbor))
                {
                    // Current g + weight of edge (just 1 for now)
                    double tempG = gScores.get(current) + 1; // + d(current, neighbor)
                    if (tempG < gScores.get(neighbor))
                    {
                        cameFrom.put(neighbor, current);
                        gScores.replace(neighbor, tempG);
                        fScores.replace(neighbor, tempG + heuristic(neighbor, end,
                                HModel.EUCLIDEAN));
                        
                        if (!openList.contains(neighbor))
                        {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
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
    
    private void initializeScores(HashMap<Node, Double> gScores, HashMap<Node, Double> fScores)
    {
        for (Node[] row : grid)
        {
            for (Node node : row)
            {
                if (!node.isWall())
                {
                    gScores.put(node, Double.POSITIVE_INFINITY);
                    fScores.put(node, Double.POSITIVE_INFINITY);
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
    
    private void readMapFile(MapList mapSelection)
    {
        try
        {
            String absolutePath = new File("").getAbsolutePath();
            File mapFile = new File(absolutePath + "/src/com/imericxu/pathfinder/attempt_2" +
                    "/maps/" + mapSelection.getName());
            Scanner sc = new Scanner(mapFile);
            
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            grid = new Node[rows][cols];
            
            for (int row = 0; row < grid.length; ++row)
            {
                for (int col = 0; col < grid[0].length; ++col)
                {
                    int cell = sc.nextInt();
                    
                    if (cell != 1)
                    {
                        grid[row][col] = new Node(row, col);
                        if (cell == 2)
                        {
                            start = grid[row][col];
                        }
                        else if (cell == 3)
                        {
                            end = grid[row][col];
                        }
                    }
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
