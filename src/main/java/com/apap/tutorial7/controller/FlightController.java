package com.apap.tutorial7.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.rest.Setting;
import com.apap.tutorial7.service.FlightService;
import com.apap.tutorial7.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * FlightController
 */
@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restFlight(){
        return new RestTemplate();
    }

    @PostMapping(value = "/add")
    public FlightModel addFlightSubmit(@RequestBody FlightModel flight) {
        return flightService.addFlight(flight);
    }

    @GetMapping(value = "/view/{flightNumber}")
    public FlightModel flightView(@PathVariable("flightNumber") String flightNumber) {
        FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        return flight;
    }

    @GetMapping(value="/all")
    public List<FlightModel> viewAllFlight() {
        return flightService.getAllFlight();
    }

    @DeleteMapping(value = "/delete/{flightId}")
    public String deleteFlight(@PathVariable("flightId") long flightId) {
        FlightModel flight = flightService.getFlighttDetailById(flightId).get();
        flightService.delete(flight);
        return "flight has been deleted";
    }

    @PutMapping(value = "/update/{flightId}")
    public String updateFlight(@PathVariable("flightId") long flightId, 
            @RequestParam(value = "destination") String destination,
            @RequestParam(value = "origin") String origin,
            @RequestParam(value = "time") Date time) {
        FlightModel flight = flightService.getFlighttDetailById(flightId).get();
        if (flight.equals(null)) {
            return "couldn't find the flight";
        }
        flight.setDestination(destination);
        flight.setOrigin(origin);
        flight.setTime(time);
        flightService.updateFlight(flight);
        return "flight update success";

    }

    @GetMapping(value= "/view")
    public String getAirportDetail(@RequestParam(value = "term") String term) throws Exception {
        String path = Setting.flightUrl + "&term=" + term;
        return restTemplate.getForEntity(path, String.class).getBody();
    }
}