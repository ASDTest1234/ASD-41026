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
import java.util.stream.Collectors;

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
    public Response getResponseById(String response_id) {
        return responseRepository.findByResponseId(response_id).orElseThrow(() ->
                new RuntimeException("Response not found with response_id: " + response_id));
    }

    public List<Response> allResponses(){
        return  responseRepository.findAll();
    }


    public List<Response> getResponsesByIds(List<String> responseIds) {
        List<ObjectId> objectIds = responseIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        return responseRepository.findAllById(objectIds);
    }


    public Response updateResponse(String response_id, String newResponseBody) {
        Response existingResponse = responseRepository.findByResponseId(response_id).orElseThrow(() ->
                new RuntimeException("Response not found with response_id: " + response_id));

        existingResponse.setResponse(newResponseBody);
        return responseRepository.save(existingResponse);
    }
    public void deleteResponse(String response_id) {
        Response existingResponse = responseRepository.findByResponseId(response_id).orElseThrow(() ->
                new RuntimeException("Response not found with response_id: " + response_id));

        responseRepository.delete(existingResponse); // Use the default delete
    }
}
