/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.agents;

import dyno.schedule.models.ShopOrderModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class ShopOrderAgent extends Agent
{

    protected ShopOrderModel data;

    /**
     * This method is automatically called when "agent".start() is executed.
     * Consider that Agent is launched for the first time. 1 set the agent
     * attributes 2 add the behaviours
     *
     */
    @Override
    protected void setup()
    {
        super.setup(); //To change body of generated methods, choose Tools | Templates.

        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            data = (ShopOrderModel)args[0];

        } else
        {
            System.out.println("Erreur lors du tranfert des parametres");
        }

        //Add the behaviours
        addBehaviour(new SendMessage(this));

        System.out.println("the sender agent " + this.getLocalName() + " is started");
    }

    /**
     * This method is automatically called after doDelete()
     */
    protected void takeDown()
    {

    }

    /**
     * ************************************
     *
     *
     * BEHAVIOURS
     *
     *
     *************************************
     */
    public class SendMessage extends SimpleBehaviour
    {

        /**
         * When an agent choose to communicate with others agents in order to
         * reach a precise decision, it tries to form a coalition. This
         * behaviour is the first step of the paxos
         *
         */
        private static final long serialVersionUID = 9088209402507795289L;

        private boolean finished = false;

        public SendMessage(final Agent myagent)
        {
            super(myagent);

        }

        public void action()
        {
            //Create a message in order to send it to the choosen agent
            final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(this.myAgent.getAID());
            //msg.setLanguage(MyOntology.LANGUAGE);
            //msg.setOntology(MyOntology.ONTOLOGY_NAME);
            //msg.setProtocol(MyOntology.PAXOS_PREPARE);

            msg.addReceiver(new AID("workCenterAgentWC1", AID.ISLOCALNAME)); // hardcoded= bad, must give it with objtab
            msg.setContent(((ShopOrderAgent) this.myAgent).data.getOrderNo());

            this.myAgent.send(msg);
            System.out.println("----> Message sent to " + msg.getAllReceiver().next() + " ,content= " + msg.getContent());
            
            msg.addReceiver(new AID("workCenterAgentWC2", AID.ISLOCALNAME)); // hardcoded= bad, must give it with objtab
            msg.setContent(((ShopOrderAgent) this.myAgent).data.getOrderNo());
            
            System.out.println("----> Message sent to " + msg.getAllReceiver().next() + " ,content= " + msg.getContent());
            this.myAgent.send(msg);
            this.finished = true;
        }

        public boolean done()
        {
            return finished;
        }
    }
}
