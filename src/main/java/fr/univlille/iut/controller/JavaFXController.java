package fr.univlille.iut.controller;

import fr.univlille.iut.model.Distance;
import fr.univlille.iut.model.DistanceComparable;
import fr.univlille.iut.model.Model;
import fr.univlille.iut.model.NormalizedEuclideanDistance;
import fr.univlille.iut.view.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.File;
import java.lang.reflect.Field;
import java.io.IOException;
import java.util.*;
import java.util.random.RandomGenerator;

public class JavaFXController<T extends DistanceComparable<T>> {

    public static final int RGB = 256;
    @FXML
    private Button menuAddData;

    // ScatterChart
    @FXML
    private ScatterChart<Double, Double> chart;

    @FXML
    private CategoryAxis chartVarietyAxis;
    @FXML
    private NumberAxis chartNumberAxis;

    // TabPane
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabData;

    @FXML
    private Tab tabFormAddData;

    @FXML
    private Tab tabFormEditAxes;

    @FXML
    private ComboBox<String> xCB, yCB;

    @FXML
    private ListView<HBox> lVDatas;

    @FXML
    private VBox scrollPaneVBoxAddDatas;

    // Attributes
    private final Model<T> observable;
    private ViewChart<T> vue;
    File csv;

    public JavaFXController(Model<T> observable) {
        this.observable = observable;
    }

    /**
     * Set the VueGraphique
     * @param vue Graphique
     */
    public void setVue(ViewChart<T> vue) {
        this.vue = vue;
    }

    /**
     * Initialisation
     */
    @FXML
    private void initialize() {
        tabPane.getTabs().remove(tabData);
        tabPane.getTabs().remove(tabFormAddData);
        tabPane.getTabs().remove(tabFormEditAxes);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        menuAddData.setOnAction(event -> openAddDataTab());
        csv = vue.getSelectedFile();
        observable.load(csv.getAbsolutePath());

        List<String> columnNames = vue.getColumnNames();
        if (columnNames == null || columnNames.size() < 2) {
            throw new IllegalStateException("Insufficient columns to initialize chart.");
        }

        for (T item : observable.getData()) {
            item.setX(columnNames.get(0));
            item.setY(columnNames.get(1));
        }

        initializeMapEnumColor(); // Déplacement de l'appel ici
        updateChart();
        initializeChart();
        initializeAddDataTab();
    }

    Map<Enum, String> ColorMap=new HashMap<>();

    private void initializeMapEnumColor(){
        try {
            T newItem = (T) observable.getData().get(0).getClass().getDeclaredConstructor().newInstance();
            for (Enum enu : newItem.getVariety()){
                ColorMap.put(enu, generateRandomColor());
            }
        } catch (Exception e){
            System.err.println("Error creating new item: " + e.getMessage());
        }
    }

    private String generateRandomColor() {
        RandomGenerator random = new Random();
        int r = random.nextInt(RGB); // Red value (0-255)
        int g = random.nextInt(RGB); // Green value (0-255)
        int b = random.nextInt(RGB); // Blue value (0-255)

        return String.format("#%02x%02x%02x", r, g, b);
    }

    private void initializeChart() {
        List<String> columnNames = vue.getColumnNames();
        if (columnNames == null || columnNames.size() < 2) {
            throw new IllegalStateException("Insufficient columns to initialize chart.");
        }

        if(chartVarietyAxis==null || chartNumberAxis==null) {
            chartVarietyAxis = new CategoryAxis();
            chartNumberAxis = new NumberAxis();
        }

        chartVarietyAxis.setLabel(columnNames.get(0));
        chartNumberAxis.setLabel(columnNames.get(1));

        if(xCB==null || yCB==null) {
            xCB = new ComboBox<>();
            yCB = new ComboBox<>();
        }

        for (String columnName : columnNames) {
            xCB.getItems().add(columnName);
            yCB.getItems().add(columnName);
        }

        xCB.setValue(columnNames.get(0));
        yCB.setValue(columnNames.get(1));
    }

    @FXML
    private void openAddDataTab() {
        if (!tabPane.getTabs().contains(tabFormAddData)) {
            tabPane.getTabs().add(tabFormAddData);
        }
        tabPane.getSelectionModel().select(tabFormAddData);

    }

    private void addAttributeToLVAddDatas(String columnName) {
        VBox vbox = new VBox();
        columnName = columnName.replace(".", "_");
        TextField textField = new TextField();
        vbox.setId(columnName + "VBox");
        textField.setId(columnName + "TextField");
        columnName = columnName.replace("_", " ");
        textField.setPromptText(columnName);
        textField.setMaxWidth(Double.MAX_VALUE);
        Label label = new Label(columnName);

        vbox.getChildren().addAll(label, textField);
        scrollPaneVBoxAddDatas.getChildren().add(vbox);
    }

    private void initializeAddDataTab() {
        for (String columnName : vue.getColumnNames()) {
            addAttributeToLVAddDatas(columnName);
        }
    }

    /**
     * Charge les données depuis un fichier csv
     */
    @FXML
    public void handleChangeData(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        vue.changeData(stage);
    }

    /**
     * Ajoute des données
     */
    public void addData() {
        double[] userInputValues = valueOfTextField();
        try {
            List<T> data = observable.getData();
            if (data.isEmpty()) {
                System.err.println("No data available in the model.");
            }

            Class<?> itemClass = data.get(0).getClass();
            T newItem = (T) itemClass.getDeclaredConstructor().newInstance();
            newItem.initializeFromValues(userInputValues);

            Distance<T> distance = new NormalizedEuclideanDistance<>();
            String variety = observable.knn(newItem, data, 3, distance);
            if (variety != null) {
                newItem.setVariety(variety);
                vue.addData(newItem, xCB.getValue(), yCB.getValue());
            } else {
                System.err.println("Variety is null for the new item.");
            }
        } catch (Exception e) {
            System.err.println("Error creating new item: " + e.getMessage());
        }
    }

    private double[] valueOfTextField() {
        List<String> columnNames = vue.getColumnNames();
        double[] data = new double[columnNames.size()];

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i).replace(".", "_");
            String vboxId = columnName + "VBox";
            String textfieldId = columnName + "TextField";

            VBox vbox = (VBox) scrollPaneVBoxAddDatas.lookup("#" + vboxId);
            if (vbox != null) {
                TextField textField = (TextField) vbox.lookup("#" + textfieldId);
                if (textField != null) {
                    data[i] = Double.parseDouble(textField.getText());
                }
            }
        }
        return data;
    }


    @FXML
    public void editDisplay() {
        if (!tabPane.getTabs().contains(tabFormEditAxes)) {
            tabPane.getTabs().add(tabFormEditAxes);
        }
        tabPane.getSelectionModel().select(tabFormEditAxes);
    }


    @FXML
    public void addDisplay() throws IOException {
        new ViewChart<>(vue);
    }

    @FXML
    private void XCBAction() {
        editAxesButtonAction();
    }

    @FXML
    private void YCBAction() {
        editAxesButtonAction();
    }

    @FXML
    private void editAxesButtonAction() {
        if (xCB.getValue() != null && yCB.getValue() != null) {
            chartVarietyAxis.setLabel(xCB.getValue());
            chartNumberAxis.setLabel(yCB.getValue());
            editXY();
            updateChart();
        }
    }

    public void editXY() {
        for(T item: observable.getData()){
            item.setX(xCB.getValue());
            item.setY(yCB.getValue());
        }
    }

    @FXML
    private void classifyDataAddDataAction(ActionEvent event) {

    }

    @FXML
    public void classifyDataAction(ActionEvent event) {

    }

    /**
     * Met à jour le graphique
     */
    public void updateChart() {
        List<T> data = vue.getData();
        chart.getData().clear();

        if (data == null || data.isEmpty()) {
            System.err.println("No data available to display.");
            return;
        }

        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (T item : data) {
            double x = observable.getX(item);
            double y = observable.getY(item);
            XYChart.Data<Double, Double> dataPoint = createDataPoint(item, x, y);
            series.getData().add(dataPoint);
        }

        chart.getData().add(series);
        chart.setLegendVisible(false);
    }

    // Crée un point de données avec un bouton personnalisé
    private XYChart.Data<Double, Double> createDataPoint(T item, double xValue, double yValue) {
        XYChart.Data<Double, Double> dataPoint = new XYChart.Data<>(xValue, yValue);
        dataPoint.setNode(createCustomButton(item));
        return dataPoint;
    }

    // Crée un bouton personnalisé
    private Button createCustomButton(T item) {
        Button button = new Button();
        button.setPrefSize(10, 10);
        String baseStyle;

        if (item.isNew()) {
            baseStyle = "-fx-background-radius: 0; -fx-border-radius: 0;";
        } else {
            baseStyle = "-fx-background-radius: 50%; -fx-border-radius: 50%; " +
                    "-fx-padding: 10; -fx-min-width: 50px; -fx-min-height: 50px; " +
                    "-fx-max-width: 50px; -fx-max-height: 50px;";
        }

        String colorStyle = "-fx-background-color: " + ColorMap.get(item.getVar()) + ";";

        button.setStyle(baseStyle + " " + colorStyle);

        button.setOnAction(event -> handleButtonAction(item));
        return button;
    }

    //Ajouter un attribut à la listeView
    private void addAttributeToLVDatas(String name, String value) {
        HBox hbox = new HBox();

        name = name.replace("_", " ");
        Label nameLabel = new Label(name + " : ");
        Label valueLabel = new Label(value);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        hbox.getChildren().addAll(nameLabel, spacer, valueLabel);
        lVDatas.getItems().add(hbox);
    }

    // Gère l'action au clic
    private void handleButtonAction(T item) {
        tabPane.getTabs().add(tabData);
        tabPane.getSelectionModel().select(tabData);
        lVDatas.getItems().clear(); // Clear existing children

        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                if (!fieldName.equals("x") && !fieldName.equals("y") && !fieldName.contains("String")) {
                    Object value = field.get(item);
                    if (value != null) {
                        addAttributeToLVDatas(fieldName, value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                System.err.println("Error getting field value: " + e.getMessage());
            }
        }
    }

    public void setTabFormEditAxes(Event event) {

    }

    public void setTabData(Event event) {

    }

    public void setTabFormAddData(Event event) {

    }
}
