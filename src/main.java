import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class main extends Application {
    ImageView[] allImageViews = new ImageView[9];
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Image totalImage = null;
        try {
            totalImage = new Image(new FileInputStream("puzzle.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int width1piece=(int)totalImage.getWidth()/3;
        int height1piece=(int)totalImage.getHeight()/3;
        Image image1 = new WritableImage(totalImage.getPixelReader(),0,0,width1piece,height1piece);
        Image image2 = new WritableImage(totalImage.getPixelReader(),width1piece,0,width1piece,height1piece);
        Image image3 = new WritableImage(totalImage.getPixelReader(),width1piece*2,0,width1piece,height1piece);
        Image image4 = new WritableImage(totalImage.getPixelReader(),0,height1piece,width1piece,height1piece);
        Image image5 = new WritableImage(totalImage.getPixelReader(),width1piece,height1piece,width1piece,height1piece);
        Image image6 = new WritableImage(totalImage.getPixelReader(),width1piece*2,height1piece,width1piece,height1piece);
        Image image7 = new WritableImage(totalImage.getPixelReader(),0,height1piece*2,width1piece,height1piece);
        Image image8 = new WritableImage(totalImage.getPixelReader(),width1piece,height1piece*2,width1piece,height1piece);
        Image image9 = new WritableImage(totalImage.getPixelReader(),width1piece*2,height1piece*2,width1piece,height1piece);
        Image[] allImage = new Image[]{image1,image2,image3,image4,image5,image6,image7,image8,image9};










        for(int i =0 ;i<allImageViews.length;i++){
            ImageView iv = new ImageView();
            allImageViews[i] = iv;
            iv.setFitHeight(120);
            iv.setPreserveRatio(true);
            iv.setImage(allImage[i]);
            //DRAG AND DROP
            iv.setOnDragDetected(event -> {
                System.out.println("Drag");
                Dragboard dragboard = iv.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cC = new ClipboardContent();
                cC.putImage(iv.getImage());
                dragboard.setContent(cC);
            });
            iv.setOnDragDone(event -> {
                System.out.println("Drag Done");
            });
            iv.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.ANY);
            });
            iv.setOnDragDropped(event -> {
                Image content = event.getDragboard().getImage();
                ((ImageView)event.getGestureSource()).setImage(iv.getImage());//PEUT PTT BUGGER JE CROIS??
                iv.setImage(content);
                event.setDropCompleted(true);
            });
        }

        allImageViews=mix(allImage,allImageViews);







        BorderPane borderPane =new BorderPane();
        HBox haut = new HBox(allImageViews[0],allImageViews[1],allImageViews[2]);haut.setAlignment(Pos.CENTER);

        HBox millieux = new HBox(allImageViews[3],allImageViews[4],allImageViews[5]);millieux.setAlignment(Pos.CENTER);

        HBox bas = new HBox(allImageViews[6],allImageViews[7],allImageViews[8]);bas.setAlignment(Pos.CENTER);
        VBox all = new VBox(haut,millieux,bas);all.setAlignment(Pos.CENTER);
        borderPane.setCenter(all);
        borderPane.setOnDragExited(event -> {
            if(isGameWon(allImage,allImageViews)){
                Alert alerte = new Alert(Alert.AlertType.INFORMATION);
                alerte.setTitle("Information");
                alerte.setHeaderText("Vous avez réussis!");
                alerte.setContentText("Voulez vous réessayer?");
                ButtonType result = alerte.showAndWait().get();
                if(result == ButtonType.OK){
                    allImageViews = mix(allImage,allImageViews);
                }
            }
        });







        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);
        primaryStage.setMaxHeight(1440);
        primaryStage.setMaxWidth(2160);

        primaryStage.show();
        primaryStage.setScene(new Scene(borderPane));
    }


    
    public ImageView[] mix (Image[] allImages,ImageView[] currImageView){
        ImageView[] mix=currImageView;
        MyImages[] lol = new MyImages[9];
        for(int i=0;i<lol.length;i++){
            MyImages curr = new MyImages(allImages[i]);
            lol[i] = curr;
        }
        for(int i=0;i<mix.length;i++) {
            int rand = (int) (Math.random() * 9);
            if(!lol[rand].picked){
                mix[i].setImage(lol[rand].image);
                lol[rand].picked=true;
            }else{
                i--;
            }
        }
        return mix;
    }
    public boolean isGameWon (Image[] allImages,ImageView[] allImageView){
        boolean rep = true;
        for(int i=0;i<allImages.length;i++){
            if(!allImageView[i].getImage().equals(allImages[i])){
                rep=false;
            }
        }
        return rep;
    }
}
