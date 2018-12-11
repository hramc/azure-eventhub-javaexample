package edu.azure.eventhub.receiver.eph;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;

import edu.azure.constants.Constants;

/**
 * 
 * @author hramc
 * 
 * Class to receive data from Event hub. It uses Eventhub processor Host azure library.
 *
 */
public class EventProcessorReciever
{
    public static void main(String args[]) throws InterruptedException, ExecutionException, UnknownHostException
    {
    	// Get the Host name for our reference
    	String HOST_NAME = InetAddress.getLocalHost().getHostName();
    	
    	// Initialize the Eventhub processor host
    	final EventProcessorHost eventHubProcessorHost = new EventProcessorHost(
                EventProcessorHost.createHostName(HOST_NAME),
                Constants.EH_NAME,
                Constants.CONSUMER_GROUP,
                Constants.LISTENER_CONNECTION_STRING.toString(),
                Constants.AZURE_STORAGE_CONNECTION_STRING,
                Constants.AZURE_STORAGE_CONTAINER_NAME);
    	
   	
        System.out.println("Registering host named " + eventHubProcessorHost.getHostName());
        
        // Event hub options
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());
        options.setMaxBatchSize(1);
        
        /**
         * whenComplete - code will get execute after register the event processor listener. 
         * if any exception in initializing in listener, we will get to know.
         * 
         * thenAccept - this code will be executed after registering the listener. 
         * here we are waiting in a loop. Till it get satisify, listener will read the data from the eventhub
         * 
         * thenCompose - it will be last step of unregistering the listener.
         * 
         */
        eventHubProcessorHost.registerEventProcessor(EventProcessor.class, options)
        .whenComplete((unused, e) ->
        {
            if (e != null)
            {
                System.out.println("Failure while registering: " + e.toString());
                if (e.getCause() != null)
                {
                    System.out.println("Inner exception: " + e.getCause().toString());
                }
            }
        })
        .thenAccept((unused) ->
        {
            System.out.println("Press any key to Stop");
            try 
            {
                System.in.read();
            }
            catch (Exception e)
            {
                System.out.println("Keyboard read failed: " + e.toString());
            }
        })
        .thenCompose((unused) ->
        {
            return eventHubProcessorHost.unregisterEventProcessor();
        })
        .exceptionally((e) ->
        {
            System.out.println("Failure while unregistering: " + e.toString());
            if (e.getCause() != null)
            {
                System.out.println("Inner exception: " + e.getCause().toString());
            }
            return null;
        })
        .get(); // Wait for everything to finish before exiting main!

        System.out.println("End of Reading Data");
    }
}
