package model;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper {

    //  * Author: Dr Matthew Morgan.
    //  * Title: JavaFX : [CMT205] JavaFX Video 2 - Login App Example
    //  * Retrieval date: 15/4/20,
    //  * URL: https://cardiff.cloud.panopto.eu/Panopto/Pages/Viewer.aspx?id=2e797d21-b9c6-4df4-ad66-ab83009f21b6 -->

    public static void showAlert (Alert.AlertType alertType, Window owner, String title, String message) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();

    }
}
