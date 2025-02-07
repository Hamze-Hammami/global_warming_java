package src;
import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
public class Earth {
    private static double[][] arrayOfEarth;
    final static int HEIGHT = 2500000, WIDTH = 3;
    private static double seaLevel;
    // map = {(long, lat) : altitude}
    static Map<Pair<Double,Double>, Double> mapOfEarth;
    public Earth(double seaLevel){
        Earth.seaLevel = seaLevel;
        readDataArray("earth.xyz");
        readDataMap("earth.xyz");
    }

    // these are all out getters which will return the value

    public double[][] getArrayOfEarth() {

        return arrayOfEarth;
    }

    public Map<Pair<Double,Double>, Double> getMapOfEarth() {

        return mapOfEarth;
    }

    public static int getHEIGHT() {

        return HEIGHT;
    }

    public static int getWIDTH() {

        return WIDTH;
    }
    public static void readDataArray(String filename){
        arrayOfEarth = new double[HEIGHT][WIDTH];
        File file = new File(filename);
        int j = 0;
        try(Scanner sc = new Scanner(file)){
            while(sc.hasNextLine()) {
                String row = sc.nextLine();
                String[] line = row.split("\\s+");
                arrayOfEarth[j][0] = Double.parseDouble(line[0]);
                arrayOfEarth[j][1] = Double.parseDouble(line[1]);
                arrayOfEarth[j][2] = Double.parseDouble(line[2]) + seaLevel;

                j++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static List<Pair<Double,Double>> coordinatesAbove(double altitude){
        List<Pair<Double,Double>> result = new ArrayList<>();
        // Add altitudes higher than given to the ArrayList.
        for(int i = 0; i < HEIGHT; i++){
                if(arrayOfEarth[i][2] > altitude){
                result.add(new Pair<>(arrayOfEarth[i][0], arrayOfEarth[i][1]));
            }
        }
        return result;
    }
    //with this we find all the percentage below
    public static List<Pair<Double, Double>> coordinatesBelow(double altitude){
        List<Pair<Double,Double>> result = new ArrayList<>();
        for(int i = 0; i < HEIGHT; i++){
            if(arrayOfEarth[i][2] < altitude){
                result.add(new Pair<>(arrayOfEarth[i][0], arrayOfEarth[i][1]));
            }
        }
        return result;
    }

    public static void percentageAbove(double altitude){
        // Print percentage of altitudes above
        List<Pair<Double,Double>> coordinates = coordinatesAbove(altitude);
        double size = coordinates.size();

        // Format used to round up the result
        DecimalFormat format = new DecimalFormat("#.#");
        double percentage = size/HEIGHT * 100.0;
        System.out.println("Proportion of coordinates above " + altitude + " meters: " + format.format(percentage) + "%");
    }

    public static void percentageBelow(double altitude){

        List<Pair<Double,Double>> coordinates = coordinatesBelow(altitude);
        double size = coordinates.size();
        DecimalFormat format = new DecimalFormat("#.#");
        double percentage = size/HEIGHT * 100.0;
        System.out.println("Proportion of coordinates below " + altitude + " meters: " + format.format(percentage) + "%");
    }

    private static boolean isValidNumber(String input){
        /*
                Checks if string is a valid number
                                                        */
        if(input == null)
            return false;
        try{
            double num = Double.parseDouble(input);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static void getAltitudeUser(){
        //Read Altitude from User and print percentage above
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.println("Please enter an altitude or \"quit\" to end program: ");
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("quit")){
                System.out.println("Bye!");
                break;
            }
            if(isValidNumber(input)) {
                percentageAbove(Double.parseDouble(input));
            } else {
                System.out.println("Invalid altitude!");
            }
        }
    }

    public static void getCoordinateUser(){

        Scanner scanner = new Scanner(System.in);
        String longitude = "", latitude = "";
        while(true){
            System.out.println("Please enter a longitude (0 -360) and latitude (-90 90): ");
              longitude = scanner.next();
              latitude = scanner.next();
            if(longitude.equalsIgnoreCase("quit") && latitude.equalsIgnoreCase("quit")){
                System.out.println("Bye!");
                break;
            }
            if(isValidNumber(longitude) && isValidNumber(latitude)) {
                double altitude =getAltitude(Double.parseDouble(longitude), Double.parseDouble(latitude));
                if(Double.isNaN(altitude)){
                    System.out.println("Altitude does not exist for these coordinates");
                } else
                    System.out.println("The altitude at longitude " + longitude + " and latitude " + latitude + " is " + altitude);
            } else {
                System.out.println("Invalid coordinates!");
            }
        }
    }

    public static void readDataMap(String filename){
        mapOfEarth = new HashMap<>();
        File file = new File(filename);
        try(Scanner sc = new Scanner(file)){
            while(sc.hasNextLine()) {
                String row = sc.nextLine();
                String[] line = row.split("\\s+");
                double longitude = Double.parseDouble(line[0]);
                double latitude = Double.parseDouble(line[1]);
                double altitude = Double.parseDouble(line[2]) + seaLevel;
                Pair<Double, Double> testPair = new Pair<>(longitude, latitude);
                if(!mapOfEarth.containsKey(testPair))
                    mapOfEarth.put(testPair, altitude);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static double getAltitude(double longitude, double latitude){
            Pair<Double,Double> testPair = new Pair<>(longitude, latitude);
            return mapOfEarth.getOrDefault(testPair, Double.NaN);
    }

    // prints the array
    public static void printArray(double[][] arr){
        for(int i = 0; i < HEIGHT; i++){
        System.out.println(arr[i][0] + " " + arr[i][1] + " " + arr[i][2]);
        }
        }

public static void main(String[] args) {
        readDataArray("earth.xyz");
        readDataMap("earth.xyz");
        // Test PrintArray
        //printArray(arrayOfEarth);

        // Test altitude from System.in
         //getAltitudeUser();
        // Test printing map
         //System.out.println(mapOfEarth.toString());

        // Test getCoordinatesUser()
         getCoordinateUser();


        }

        }

