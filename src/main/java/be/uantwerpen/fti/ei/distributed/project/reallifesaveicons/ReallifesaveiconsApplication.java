package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons;

import be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer.NamingServer;
import be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer.MulticastListener;
import be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer.NamingServerServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class ReallifesaveiconsApplication {

    public static void main(String[] args) {

        new Thread(new MulticastListener("228.5.6.7", 6789, new NamingServerServiceImpl())).start();
        SpringApplication.run(ReallifesaveiconsApplication.class, args);

    }

}
