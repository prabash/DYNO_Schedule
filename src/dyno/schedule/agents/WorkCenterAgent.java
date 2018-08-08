/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.agents;

import dyno.schedule.data.WorkCenterOpAllocDataManager;
import dyno.schedule.models.WorkCenterModel;
import dyno.schedule.utils.DateTimeUtil;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prabash
 */
public class WorkCenterAgent extends Agent
{

    protected WorkCenterModel workCenter;
    // the target date requested by the shop order agent for a particular operation
    Date requestedOpDate;
    // the best date and timeblock available to the work center agent
    Date bestOfferedDate;

    protected void setup()
    {
        super.setup();

        // Register the book-selling service in the yellow pages
        DFAgentDescription dfAgentDesc = new DFAgentDescription();
        dfAgentDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();

        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            workCenter = (WorkCenterModel) args[0];

            // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
            // therefore this should be dynamically set.
            serviceDescription.setType("work-center-" + workCenter.getWorkCenterType());
            serviceDescription.setName("schedule-work-center-service");
            dfAgentDesc.addServices(serviceDescription);
            try
            {
                // register the work center agent service
                DFService.register(this, dfAgentDesc);
            } catch (FIPAException fe)
            {
                fe.printStackTrace();
            }

        } else
        {
            System.out.println("Error with the Work Center arguments");
        }

        //Add the behaviours
        //addBehaviour(new ReceiveMessage(this));
        addBehaviour(new BOfferAvailableDate());
        addBehaviour(new BAssignOperationToWorkCenter());

        System.out.println("the Work Center agent " + this.getLocalName() + " is started");
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
    public class ReceiveMessage extends SimpleBehaviour
    {

        /**
         * When an agent choose to communicate with others agents in order to
         * reach a precise decision, it tries to form a coalition. This
         * behaviour is the first step of the paxos
         *
         */
        private static final long serialVersionUID = 9088209402507795289L;

        private boolean finished = false;

        public ReceiveMessage(final Agent myagent)
        {
            super(myagent);

        }

        public void action()
        {
            //1) receive the message
            final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

            final ACLMessage msg = this.myAgent.receive(msgTemplate);
            if (msg != null)
            {
                System.out.println("<----Message received from " + msg.getSender() + " ,content= " + msg.getContent());
                this.finished = true;
            } else
            {
                System.out.println("Receiver - No message received");
            }
        }

        public boolean done()
        {
            return finished;
        }

    }

    /**
     * Inner class OfferRequestsServer. This is the behaviour used by Work
     * Center agents to serve incoming requests for offer from Shop Order
     * agents. The work center agent gets the target date and sends a response
     * with the earliest available date that comes after that.
     */
    private class BOfferAvailableDate extends CyclicBehaviour
    {

        DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();
        DateFormat dateTimeFormat = DateTimeUtil.getDefaultSimpleDateTimeFormat();
        
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                try
                {
                    // CFP Message received. Process it
                    requestedOpDate = dateTimeFormat.parse(msg.getContent());
                    ACLMessage reply = msg.createReply();

                    // you should get the date related to the work center that is the earliest date after the target date
                    bestOfferedDate = WorkCenterOpAllocDataManager.getBestDateTimeOffer(workCenter.getWorkCenterNo(), requestedOpDate);

                    if (bestOfferedDate != null)
                    {
                        // reply with the earliest available date/timeblock that comes after the target date
                        reply.setPerformative(ACLMessage.PROPOSE);

                        // offer should be included with the time as well, therefore the dateTimeFormat is used
                        reply.setContent(dateTimeFormat.format(bestOfferedDate));
                    }
                    myAgent.send(reply);
                } catch (ParseException ex)
                {
                    Logger.getLogger(WorkCenterAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else
            {
                block();
            }
        } 
    }  // End of inner class BOfferAvailableDate

    /**
     * Inner class PurchaseOrdersServer. This is the behaviour used by
     * Book-seller agents to serve incoming offer acceptances (i.e. purchase
     * orders) from buyer agents. The seller agent removes the purchased book
     * from its catalogue and replies with an INFORM message to notify the buyer
     * that the purchase has been sucesfully completed.
     */
    private class BAssignOperationToWorkCenter extends CyclicBehaviour
    {
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                //Integer price = (Integer) catalogue.remove(title);
                if (bestOfferedDate != null)
                {
                    reply.setPerformative(ACLMessage.INFORM);
                    //update the excel sheet with the date
                    System.out.println(title + " sold to agent " + msg.getSender().getName());
                }
                myAgent.send(reply);
            } else
            {
                block();
            }
        }
    }  // End of inner class BAssignOperationToWorkCenter
}
