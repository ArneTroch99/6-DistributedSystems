package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class NamingServerImpl implements NamingServerService {

    private File mapFile;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        mapFile = new File("mapFile.xml");
        if (!mapFile.exists()){
            mapFile.mkdir();
        }
    }

    @Override
    public void addNode(String name) {
        System.out.println(mapFile.hashCode());

    }

    @Override
    public void deleteNode(String name) {

    }

    @Override
    public String getFileLocation(String fileName) {
        return null;
    }
}
