package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NamingServerServiceImpl implements NamingServerService{

    private File file;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, String> map;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    @Override
    public void init(String mapFile) {
        map = new HashMap<>();
    }

    private void save() {
        try {
            mapper.writeValue(file, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteNode(String name) {
        Set<Integer> keys = map.keySet();
        int id = hash(name);
        if (!keys.contains(id)) {
            System.out.println("Node is not in map!");
            return false;
        } else {
            System.out.println("Deleting node from network: " + hash(name) + " : " + name);
            map.remove(hash(name));
            save();
            return true;
        }
    }

    @Override
    public String getFileLocation(String fileName) {
        int id = hash(fileName);
        System.out.println("File hash: " + id);
        Set<Integer> keys = map.keySet();
        while (id >= 0) {
            if (keys.contains(id)) {
                logger.debug("Resulting ID: " + id + " with ip: " + map.get(id));
                return map.get(id);
            }
            id--;
        }
        id = Collections.max(keys);
        logger.debug("No lowerid! Resulting ID: " + id + " with ip: " + map.get(id));
        return map.get(id);
    }

    //This method is called by the multicastListener, not the REST controller!
    public boolean addNode(String ip) {

        logger.info("AddNode method entered");
        //Drop everything before "@" to avoid using unwanted data
        ip = ip.trim();
        ip = ip.substring(ip.indexOf("@") + 1);

        Set<Integer> keys = map.keySet();

        int id = hash(ip);
        if (keys.contains(id)) {
            logger.info("Node already exists!");
            return false;
        } else {
            logger.info("Adding new node to network: " + hash(ip) + " : " + ip);
            synchronized (this){
                map.put(hash(ip), ip);
            }
            save();
            return true;
        }
    }


    private int hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            int temp = 32768;
            return no.mod(BigInteger.valueOf(temp)).intValue();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
