package pro.kaspiotr.elevatorcontrolsystem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    private static final String APP_TITLE = "Elevator Control System";

    @RequestMapping("/")
    public String index() {
        return APP_TITLE;
    }

}
