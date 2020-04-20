package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NamingServerServiceImpl implements NamingServerService {

    private File file;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, String> map;

    @Override
    public void init(String mapFile) {
        try {
            file = new File(mapFile);
            if (file.createNewFile()) {
                map = new HashMap<>();
            } else {
                map = mapper.readValue(file, new TypeReference<Map<Integer, String>>(){});
                System.out.println(map);
            }
        } catch (IOException e) {
            map = new HashMap<>();
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            mapper.writeValue(file, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addNode(String name) {
        Set<Integer> keys = map.keySet();
        int id = hash(name);
        if (keys.contains(id)){
            System.out.println("Node already exists!");
            return false;
        } else {
            System.out.println("Adding new node to network: " + hash(name) + " : " + name);
            map.put(hash(name), name);
            save();
            return true;
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
        while (id >= 0){
            if (keys.contains(id)){
                System.out.println("Resulting ID: " + id + " with ip: " + map.get(id));
                return map.get(id);
            }
            id--;
        }
        id = Collections.max(keys);
        System.out.println("No lowerid! Resulting ID: " + id + " with ip: " + map.get(id));
        return map.get(id);
    }

    private int hash(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            int temp = 32768;
            return no.mod(BigInteger.valueOf(temp)).intValue();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
