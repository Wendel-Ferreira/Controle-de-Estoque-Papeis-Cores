package com.papeis.cores.EstoquePapeis.Cores.Model;

import javafx.scene.control.Alert;

public class Alerts {

    public static void showAlert(String title, String header, String Text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Text);

        alert.show();
    }
}
