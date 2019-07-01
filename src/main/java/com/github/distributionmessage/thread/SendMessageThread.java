package com.github.distributionmessage.thread;

import com.ibm.mq.jms.MQQueue;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

@Data
public class SendMessageThread implements Runnable {

    private static final Log logger = LogFactory.getLog(SendMessageThread.class);

    private static ExecutorService executorService;

    private Object message;

    private MQQueue queue;

    private JmsTemplate jmsTemplate;

    private MessagePostProcessor messagePostProcessor;

    public SendMessageThread(JmsTemplate jmsTemplate, Object message, MQQueue queue, MessagePostProcessor messagePostProcessor) {
        this.jmsTemplate = jmsTemplate;
        this.message = message;
        this.queue = queue;
        this.messagePostProcessor = messagePostProcessor;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        this.jmsTemplate.convertAndSend(this.queue, this.message, this.messagePostProcessor);
        logger.info("workQueue size[" + SendMessageThread.getWorkQueueSize()
                +"] send message to queue[" + this.queue.getBaseQueueName() + "] use["
                + ((double)(System.nanoTime() - startTime) / 1000000.0) + "]ms");
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void setExecutorService(ExecutorService executorService) {
        SendMessageThread.executorService = executorService;
    }

    public static int getWorkQueueSize() {
        int result = 0;
        if (executorService != null) {
            if (executorService instanceof ThreadPoolExecutor) {
                BlockingQueue<Runnable> workQueue = ((ThreadPoolExecutor)executorService).getQueue();
                if (workQueue instanceof LinkedBlockingDeque) {
                    result = ((LinkedBlockingDeque)workQueue).size();
                }
            }
        }
        return result;
    }
}
