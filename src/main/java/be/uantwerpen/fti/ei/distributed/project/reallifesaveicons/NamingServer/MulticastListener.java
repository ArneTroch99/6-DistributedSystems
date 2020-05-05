package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;

@Component
public class MulticastListener {

    private String groupAddress;
    private int port;
    private boolean isRunning = true;
    private final NamingServerService namingServerService;
    private MulticastSocket s = null;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    @Autowired
    public MulticastListener(NamingServerService namingServerService) {
        this.namingServerService = namingServerService;
    }

    @PostConstruct
    public void init() {
        port = 6789;
        groupAddress = "228.5.6.7";
        new Thread(this::run).start();
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


    public void run() {

        logger.info("Multicast listener running");

        try {
            openSocket();
            InetAddress group = InetAddress.getByName(groupAddress);
            s.joinGroup(group);
            logger.info("Socket is opened");

            while (this.isRunning) {

                logger.debug("Loop entered");

                byte[] buf = new byte[32768];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);

                s.receive(recv);
                String input = new String(recv.getData());
                logger.debug("Received: " + input);
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
