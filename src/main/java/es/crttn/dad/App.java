package es.crttn.dad;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * Hello world!
 *
 */
public class App extends Application {

    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty width = new SimpleDoubleProperty();
    private DoubleProperty height = new SimpleDoubleProperty();

    private IntegerProperty red = new SimpleIntegerProperty();
    private IntegerProperty green = new SimpleIntegerProperty();
    private IntegerProperty blue = new SimpleIntegerProperty();

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("iniciado");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".VentanasConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (configFile.exists()) {

            FileInputStream fis = new FileInputStream(configFile);

            Properties props = new Properties();
            props.load(fis);

            width.set(Double.parseDouble(props.getProperty("size.width")));
            height.set(Double.parseDouble(props.getProperty("size.height")));
            x.set(Double.parseDouble(props.getProperty("location.x")));
            y.set(Double.parseDouble(props.getProperty("location.y")));
            red.setValue(Integer.parseInt(props.getProperty("red.color")));
            green.setValue(Integer.parseInt(props.getProperty("green.color")));
            blue.setValue(Integer.parseInt(props.getProperty("blue.color")));

        } else {

            width.set(320);
            height.set(200);
            x.set(0);
            y.set(0);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Slider redSlider = new Slider();
        redSlider.setMin(0);
        redSlider.setMax(255);
        redSlider.setShowTickLabels(true);
        redSlider.setShowTickMarks(true);
        redSlider.setMajorTickUnit(255);
        redSlider.setMinorTickCount(5);

        Slider greenSlider = new Slider();
        greenSlider.setMin(0);
        greenSlider.setMax(255);
        greenSlider.setShowTickLabels(true);
        greenSlider.setShowTickMarks(true);
        greenSlider.setMajorTickUnit(255);
        greenSlider.setMinorTickCount(5);

        Slider blueSlider = new Slider();
        blueSlider.setMin(0);
        blueSlider.setMax(255);
        blueSlider.setShowTickLabels(true);
        blueSlider.setShowTickMarks(true);
        blueSlider.setMajorTickUnit(255);
        blueSlider.setMinorTickCount(5);

        VBox root = new VBox();
        root.setFillWidth(false);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(redSlider, greenSlider, blueSlider);

        Scene scene = new Scene(root, width.get(), height.get());

        primaryStage.setX(x.get());
        primaryStage.setY(y.get());
        primaryStage.setTitle("Ventana con memoria");
        primaryStage.setScene(scene);
        primaryStage.show();

        x.bind(primaryStage.xProperty());
        y.bind(primaryStage.yProperty());
        width.bind(primaryStage.widthProperty());
        height.bind(primaryStage.heightProperty());

//      Actualizar el color del fondo al iniciar con los valores del "porps"
        Color rgbColor = Color.rgb(red.getValue(), green.getValue(), blue.getValue());
        BackgroundFill bgf = new BackgroundFill(rgbColor, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bgf));

        redSlider.valueProperty().bindBidirectional(red);

        red.addListener((o, ov, nv) -> {
            Color r = Color.rgb(nv.intValue(), green.getValue(), blue.getValue());
            root.setBackground(Background.fill(r));
        });

        greenSlider.valueProperty().bindBidirectional(green);

        green.addListener((o, ov, nv) -> {
            Color g = Color.rgb(red.getValue(),nv.intValue(), blue.getValue());
            root.setBackground(Background.fill(g));
        });

        blueSlider.valueProperty().bindBidirectional(blue);

        blue.addListener((o, ov ,nv) -> {
            Color b = Color.rgb(red.getValue(),green.getValue(), nv.intValue());
            root.setBackground(Background.fill(b));
        });

    }


    @Override
    public void stop() throws Exception {
        System.out.println("cerrado");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".VentanasConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (!configFolder.exists()) {

            configFolder.mkdir();
        }


        FileOutputStream fos = new FileOutputStream(configFile);

        Properties props = new Properties();
        props.setProperty("size.width", "" + width.getValue());
        props.setProperty("size.height", "" +  height.getValue());
        props.setProperty("location.x", "" + x.getValue());
        props.setProperty("location.y", "" + y.getValue());
        props.setProperty("red.color", "" + red.getValue());
        props.setProperty("green.color", "" + green.getValue());
        props.setProperty("blue.color", "" + blue.getValue());
        props.store(fos, "Estado de la ventana");
    }
}
