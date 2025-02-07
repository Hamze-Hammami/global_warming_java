package src;

public class MapCoordinate implements Comparable{
    private double  longitude, latitude, altitude;

    public MapCoordinate(double longitude, double latitude, double altitude){
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public double getDistanceBetween(MapCoordinate coord1, MapCoordinate coord2){
        // Using the Haversine formula
        // Reference: https://www.movable-type.co.uk/scripts/latlong.html

        double lat1 = coord1.getLatitude(), lat2 = coord2.getLatitude();
        double long1 = coord1.getLongitude(), long2 = coord2.getLongitude();
        double radius = 6371e3;
        double theta1 = coord1.getLatitude();
        double theta2 = coord2.getLatitude();
        double deltaTheta = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(long2 - long1);

        double a = Math.sin(deltaTheta/2) * Math.sin(deltaTheta/2) +
                Math.cos(theta1) * Math.cos(theta2) *
                Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radius * c;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getAltitude() {
        return altitude;
    }

    @Override
    public int compareTo(Object o) {
        MapCoordinate coord = (MapCoordinate) o;

        // Ternary Operator
        return this.longitude == coord.getLongitude() &&
                this.latitude == coord.getLatitude() &&
                this.altitude == coord.getAltitude() ? 1 : 0;
    }

    @Override
    public boolean equals(Object o){
        return this.compareTo((MapCoordinate) o) > 0;
    }

    @Override
    public String toString(){
        return "(" + longitude + ", " + latitude + ", " + altitude + ")";
    }
}
