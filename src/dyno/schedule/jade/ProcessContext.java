/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.jade;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prabash
 */
public class ProcessContext
{

    // private final MainApplication maniApp;
    private final String hostName;
    // private final AgentMap agentMap;
    private ContainerController agentConainer;

    public ProcessContext(/*MainApplication mainApp,*/String hostName)
    {
        this.hostName = hostName;
        createContainer(this.hostName);
    }

    private void createContainer(String hostName)
    {

        Runtime rt = Runtime.instance();
        // 1) create a platform (main container+DF+AMS)
        Profile pMain = new ProfileImpl(hostName, 8888, null);
        AgentContainer mainContainerRef = rt.createMainContainer(pMain); //DF and AMS are include
        System.out.println("Main container created.");

        ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
        agentConainer = rt.createAgentContainer(pContainer);
        System.out.println("Agent container created.");
    }

    public <T extends Agent> void addAgents(String name, T classType)
    {

        System.out.println("Creating agnet - " + name);
        try
        {

            List<String> data = new ArrayList<>();
            data.add("1");
            data.add("2");
            data.add("3");
            Object[] objtab = new Object[]
            {
                data
            };//used to give informations to the agent
            AgentController ag = agentConainer.createNewAgent(name, classType.getClass().getName(), objtab);

        } catch (StaleProxyException ex)
        {
            Logger.getLogger(ProcessContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Created agnet - " + name);
    }
}
