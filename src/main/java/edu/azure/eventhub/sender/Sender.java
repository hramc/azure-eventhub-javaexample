package edu.azure.eventhub.sender;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;

import edu.azure.constants.Constants;

/**
 * 
 * @author hramc
 * 
 * Class to send a message into event hub
 *
 */
public class Sender {

    public static void main(String[] args)
            throws EventHubException, ExecutionException, InterruptedException, IOException {

    	// Executor Service required by the Eventhub client
    	ExecutorService executorService = Executors.newFixedThreadPool(1);
    	
    	// Eventhub client
    	final EventHubClient ehClient = EventHubClient.createSync(Constants.SENDER_CONNECTION_STRING.toString(), executorService);
 
    	// Send JSON Data Data 
    	ehClient.sendSync(EventData.create(new Gson().fromJson(new FileReader(
    			new File(Sender.class.getClassLoader().getResource(Constants.SAMPLE_FILE_NAME).getFile())),
    			JsonObject.class).
    			toString().getBytes(Charset.defaultCharset())));
    	
    	// close the client at the end of your program
    	ehClient.closeSync();
    	
    	// Shutdown the executor service
    	executorService.shutdown();

    }
    
 }
