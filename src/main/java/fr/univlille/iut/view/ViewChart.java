package fr.univlille.iut.view;

import fr.univlille.iut.controller.JavaFXController;
import fr.univlille.iut.model.DistanceComparable;
import fr.univlille.iut.model.Iris;
import fr.univlille.iut.model.Pokemon;
import fr.univlille.iut.utils.Observable;
import fr.univlille.iut.utils.Observer;
import fr.univlille.iut.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;

public class ViewChart<T extends DistanceComparable<T>> implements Observer {
    private Model<T> observable = null;
    private boolean wasNotified;
    private JavaFXController<T> controller;
    private List<String> columnNames = new ArrayList<>();
    private String title;
    File selectedFile;

    public ViewChart(Stage primaryStage) throws IOException {
        selectedFile = pickFile(primaryStage);
        if (selectedFile == null) return;

        String name = noExtension(selectedFile);
        if (!initializeModel(selectedFile)) return;

        try {
            columnNames = readColumnNamesFromFile(selectedFile);
        } catch (IOException e) {
            showErrorDialog("Error Reading File", "An error occurred while reading the file: " + e.getMessage());
            return;
        }

        initializeUI(primaryStage, name);
    }

    public ViewChart(ViewChart<T> formerVue) throws IOException {
        Stage primaryStage=new Stage();
        selectedFile = formerVue.getSelectedFile();
        String name = noExtension(selectedFile);
        observable = formerVue.getObservable();
        observable.attach(this);
        columnNames = formerVue.getColumnNames();

        initializeUI(primaryStage, name);
        controller.updateChart();
        formerVue.controller.updateChart();
    }

    public void changeData(Stage primaryStage) {
        File newFile = pickFile(primaryStage);
        if (newFile == null) return;

        String name = noExtension(newFile);
        if (!initializeModel(newFile)) return;

        try {
            columnNames = readColumnNamesFromFile(newFile);
        } catch (IOException e) {
            showErrorDialog("Error Reading File", "An error occurred while reading the file: " + e.getMessage());
            return;
        }

        selectedFile = newFile;
        try {
            initializeUI(primaryStage, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to select a CSV file using a FileChooser.
     */
    protected File pickFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("res/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Choose a CSV File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            showWarningDialog("No File Selected", "You must select a valid CSV file.");
        }
        return selectedFile;
    }

    /**
     * Extracts the file name without its extension.
     */
    private String noExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * Initializes the model based on the selected file's content.
     */
    private boolean initializeModel(File file) {
        try {
            if (file.getName().equalsIgnoreCase("iris.csv")) {
                observable = new Model<>((Class<T>) Iris.class);
                observable.attach(this);

            } else if (file.getName().equalsIgnoreCase("pokemon_train.csv")) {
                observable = new Model<>((Class<T>) Pokemon.class);
                observable.attach(this);

            } else {
                showErrorDialog("Unsupported File", "The file " + file.getName() + " is not supported.");
                return false;
            }
        } catch (Exception e) {
            showErrorDialog("Initialization Error", "Could not initialize the model: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Reads column names from the selected file.
     */
    private List<String> readColumnNamesFromFile(File file) throws IOException {
        List<String> columns = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String [] headerLine = reader.readLine().split(",");
            String value = reader.readLine();
            if (value != null) {
                int cpt=0;
                for (String val : value.split(",")) {
                    if (isDouble(val.trim())) {
                        columns.add(headerLine[cpt].replace("\"", ""));
                    }
                    cpt++;
                }
            }
        }
        return columns;
    }


    /**
     * Checks if a string represents a valid double.
     */
    private boolean isDouble(String str) {
        try{
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Initializes the JavaFX UI with the specified name.
     */
    private void initializeUI(Stage stage, String name) throws IOException {
        title = "Chart for: " + name;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout.fxml"));
        loader.setControllerFactory(controllerClass -> {
            controller = new JavaFXController<>(observable);
            controller.setVue(this);
            return controller;
        });

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        boolean wasMaximized = stage.isMaximized();
        stage.setMaximized(!wasMaximized);
        stage.show();
    }

    @Override
    public void update(Observable o) {
        controller.updateChart();

    }

    @Override
    public void update(Observable o, Object arg) {
        controller.updateChart();

    }

    public Model<T> getObservable() {
        return observable;
    }

    public List<T> getData() {
        return observable.getData();
    }

    /**
     * Displays an error dialog with a specific title and message.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a warning dialog with a specific title and message.
     */
    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setData(List<T> data) {
        this.observable.setData(data);
    }

    public void addData(T item, String axeX, String axeY) {
        this.observable.addData(item, axeX, axeY);
    }
}
