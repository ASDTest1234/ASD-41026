package com.example.asd2.Controller;

import com.example.asd2.Model.Response;
import com.example.asd2.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("minh/response")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response> createResponse(@RequestBody Map<String, String> payload){
        return new ResponseEntity<Response>(responseService.createResponse(payload.get("response_id"), payload.get("responseBody"), payload.get("ticket_id")), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getResponse(@PathVariable String id) {
        try {
            Response response = responseService.getResponseById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/allresponses")
    public ResponseEntity<List<Response>> getAllResponses(){
        return new ResponseEntity<List<Response>>(responseService.allResponses(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateResponse(@PathVariable String id, @RequestBody Map<String, String> payload) {
        String newResponseBody = payload.get("response");
        Response updatedResponse = responseService.updateResponse(id, newResponseBody);
        return ResponseEntity.ok(updatedResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable String id) {
        try {
            responseService.deleteResponse(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
