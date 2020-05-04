package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastListener implements Runnable {

    private String groupAddress;
    private int port;
    private boolean isRunning = true;
    private NamingServerService namingServer;

    //Multithread stuff
    private Thread runningThread = null;
    private int connectionAmount = 0;
    public int runningThreads = 0;

    private MulticastSocket s = null;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    public MulticastListener(String groupAddress, int port, NamingServerService namingServer) {
        this.groupAddress = groupAddress;
        this.port = port;
        this.namingServer = namingServer;
    }

    private synchronized boolean isRunning() {
        return this.isRunning;
    }

    public synchronized void stop() {
        this.isRunning = false;
        this.s.close();
    }

    private void openSocket() {
        try {
            this.s = new MulticastSocket(this.port);
        } catch (IOException e) {
            logger.info(s.toString());
        }
    }

    @Override
    public void run() {

        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        logger.info("Multicast listener running");

        try {
            openSocket();
            InetAddress group = InetAddress.getByName(groupAddress);
            s.joinGroup(group);
            logger.info("Socket is opened");

            while (this.isRunning()) {
                //Waits for new connection

                while (!s.isConnected()) ;

                logger.info("New connection, total: " + ++connectionAmount);
                runningThreads++;

                new Thread(
                        new runnableMulticastListener(
                                s, namingServer)
                ).start();
            }

            this.stop();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
