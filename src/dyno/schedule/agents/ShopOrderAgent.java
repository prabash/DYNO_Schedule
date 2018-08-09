/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.agents;

import dyno.schedule.utils.DateTimeUtil;
import dyno.schedule.models.ShopOrderModel;
import dyno.schedule.models.ShopOrderOperationModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
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
public class ShopOrderAgent extends Agent
{

    protected ShopOrderModel shopOrder;

    // The target date that the operation should be started (FS) or Ended (BS)
    private Date targetOperationDate = null;
    private int targetOperationID = 0;
    // The list of known workcenter agents
    private AID[] workCenterAgents;

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
            shopOrder = (ShopOrderModel) args[0];
            for (ShopOrderOperationModel operation : shopOrder.getOperations())
            {
                try
                {
                    // if the targetOperationDate is null (initially), set the SO created date as the startdate and concatenate with 8.00AM as the starting time
                    // if the targetOperationDate is available use it as it is (since it will already have the time portion available)
                    targetOperationDate = targetOperationDate == null ? DateTimeUtil.ConcatenateDateTime(shopOrder.getCreatedDate(), DateTimeUtil.getDefaultSimpleTimeFormat().parse("08:00:00")) : targetOperationDate;
                    targetOperationID = operation.getOperationId();
                } catch (ParseException ex)
                {
                    Logger.getLogger(ShopOrderAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Target operation date is " + targetOperationDate);

                // Add a TickerBehaviour that schedules a request to seller agents every minute
//                addBehaviour(new TickerBehaviour(this, 5000)
//                {
//                    protected void onTick()
//                    {
                        System.out.println("Trying to schedule operation : " + targetOperationDate);
                        
                        // Update the list of seller agents
                        DFAgentDescription template = new DFAgentDescription();
                        ServiceDescription serviceDesc = new ServiceDescription();
                        // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
                        // therefore this should be dynamically set.
                        serviceDesc.setType("work-center-" + operation.getWorkCenterType());
                        serviceDesc.setName("schedule-work-center-service");
                        
                        template.addServices(serviceDesc);
                        try
                        {
                            // find the agents belonging to the certain work center type
                            DFAgentDescription[] result = DFService.search(this, template);
                            System.out.println("Found the following seller agents:");
                            workCenterAgents = new AID[result.length];
                            for (int i = 0; i < result.length; ++i)
                            {
                                workCenterAgents[i] = result[i].getName();
                                System.out.println(workCenterAgents[i].getName());
                            }
                        } catch (FIPAException fe)
                        {
                            fe.printStackTrace();
                        }

                        // Perform the request
                        addBehaviour(new BWorkCenterRequestHandler(operation));
//                    }
//                });
            }

        } else
        {
            System.out.println("Error with the Shop Order arguments");
        }
//        //Add the behaviours
//        addBehaviour(new SendMessage(this));

        System.out.println("the Shop Order Agent " + this.getLocalName() + " is started");
    }

    /**
     * This method is automatically called after doDelete()
     */
    @Override
    protected void takeDown()
    {
        System.out.println("test");
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
            msg.setContent(((ShopOrderAgent) this.myAgent).shopOrder.getOrderNo());

            this.myAgent.send(msg);
            System.out.println("----> Message sent to " + msg.getAllReceiver().next() + " ,content= " + msg.getContent());

            msg.addReceiver(new AID("workCenterAgentWC2", AID.ISLOCALNAME)); // hardcoded= bad, must give it with objtab
            msg.setContent(((ShopOrderAgent) this.myAgent).shopOrder.getOrderNo());

            System.out.println("----> Message sent to " + msg.getAllReceiver().next() + " ,content= " + msg.getContent());
            this.myAgent.send(msg);
            this.finished = true;
        }

        public boolean done()
        {
            return finished;
        }
    }

    private class BWorkCenterRequestHandler extends Behaviour
    {
        private int step = 0; // is used in the switch statement inside the action method.
        private AID bestWorkCenter; // work center that provides the best target date
        private Date bestOfferedDate; // The best possible start date if forward scheduling / end date if backward scheduling
        private int repliesCount = 0; // The counter of replies from work center agents
        private MessageTemplate msgTemplate; // The template to receive replies
        private final String converstaionId = "work-center-request";
        DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();
        DateFormat dateTimeFormat = DateTimeUtil.getDefaultSimpleDateTimeFormat();
        private ShopOrderOperationModel currentOperation;

        public BWorkCenterRequestHandler(ShopOrderOperationModel operation)
        {
            currentOperation = operation;
        }

        @Override
        public void action()
        {
            switch (step)
            {
                case 0:
                    // Send the cfp (Call for Proposal) to all sellers
                    ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < workCenterAgents.length; ++i)
                    {
                        cfpMessage.addReceiver(workCenterAgents[i]);
                    }
                    cfpMessage.setContent(dateTimeFormat.format(targetOperationDate));
                    cfpMessage.setConversationId(converstaionId);
                    cfpMessage.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value (can be something with the Shop Order No + operation No. and time)
                    myAgent.send(cfpMessage);

                    // Prepare the template to get proposals
                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(converstaionId),
                            MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    // Receive all proposals/refusals from seller agents
                    ACLMessage reply = myAgent.receive(msgTemplate);
                    // Date offered by the work center agent
                    Date offeredDate;
                    if (reply != null)
                    {
                        // Reply received
                        if (reply.getPerformative() == ACLMessage.PROPOSE)
                        {
                            try
                            {
                                // This is an offer, recieved with the date and the time
                                offeredDate = dateTimeFormat.parse(reply.getContent());
                                
                                System.out.println("++++++ offeredDate : " + offeredDate);
                                System.out.println("++++++ targetOperationDate : " + targetOperationDate);
                                System.out.println("++++++ bestOfferedDate : " + bestOfferedDate);

                                // if forward scheduling the offered date should be the earliest date/time that comes on or after the target date
                                if (bestWorkCenter == null || ((offeredDate.equals(targetOperationDate) || offeredDate.after(targetOperationDate)) && offeredDate.before(bestOfferedDate)))
                                {
                                    // This is the best offer at present
                                    bestOfferedDate = offeredDate;
                                    bestWorkCenter = reply.getSender();
                                    System.out.println("Current best offered time : " + bestOfferedDate + " by Work Center Agent : " + bestWorkCenter);
                                }
                            } catch (ParseException ex)
                            {
                                Logger.getLogger(ShopOrderAgent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        repliesCount++;
                        if (repliesCount >= workCenterAgents.length)
                        {
                            // We received all replies
                            step = 2;
                            System.out.println("++++++ RECEIVED ALL OFFERES! ++++++++");
                        }
                    } else
                    {
                        block();
                    }
                    break;
                case 2:
                    // Send the confirmation to the work center that sent the best date
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(bestWorkCenter);
                    order.setContent(String.valueOf(targetOperationID));
                    order.setConversationId(converstaionId);
                    order.setReplyWith("setOperation" + System.currentTimeMillis());
                    myAgent.send(order);
                    // Prepare the template to get the purchase order reply
                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(converstaionId),
                            MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    
                    // after accepting the offer, the new targetOperationDate should be the accepted date, for the next operation to begin
                    targetOperationDate = bestOfferedDate;
                    
                    step = 3;
                    break;
                case 3:
                    // Receive the confirmation reply
                    reply = myAgent.receive(msgTemplate);
                    if (reply != null)
                    {
                        // confirmation reply received
                        if (reply.getPerformative() == ACLMessage.INFORM)
                        {
                            // Date set successfully. We can terminate
                            System.out.println("Operation " + currentOperation.getOperationId() + " was successfully scheduled on " + targetOperationDate + " at work center" + reply.getSender().getName());
                            System.out.println("Set Date = " + bestOfferedDate);
                            
                            //myAgent.doDelete();
                        } else
                        {
                            System.out.println("Attempt failed: requested book already sold.");
                        }

                        step = 4;
                    } else
                    {
                        block();
                    }
                    break;
            }

        }

        @Override
        public boolean done()
        {
            if (step == 2 && bestWorkCenter == null)
            {
                System.out.println("Attempt failed: there are no work centers available for the date" + targetOperationDate);
            }
            return ((step == 2 && bestWorkCenter == null) || step == 4);
        }
    }
}
