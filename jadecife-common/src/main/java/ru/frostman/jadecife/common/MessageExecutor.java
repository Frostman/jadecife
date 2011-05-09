package ru.frostman.jadecife.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.model.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author slukjanov aka Frostman
 */
public class MessageExecutor {
    private static final Logger log = LoggerFactory.getLogger(MessageExecutor.class);

    private final MessageHandler handler;
    private final BlockingQueue<Message> messages;

    public MessageExecutor(MessageHandler handler, int queueCapacity) {
        this.handler = handler;
        messages = new LinkedBlockingQueue<Message>(queueCapacity);
    }

    public boolean add(Message message) {
        boolean result = true;
        if (message != null) {
            try {
                messages.put(message);
            } catch (InterruptedException e) {
                result = false;
                log.debug("MessageExecutor.add interrupted, msg: " + message, e);
            }
        }

        return result;
    }

    private Message take() {
        Message message = null;
        while (message == null) {
            try {
                message = messages.take();
            } catch (InterruptedException e) {
                log.debug("MessageExecutor.take interrupted", e);
            }
        }

        return message;
    }


    private final class Worker implements Runnable {
        private final Thread thread;
        private volatile boolean work = true;

        public Worker() {
            thread = new Thread(this);

            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }

            thread.start();
        }

        @Override
        public void run() {
            Message message = null;
            while (work) {
                message = take();

                try {
                    handler.handleMessage(message);
                } catch (Exception e) {
                    log.warn("Exception while handling message", e);
                }
            }

        }
    }

    public interface MessageHandler {

        void handleMessage(Message message);

    }
}
