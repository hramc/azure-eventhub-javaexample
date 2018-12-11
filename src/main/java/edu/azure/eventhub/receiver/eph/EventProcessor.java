package edu.azure.eventhub.receiver.eph;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

/**
 * 
 * @author hramc
 * 
 * Listener Class of Event hub
 * 
 * It will get execute for each partition & messages.
 *
 */
public class EventProcessor implements IEventProcessor
{
	
	private long checkpointBatchingCount = 0;
	
    // OnOpen is called when a new event processor instance is created by the host. 
    @Override
    public void onOpen(PartitionContext context) throws Exception
    {
        System.out.println("Event Hub Reader: Partition " + context.getPartitionId() + " is opening");
    }

    // OnClose is called when an event processor instance is being shut down. 
    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception
    {
        System.out.println("Event Hub Reader: Partition " + context.getPartitionId() + " is closing for reason " + reason.toString());
    }

    // onError is called when an error occurs in EventProcessorHost code that is tied to this partition, such as a receiver failure.
    @Override
    public void onError(PartitionContext context, Throwable error)
    {
        System.out.println("Event Hub Reader: Partition " + context.getPartitionId() + " onError: " + error.toString());
    }

    // onEvents is called when events are received on this partition of the Event Hub. 
    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> events) throws Exception
    {
        System.out.println("Event Hub Reader: Partition " + context.getPartitionId() + " got event batch");
        int eventCount = 0;
        for (EventData data : events)
        {
            try
            {
                System.out.println("Event Hub Reader (" + context.getPartitionId() + "," + data.getSystemProperties().getOffset() + "," +
                        data.getSystemProperties().getSequenceNumber() + "): " + new String(data.getBytes(), "UTF8"));
                eventCount++;

                
                
                // Checkpointing persists the current position in the event stream for this partition and means that the next
                // time any host opens an event processor on this event hub+consumer group+partition combination, it will start
                // receiving at the event after this one. 
                this.checkpointBatchingCount++;
                if ((checkpointBatchingCount % 5) == 0)
                {
                    
                    // Checkpoints are created asynchronously. It is important to wait for the result of checkpointing
                    // before exiting onEvents or before creating the next checkpoint, to detect errors and to ensure proper ordering.
                	context.checkpoint(data).get();
                   
                }
            }
            catch (Exception e)
            {
                System.out.println("Processing failed for an event: " + e.toString());
            }
        }
        System.out.println("Event Hub Reader: Partition " + context.getPartitionId() + " batch size was " + eventCount + " for host " + context.getOwner());
    }
}
