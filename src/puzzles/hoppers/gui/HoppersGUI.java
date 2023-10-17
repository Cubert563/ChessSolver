package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+
            "red_frog.png"));

    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR +
            "green_frog.png"));

    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR +
            "water.png"));

    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR +
            "lily_pad.png"));

    private Stage stage;

    private GridPane Hoppers;

    private HoppersModel model;

    private BorderPane pane;

    private Label label;


    /**
     * Initializes the model.
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.label = new Label("Loaded: " + filename);
        try {
            this.model = new HoppersModel(filename);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }


    /**
     * Creates a gridpane of buttons representing the hoppers game.
     * @return A gridpane of buttons.
     */
    public GridPane makeHoppers() {
        String[][] grid = this.model.getConfigArray();
        this.Hoppers = new GridPane();
        for (int row = 0; row < this.model.getnumRows(); row++) {
            for (int col = 0; col < this.model.getNumCols(); col++) {
                int Hrow  = row;
                int Hcol = col;
                if (grid[row][col].equals("G")) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(greenFrog));
                    this.Hoppers.add(button, col, row);
                    button.setOnAction(event -> this.model.select(Hrow, Hcol));
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                else if (grid[row][col].equals("R")) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(redFrog));
                    this.Hoppers.add(button, col, row);
                    button.setOnAction(event -> this.model.select(Hrow, Hcol));
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                else if (grid[row][col].equals("*")) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(water));
                    this.Hoppers.add(button, col, row);
                    button.setOnAction(event -> this.model.select(Hrow, Hcol));
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                else {
                    Button button = new Button();
                    button.setGraphic(new ImageView(lilyPad));
                    this.Hoppers.add(button, col, row);
                    button.setOnAction(event -> this.model.select(Hrow, Hcol));
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
            }
        }
        return Hoppers;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.pane = new BorderPane();
        pane.setCenter(makeHoppers());
        HBox box1 = new HBox();
        box1.setAlignment(Pos.CENTER);
        box1.getChildren().add(label);
        pane.setTop(box1);
        HBox box2 = new HBox();
        box2.setAlignment(Pos.CENTER);
        Button load = new Button("Load");
        load.setOnAction(event -> this.load());
        Button reset = new Button("Reset");
        reset.setOnAction(event -> this.model.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(event -> this.model.hint());
        box2.getChildren().add(load);
        box2.getChildren().add(reset);
        box2.getChildren().add(hint);
        pane.setBottom(box2);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Hoppers");
        stage.show();
    }

    /**
     * Loads a new file to use.
     */
    public void load() {
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "chess";  // or "hoppers"
        chooser.setInitialDirectory(new File(currentPath));
    }


    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        this.label.setText(msg);
        GridPane gridPane = this.makeHoppers();
        gridPane = this.makeHoppers();
        this.pane.setCenter(gridPane);

        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
