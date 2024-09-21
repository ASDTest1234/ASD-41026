package com.example.asd2.service;

import com.example.asd2.Model.Response;
import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.ResponseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.SelectionOperators.First.first;

@Service
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public Response createResponse(String response_id, String responseBody, String ticket_id){
        Response response = responseRepository.insert(new Response(response_id, responseBody));


        mongoTemplate.update(Ticket.class)
                .matching(Criteria.where("ticketId").is(ticket_id))
                .apply(new Update().push("responseIds").value(response))
                .first();

                return response;
    }
    public Response getResponseById(String Id) {
        return responseRepository.findById(new ObjectId(Id)).orElseThrow(() ->
                new RuntimeException("Response not found with id: " + Id));
    }

    public List<Response> allResponses(){
        return  responseRepository.findAll();
    }

    public Response updateResponse(String Id, String newResponseBody) {
        // Find the existing response by OBJECTID (_id found in database)
        Response existingResponse = responseRepository.findById(new ObjectId(Id)).orElseThrow(() ->
                new RuntimeException("Response not found with id: " + Id));

        existingResponse.setResponse(newResponseBody);

        return responseRepository.save(existingResponse);
    }
    public void deleteResponse(String Id) {
        // Delete response using OBJECTID (_id found in database)
            if (!responseRepository.existsById(new ObjectId(Id))) {
                throw new RuntimeException("Response not found with id: " + Id);
            }
            responseRepository.deleteById(new ObjectId(Id));
    }
}