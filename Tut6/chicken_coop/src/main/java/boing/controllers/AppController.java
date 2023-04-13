package boing.controllers;

import boing.model.Chicken;
import boing.model.ChickenCoop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {

    // "/" is the root page; if we don't override this, its going to
    // look into resources for a file called "index.html", and serve up that.
    @GetMapping("/") //get mapping means this is the info the user is requesting when at this URL
    public String chookHomePage(
            // This one allows us to grab a value from the query string
            @RequestParam(value = "coop", defaultValue = "ChookTown") String coopName,
            // This will allow us to pass stuff to the template
            Model model //acts as interface for data
    ) {
        ChickenCoop coop = ChickenCoop.getCoop(coopName);

        // Just to prepopulate some data
        /*
        This is us adding data to our model - the data structure underpinning the entire system
         */
        if (coop.getChooks().size() == 0) {
            coop.addChook(new Chicken("walter", 5, "seeds", "img/cat.jpg"));
            coop.addChook(new Chicken("george", 3, "spinach", "img/bro.jpg"));
            coop.addChook(new Chicken("susan", 1, "dirt", "img/stare.jpg"));
            coop.addChook(new Chicken("alex", 7, "tofu", "img/nap.jpg"));
        }

        // With these, from the template we can access attributes
        // "name", "date" and "chooks"
        /*
        This is us adding data to the Spring model - I think attributes are more easily exposed to the user.
         */
        model.addAttribute("name", coopName);
        model.addAttribute("date", coop.getCreated().toString());
        model.addAttribute("chooks", coop.getChooks());

        return "chookhome"; // Name of the file we want to serve (html!)
    }

    @PostMapping("/newchook")
    public ResponseEntity<String> addChook(
            @RequestParam(value = "coop", defaultValue = "ChookTown") String coopName,
            // @RequestParams here are being used for both query strings and form data,
            // for different solution can also have a look at
            // https://spring.io/guides/gs/handling-form-submission/
            @RequestParam(value = "name") String name,
            @RequestParam(value = "powerlevel") int powerLevel,
            @RequestParam(value = "favfood") String fav,
            @RequestParam(value = "imgpath") String imgPath
    ) {
        ChickenCoop coop = ChickenCoop.getCoop(coopName);
        coop.addChook(new Chicken(
                name,
                powerLevel,
                fav,
                imgPath
        ));

        return ResponseEntity.status(HttpStatus.OK).body("Success! Go back to the page");
    }
}
