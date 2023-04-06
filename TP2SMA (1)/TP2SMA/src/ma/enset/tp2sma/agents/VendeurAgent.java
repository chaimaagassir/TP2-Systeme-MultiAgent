package ma.enset.tp2sma.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.Random;

public class VendeurAgent extends GuiAgent {
    private VendeurGUI serverGUI;
    DFAgentDescription dfAgentDescription;

    @Override
    protected void setup() {
        dfAgentDescription = new DFAgentDescription();
        serverGUI=(VendeurGUI)getArguments()[0];
        serverGUI.setServerAgent(this);
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message=receive();
                if(message!=null){
                    serverGUI.showMessage(message.getContent());
                  if (message.getPerformative()==ACLMessage.CFP){
                      ACLMessage message1=message.createReply();

                      message1.setPerformative(ACLMessage.PROPOSE);
                      System.out.println(message);
                      System.out.println(message1);
                      if(message.getContent().equals("mac1")){
                          message1.setContent(String.valueOf(new Random().nextFloat()*20000));
                          send(message1);
                      }else if(message.getContent().equals("mac2")){
                          message1.setContent(String.valueOf(new Random().nextFloat()*20000));
                          send(message1);
                      }
                  }else if(message.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
                      ACLMessage message1=message.createReply();
                      message1.setPerformative(ACLMessage.AGREE);
                      send(message1);
                  }
                }else {
                    block();
                }
            }
        });
        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforeMove() {

    }

    @Override
    protected void afterMove() {

    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        Iterator<String> it=guiEvent.getAllParameter();
        while(it.hasNext()){
            ServiceDescription serviceDescription=new ServiceDescription();
            serviceDescription.setName(it.next());
            serviceDescription.setType(it.next());
            dfAgentDescription.addServices(serviceDescription);
            try {
                DFService.register(this,dfAgentDescription);
            } catch (FIPAException e) {

            }
        }

    }
}
