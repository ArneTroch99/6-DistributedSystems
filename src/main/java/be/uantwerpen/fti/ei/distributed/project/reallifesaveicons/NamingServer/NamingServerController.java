package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@RestController
public class NamingServerController {

    private NamingServerService namingServerService;

    @PostConstruct
    public void initialize(){
        namingServerService = new NamingServerServiceImpl();
        this.namingServerService.init("map.json");
    }


    @RequestMapping(value = "/addNode", method = RequestMethod.PUT)
    public ResponseEntity addNode(@RequestParam(value = "name", required = false, defaultValue = "") String ipAddress,
                                  HttpServletRequest request){
        boolean worked;
        if (ipAddress.equals("")){
            worked = this.namingServerService.addNode(request.getRemoteAddr());
        } else {
            worked = this.namingServerService.addNode(ipAddress);
        }
        if (worked){
            return new ResponseEntity(HttpStatus.OK);
        } else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A node with that IP already exists!");
        }
    }

    @RequestMapping(value = "/deleteNode", method = RequestMethod.DELETE)
    public ResponseEntity deleteNode(@RequestParam(value = "name", required = false, defaultValue = "") String ipAddress,
                                     HttpServletRequest request){
        boolean worked;
        if (ipAddress.equals("")){
            worked = this.namingServerService.deleteNode(request.getRemoteAddr());
        } else {
            worked = this.namingServerService.deleteNode(ipAddress);
        }
        if (worked){
            return new ResponseEntity(HttpStatus.OK);
        } else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A node with that IP does not exist!");
        }
    }

    @RequestMapping(value = "/fileLocation", method = RequestMethod.GET)
    public String requestFileLocation(@RequestParam(value = "filename") String fileName){
        return this.namingServerService.getFileLocation(fileName);
    }

}
