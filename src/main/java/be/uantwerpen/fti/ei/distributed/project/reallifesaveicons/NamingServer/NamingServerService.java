package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.stereotype.Service;

@Service
public interface NamingServerService {


    boolean addNode(String ip);

    boolean deleteNode(String name);

    String getFileLocation(String fileName);
}
