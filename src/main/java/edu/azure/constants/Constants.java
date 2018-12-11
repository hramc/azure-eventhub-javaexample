package edu.azure.constants;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

public interface Constants {
	
	// Eventhub Namespace
	String EH_NAMESPACE="<--Evenhub Name Space-->";
	String EH_NAME = "<-- Event hub Name -->";
	
	String SENDER_SAS_KEY_NAME = "<-- Sender SAS Key Name -->";
	String SENDER_SAS_KEY="<-- Sender SAS KEY (Primary Key) -->";
	String SAMPLE_FILE_NAME = "sample.json";
	
	String LISTEN_SAS_KEY_NAME = "<-- Listener SAS Key Name -->";
	String LISTEN_SAS_KEY="<-- Listener SAS KEY (Primary Key) -->";
	String CONSUMER_GROUP="<-- consumer group (required for listerner) -->";
	
	// Azure Storage information required for Event hub listener to do checkpoint 
	String AZURE_STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=<accountName>;AccountKey=<primaryKey>;EndpointSuffix=core.windows.net";
    String AZURE_STORAGE_CONTAINER_NAME = "<-- container name -->";
     
     
				
	final ConnectionStringBuilder SENDER_CONNECTION_STRING = new ConnectionStringBuilder()
	        .setNamespaceName(EH_NAMESPACE)
	        .setEventHubName(EH_NAME)
	        .setSasKeyName(SENDER_SAS_KEY_NAME)
	        .setSasKey(SENDER_SAS_KEY);
	
	 final ConnectionStringBuilder LISTENER_CONNECTION_STRING = new ConnectionStringBuilder()
             .setNamespaceName(EH_NAMESPACE)
             .setEventHubName(EH_NAME)
             .setSasKeyName(LISTEN_SAS_KEY_NAME)
             .setSasKey(LISTEN_SAS_KEY);
	
}
