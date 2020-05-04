package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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

        ResponseEntity r = (node != null) ? new ResponseEntity(node, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
        return r;

    }

    @RequestMapping(value = "/fileLocation", method = RequestMethod.GET)
    public String requestFileLocation(@RequestParam(value = "filename") String fileName) {
        return this.namingServerService.getFileLocation(fileName);
    }

    @RequestMapping (value = "/leave", method = RequestMethod.POST)
    public ResponseEntity<String> nodeLeave(@RequestParam(value = "id") String nodeId, @RequestParam(value = "lower") String lowerID,
                                                    @RequestParam(value = "upper") String upperID){

        ArrayList<String> neighbours = namingServerService.leave(nodeId, lowerID, upperID);

        ResponseEntity r = (neighbours != null) ? new ResponseEntity(neighbours, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
        return r;
    }


    /*@RequestMapping(value = "/deleteNode", method = RequestMethod.DELETE)
    public ResponseEntity deleteNode(@RequestParam(value = "ip", required = false, defaultValue = "") String ipAddress,
                                     HttpServletRequest request) {
        boolean worked;
        if (ipAddress.equals("")) {
            worked = this.namingServerService.deleteNode(request.getRemoteAddr());
        } else {
            worked = this.namingServerService.deleteNode(ipAddress);
        }
        if (worked) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A node with that IP does not exist!");
        }
    }*/



    /*@RequestMapping(value = "/shutDown", method = RequestMethod.DELETE)
    public void shutDown(@RequestParam value =  "ip", required =)*/

}
