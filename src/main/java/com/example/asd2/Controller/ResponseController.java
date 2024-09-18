package com.example.asd2.Controller;

import com.example.asd2.Model.Response;
import com.example.asd2.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("minh/responses")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response> createResponse(@RequestBody Map<String, String> payload){
        return new ResponseEntity<Response>(responseService.createResponse(payload.get("responseBody"), payload.get("ticket_id")), HttpStatus.CREATED);
    }
}
