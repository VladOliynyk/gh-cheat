package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.util.Objects;


public class Controller {

    @FXML
    private Pane myPane;

    @FXML
    private Button buttonRock;

    @FXML
    private Button buttonHelp;

    @FXML
    private Button buttonStop;

    @FXML
    private Button buttonSetupGamearea;

    @FXML
    private Text coordsLabel;


    @FXML
    public void initialize() {

        buttonRock.setDisable(true);
        buttonStop.setDisable(true);
        buttonSetupGamearea.setDisable(false);
        buttonRock.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // type something here or go to onClickMethod
            }
        });
        //buttonStop.setDisable(true);
        buttonStop.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // type something here or go to onClickMethod
            }
        });

        buttonHelp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String switchScene = "./helpScene.fxml";
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource(switchScene));
                    Parent widget = loader.load();
                    myPane.getChildren().clear();
                    myPane.getChildren().add(widget);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private int yellowX = 0;
    private int yellowY = 0;
    private Game game;


    @FXML
    private RadioButton isMultiplayerRadioBtn;

    @FXML
    public void onRockClickMethod() throws AWTException {
        if (!buttonRock.isDisable()) {
            boolean isMultiplayer;
            isMultiplayer = isMultiplayerRadioBtn.isSelected();

            game = new Game(yellowX, yellowY, isMultiplayer);
            game.launchGame();
            disableSetupButton();
            enableStopButton();
            disableRockButton();
            playRockSound();
        }
    }


    @FXML
    public void onStopClickMethod() {
        game.stopGame();
        if (yellowX != 0) {
            enableRockButton();
        }
        disableStopButton();
        enableSetupButton();
    }


    @FXML
    public void onHelpClickMethod() {

    }

    @FXML
    public void onSetupGameareaMethod() {
        setUpGameAreaCoords();
    }

    private void setUpGameAreaCoords() {
        Thread setUpGameAreaCoordsThread = new Thread(new Runnable() {
            public int X;
            public int Y;

            public void run() {
                String str = "processing";
                coordsLabel.setText(str);
                disableSetupButton();
                disableRockButton();

                int[] xArray = {-1, -2, -3};
                int[] yArray = {-1, -2, -3};

                while (true) {

                    if (Objects.equals(coordsLabel.getText(), "processing...")) {
                        str = "processing";
                    } else {
                        str += ".";
                    }
                    coordsLabel.setText(str);


                    PointerInfo pointer = MouseInfo.getPointerInfo();
                    X = (int) pointer.getLocation().getX();
                    Y = (int) pointer.getLocation().getY();

                    if (xArray[0] == xArray[1] && xArray[0] == xArray[2] && yArray[0] == yArray[1] && yArray[0] == yArray[2]) {
                        yellowX = xArray[0];
                        yellowY = yArray[0];
                        coordsLabel.setText(yellowX + ":" + yellowY);
                        playSetupSound();
                        enableRockButton();
                        enableSetupButton();
                        break;
                    } else {
                        xArray[0] = xArray[1];
                        yArray[0] = yArray[1];
                        xArray[1] = xArray[2];
                        yArray[1] = yArray[2];
                        xArray[2] = X;
                        yArray[2] = Y;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        });


        setUpGameAreaCoordsThread.start();
    }

    private void enableRockButton() {
        buttonRock.setDisable(false);
    }

    private void disableRockButton() {
        buttonRock.setDisable(true);
    }

    private void enableSetupButton() {
        buttonSetupGamearea.setDisable(false);
    }

    private void disableSetupButton() {
        buttonSetupGamearea.setDisable(true);
    }

    private void enableStopButton() {
        buttonStop.setDisable(false);
    }

    private void disableStopButton() {
        buttonStop.setDisable(true);
    }

    private void playSetupSound() {
        String musicFile = "src/sample/sounds/setupsound.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        Thread musicSetupThread = new Thread(new Runnable() {
            public void run() {
                mediaPlayer.play();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        musicSetupThread.start();
    }

    private void playRockSound() {
        String musicFile = "src/sample/sounds/rocksound.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        Thread musicRockThread = new Thread(new Runnable() {
            public void run() {
                mediaPlayer.play();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        musicRockThread.start();
    }
}
