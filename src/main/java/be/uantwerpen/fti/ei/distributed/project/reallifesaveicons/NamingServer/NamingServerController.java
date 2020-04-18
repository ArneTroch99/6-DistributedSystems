package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.StringResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class NamingServerController {

    private NamingServerService namingServerService;


    @RequestMapping(value = "/addNode", method = RequestMethod.PUT)
    public StringResponse addNode(@RequestParam(value = "name", required = false, defaultValue = "") String fileName,
            HttpServletRequest request){
        this.namingServerService.addNode(request.getRemoteAddr());
        return null;
    }

    @RequestMapping(value = "/deleteNode", method = RequestMethod.DELETE)
    public StringResponse deleteNode(@RequestParam(value = "name") String ipAddress){
        // verwijderen node
        return null;
    }

    @RequestMapping(value = "/requestFileLocation", method = RequestMethod.GET)
    public String requestFileLocation(@RequestParam(value = "filename") String fileName){
        // fileLocation
        return null;
    }

}
