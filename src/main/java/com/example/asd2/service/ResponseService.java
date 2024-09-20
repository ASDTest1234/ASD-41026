package com.example.asd2.service;

import com.example.asd2.Model.Response;
import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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
                .matching(Criteria.where("ticket_id").is(ticket_id))
                .apply(new Update().push("responseIds").value(response))
                .first();

                return response;
    }
}
