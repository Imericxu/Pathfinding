package com.imericxu.pathfinder;

import com.imericxu.pathfinder.visual.AnimatedCanvas;

import javax.swing.*;

public class Main
{
    private static JFrame frame;
    private static AnimatedCanvas canvas;
    private static final int ROWS = 60;
    private static final int COLS = 60;
    
    public static void main(String[] args)
    {
        frame = new JFrame("A* Pathfinder");
        canvas = new AnimatedCanvas(ROWS, COLS);
        frame.add(canvas);
        frame.getContentPane().setPreferredSize(canvas.getSize());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        JFrame resetFrame = new JFrame("Reset Window");
        JButton btn = new JButton("Reset");
        btn.addActionListener(e -> reset());
        JPanel panel = new JPanel();
        panel.add(btn);
        panel.validate();
        resetFrame.setContentPane(panel);
        resetFrame.pack();
        resetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resetFrame.setLocation(0, 0);
        resetFrame.setVisible(true);
    }
    
    private static void reset()
    {
        frame.remove(canvas);
        frame.revalidate();
        frame.repaint();
        canvas = new AnimatedCanvas(ROWS, COLS);
        frame.add(canvas);
        frame.revalidate();
    }
}
