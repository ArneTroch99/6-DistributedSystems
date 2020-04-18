package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.stereotype.Service;

@Service
public interface NamingServerService {

    void init(String mapFile);

    void addNode(String name);

    void deleteNode(String name);

    String getFileLocation(String fileName);
}
