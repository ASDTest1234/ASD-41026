package com.example.asd2.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;
    private String ticketId;
    private String customerId;
    private String issue;
    private String description;
    private String date;


}
