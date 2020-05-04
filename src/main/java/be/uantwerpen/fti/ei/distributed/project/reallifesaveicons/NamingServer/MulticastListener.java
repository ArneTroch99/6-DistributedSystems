package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class MulticastListener implements Runnable {

    private final String groupAddress;
    private final int port;
    private boolean isRunning = true;
    private final NamingServerService namingServerService;
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

        logger.info("Multicast listener running");

        try {
            openSocket();
            InetAddress group = InetAddress.getByName(groupAddress);
            s.joinGroup(group);
            logger.info("Socket is opened");


            byte[] buf = new byte[32768];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);

            while (this.isRunning) {

                logger.info("Loop entered");
                s.receive(recv);
                String input = new String(recv.getData());
                logger.info("Received: "+input);
                Thread t = new Thread(() -> namingServerService.addNode(input));
                t.start();

            }
        } catch (Exception e) {
            isRunning = false;
            logger.info(e.toString());
        }

        this.stop();
    }


}
