package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

import static sun.jvm.hotspot.runtime.PerfMemory.start;

public class MulticastListener implements Runnable {

    private String groupAddress;
    private int port;
    private boolean isRunning = true;
    private NamingServerService namingServerService;

    //Multithread stuff
    private Thread runningThread = null;
    private int connectionAmount = 0;
    public int runningThreads = 0;

    private MulticastSocket s = null;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    public MulticastListener(String groupAddress, int port, NamingServerService namingServerService) {
        this.groupAddress = groupAddress;
        this.port = port;
        this.namingServerService = namingServerService;
    }

    public void stop() {
        this.isRunning = false;
        this.s.close();
        logger.info("Multicast listener stopped!");
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


            byte[] buf = new byte[32768];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);

            while (this.isRunning) {
                
                s.receive(recv);

                Thread t = new Thread(() -> namingServerService.addNode(recv.getData().toString()));
                t.start();

            }
        } catch (Exception e) {
            isRunning = false;
            logger.info(e.toString());
        }

        this.stop();
    }


}
