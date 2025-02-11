package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
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

    @Autowired
    ParticipantService participantService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Meeting meeting = meetingsService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingsService.delete(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingsService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        updatedMeeting.setId(id);
        updatedMeeting.setDescription(updatedMeeting.getDescription());
        meetingsService.update(updatedMeeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @PutMapping("/{id}/{login}")
    public ResponseEntity<?> addMeetingsParticipants(@PathVariable("id") long id, @PathVariable("login") String participantLogin, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingsService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        updatedMeeting.setId(id);
        Participant participant = participantService.findByLogin(participantLogin);
        updatedMeeting.addParticipant(participant);
        System.out.println(updatedMeeting.getParticipants());
        meetingsService.update(updatedMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/{login}")
    public ResponseEntity<?> deleteMeetingsParticipants(@PathVariable("id") long id, @PathVariable("login") String participantLogin, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingsService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        updatedMeeting.setId(id);
        Participant participant = participantService.findByLogin(participantLogin);
        updatedMeeting.removeParticipant(participant);
        System.out.println(updatedMeeting.getParticipants());
        meetingsService.update(updatedMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
