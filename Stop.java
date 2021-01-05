public class Stop implements Comparable {

    private String cityName;
    private String abbreviation;
    private int latDeg;
    private int latMin;
    private int longDeg;
    private int longMin;




    public Stop(String cityName, String abbreviation, int latDeg, int latMin, int longDeg, int longMin) {
        this.cityName = cityName;
        this.abbreviation = abbreviation;
        this.latDeg = latDeg;
        this.latMin = latMin;
        this.longDeg = longDeg;
        this.longMin = longMin;

    }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public void setLatDeg(int latDeg) {
        this.latDeg = latDeg;
    }

    public void setLatMin(int latMin) {
        this.latMin = latMin;
    }

    public void setLongDeg(int longDeg) {
        this.longDeg = longDeg;
    }

    public void setLongMin(int longMin) {
        this.longMin = longMin;
    }

    public String getCityName() {
        return cityName;
    }

    public int getLatDeg() {
        return latDeg;
    }

    public int getLatMin() {
        return latMin;
    }

    public int getLongDeg() {
        return longDeg;
    }

    public int getLongMin() {
        return longMin;
    }

    public String getAbbreviation() { return abbreviation; }

    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    public String toString() {
        return cityName + ", " + abbreviation;
    }

    public int compareTo(Object obj) {
        Stop p = (Stop) obj;
        return cityName.compareTo(p.cityName);
    }

    public String toData(){
        return cityName + "_" + abbreviation + "_" + latDeg + "_" + latMin + "_" + longDeg + "_" + longMin;
    }
}
