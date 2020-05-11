package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class NamingServerController {

    private NamingServerService namingServerService;

    @Autowired
    public void initialize(NamingServerService namingServerService) {
        this.namingServerService = namingServerService;
    }

    //Get methods
    @RequestMapping (value = "/nodeip", method = RequestMethod.GET)
    public ResponseEntity<String> nodeIP(@RequestParam(value = "id") String id){

        String node = namingServerService.getNode(id);

        ResponseEntity r = (node != null) ? new ResponseEntity(node, HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
        return r;
    }

    @RequestMapping(value = "/fileLocation", method = RequestMethod.GET)
    public ResponseEntity<String> requestFileLocation(@RequestParam(value = "filename") String fileName) {

        String location = this.namingServerService.getFileLocation(fileName);
        ResponseEntity r = (location != null) ? new ResponseEntity(location, HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
        return r;
    }

    @RequestMapping (value = "/leave", method = RequestMethod.GET)
    public ResponseEntity<String> nodeLeave(@RequestParam(value = "id") String nodeId, @RequestParam(value = "lower") String lowerID,
                                                    @RequestParam(value = "upper") String upperID){

        ArrayList<String> neighbours = namingServerService.leave(nodeId, lowerID, upperID);

        ResponseEntity r = (neighbours != null) ? new ResponseEntity(neighbours, HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
        return r;
    }

    //Network management (admin methods)
    @RequestMapping(value = "allnodes", method = RequestMethod.GET)
    public ResponseEntity<String> allNodes(){

        Map<Integer,String> nodes = namingServerService.getAll();

        ResponseEntity r = (!nodes.isEmpty()) ? new ResponseEntity(nodes, HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
        return r;
    }

}
