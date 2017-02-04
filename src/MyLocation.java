
public class MyLocation {
    private double longitude;
    private double latitude;

    //широта и долгота
    public MyLocation(double latitude, double longitude) {
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public MyLocation(){
        this.latitude = 0;
        this.longitude = 0;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    //иначе не посылается
    public float getFloatLongitude() {
        return (float)longitude;
    }

    public float getFloatLatitude() {
        return (float)latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //возвращает дистанцию между двумя местами
    public double distanceTo(MyLocation that) {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        double METRS_PER_MILES = 1609.344;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(that.latitude);
        double lon2 = Math.toRadians(that.longitude);

        //не вникай
        //окей))
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles * METRS_PER_MILES;
        return statuteMiles;
    }

    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }

}
