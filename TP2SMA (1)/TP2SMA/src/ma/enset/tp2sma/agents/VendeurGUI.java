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

public class VendeurGUI extends Application {
    private VendeurAgent serverAgent;
    private ObservableList<String> data= FXCollections.observableArrayList();
    public static void main(String[] args) throws Exception {
       launch(args);
    }

    private void startConatiner() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agent=container.createNewAgent("server","ma.enset.tp2sma.agents.VendeurAgent",new Object[]{this});
        agent.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startConatiner();
        BorderPane root=new BorderPane();
        HBox hbox=new HBox();
        Label labelName=new Label("Nom : ");
        Label labelType=new Label("Type : ");
        TextField txtName= new TextField();
        TextField txtType= new TextField();
        Button buttonAdd=new Button("Ajouter");
        hbox.getChildren().addAll(labelName,txtName,labelType,txtType,buttonAdd);
        ListView<String> listView=new ListView<>(data);
        root.setCenter(listView);
        root.setTop(hbox);
        Scene scene=new Scene(root,400,300);
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonAdd.setOnAction(event -> {
            String name=txtName.getText();
            String type=txtType.getText();
            GuiEvent guiEvent=new GuiEvent(this,1);
            guiEvent.addParameter(txtName);
            guiEvent.addParameter(txtType);
            serverAgent.onGuiEvent(guiEvent);
            data.add(name+" "+type);
        });
    }
    public void showMessage(String message){
        Platform.runLater(()->{
            data.add(message);
        });

    }

    public VendeurAgent getServerAgent() {
        return serverAgent;
    }

    public void setServerAgent(VendeurAgent serverAgent) {
        this.serverAgent = serverAgent;
    }
}
