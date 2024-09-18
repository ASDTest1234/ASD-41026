package com.example.asd2.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "responses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @Id
    private ObjectId id;
    private String response;

    public Response(String response) {
        this.response = response;
    }
}
