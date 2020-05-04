package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class runnableMulticastListener implements Runnable {

    DatagramSocket s = null;
    int connectionAmount;
    NamingServerService namingServer;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    public runnableMulticastListener(DatagramSocket socket, NamingServerService namingServer) {
        this.s = socket;
        this.connectionAmount = connectionAmount;
        this.namingServer = namingServer;
    }

    @Override
    public void run() {

        byte[] buf = new byte[32768];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        try {
            s.receive(recv);
            s.disconnect();
            logger.info("Packet received");

            String input = new String(recv.getData());
            namingServer.addNode(input);

        } catch (IOException e) {
            logger.info(e.toString());
        }

    }


}
