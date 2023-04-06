package ma.enset.tp2sma.agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ClientGUI extends Application {
     private ClientAgent clientAgent;
    private ObservableList<String> data= FXCollections.observableArrayList();
    public static void main(String[] args) throws Exception {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startCotainer();
        BorderPane root=new BorderPane();

        ListView<String> listView=new ListView<>(data);
        Button buttonSend=new Button("Search");
        TextField textType=new TextField();
        Label labelType=new Label("Type");
        HBox hBox=new HBox(labelType,textType,buttonSend);
        root.setCenter(listView);
        root.setTop(hBox);
        Scene scene=new Scene(root,400,300);
        primaryStage.setScene(scene);
        primaryStage.show();
        buttonSend.setOnAction(event -> {
            String type=textType.getText();

            GuiEvent guiEvent=new GuiEvent(this,1);
            guiEvent.addParameter(type);
            clientAgent.onGuiEvent(guiEvent);
        });
    }
    private void startCotainer() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agentClient1=container.createNewAgent("client2","ma.enset.tp2sma.agents.ClientAgent",new Object[]{this});
        agentClient1.start();
    }

    public void setClientAgent(String clientAgent) {
        this.clientAgent = clientAgent;
    }
    public  void showService(String service){
        Platform.runLater(()->{
            data.add(service);
        });
        data.add(service);
    }
}
