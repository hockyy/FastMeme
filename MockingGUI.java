import javafx.scene.control.Alert.*;
import javafx.scene.control.ButtonType.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.stage.*;
import java.util.*;
import java.io.*;
import java.util.*;

class Delta{
    double x,y;
}

public class MockingGUI extends Application{
    final static int CANVASX = 700;
    final static int CANVASY = 500;

    public String randomize(String s){
        Random rand = new Random();
        String ret = "";
        for(int i = 0;i < s.length();i++){
            char ch = s.charAt(i);
            if(ch == '&'){
                ret += '\n';
                continue;
            }
            int isUpper = rand.nextInt(2);
            if(isUpper == 0){
                if('a' <= ch && ch <= 'z') ch = (char)(ch-'a'+'A');
            }else{
                if('A' <= ch && ch <= 'Z') ch = (char)(ch-'A'+'a');
            }
            ret += ch;
        }
        return ret;
    }

    public void clear(GridPane gridPane){
        Set <Node> deleteNodes = new HashSet<>();
        for(Node child : gridPane.getChildren()){
            Integer rowIndex = rowIndex = gridPane.getRowIndex(child);
            if(rowIndex != null) deleteNodes.add(child);
        }
        gridPane.getChildren().removeAll(deleteNodes);
    }

    @Override
    public void start(Stage stage) throws Exception{
        VBox mainPane = new VBox(7);
        mainPane.setPadding(new Insets(10,10,10,10));
        mainPane.setAlignment(Pos.TOP_CENTER);
        Text titleText = new Text("Your best meme Generator!");
        titleText.setFont(Font.font(15));
        titleText.setStyle("-fx-text-fill: #000372;");
        ChoiceBox memeChooser = new ChoiceBox(FXCollections.observableArrayList("Doge Swole Meme", "Spongebob Mocking Meme"));

        Pane memePane = new Pane();
        FileInputStream memeStream = new FileInputStream("pics/title.jpg");
        ImageView mainMeme = new ImageView(new Image(memeStream));
        mainMeme.setX(CANVASX/2-200);
        mainMeme.setFitWidth(400);
        mainMeme.setPreserveRatio(true);
        memePane.getChildren().add(mainMeme);

        final GridPane textInputPane = new GridPane();
        textInputPane.setAlignment(Pos.CENTER);
        textInputPane.setVgap(10);
        textInputPane.setHgap(10);

        Button saveButton = new Button("Save this meme");

        mainPane.getChildren().addAll(titleText, memeChooser, memePane, textInputPane, saveButton);
        
        memeChooser.setOnAction(e -> {
            System.out.println(memeChooser.getValue());
            if(memeChooser.getValue().equals("Doge Swole Meme")){
                try{
                    memePane.getChildren().clear();
                    memePane.getChildren().add(mainMeme);
                    mainMeme.setImage(new Image(new FileInputStream("pics/doge.jpg")));
                }catch(Exception ex){
                    System.out.println("Doge file not found!");
                }

                clear(textInputPane);

                TextField textInput1 = new TextField();
                Text descInput1 = new Text("Big doge name");

                TextField textInput2 = new TextField();
                Text descInput2 = new Text("Big doge text");

                TextField textInput3 = new TextField();
                Text descInput3 = new Text("Small doge name");

                TextField textInput4 = new TextField();
                Text descInput4 = new Text("Small doge text");

                textInputPane.add(descInput1,0,0);
                textInputPane.add(textInput1,1,0);

                textInputPane.add(descInput2,2,0);
                textInputPane.add(textInput2,3,0);

                textInputPane.add(descInput3,0,1);
                textInputPane.add(textInput3,1,1);

                textInputPane.add(descInput4,2,1);
                textInputPane.add(textInput4,3,1);

            }else{
                try{
                    memePane.getChildren().clear();
                    memePane.getChildren().add(mainMeme);
                    mainMeme.setImage(new Image(new FileInputStream("pics/spongebob.jpg")));
                }catch(Exception ex){
                    System.out.println("Doge file not found!");
                }

                clear(textInputPane);

                TextField textInput = new TextField("stupid");
                Text descInput = new Text("Mocking text");
                Text desc = new Text("sTuPid");
                //Setting the font of the text
                desc.setStyle("-fx-font: 40px \"Consolas\"; -fx-text-alignment: center; -fx-font-weight: Bold; -fx-fill: #FFFFFF; -fx-stroke: black; -fx-stroke-width: 1;");
                double curX = 200, curY = 200;
                desc.setX(200);
                desc.setY(200);
                final Delta dragDelta = new Delta();
                desc.setOnMousePressed(mouseEvent -> {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = desc.getX() - mouseEvent.getScreenX();
                    dragDelta.y = desc.getY() - mouseEvent.getScreenY();
                });
                desc.setOnMouseDragged(mouseEvent -> {
                    desc.setX(mouseEvent.getScreenX() + dragDelta.x);
                    desc.setY(mouseEvent.getScreenY() + dragDelta.y);
                });
                desc.setOnScroll(scrollEvent -> {
                    // Adjust the zoom factor as per your requirement
                    double zoomFactor = 1.05;
                    double deltaY = scrollEvent.getDeltaY();
                    if (deltaY < 0){
                        zoomFactor = 2.0 - zoomFactor;
                    }
                    desc.setScaleX(desc.getScaleX() * zoomFactor);
                    desc.setScaleY(desc.getScaleY() * zoomFactor);
                });

                textInput.setOnKeyReleased(keyEvent -> {
                    String currentString = textInput.getText();
                    currentString = randomize(currentString);
                    desc.setText(currentString);
                });

                memePane.getChildren().add(desc);

                textInputPane.add(descInput,0,0);
                textInputPane.add(textInput,1,0);

            }
        });

        mainPane.setStyle("-fx-background-color: #C9FFFE");
        Scene scene = new Scene(mainPane, 700, 480);

        stage.getIcons().add(new Image(new FileInputStream("pics/logo.png")));
        stage.setScene(scene);
        stage.setTitle("Your stupid meme generator");
        stage.show();

        stage.setOnCloseRequest(e -> {
            Alert closeAlert = new Alert(AlertType.CONFIRMATION);
            closeAlert.setTitle("Exiting..");
            closeAlert.setHeaderText("Close this coolest smith?");
            closeAlert.setContentText("You haven't saved yet bruv");
      // option != null.
            Optional<ButtonType> option = closeAlert.showAndWait();
            if(option.get() == ButtonType.OK){
            }else e.consume();
        });
    }

    public static void main(String[] args){
        Application.launch();
    }

}