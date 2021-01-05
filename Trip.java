import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class Trip {

    private String name;
    ObservableList<Stop>TripList = FXCollections.observableArrayList();


    public Trip(String intName){
        name=intName;

    }

    public void addStop(Stop city){
        TripList.add(0, city);
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<Stop> getStops() {
        return TripList;
    }

    public void setStops(ObservableList<Stop> stops) {
        TripList = stops;
    }

    public void insertStops(int index, Stop stop){
        TripList.add(index, stop);
    }


    public String toString() {
        String newString = "";
        newString += (name + ":");
        for (int i = 1; i <= TripList.size(); i++){
            newString += ("" + i + ":");
            newString += (TripList.get(i-1).toString() + "\n");
        }
        return newString;
    }

    public static void addTrip (Stop stop){

    }

}
