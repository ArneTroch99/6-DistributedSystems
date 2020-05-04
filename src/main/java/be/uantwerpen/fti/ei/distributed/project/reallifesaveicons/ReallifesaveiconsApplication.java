package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons;

import be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ReallifesaveiconsApplication {

    NamingServerService namingServerService;

    public static void main(String[] args) {

        SpringApplication.run(ReallifesaveiconsApplication.class, args);

    }

}
