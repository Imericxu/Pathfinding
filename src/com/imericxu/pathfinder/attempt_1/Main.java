package com.imericxu.pathfinder.attempt_1;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Pathfinder");
        Canvas canvas = new Canvas();
        frame.add(canvas);
        frame.getContentPane().setPreferredSize(canvas.getSize());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
