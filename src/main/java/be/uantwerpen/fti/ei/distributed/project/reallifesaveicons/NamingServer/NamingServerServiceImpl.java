package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Component
public class NamingServerServiceImpl implements NamingServerService {

    private final HTTPClient httpClient;

    static private Map<Integer, String> map = new HashMap<>();
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    @Autowired
    public NamingServerServiceImpl(HTTPClient httpClient) {

        /*//DEBUG!!!!!
        map.put(10, "192.168.0.1");
        map.put(20, "192.168.0.2");
        map.put(30, "192.168.0.3");
        // END DEBUG*/

        this.httpClient = httpClient;
    }


    @Override
    public String getNode(String id) {
        Integer idInt = Integer.parseInt(id);
        return map.get(idInt);

    }

    @Override
    public ArrayList<String> leave(String nodeID, String lowerID, String upperID) {

        Set<Integer> keys = map.keySet();
        Integer[] ids = {Integer.parseInt(nodeID), Integer.parseInt(lowerID), Integer.parseInt(upperID)};

        //The check if all the nodes exist
        for (int i = 0; i < 3; i++) {
            if (!keys.contains(ids[i])) {
                logger.debug("At least one node is not in the map!");
                return null;
            }
        }
        ArrayList<String> neighbours = new ArrayList<>(Arrays.asList(map.get(ids[1]),map.get(ids[2])));

        logger.info("Deleting node from network: " + map.get(ids[0]));
        map.remove(ids[0]);

        return neighbours;
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
    @Override
    public boolean addNode(String ip) {

        logger.info("AddNode method entered");
        //Drop everything before "@" to avoid using unwanted data
        ip = ip.trim();
        ip = ip.substring(ip.indexOf("@") + 1);

        Set<Integer> keys;

        synchronized (this) {
            keys = map.keySet();
        }

        int id = hash(ip);
        if (keys.contains(id)) {
            logger.info("Node" + ip + " already exists!");
            return false;
        } else {
            logger.info("Adding new node to network: " + hash(ip) + " : " + ip);

            try {
                httpClient.putHTTP(ip, "/bootstrap?namingip=" + InetAddress.getLocalHost().getHostAddress());
                synchronized (this) {
                    map.put(hash(ip), ip);
                }
            } catch (UnknownHostException e) {
                logger.info(e.toString());
            } catch (Exception e) {
                logger.debug(e.toString());
            }
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
