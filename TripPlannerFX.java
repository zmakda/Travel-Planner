import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javafx.collections.FXCollections;

public class TripPlannerFX extends Application {
    private Stage primaryStage;
    private TextField city;
    private TextField state;
    private TextField LatDegrees;
    private TextField LatMinutes;
    private TextField LongDegrees;
    private TextField LongMinutes;
    private File f = new File("stops.txt");
    private PrintWriter pw;
    private FileChooser fileChooser;
    private String filepath;
    private StackPane stackpanemap;
    private AnchorPane draw;
    private static ObservableList<Stop> tripObservableList = FXCollections.observableArrayList();
    private static ListView<Stop> possibleStopsTrip = new ListView(tripObservableList);





    public void start(Stage initprimaryStage) throws Exception {
        primaryStage = initprimaryStage;
        layoutGUI();
    }

    public void layoutGUI() {
        primaryStage.setTitle("Vacation Planner");

        //main boxes
        VBox main = new VBox();
        GridPane gptop = new GridPane();
        HBox HBMiddle = new HBox();
        HBMiddle.setSpacing(10);
        HBox HBBottom = new HBox();
        HBBottom.setSpacing(10);

        //buttons for top box
        javafx.scene.control.Button newB = new javafx.scene.control.Button("New");
        javafx.scene.control.Button loadB = new javafx.scene.control.Button("Load");
        javafx.scene.control.Button saveB = new javafx.scene.control.Button("Save");
        gptop.add(newB, 0, 0);
        gptop.add(loadB, 1, 0);
        gptop.add(saveB, 2, 0);
        gptop.setHgap(30.0);


        final Trip[] currentTrip = {null};
        Label labelTotalMileage = new Label("Total Mileage: ");


        //New Trip Button
        EventHandler<ActionEvent> NewTrip = new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("Trip");
                dialog.setTitle("Trip Dialog");
                dialog.setHeaderText("Vacation Planner");
                dialog.setContentText("Please enter your trip name: ");


                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    primaryStage.setTitle("Vacation Planner " + result.get());
                    Trip newTrip = new Trip(result.get());
                    currentTrip[0] = newTrip;
                    possibleStopsTrip.setItems(currentTrip[0].getStops());

                }
                Map();
            }
        };
        newB.setOnAction(NewTrip);

        //Load Trip Button
        EventHandler<ActionEvent> LoadTrip = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                possibleStopsTrip.getItems().clear();
                fileChooser = new FileChooser();
                filepath = Paths.get(".").toAbsolutePath().normalize().toString();
                fileChooser.setInitialDirectory(new File(filepath));
                FileChooser.ExtensionFilter txtExtensionFilter = new FileChooser.ExtensionFilter(
                                "Text File (.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(txtExtensionFilter);
                fileChooser.setSelectedExtensionFilter(txtExtensionFilter);
                File openFile = fileChooser.showOpenDialog(primaryStage);
                String filename = openFile.getName();
                initList(filename, tripObservableList);
                possibleStopsTrip.setItems(tripObservableList);

                labelTotalMileage.setText("Total Mileage: " + findTotalMileage());
                Map();





            }
        };
        loadB.setOnAction(LoadTrip);

        //Save Trip Button
        EventHandler<ActionEvent> SaveTrip = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                try {
                    filepath = Paths.get(".").toAbsolutePath().normalize().toString();
                    fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File(filepath));
                    FileChooser.ExtensionFilter txtExtensionFilter = new FileChooser.ExtensionFilter(
                                    "Text File (.txt)", "*.txt");
                    fileChooser.getExtensionFilters().add(txtExtensionFilter);
                    fileChooser.setSelectedExtensionFilter(txtExtensionFilter);
                    fileChooser.setInitialFileName(primaryStage.getTitle().substring(17) + ".txt");
                    File selectedfile = fileChooser.showSaveDialog(primaryStage);
                    FileWriter fileWriter = new FileWriter(selectedfile, false);
                    pw = new PrintWriter(fileWriter);
                    for (int i = 0; i < currentTrip[0].getStops().size(); i++){
                        pw.println(currentTrip[0].getStops().get(i).toData());
                    }
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        saveB.setOnAction(SaveTrip);

        //Toggle
        final Stop[] current = new Stop[1];
        EventHandler<MouseEvent> toggle = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (current[0] != null && current[0] == possibleStopsTrip.getSelectionModel().getSelectedItem()){
                    possibleStopsTrip.getSelectionModel().clearSelection();
                    current[0] = null;
                }
                else {
                    current[0] =possibleStopsTrip.getSelectionModel().getSelectedItem();
                    city.setText(current[0].getCityName());
                    state.setText(current[0].getAbbreviation());
                    LatDegrees.setText("" + current[0].getLatDeg());
                    LatMinutes.setText("" + current[0].getLatMin());
                    LongDegrees.setText("" + current[0].getLongDeg());
                    LongMinutes.setText("" + current[0].getLongMin());
                }
            }
        };
        possibleStopsTrip.setOnMouseClicked(toggle);



        //image
        final String FILE_PROTOCOL = "file:";
        final String IMAGES_PATH = "./Image/";
        final String USA_MAP_IMAGE_URL = FILE_PROTOCOL + IMAGES_PATH + "usa_map.jpg";
        Image usaMap = new Image(USA_MAP_IMAGE_URL);
        ImageView iv1 = new ImageView();
        iv1.setImage(usaMap);
        iv1.setFitWidth(640);
        iv1.setFitHeight(334.5);
        stackpanemap = new StackPane();
        draw = new AnchorPane();
        stackpanemap.getChildren().addAll(iv1, draw);





        //middle section
        javafx.scene.control.Label trip_stopslabel = new Label("Trip Stops");
        GridPane gp2 = new GridPane();
        javafx.scene.control.Button AddtoTrip = new javafx.scene.control.Button("+");
        javafx.scene.control.Button RemovefromTrip = new javafx.scene.control.Button("-");
        gp2.add(AddtoTrip, 0, 0);
        gp2.add(RemovefromTrip, 1, 0);
        HBox f = new HBox();
        f.getChildren().addAll(trip_stopslabel, gp2);




        VBox VBoxInMiddleHBox = new VBox();
        VBoxInMiddleHBox.setStyle("-fx-background-color:orange;"
                + "-fx-border-color:black;"+ "-fx-padding: 10;"
                + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + "-fx-border-radius: 10;" + "-fx-border-color:black;");
     //   Label labelTotalMileage = new Label("Total Mileage: ");




        VBoxInMiddleHBox.getChildren().addAll(f, possibleStopsTrip, labelTotalMileage);
        HBMiddle.getChildren().addAll(stackpanemap, VBoxInMiddleHBox);


        //bottom section
        VBox VBoxInBottomBox = new VBox();
        HBox g = new HBox();
        Label labelPossibleStops = new Label("Possible Stops: ");
        javafx.scene.control.Button AddtoStops = new javafx.scene.control.Button("+");
        javafx.scene.control.Button RemovefromStops = new javafx.scene.control.Button("-");




        GridPane gp3 = new GridPane();
        gp3.add(AddtoStops, 0, 0);
        gp3.add(RemovefromStops, 1, 0);
        g.getChildren().addAll(labelPossibleStops, gp3);


        ObservableList<Stop> cities = FXCollections.observableArrayList();
        initList("stops.txt",cities);
        ListView<Stop> possibleStopsList = new ListView<>(cities);

        //Add Stop Button
        EventHandler<MouseEvent> plusbutton = new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                addtopossiblestop(cities);
                city.clear();
                state.clear();
                LatDegrees.clear();
                LatMinutes.clear();
                LongDegrees.clear();
                LongMinutes.clear();
            }
        };

        AddtoStops.setOnMouseClicked(plusbutton);




        //       Remove stop button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this stop?");
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    possibleStopsList.getItems().remove(possibleStopsList.getSelectionModel().getSelectedItem());
                    removeFromFile("stops.txt", possibleStopsList.getItems());
                    city.clear();
                    state.clear();
                    LatDegrees.clear();
                    LatMinutes.clear();
                    LongDegrees.clear();
                    LongMinutes.clear();

                }
            }
        };
        RemovefromStops.setOnAction(event);


        //Adds Stops to trip
        EventHandler<ActionEvent> plusButton = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {


                if (possibleStopsTrip.getSelectionModel().isEmpty()) {

                    currentTrip[0].addStop(possibleStopsList.getSelectionModel().getSelectedItem());
                    Map();
                }
                else{
                    currentTrip[0].insertStops(possibleStopsTrip.getSelectionModel().getSelectedIndex(),
                            possibleStopsList.getSelectionModel().getSelectedItem());
                    Map();
                }

                labelTotalMileage.setText("Total Mileage: " + findTotalMileage());


            }
        };
        AddtoTrip.setOnAction(plusButton);

        //       Remove stop from trip button
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this stop?");
        EventHandler<ActionEvent> removefromTrip = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Optional<ButtonType> result = alert2.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    possibleStopsTrip.getItems().remove(possibleStopsTrip.getSelectionModel().getSelectedItem());
                }

                labelTotalMileage.setText("Total Mileage: " + findTotalMileage());
                Map();
            }
        };
        RemovefromTrip.setOnAction(removefromTrip);



        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        city = new TextField();
        state = new TextField();
        LatDegrees = new TextField();
        LatMinutes = new TextField();
        LongDegrees = new TextField();
        LongMinutes = new TextField();
        pane.add(new Label("City:"), 0, 0);
        pane.add(city, 1, 0);
        pane.add(new Label("State:"), 0, 1);
        pane.add(state, 1, 1);
        pane.add(new Label("Latitude Degrees:"), 0, 2);
        pane.add(LatDegrees, 1, 2);
        pane.add(new Label("Latitude Minutes:"), 0, 3);
        pane.add(LatMinutes, 1, 3);
        pane.add(new Label("Longitude Degrees:"), 0, 4);
        pane.add(LongDegrees, 1, 4);
        pane.add(new Label("Longitude Minutes:"), 0, 5);
        pane.add(LongMinutes, 1, 5);
        Button Update = new Button("Update");
        pane.add(Update, 1, 6);
        GridPane.setHalignment(Update, HPos.RIGHT);

        //Shows City Information when clicked
        final Stop[] curStop = new Stop[1];
        EventHandler<javafx.scene.input.MouseEvent> event2 = new EventHandler<MouseEvent>() {

            public void handle(MouseEvent e)
            {
               if(curStop[0] != null && curStop[0] == possibleStopsList.getSelectionModel().getSelectedItem()) {
                   possibleStopsList.getSelectionModel().clearSelection();
                   curStop[0] = null;
                   city.clear();
                   state.clear();
                   LatDegrees.clear();
                   LatMinutes.clear();
                   LongDegrees.clear();
                   LongMinutes.clear();
               }
               else{
                   curStop[0] =possibleStopsList.getSelectionModel().getSelectedItem();
                   city.setText(curStop[0].getCityName());
                   state.setText(curStop[0].getAbbreviation());
                   LatDegrees.setText("" + curStop[0].getLatDeg());
                   LatMinutes.setText("" + curStop[0].getLatMin());
                   LongDegrees.setText("" + curStop[0].getLongDeg());
                   LongMinutes.setText("" + curStop[0].getLongMin());
               }
               }

            }
        ;
        possibleStopsList.setOnMouseReleased(event2);

        //Update Button
        EventHandler<ActionEvent> UpdateButton = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e)
            {
                Stop curStop = (Stop) possibleStopsList.getSelectionModel().getSelectedItem();
                curStop.setCityName(city.getText());
                curStop.setAbbreviation(state.getText());
                curStop.setLatDeg(Integer.parseInt(LatDegrees.getText()));
                curStop.setLatMin(Integer.parseInt(LatMinutes.getText()));
                curStop.setLongDeg(Integer.parseInt(LongDegrees.getText()));
                curStop.setLongMin(Integer.parseInt(LongMinutes.getText()));

                Alert alert = new Alert(Alert.AlertType.ERROR);
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a Latitude from 25° to 50°");
                alert2.setTitle("Error Dialog");
                alert2.setHeaderText("Invalid Input");
                alert2.setContentText("Please enter a Longitude from 65° to 125°");

                if (Integer.parseInt(LatDegrees.getText()) < 25 || Integer.parseInt(LatDegrees.getText()) > 50){
                    alert.showAndWait();

                }
                else if(Integer.parseInt(LongDegrees.getText()) < 65 || Integer.parseInt(LongDegrees.getText()) > 125){
                    alert2.showAndWait();
                }
                else{

                addToFile(curStop, cities);
                Collections.sort(cities);

            }

        }
        };
        Update.setOnAction(UpdateButton);










        VBoxInBottomBox.getChildren().addAll(g, possibleStopsList);
        VBoxInBottomBox.setStyle("-fx-background-color:orange;" + "-fx-padding: 10;"
                + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + "-fx-border-radius: 10;" + "-fx-border-color:black;");

        final String FILE_PROTOCOL2 = "file:";
        final String IMAGES_PATH2 = "./Image/";
        final String JAMES = FILE_PROTOCOL2 + IMAGES_PATH2 + "JamesFinn.jpg";
        Image James = new Image(JAMES);
        ImageView thankyouJames = new ImageView();
        thankyouJames.setImage(James);

        HBBottom.getChildren().addAll(VBoxInBottomBox, pane);
        pane.setStyle("-fx-background-color:orange;"
                + "-fx-border-color:black;" + "-fx-padding: 10;"
                + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + "-fx-border-radius: 10;" );


        gptop.setStyle("-fx-background-color:orange;" + "-fx-padding: 10;"
                + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + "-fx-border-radius: 10;" + "-fx-border-color:black;");



        //don't touch
        main.setSpacing(10);
        main.getChildren().addAll(gptop, HBMiddle, HBBottom);
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

        public void addtopossiblestop(ObservableList<Stop> cities){

            String cityName = city.getText();
            String stateName = state.getText();
            int latDegreesString = Integer.parseInt(LatDegrees.getText());
            int latMinutesString =  Integer.parseInt(LatMinutes.getText());
            int longDegreesString = Integer.parseInt(LongDegrees.getText()) ;
            int longMinutesString = Integer.parseInt(LongMinutes.getText());
            Stop city = new Stop(cityName,stateName, latDegreesString, latMinutesString, longDegreesString,
                    longMinutesString);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter a Latitude from 25° to 50°");
            alert2.setTitle("Error Dialog");
            alert2.setHeaderText("Invalid Input");
            alert2.setContentText("Please enter a Longitude from 65° to 125°");
            if (latDegreesString < 25 || latDegreesString > 50){
                alert.showAndWait();

            }
            else if(longDegreesString < 65 || longDegreesString > 125){
                alert2.showAndWait();
            }
            else{
                cities.add(city);
                addToFile(city,cities);
                Collections.sort(cities);
            }



    }


        public static void main (String[]args){
            launch(args);
        }

        public void addToFile(Stop city, ObservableList<Stop> list){

            try{
                pw = new PrintWriter(f);
            }
            catch (FileNotFoundException e) {
            }
            for(Stop c: list){
                pw.println(c.toData());
            }
            pw.close();
        }

        public void removeFromFile(String filename, ObservableList<Stop> list){

            try {
                PrintWriter pw = new PrintWriter("stops.txt");
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

           addToFile(null, list);


            }

        public void initList(String filename,ObservableList<Stop> list){
            BufferedReader bf = null;

            try{
                bf = new BufferedReader(new FileReader(new File(filename)));
                while(true){
                    String x = bf.readLine();
                    if (x == null)
                        break;
                    String[] arr = x.split("_");
                    list.add(new Stop(arr[0],arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),
                            Integer.parseInt(arr[4]),Integer.parseInt(arr[5])));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void Map() {

            draw.getChildren().removeAll(draw.getChildren());

            double BeforeLat = -1;
            double BeforeLon = -1;

            for (int i = 0; i < possibleStopsTrip.getItems().size(); i++) {

                Stop temp = possibleStopsTrip.getItems().get(i);
                Circle tempC = new Circle(5);
                tempC.setStroke(Color.GOLD);

                double newlat = temp.getLatDeg() + (temp.getLatMin()/60.0);
                double newlon = temp.getLongDeg() + (temp.getLongMin()/60.0);

                newlat = 334.5 - ((newlat - 25)*(334.5/25));
                newlon = 640 - ((newlon - 65)*(640/60.0));

                if (BeforeLat != -1 && BeforeLon != -1) {
                    Line path = new Line();
                    path.setStroke(Color.GOLD);
                    path.setStartX(BeforeLon);
                    path.setStartY(BeforeLat);
                    path.setEndX(newlon);
                    path.setEndY(newlat);
                    draw.getChildren().add(path);
                }
                BeforeLat = newlat;
                BeforeLon = newlon;

                tempC.setCenterX(newlon);
                tempC.setCenterY(newlat);

                draw.getChildren().add(tempC);


            }
        }

        public static double totalMileage(Stop stop1, Stop stop2){
            double RADIAN_FACTOR = 180/Math.PI;
            double EARTH_RADIUS = 3958.756;
            double stop1lat;
            double stop1long;
            double stop2lat;
            double stop2long;

            stop1lat = stop1.getLatDeg() + (stop1.getLatMin()/60.0);
            stop1long = stop1.getLongDeg() + (stop1.getLongMin()/60.0);
            stop2lat = stop2.getLatDeg() + (stop2.getLatMin()/60.0);
            stop2long = stop2.getLongDeg() + (stop2.getLongMin()/60.0);

            double x = (Math.sin(stop1lat/RADIAN_FACTOR) * Math.sin(stop2lat/RADIAN_FACTOR))
                    + (Math.cos(stop1lat/RADIAN_FACTOR)
                    * Math.cos(stop2lat/RADIAN_FACTOR)
                    * Math.cos((stop2long/RADIAN_FACTOR) - (stop1long/RADIAN_FACTOR)));

            double distance = EARTH_RADIUS * Math.atan((Math.sqrt(1 - Math.pow(x, 2))/x));

            return distance;

        }

        public double findTotalMileage(){
            double total = 0.0;

            for(int i = 0; i < possibleStopsTrip.getItems().size()-1; i++){
                if (possibleStopsTrip.getItems().size() > 1){
                    double mileage = totalMileage(possibleStopsTrip.getItems().get(i),
                            possibleStopsTrip.getItems().get(i+1));
                    total += mileage;
                }
            }
            double roundedTotal = Math.round(total);
            return roundedTotal;

        }





    }


