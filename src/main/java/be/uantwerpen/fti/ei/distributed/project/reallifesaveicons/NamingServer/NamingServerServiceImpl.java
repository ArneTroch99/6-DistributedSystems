package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            file = new File(mapFile + ".json");
            if (file.createNewFile()) {
                map = new HashMap<>();
            } else {
                System.out.println("reading map");
                map = new HashMap<>();
                map = mapper.readValue(mapFile, new TypeReference<Map<Integer, String>>() {
                });
                System.out.println(map.keySet());
            }
        } catch (IOException e) {
            map = new HashMap<>();
        }
    }

    private void save() {
        try {
            String json = mapper.writeValueAsString(map);
            mapper.writeValue(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNode(String name) {
        map.put(hash(name), name);
        System.out.println(map.get(hash(name)));
        save();
    }

    @Override
    public void deleteNode(String name) {
        map.remove(hash(name));
        save();
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
        id = map.size()-1;
        System.out.println("No lowerid! Resulting ID: " + id + " with ip: " + map.get(id));
        return map.get(id);
    }

    private int hash(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            byte[] byteString = hashtext.getBytes();
            return (byteString[0] << 7) + ((byteString[1]&0b11111110) >> 1);
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
