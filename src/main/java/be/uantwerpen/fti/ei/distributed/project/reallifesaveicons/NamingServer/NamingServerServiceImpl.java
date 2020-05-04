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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class NamingServerServiceImpl implements NamingServerService {

    private final HTTPClient httpClient;

    static private Map<Integer, String> map = new HashMap<>();
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);

    @Autowired
    public NamingServerServiceImpl(HTTPClient httpClient) {
        this.httpClient = httpClient;
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
            logger.info("Node already exists!");
            return false;
        } else {
            logger.info("Adding new node to network: " + hash(ip) + " : " + ip);
            synchronized (this) {
                map.put(hash(ip), ip);
                try {
                    httpClient.putHTTP(ip, "/bootstrap?" + InetAddress.getLocalHost());
                } catch (UnknownHostException e) {
                    logger.info(e.toString());
                }
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
