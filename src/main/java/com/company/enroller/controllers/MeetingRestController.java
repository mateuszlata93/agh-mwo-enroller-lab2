package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
        if (meetingsService.findById(meeting.getId()) != null) {
            return new ResponseEntity<String>(
                    "Unable to create. A participant with login " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingsService.add(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }
}
