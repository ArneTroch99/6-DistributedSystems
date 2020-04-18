package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

@RestController
public class NamingServerController {

    private NamingServerService namingServerService;

    @PostConstruct
    public void initialize(){
        namingServerService = new NamingServerServiceImpl();
        this.namingServerService.init("map");
    }


    @RequestMapping(value = "/addNode", method = RequestMethod.PUT)
    public void addNode(@RequestParam(value = "name", required = false, defaultValue = "") String ipAddress,
            HttpServletRequest request){
        if (ipAddress.equals("")){
            this.namingServerService.addNode(request.getRemoteAddr());
        } else {
            this.namingServerService.addNode(ipAddress);
        }
    }

    @RequestMapping(value = "/deleteNode", method = RequestMethod.DELETE)
    public void deleteNode(@RequestParam(value = "name", required = false, defaultValue = "") String ipAddress,
                                     HttpServletRequest request){
        if (ipAddress.equals("")){
            this.namingServerService.deleteNode(request.getRemoteAddr());
        } else {
            this.namingServerService.deleteNode(ipAddress);
        }
    }

    @RequestMapping(value = "/fileLocation", method = RequestMethod.GET)
    public String requestFileLocation(@RequestParam(value = "filename") String fileName){
        return this.namingServerService.getFileLocation(fileName);
    }

}
