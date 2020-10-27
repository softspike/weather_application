package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Dynamically allows to drop in .csv files in the project's CSV folder, then collects each station's name.
public class StationsUtil {

    public static List<String> loadAll() {
        File folder = new File("src/CSV");
        File[] files = folder.listFiles();

        List<String> fileNames = new ArrayList<>();

        for (File file : files) {
            fileNames.add(file.getName().replace(".csv", ""));
        }

        Collections.sort(fileNames);
        return fileNames;
    }

    public static StationData load(String station, String year) throws IOException {
        List<String> inputtempMaxi = new ArrayList<>();
        List<String> inputtempMini = new ArrayList<>();
        List<String> inputFrost = new ArrayList<>();
        List<String> inputrainfall = new ArrayList<>();

        //  * Title: BufferedReader
        //  * Retrieval date: 1/5/20,
        //  * URL: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html -->

        File inputFile = new File(new File("src/CSV/" + station + ".csv").getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        String line = "";

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");

            if (data[0].contains(year)) {
                inputtempMaxi.add(data[2]);
                inputtempMini.add(data[3]);
                inputFrost.add(data[4]);
                inputrainfall.add(data[5]);
            }
        }

        StationData stationData = new StationData();
        stationData.setName(station);
        stationData.setYear(year);
        stationData.setInputtempMini(inputtempMini);
        stationData.setInputtempMaxi(inputtempMaxi);
        stationData.setInputairFrost(inputFrost);
        stationData.setInputrainfall(inputrainfall);

        return stationData;
    }
}
