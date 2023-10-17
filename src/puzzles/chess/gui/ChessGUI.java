package puzzles.chess.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.hoppers.model.HoppersModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
/**
 * Makes the chess board and pieces perceivable to the naked eye
 *
 *
 * Author: Jack Werremeyer
 * */

public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /**The chess board made of buttons*/
    private Button[][] board;
    /**Tells the player info about their actions*/
    private Label infoDump;

    /**A crude check of whether the piece being clicked is taking another or being taken*/
    private int selectNum;

    /**the application*/
    private Stage stage;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /**images*/
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));
    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));


    /**the coords of the piece that will take a life*/
    private int firstRow;
    private int firstCol;

    /**the main display*/
    private BorderPane pane;

    /**thingy*/
    private boolean intialized;

    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    /**initializes stuff*/
    @Override
    public void init() throws IOException {
        // get the file name from the command line
        this.intialized = false;
        String filename = getParameters().getRaw().get(0);
        this.model = new ChessModel(filename);
        model.addObserver(this);
        this.board = new Button[model.getRows()][model.getCols()];
        this.infoDump = new Label("");
        this.selectNum = 0;
    }

    /**creates the board and buttons and text*/
    @Override
    public void start(Stage stage) throws Exception {
        this.intialized = true;
        this.stage = stage;
        stage.setTitle("Chess GUI");
        pane = new BorderPane();
        pane.setCenter(this.makeBoard());
        pane.setBottom(this.nameButtons());
        pane.setTop(infoDump);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    /**makes the chess board*/
    private GridPane makeBoard(){
        GridPane result = new GridPane();
        int colorTest = 0;
        for(int i = 0; i < model.getRows(); i++){
            for(int j = 0; j < model.getCols(); j++){
                this.board[i][j] = new Button();
                colorTest = i+j;

                if((colorTest % 2) == 0){
                    this.board[i][j].setBackground(DARK);
                }else{this.board[i][j].setBackground(LIGHT);}

                if(!model.getCell(i,j).equals(".")){
                    if(model.getCell(i,j).equals("P")){
                        this.board[i][j].setGraphic(new ImageView(pawn));
                    }
                    if(model.getCell(i,j).equals("N")){
                        this.board[i][j].setGraphic(new ImageView(knight));
                    }
                    if(model.getCell(i,j).equals("B")){
                        this.board[i][j].setGraphic(new ImageView(bishop));
                    }
                    if(model.getCell(i,j).equals("R")){
                        this.board[i][j].setGraphic(new ImageView(rook));
                    }
                    if(model.getCell(i,j).equals("K")){
                        this.board[i][j].setGraphic(new ImageView(king));
                    }
                    if(model.getCell(i,j).equals("Q")){
                        this.board[i][j].setGraphic(new ImageView(queen));
                    }
                }else{this.board[i][j].setGraphic(null);}

                buttonInput(i ,j ,this.board[i][j]);
                this.board[i][j].setMinSize(ICON_SIZE, ICON_SIZE);
                this.board[i][j].setMaxSize(ICON_SIZE, ICON_SIZE);
                result.add(this.board[i][j], j ,i);
            }
        }
        return result;
    }


    /**makes the buttons*/
    private FlowPane nameButtons() throws IOException{
        FlowPane result = new FlowPane();

        Button load = new Button("load");
        load.setOnAction( e -> fileLoad());
        Button hint = new Button("hint");
        hint.setOnAction(e -> model.hint());
        Button reset = new Button("reset");
        reset.setOnAction(e -> {
            try {
                model.reset();
            } catch (IOException ignored) {
            }
        });

        result.getChildren().add(load);
        result.getChildren().add(hint);
        result.getChildren().add(reset);
        return result;
    }


    /**allows the user to choose a file */
    private void fileLoad(){
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "chess";  // or "hoppers"
        chooser.setInitialDirectory(new File(currentPath));
        File selectedFile = chooser.showOpenDialog(stage);
        currentPath = selectedFile.getPath();

        model.load(currentPath);
    }

    /**assigns coordinates to each button*/
    private void buttonInput(int row, int col, Button button){
        button.setOnAction(e -> this.select(row, col));
    }

    /**what happens when you select a piece*/
    private void select(int row, int col){
        model.validSelect(String.valueOf(row),
                String.valueOf(col), selectNum);
        if(selectNum == 0){
            selectNum = 1;
            firstRow = row;
            firstCol = col;
        }else{
            selectNum = 0;
            model.move(firstRow, firstCol, row, col);
        }
    }


    /**updates the gamestate of the board*/
    @Override
    public void update(ChessModel chessModel, String msg) {

        if(msg.charAt(0) == 'L'){
            board = new Button[model.getRows()][model.getCols()];
            stage.close();
            pane.setCenter(makeBoard());
            stage.show();
        }

        infoDump.setText(msg);

        for(int i = 0; i < model.getRows(); i++){
            for(int j = 0; j < model.getCols(); j++){

                if(!model.getCell(i,j).equals(".")){
                    if(model.getCell(i,j).equals("P")){
                        this.board[i][j].setGraphic(new ImageView(pawn));
                    }
                    if(model.getCell(i,j).equals("N")){
                        this.board[i][j].setGraphic(new ImageView(knight));
                    }
                    if(model.getCell(i,j).equals("B")){
                        this.board[i][j].setGraphic(new ImageView(bishop));
                    }
                    if(model.getCell(i,j).equals("R")){
                        this.board[i][j].setGraphic(new ImageView(rook));
                    }
                    if(model.getCell(i,j).equals("K")){
                        this.board[i][j].setGraphic(new ImageView(king));
                    }
                    if(model.getCell(i,j).equals("Q")){
                        this.board[i][j].setGraphic(new ImageView(queen));
                    }
                }else{
                    this.board[i][j].setGraphic(null);
                }
            }
        }



        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    /**the main*/
    public static void main(String[] args) {
        Application.launch(args);
    }
}
