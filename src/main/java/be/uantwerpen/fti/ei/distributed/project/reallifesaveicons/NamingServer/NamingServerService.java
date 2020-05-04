package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface NamingServerService {

    String getNode(String id);

    boolean addNode(String ip);

    ArrayList<String> leave(String nodeID, String lowerID, String upperID);

    String getFileLocation(String fileName);
}
