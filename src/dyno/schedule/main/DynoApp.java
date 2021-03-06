/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.main;

import dyno.schedule.agents.ManagerAgent;
import dyno.schedule.agents.ShopOrderAgent;
import dyno.schedule.agents.WorkCenterAgent;
import dyno.schedule.data.ShopOrderDataManager;
import dyno.schedule.data.WorkCenterDataManager;
import dyno.schedule.data.WorkCenterOpAllocDataManager;
import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.ShopOrderModel;
import dyno.schedule.models.WorkCenterModel;
import dyno.schedule.models.WorkCenterOpAllocModel;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Prabash
 */
public class DynoApp
{

    private static String hostname = "127.0.0.1";
    private static HashMap<String, ContainerController> containerList = new HashMap<String, ContainerController>();// container's name - container's ref
    private static List<AgentController> agentList;// agents's ref
    private static Runtime rt;
    private static DataGetMethod getMethod = DataGetMethod.Excel;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        List<ShopOrderModel> shopOrders = (new ShopOrderDataManager(getMethod)).loadShopOrderDetails();
        List<WorkCenterModel> workCenters = (new WorkCenterDataManager(getMethod)).loadWorkCenterDetails();
        List<WorkCenterOpAllocModel> workCenterOpAlloc = (new WorkCenterOpAllocDataManager(getMethod)).loadWorkCenterOpAllocDetails();
        
        //1), create the platform (Main container (DF+AMS) + containers + monitoring agents : RMA and SNIFFER)
        rt = emptyPlatform(containerList);

        // TODO get the default container.
        ContainerController container = containerList.get("container0");
        
        agentList = new ArrayList<>();
        //2) create agents and add them to the platform.
        agentList.addAll(createShopOrderAgents(container, shopOrders));
        agentList.addAll(createWorkCenterAgents(container, workCenters));
        
        try
        {
            System.out.println("Press a key to start the agents");
            System.in.read();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //3) launch agents
        startAgents(agentList);
    }

    /**
     * Create an empty platform composed of 1 main container and 3 containers.
     *
     * @return a ref to the platform and update the containerList
     */
    private static Runtime emptyPlatform(HashMap<String, ContainerController> containerList)
    {
        Runtime rt = Runtime.instance();

        // Create a platform (main container + DF + AMS)
        Profile mainProf = new ProfileImpl(hostname, 8888, null);
        System.out.println("Launching the main container ... " + mainProf);
        AgentContainer mainContainerRef = rt.createMainContainer(mainProf); // Including DF and AMS

        // 2) create the containers
        containerList.putAll(createContainers(rt));

        // 3) create monitoring agents : rma agent, used to debug and monitor the platform; sniffer agent, to monitor communications; 
        createMonitoringAgents(mainContainerRef);

        System.out.println("Plaform ok");

        return rt;
    }

    /**
     * Create the containers used to hold the agents
     *
     * @param rt The reference to the main container
     * @return an Hmap associating the name of a container and its object
     * reference.
     *
     * note: there is a smarter way to find a container with its name, but we go
     * fast to the goal here. Cf jade's doc.
     */
    private static Map<String, ContainerController> createContainers(Runtime rt)
    {
        String containerName;
        ProfileImpl pContainer;
        ContainerController containerRef;
        HashMap<String, ContainerController> containerList = new HashMap<String, ContainerController>();//bad to do it here.

        System.out.println("Launching containers ...");

        //create the container0	
        containerName = "container0";
        pContainer = new ProfileImpl(null, 8888, null);
        System.out.println("Launching container " + pContainer);
        containerRef = rt.createAgentContainer(pContainer); // ContainerController replace AgentContainer in the new versions of Jade.
        containerList.put(containerName, containerRef);

        System.out.println("Launching containers done");
        return containerList;
    }

    private static void createMonitoringAgents(ContainerController mc)
    {
        System.out.println("Launching the rma agent on the main container ...");
        AgentController rma;

        try
        {
            rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();
        } catch (StaleProxyException e)
        {
            e.printStackTrace();
            System.out.println("Launching of rma agent failed");
        }

        System.out.println("Launching  Sniffer agent on the main container...");
        AgentController snif = null;

        try
        {
            snif = mc.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer", new Object[0]);
            snif.start();

        } catch (StaleProxyException e)
        {
            e.printStackTrace();
            System.out.println("launching of sniffer agent failed");
        }
    }

    /**
     * ********************************************
     *
     * Methods used to create the agents and to start them
     *
     *********************************************
     */
    /**
     * Creates the agents and add them to the agentList. agents are NOT started.
     *
     * @param containerList :Name and container's ref
     * @param sniff : a ref to the sniffeur agent
     * @return the agentList
     */
    private static List<AgentController> createAgents(HashMap<String, ContainerController> containerList)
    {
        System.out.println("Launching agents...");
        ContainerController c;
        String agentName;
        List<AgentController> agentList = new ArrayList();

        //Agent0 on container0
        c = containerList.get("container0");
        agentName = "ShopOrderAgent1";
        try
        {

            List<String> data = new ArrayList<String>();
            data.add("1");
            data.add("2");
            data.add("3");
            Object[] objtab = new Object[]
            {
                data
            };//used to give informations to the agent
            AgentController ag = c.createNewAgent(agentName, ShopOrderAgent.class.getName(), objtab);
            agentList.add(ag);
            System.out.println(agentName + " launched");
        } catch (StaleProxyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Agent1 and Agent2 on container1
        c = containerList.get("container0");
        agentName = "WorkCenterAgent1";
        try
        {
            Object[] objtab = new Object[]
            {
            };//used to give informations to the agent
            AgentController ag = c.createNewAgent(agentName, WorkCenterAgent.class.getName(), objtab);
            agentList.add(ag);
            System.out.println(agentName + " launched");
        } catch (StaleProxyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        agentName = "ManagerAgent1";
        try
        {
            Object[] objtab = new Object[]
            {
            };//used to give informations to the agent
            AgentController ag = c.createNewAgent(agentName, ManagerAgent.class.getName(), objtab);
            agentList.add(ag);
            System.out.println(agentName + " launched");
        } catch (StaleProxyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Agent3 on container2
        c = containerList.get("container0");
        agentName = "ManagerAgent2";
        try
        {
            Object[] objtab = new Object[]
            {
            };//used to give informations to the agent
            AgentController ag = c.createNewAgent(agentName, ManagerAgent.class.getName(), objtab);
            agentList.add(ag);
            System.out.println(agentName + " launched");
        } catch (StaleProxyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Agents launched...");
        return agentList;
    }

    
    private static List<AgentController> createShopOrderAgents(ContainerController container, List<ShopOrderModel> shopOrders)
    {
        System.out.println("Creating Shop Order Agents...");
        String agentName;
        List<AgentController> agentList = new ArrayList();

        for (ShopOrderModel shopOrder : shopOrders)
        {
            agentName = "shopOrderAgent" + shopOrder.getOrderNo();
            try
            {
                Object[] objtab = new Object[]
                {
                    shopOrder
                };//used to give informations to the agent

                AgentController agentController = container.createNewAgent(agentName, ShopOrderAgent.class.getName(), objtab);
                agentList.add(agentController);
                System.out.println(agentName + " launched");
            } catch (StaleProxyException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return agentList;
    }

    private static List<AgentController> createWorkCenterAgents(ContainerController container, List<WorkCenterModel> workCenters)
    {
        System.out.println("Creating Work Center Agents...");
        String agentName;
        List<AgentController> agentList = new ArrayList();

        for (WorkCenterModel workCenter : workCenters)
        {
            agentName = "workCenterAgent" + workCenter.getWorkCenterNo();
            try
            {
                Object[] objtab = new Object[]
                {
                    workCenter
                };//used to give informations to the agent

                AgentController agentController = container.createNewAgent(agentName, WorkCenterAgent.class.getName(), objtab);
                agentList.add(agentController);
                System.out.println(agentName + " launched");
            } catch (StaleProxyException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return agentList;
    }

    /**
     * Start the agents
     *
     * @param agentList
     */
    private static void startAgents(List<AgentController> agentList)
    {

        System.out.println("Starting agents...");

        for (final AgentController ac : agentList)
        {
            try
            {
                ac.start();
            } catch (StaleProxyException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        System.out.println("Agents started...");
    }
}
