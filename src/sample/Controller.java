package sample;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.beans.binding.ObjectExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.AlertHelper;
import model.StationData;
import model.WeatherLineInfo;
import model.StationsUtil;

public class Controller implements Initializable {

    /////////////////////////// ITERATE OVER .CSV FILE INPUT ///////////////////////////////////////////////////////////

    private List <String> stations;
    private List <String> years;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Dynamically loads data from StationUtil class (package)
        stations = StationsUtil.loadAll();

        metStation.getItems().addAll(stations);
        metStationData.getItems().addAll(stations);
        comparisonBaseStations.getItems().addAll(stations);

        years = Arrays.asList("2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019");

        yearList.getItems().addAll(years);
        toggleData.getItems().addAll(years);
        comparisonBaseYears.getItems().addAll(years);

    }

    /////////////////////////// WINDOW BUTTONS [CLOSE & MINIMIZE] START ////////////////////////////////////////////////

    @FXML
    private Button btnExit;
    @FXML
    private Button btnMini;

    @FXML //Private method to close the application
    private void closeApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to close the app?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();
        }

    }

    @FXML //Private method to minimize the application
    private void minimizeApp() {
        Stage stage = (Stage) btnMini.getScene().getWindow();
        stage.setIconified(true);
    }

    /////////////////////////// WINDOW BUTTONS (CLOSE & MINIMIZE) END //////////////////////////////////////////////////

    /////////////////////////// GENERATE WEATHER REPORT [tab 1] [question 3] START /////////////////////////////////////

    @FXML
    private void CreateReport() {

        int lastStation = 1;

        try {
            FileWriter myWriter;

            myWriter = new FileWriter("Weather Report.txt");
            //Get the folder path to open
            File folder = new File("src/CSV/");
            //Get the the list of all files in the folder
            File[] files = folder.listFiles();
            Window owner = metStation.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information",
                    "Report Generated Successfully");
            System.out.println("Report created");

            //Loop to read all the files from the CSV folder
            for (File file : files) {

                //Get names (replace .csv with "." in the files)
                String FileName = file.getName().replace(".csv", "");

                //  * Title: JavaFX : Java HashMap
                //  * Retrieval date: 21/4/20,
                //  * URL: https://www.w3schools.com/java/java_hashmap.asp -->

                //Hashmap class to implement Map (local) storage/saved class WeatherLineInfo (grouped)
                Map<Integer, Map<Integer, WeatherLineInfo>> DatesMap = new HashMap<Integer, Map<Integer, WeatherLineInfo>>();

                String csvFile = "src/CSV/" + FileName + ".csv";
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";

                //Print out - to generated .txt
                //Sequence Number
                myWriter.write("Number: " + "<" + (Integer.toString(lastStation)) + ">" + "\n");
                //Station Name
                myWriter.write("Name: " + "<" +FileName + ">" + "\n");


                //Set min temp for comparison
                double MaxTemp = 0;
                //Max temp - year set
                WeatherLineInfo MaxTempWeatherInfo = new WeatherLineInfo();

                //Set min temp for comparison
                double MinTemp = 100;
                //Min temp - year set
                WeatherLineInfo MinTempWeatherInfo = new WeatherLineInfo();

                //Set min number for comparison
                Integer TotalFrost = 0;
                double TotalRainfall = 0;

                List<WeatherLineInfo> weatherLineInfos = new ArrayList<WeatherLineInfo>();
                br = new BufferedReader(new FileReader(csvFile));
                //Read file one the time and read  it's line so could add those lines to array
                while ((line = br.readLine()) != null) {

                    String[] WeatherInfo = line.split(cvsSplitBy);
                    //Class to store info
                    WeatherLineInfo model = new WeatherLineInfo();

                    model.StationName = FileName;
                    model.StationNumber = lastStation;

                    model.Year = Integer.parseInt(WeatherInfo[0]);
                    model.Month = Integer.parseInt(WeatherInfo[1]);
                    model.TempMax = Double.parseDouble(WeatherInfo[2]);
                    model.TempMin = Double.parseDouble(WeatherInfo[3]);
                    model.Frost = Integer.parseInt(WeatherInfo[4]);
                    model.RainDays = Double.parseDouble(WeatherInfo[5]);
                    //Add file class to array
                    weatherLineInfos.add(model);

                    //Check if year does not exist
                    if (!DatesMap.containsKey(model.Year)) {
                        //Assign values
                        DatesMap.put(model.Year, new HashMap<Integer, WeatherLineInfo>());
                    }

                    //Obtain key and value from the array
                    HashMap<Integer, WeatherLineInfo> Entity = (HashMap<Integer, WeatherLineInfo>) DatesMap
                            .get(model.Year);

                    Entity.put(model.Month, model);

                    for (Map.Entry<Integer, Map<Integer, WeatherLineInfo>> YearInfo : DatesMap
                            .entrySet()) {

                        //Check if the (max temp) & (min temp) are higher than saved in variable, if yes - assign
                        for (Map.Entry<Integer, WeatherLineInfo> MonthInfo : YearInfo.getValue()
                                .entrySet()) {

                            if (MonthInfo.getValue().TempMax>MaxTemp) {
                                MaxTemp=MonthInfo.getValue().TempMax;
                                MaxTempWeatherInfo = MonthInfo.getValue();
                            }

                            if (MonthInfo.getValue().TempMin<MinTemp) {
                                MinTemp=MonthInfo.getValue().TempMin;
                                MinTempWeatherInfo = MonthInfo.getValue();
                            }

                            TotalFrost += MonthInfo.getValue().Frost * 9 /12;
                            TotalRainfall += MonthInfo.getValue().RainDays * 9 /12;

                        }

                    }

                }

                //Print out - to generated .txt
                myWriter.write("Highest: " + "<" + (Integer.toString(MaxTempWeatherInfo.Month)) + "/"+ (Integer.toString(MaxTempWeatherInfo.Year)) + " with the highest tmax " + (Double.toString(MaxTempWeatherInfo.TempMax)) + "°C>" + "\n");
                myWriter.write("Lowest: " + "<" + (Integer.toString(MinTempWeatherInfo.Month)) + "/" + (Integer.toString(MinTempWeatherInfo.Year)) + " with the lowest tmin " + (Double.toString(MinTempWeatherInfo.TempMin)) + "°C>" + "\n");
                myWriter.write("Average annual af: " + "<" + (Integer.toString(TotalFrost/365)) + " days>" + "\n");
                myWriter.write("Average annual rainfall: " + "<" + (Double.toString(TotalRainfall/365)) + " mm>" + "\n\n");


                lastStation++;
            }

            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    //  * Title: JavaFX : How do I open the JavaFX FileChooser from a controller class?
    //  * Retrieval date: 21/4/20,
    //  * URL: http://stackoverflow.com/questions/25491732/how-do-i-open-the-javafx-filechooser-from-a-controller-class -->

    @FXML
    private void locateFile() {
        //Opens directory to select generated .txt
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");

        //Adds file name filter
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "Weather Report.txt"));

        File selectedFile = chooser.showOpenDialog(new Stage());
    }


    //////////////////////// QUESTION 3 GENERATE WEATHER REPORT [tab 1] END ////////////////////////////////////////////

    //////////////////////// QUESTION 1 WEATHER IN 2019 [tab 1] START //////////////////////////////////////////////////

    @FXML
    private ComboBox<String> metStation;
    @FXML
    private Label tempMini;
    @FXML
    private Label tempMaxi;
    @FXML
    private Label airFrost;
    @FXML
    private Label rainfall;


    @FXML
    private void view() {
        if (metStation.getValue() == null) {
            Window owner = metStation.getScene().getWindow();
            ///Label error handler, displayed if requested data is missing in .csv
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information Error",
                    "Please select a Meteorological Station");
        } else {
            //Displays N/A in GUI labels if no data available for selected station
            tempMaxi.setText("N/A");
            tempMini.setText("N/A");
            airFrost.setText("N/A");
            rainfall.setText("N/A");

            try {
                archiveChart(metStation.getValue(), "2019");
            } catch (Exception e) {
                Window owner = metStation.getScene().getWindow();
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error",
                        "No data available for the selected station");
            }
        }
    }

    @FXML
    private void archiveChart(String stationName, String year) {
        //The following function takes the Station name and its respective year, then dissects and converts the string data to doubles,
        //The data then will be used for calculating Max, Min temperatures and Total air frost and rainfall days.

        String name = stationName;

        try {
            StationData stationData = StationsUtil.load(name, year);

            List<String> inputtempMini = stationData.getInputtempMini();
            List<String> inputtempMaxi = stationData.getInputtempMaxi();
            List<String> inputairFrost = stationData.getInputairFrost();
            List<String> inputrainfall = stationData.getInputrainfall();

            double[] toNumberMini = new double[inputtempMini.size()];
            for (int i = 0; i < inputtempMini.size(); ++i) {
                toNumberMini[i] = Double.parseDouble(inputtempMini.get(i));
            }

            double[] toNumberMaxi = new double[inputtempMaxi.size()];
            for (int i = 0; i < inputtempMaxi.size(); ++i) {
                toNumberMaxi[i] = Double.parseDouble(inputtempMaxi.get(i));
            }

            double[] toNumberFrost = new double[inputairFrost.size()];
            for (int i = 0; i < inputairFrost.size(); ++i) {
                toNumberFrost[i] = Double.parseDouble(inputairFrost.get(i));
            }

            double[] toNumberRainfall = new double[inputrainfall.size()];
            for (int i = 0; i < inputrainfall.size(); ++i) {
                toNumberRainfall[i] = Double.parseDouble(inputrainfall.get(i));
            }


            double MinimumTemp = toNumberMini[0];
            for (int i = 0; i < toNumberMini.length; i++) {
                if (toNumberMini[i] < MinimumTemp) {
                    MinimumTemp = toNumberMini[i];
                }
            }
            tempMini.setText(String.valueOf(MinimumTemp));

            double MaximumTemp = toNumberMaxi[0];
            for (int i = 0; i < toNumberMaxi.length; i++) {
                if (toNumberMaxi[i] > MaximumTemp) {
                    MaximumTemp = toNumberMaxi[i];
                }
            }
            tempMaxi.setText(String.valueOf(MaximumTemp));

            double DaysOfFrost = 0;
            for (int i = 0; i < toNumberFrost.length; i++) {
                DaysOfFrost += toNumberFrost[i];
            }

            int TotalDaysOfFrost = (int) DaysOfFrost;
            airFrost.setText(String.valueOf(TotalDaysOfFrost));

            double DaysOfRain = 0;
            for (int i = 0; i < toNumberRainfall.length; i++) {
                DaysOfRain += toNumberRainfall[i];
            }

            int TotalDaysOfRain = (int) DaysOfRain;
            rainfall.setText(String.valueOf(TotalDaysOfRain));


            //Checked exception handler (try-catch blocks)
        } catch (FileNotFoundException error) {
            //File with the specified pathname does not exist
            System.out.println(error.getMessage());
            System.out.println("The file does not exist");
        } catch (IOException error) {
            System.out.println("I/O error occurred");
        }
    }
    /////////////////////////// QUESTION 1 WEATHER IN 2019 [tab 1] END /////////////////////////////////////////////////

    /////////////////////////// QUESTION 2 ANALYSE INDIVIDUAL STATION DATA [tab 2] START ///////////////////////////////

    @FXML
    private ComboBox<String> metStationData;
    @FXML
    private ComboBox<String> yearList;
    @FXML
    private LineChart MinMaxDataChart;
    @FXML
    private BarChart barFrostDataChart;
    @FXML
    private BarChart barRainfallDataChart;
    @FXML
    private ComboBox<String> toggleData;


    //Values are stored in the following series
    private XYChart.Series<String, Number> tempMin1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> tempMax1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> frost1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> rain1 = new XYChart.Series<>();

    private XYChart.Series<String, Number> tempMin2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> tempMax2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> frost2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> rain2 = new XYChart.Series<>();


    @FXML
    private void viewChart() {

        MinMaxDataChart.getData().clear();
        barFrostDataChart.getData().clear();
        barRainfallDataChart.getData().clear();

        if (metStationData.getValue() != null && yearList.getValue() != null) {

            stationDataCharts(metStationData.getValue(), yearList.getValue(), tempMin1, tempMax1, frost1, rain1);

        } else {

            Window owner = metStationData.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information Error",
                    "Please select a Meteorological Station");
        }
    }

    @FXML//Avoids chart clutter and compares only two sets of year at once
    private void addYear() {
        if (!toggleData.getItems().isEmpty()) {
            if (metStationData.getValue() != null) {
                MinMaxDataChart.getData().remove(tempMin2);
                MinMaxDataChart.getData().remove(tempMax2);
                barFrostDataChart.getData().remove(frost2);
                barRainfallDataChart.getData().remove(rain2);

                stationDataCharts(metStationData.getValue(), toggleData.getValue(),tempMin2, tempMax2, frost2,
                        rain2);

            } else {

                Window owner = toggleData.getScene().getWindow();
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information Error",
                        "Please select a Meteorological Station");
            }
        } else {
            if (!tempMax2.getData().isEmpty()) {
                tempMax2.getData().clear();
            }

            if (!tempMin2.getData().isEmpty()) {
                tempMin2.getData().clear();
            }

            if (!frost2.getData().isEmpty()) {
                frost2.getData().clear();
            }

            if (!rain2.getData().isEmpty()) {
                rain2.getData().clear();
            }
            MinMaxDataChart.getData().remove(tempMax2);
            MinMaxDataChart.getData().remove(tempMin2);
            barFrostDataChart.getData().remove(frost2);
            barRainfallDataChart.getData().remove(rain2);
        }
    }

    //Manipulates visualisations in charts .getData().remove
    @FXML
    private void tbMinClicked() {
        viewChart();

        if (toggleData.getValue() != null) {
            addYear();
            MinMaxDataChart.getData().remove(tempMax2);
        }
        MinMaxDataChart.getData().remove(tempMax1);
    }

    @FXML
    private void tbMaxClicked() {
        viewChart();

        if (toggleData.getValue() != null) {
            addYear();
            MinMaxDataChart.getData().remove(tempMin2);
        }
        MinMaxDataChart.getData().remove(tempMin1);
    }

    @FXML
    private void tbBothClicked() {
        viewChart();

        if (toggleData.getValue() != null) {
            addYear();
        }
    }

    //Clear data from charts
    @FXML
    private void clearData() {

        MinMaxDataChart.getData().clear();
        barFrostDataChart.getData().clear();
        barRainfallDataChart.getData().clear();
    }

    @FXML
    private void stationDataCharts(String name, String year, XYChart.Series tminn, XYChart.Series tmaxn,
                                   XYChart.Series afn, XYChart.Series rainn) {

        try {
            StationData stationData = StationsUtil.load(name, year);

            List<String> inputtempMini = stationData.getInputtempMini();
            List<String> inputtempMaxi = stationData.getInputtempMaxi();
            List<String> inputairFrost = stationData.getInputairFrost();
            List<String> inputrainfall = stationData.getInputrainfall();

            //Parse data in list <String> to double list
            double[] toNumberMini = new double[inputtempMini.size()];
            for (int i = 0; i < inputtempMini.size(); ++i) {
                toNumberMini[i] = Double.parseDouble(inputtempMini.get(i));
            }

            double[] toNumberMaxi = new double[inputtempMaxi.size()];
            for (int i = 0; i < inputtempMaxi.size(); ++i) {
                toNumberMaxi[i] = Double.parseDouble(inputtempMaxi.get(i));
            }

            double[] toNumberFrost = new double[inputairFrost.size()];
            for (int i = 0; i < inputairFrost.size(); ++i) {
                toNumberFrost[i] = Double.parseDouble(inputairFrost.get(i));
            }

            double[] toNumberRainfall = new double[inputrainfall.size()];
            for (int i = 0; i < inputrainfall.size(); ++i) {
                toNumberRainfall[i] = Double.parseDouble(inputrainfall.get(i));
            }

            if (!tmaxn.getData().isEmpty()) {
                tmaxn.getData().clear();
            }

            if (!tminn.getData().isEmpty()) {
                tminn.getData().clear();
            }

            if (!afn.getData().isEmpty()) {
                afn.getData().clear();
            }

            if (!rainn.getData().isEmpty()) {
                rainn.getData().clear();
            }

            //Add data in double [] to chart series
            for (int i = 0; i < toNumberMaxi.length; i++) {
                XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1), toNumberMaxi[i]);
                Double.parseDouble(inputtempMaxi.get(i));
                data.setNode(createTemperatureDataNode(data.YValueProperty()));

                tmaxn.getData().add(data);
            }

            for (int i = 0; i < toNumberMini.length; i++) {
                XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1), toNumberMini[i]);
                data.setNode(createTemperatureDataNode(data.YValueProperty()));

                tminn.getData().add(data);
            }

            for (int i = 0; i < toNumberFrost.length; i++) {
                XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                        toNumberFrost[i]);
                data.setNode(createDataNode(data.YValueProperty()));

                afn.getData().add(data);
            }

            for (int i = 0; i < toNumberRainfall.length; i++) {
                XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                        toNumberRainfall[i]);
                data.setNode(createDataNode(data.YValueProperty()));

                rainn.getData().add(data);
            }

            MinMaxDataChart. getData().add(tmaxn);
            MinMaxDataChart.getData().add(tminn);
            barFrostDataChart.getData().add(afn);
            barRainfallDataChart.getData().add(rainn);

            tmaxn.setName(year + " tmax");
            tminn.setName(year + " tmin");
            afn.setName(year);
            rainn.setName(year);


        } catch (FileNotFoundException error) {

            System.out.println(error.getMessage());
            System.out.println("The file does not exist");
        } catch (IOException error) {
            System.out.println("I/O error occurred");
        }

        if (tmaxn.getData().isEmpty() || tminn.getData().isEmpty() || afn.getData().isEmpty()
                || rainn.getData().isEmpty()) {
            Window owner = metStationData.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error",
                    "No data available for the selected station");
        }

    }

    /////////////////////////// QUESTION 2 ANALYSE INDIVIDUAL STATION DATA [tab 2] END /////////////////////////////////

    //  * Title: JavaFX : JavaFX LineChart with Values
    //  * Retrieval date: 2/5/20
    //  * URL: https://stackoverflow.com/questions/55946240/javafx-linechart-with-values -->

    //Set a custom node for each data in pane.
    private static Node createTemperatureDataNode(ObjectExpression<Number> value) {
        Pane pane = (Pane) createDataNode(value);
        //Set node size
        pane.setShape(new Circle(6.0));
        pane.setScaleShape(false);

        Label label = (Label) pane.getChildren().get(0);
        //Set values positioning on chart
        label.translateYProperty().bind(label.heightProperty().divide(-1.5));

        return pane;
    }
  
    private static Node createDataNode(ObjectExpression<Number> value) {
        Label label = new Label();
        //Bind label text value to string
        label.textProperty().bind(value.asString("%,.2f"));
        label.setTextFill(Color.WHITE);

        return new Pane(label);
    }

    /////////////////////////// QUESTION 2 COMPARE STATIONS [tab 3] START //////////////////////////////////////////////

    @FXML
    private GridPane stationsComparisonGrid;
    @FXML
    private ComboBox<String> comparisonBaseStations;
    @FXML
    private ComboBox<String> comparisonBaseYears;
    @FXML
    private LineChart minMaxComparisonChart;
    @FXML
    private BarChart airFrostComparisonChart;
    @FXML
    private BarChart rainFallComparisonChart;
    @FXML
    private Button addComparisonStationButton;

    //Set comparison combo boxes (meteorological station and year) location on Grid
    private int comparisonGridRowIndex = 2;

    private List<StationData> comparisonStations = new ArrayList<>();

    XYChart.Series<String, Number> minTempSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> maxTempSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> airFrostSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> rainFallSeries = new XYChart.Series<>();

    //  * Author: J.Lenkov.
    //  * Title: Adding Children to a GridPane
    //  * Retrieval date: 19/4/20,
    //  * URL: http://tutorials.jenkov.com/javafx/gridpane.html -->

    //Comparison station boxes
    ComboBox stationsBox = new ComboBox();
    ComboBox yearsBox = new ComboBox();
    Button button = new Button();


    public void addComparisonStation() {
        //Increment current row's index
        comparisonGridRowIndex++;

        //Moves Add Station button 1 row down
        GridPane.setRowIndex(addComparisonStationButton, comparisonGridRowIndex + 1);

        stationsBox = new ComboBox();
        stationsBox.setPromptText("Meteorological Station");
        stationsBox.getItems().addAll(stations);

        yearsBox = new ComboBox();
        yearsBox.setPromptText("Historical Data");
        yearsBox.getItems().addAll(years);

        button = new Button();
        button.setText("X");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Remove all nodes in the same row
                stationsComparisonGrid.getChildren().removeAll(stationsBox, yearsBox, button);

                int buttonIndex = GridPane.getRowIndex(button);

                //Updated row indexes below
                for (Node child : stationsComparisonGrid.getChildren()) {
                    int childIndex = GridPane.getRowIndex(child);

                    if (childIndex > buttonIndex) {
                        GridPane.setRowIndex(child, childIndex - 1);

                    }
                }

                //Decrement next row's index
                comparisonGridRowIndex--;
            }
        });

        //Node child, int columnIndex, int rowIndex, int colspan, int rowspan
        stationsComparisonGrid.add(stationsBox, 1, comparisonGridRowIndex, 2, 1);
        stationsComparisonGrid.add(yearsBox, 3, comparisonGridRowIndex, 1, 1);
        stationsComparisonGrid.add(button, 4, comparisonGridRowIndex, 1, 1);
    }

    public void generateComparisonCharts() {
        try {
            List<ComboBox> stationBoxes = new ArrayList<>();
            List<ComboBox> yearBoxes = new ArrayList<>();

            int addButtonRow = GridPane.getRowIndex(addComparisonStationButton);

            //Collects all station and year boxes
            for (Node child : stationsComparisonGrid.getChildren()) {
                Integer childRowIndex = GridPane.getRowIndex(child);
                Integer childColumnIndex = GridPane.getColumnIndex(child);

                //Makes sure row and column are not null to avoid null pointers
                if (childRowIndex != null && childColumnIndex != null && childRowIndex < addButtonRow) {
                    // Station combo box is always at 1st column and starts from 2nd row
                    if (childRowIndex > 1 && childColumnIndex == 1) {
                        stationBoxes.add((ComboBox) child);

                    }

                    //Year combo box is place at 3rd column and 2nd row
                    if (childRowIndex > 1 && childColumnIndex == 3) {
                        yearBoxes.add((ComboBox) child);
                    }

                }

            }

            //Clears old data
            comparisonStations.clear();

            //Iterates over stations and years
            for (int i = 0; i < stationBoxes.size(); i++) {
                ComboBox stationBox = stationBoxes.get(i);
                ComboBox yearBox = yearBoxes.get(i);

                String selectedStation = (String) stationBox.getSelectionModel().getSelectedItem();
                String selectedYear = (String) yearBox.getSelectionModel().getSelectedItem();

                //Adds only those stations are selected and have selected year
                if (selectedStation != null && selectedYear != null) {
                    StationData stationData = StationsUtil.load(selectedStation, selectedYear);

                    comparisonStations.add(stationData);

                }
            }

            //Clear charts (prevents from duplicating selected data)
            minMaxComparisonChart.getData().clear();
            airFrostComparisonChart.getData().clear();
            rainFallComparisonChart.getData().clear();

            for (StationData stationData : comparisonStations) {

                minTempSeries = new XYChart.Series<>();
                maxTempSeries = new XYChart.Series<>();
                airFrostSeries = new XYChart.Series<>();
                rainFallSeries = new XYChart.Series<>();

                List<String> inputtempMini = stationData.getInputtempMini();
                List<String> inputtempMaxi = stationData.getInputtempMaxi();
                List<String> inputairFrost = stationData.getInputairFrost();
                List<String> inputrainfall = stationData.getInputrainfall();

                for (int i = 0; i < inputtempMini.size(); i++) {
                    XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                            Double.parseDouble(inputtempMini.get(i)));
                    data.setNode(createTemperatureDataNode(data.YValueProperty()));

                    minTempSeries.getData().add(data);
                }

                for (int i = 0; i < inputtempMaxi.size(); i++) {
                    XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                            Double.parseDouble(inputtempMaxi.get(i)));
                    data.setNode(createTemperatureDataNode(data.YValueProperty()));

                    maxTempSeries.getData().add(data);
                }

                for (int i = 0; i < inputairFrost.size(); i++) {
                    XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                            Double.parseDouble(inputairFrost.get(i)));
                    data.setNode(createDataNode(data.YValueProperty()));

                    airFrostSeries.getData().add(data);
                }

                for (int i = 0; i < inputrainfall.size(); i++) {
                    XYChart.Data data = new XYChart.Data<>(Integer.toString(i + 1),
                            Double.parseDouble(inputrainfall.get(i)));
                    data.setNode(createDataNode(data.YValueProperty()));

                    rainFallSeries.getData().add(data);

                }
                //Get data (to charts)
                minTempSeries.setName(stationData.getYear() + " tmin");
                maxTempSeries.setName(stationData.getYear() + " tmax");

                airFrostSeries.setName(stationData.getYear());
                rainFallSeries.setName(stationData.getYear());

                minMaxComparisonChart.getData().add(minTempSeries);
                minMaxComparisonChart.getData().add(maxTempSeries);

                airFrostComparisonChart.getData().add(airFrostSeries);
                rainFallComparisonChart.getData().add(rainFallSeries);

            }


        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
            System.out.println("The file does not exist");
        } catch (IOException error) {
            System.out.println("I/O error occurred");
        }

        if (maxTempSeries.getData().isEmpty() || minTempSeries.getData().isEmpty() || airFrostSeries.getData().isEmpty()
                || rainFallSeries.getData().isEmpty()) {
            Window owner = metStationData.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error",
                    "No data available for the selected the selected station");
        }

    }

    @FXML //Compare Min temperature
    private void tbMin2Clicked() {
        generateComparisonCharts();
        minMaxComparisonChart.getData().remove(maxTempSeries);;
        if(minMaxComparisonChart.getData().size()>1){
            minMaxComparisonChart.getData().remove(1);
        }

    }

    @FXML //Compare Max temperature
    private void tbMax2Clicked() {
        generateComparisonCharts();
        minMaxComparisonChart.getData().remove(minTempSeries);
        if(minMaxComparisonChart.getData().size()>1){
            minMaxComparisonChart.getData().remove(0);
        }

    }

    @FXML //Compare Min & Max temperatures
    private void tbBoth2Clicked() {
        generateComparisonCharts();

    }

    @FXML
    private void clearData2() {

        minMaxComparisonChart.getData().clear();
        airFrostComparisonChart.getData().clear();
        rainFallComparisonChart.getData().clear();
    }

    /////////////////////////// QUESTION 2 COMPARE STATIONS [tab 3] END ////////////////////////////////////////////////
}
