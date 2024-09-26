package com.example.asd2.Controller;
import com.example.asd2.Model.Users;
import com.example.asd2.Service.UserService;
import com.example.asd2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/testing")
public class UserController {

    @Autowired
    private UserService userService; // Use userService for business logic

    @Autowired
    private UserRepository userRepository; // Direct use of userRepository for /all

//    @GetMapping("/email/{email}")
//    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
//        Optional<Users> user = userService.getUserByEmail(email);
//        return user.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }


//    @GetMapping("/all")
//    public List<Users> getAllUsers() {
//        List<Users> users = userRepository.findAll();
//        System.out.println("Retrieved users: " + users);
//        return users;
//    }
//    @GetMapping("/all")
//    public List<Users> getAllUsers() {
//        return userService.getAllUsers();
//    }

//
//    @GetMapping("/all")
//    public ResponseEntity<List<Users>> getAllUsers(){
//        return new ResponseEntity<List<Users>>(userService.getAllUsers(), HttpStatus.OK);
//    }


    @GetMapping("/test")
    public ResponseEntity<List<Users>> testDatabaseRead() {
        List<Users> users = userService.getAllUsers();  // Fetch all users from the database
        return new ResponseEntity<>(users, HttpStatus.OK);  // Return the users in the response
    }

//    @GetMapping("/ByEmail/{Email}")
//    public ResponseEntity<Optional<Users>> searchAEmail(@PathVariable String Email){
//        return new ResponseEntity<Optional<Users>>(userService.aEmail(Email), HttpStatus.OK);
//    }

    @GetMapping("/{userID}")
    public ResponseEntity<Optional<Users>> searchID(@PathVariable String userID){
//        return new ResponseEntity<Optional<Users>>(Optional.ofNullable(userService.getUserByID(ID)), HttpStatus.OK);
        return new ResponseEntity<Optional<Users>>(userService.getUserByID(userID), HttpStatus.OK);
    }


}
