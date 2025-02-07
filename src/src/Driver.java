package src;

import javax.swing.*;

public class Driver {

        public static void main(String[] args) {
                double seaLevel = 0;
                if(args.length != 0) {
                        seaLevel = Double.parseDouble(args[0]);
                }
                JFrame frame = new JFrame("Earth");
                TopLevelWindow tlw = new TopLevelWindow(seaLevel);
                frame.addMouseListener(tlw);
                frame.getContentPane().add(tlw);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.pack();
                frame.setVisible(true);
        }
}