package app.display;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildingDisplay {

    @RequestMapping("/")
    public String index() {
        return "Congratulations from BlogController.java";
    }

}
