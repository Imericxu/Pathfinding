package com.imericxu.pathfinder.attempt_2;

import com.imericxu.pathfinder.attempt_2.visual.AnimatedCanvas;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("A* Pathfinder");
        AnimatedCanvas canvas = new AnimatedCanvas(30, 30);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(canvas.getSize());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
