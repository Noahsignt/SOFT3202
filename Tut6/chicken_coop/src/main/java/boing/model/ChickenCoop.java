package boing.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// If you want to practice your SOFT2201 skills,
// have a go at fixing this to be designed in a better way
// (is a particular SOLID principle kinda broken here?)
// May also be worth looking into dependency injection in Spring...
public class ChickenCoop {
    private static Map<String, ChickenCoop> instances = new HashMap<>();

    private List<Chicken> chooks;
    private String coopName;
    private LocalDateTime created;

    private ChickenCoop(String name) {
        this.chooks = new ArrayList<>();
        this.coopName = name;
        this.created = LocalDateTime.now();
    }

    public static ChickenCoop getCoop(String coop) {
        if (!instances.containsKey(coop)) {
            instances.put(coop, new ChickenCoop(coop));
        }

        return instances.get(coop);
    }

    // Getters
    public List<Chicken> getChooks() {
        return new ArrayList<>(this.chooks);
    }

    public String getCoopName() {
        return coopName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    // Util
    public boolean addChook(Chicken c) {
        return c != null ? this.chooks.add(c) : false;
    }

    public boolean removeChook(Chicken c) {
        return c != null ? this.chooks.remove(c) : false;
    }
}
