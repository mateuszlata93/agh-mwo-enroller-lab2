package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingsService;

    @GetMapping("")
    public ResponseEntity<?> getMeetings(){
        Collection<Meeting> meetings = meetingsService.getAll();
        return  new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id){
        Meeting meeting = meetingsService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }
}
