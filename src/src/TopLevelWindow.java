package src;
import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TopLevelWindow extends JPanel implements MouseListener {

    static final int WIDTH = 1080, HEIGHT = 540;
    static final double SCALE_FACTOR =3;
    private Earth earth;
    private double[][] coordinates;
    private double sealevel;
    private ArrayList<MapCoordinate> coords;
    public Map<Pair<Double, Double>, MapCoordinate> ellipsePositions;
    public ArrayList<Ellipse2D> ellipses;
    MapCoordinate prevSelectedCoord = null;
    MapCoordinate selectedCoord = null;
    File coordsFile = new File("coordinates.txt");

    public TopLevelWindow(double seaLevel){
        earth = new Earth(seaLevel);
        coords = new ArrayList<>();
        clearFile();
        this.sealevel = seaLevel;
        coordinates = earth.getArrayOfEarth();
        ellipsePositions = new HashMap<>();
        ellipses = new ArrayList<>();
    }

    private static Pair<Double,Double> scaleCoordinates(double longitude, double latitude){
        return new Pair<>(((longitude + 180) % 360) * SCALE_FACTOR , (latitude - 90) * -SCALE_FACTOR);
    }

    private void writeCoordToFile(MapCoordinate coord){
        try {
            Files.write(Paths.get(coordsFile.getName()), coord.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFile(){
        try {
            PrintWriter writer = new PrintWriter(coordsFile);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g.create();
        for(int i = 0; i < Earth.getHEIGHT(); i++){
            double longitude = coordinates[i][0];
            double latitude = coordinates[i][1];
            int altitude = (int) coordinates[i][2];
            Pair<Double,Double> coord = scaleCoordinates(longitude, latitude);
            ellipsePositions.put(coord, new MapCoordinate(longitude,latitude,altitude));
            Ellipse2D.Double point = new Ellipse2D.Double(coord.getKey(), coord.getValue(),2,2);
            ellipses.add(point);
            Color chosenColor;
            if(altitude < sealevel*2.5) {
                chosenColor = new Color(98, 173, 250);

                // MAKE COLORS DARKER
                for (int j = 0; j < Math.abs(altitude); j += 50) {
                    chosenColor = new Color(Math.max(chosenColor.getRed() - 25, 0), Math.max(chosenColor.getGreen() - 13, 0), chosenColor.getBlue() - 1);
                }
            } else if(altitude > sealevel * 2.5 && altitude < 150 + sealevel * 2.5){
                chosenColor = new Color(171, 210, 171);
                for(int j = 0; j< altitude; j += 50){
                    chosenColor = new Color(Math.max(chosenColor.getRed() - 25, 0), chosenColor.getGreen() -1, Math.max(chosenColor.getBlue() - 13, 0));
                }

            } else if (altitude > 150 + sealevel * 2.5 && altitude < 600 + sealevel * 2.5){
                chosenColor = new Color(230, 212, 164);
                for(int j = 0; j < altitude; j+= 40){
                    chosenColor.darker().darker().darker();
                }
            } else if (altitude > 600 + sealevel * 2.5 && altitude < 1900 + sealevel * 2.5){
                chosenColor = new Color(167, 161, 151);
                chosenColor.brighter();
                for(int j = 0; j < altitude; j+= 100){
                    chosenColor = new Color(Math.max(chosenColor.getRed() - 1, 0), Math.max(chosenColor.getGreen() -1,0), Math.max(chosenColor.getBlue() - 1, 0));
                }
            } else {
                chosenColor = new Color(255, 255, 255);
                for(int j = 0; j < altitude; j+= 700){
                    chosenColor = new Color(Math.max(chosenColor.getRed() - 10, 0), Math.max(chosenColor.getGreen() -10,0), Math.max(chosenColor.getBlue() - 10, 0));
                }
            }
            graphics2D.setColor(chosenColor);
            graphics2D.fill(point);
        }
    }

    @Override
    @Transient
    public Color getBackground(){
        return Color.black;
    }



    @Override
    @Transient  // Not Serializable
    public Dimension getPreferredSize(){
        return new Dimension(WIDTH,HEIGHT);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }



    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        long input = mouseEvent.getModifiersEx();
        Point mousePos = mouseEvent.getPoint();
        if(input == InputEvent.BUTTON1_DOWN_MASK){
            // Left Click
            for(Ellipse2D circle : ellipses){
                if(circle.contains(mousePos)){
                    prevSelectedCoord = selectedCoord;
                    selectedCoord = ellipsePositions.get(new Pair<>(circle.getX(), circle.getY()));
                    if(prevSelectedCoord != null)
                        System.out.println("Distance between " + selectedCoord.toString() + " and " + prevSelectedCoord.toString() + " = " + selectedCoord.getDistanceBetween(selectedCoord, prevSelectedCoord) + "km");
                    coords.add(selectedCoord);
                    // add selectedCoord to file
                    writeCoordToFile(selectedCoord);
                    System.out.println("(" + selectedCoord.getLongitude() + ", " + selectedCoord.getLatitude() + ")");
                    Collections.sort(coords);
                    break;
                }
            }

        } else if (input == InputEvent.BUTTON3_DOWN_MASK){
            // Right Click
            if(!coords.isEmpty()) {
                if(coords.contains(selectedCoord)) {
                    coords.remove(selectedCoord);
                    System.out.println("Removed " + selectedCoord.toString() + " from array");
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}
