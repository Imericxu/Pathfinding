package com.imericxu.pathfinder.attempt_2;

import com.imericxu.pathfinder.attempt_2.visual.AnimatedCanvas;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("A* Pathfinder");
        AnimatedCanvas canvas = new AnimatedCanvas(80, 80);
//        Canvas canvas = new Canvas();
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(canvas.getSize());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
