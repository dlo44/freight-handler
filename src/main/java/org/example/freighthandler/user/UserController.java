package org.example.freighthandler.user;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<UserDTO> getUserByUserId(@PathVariable Long userId) {
        try {
            User user = userService.findByUserIdNumber(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Map Entity -> DTO
            UserDTO dto = modelMapper.map(user, UserDTO.class);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO userRequest) {
        try {
            User user = new User();
            user.setUserId(userRequest.getUserId());
            user.setName(userRequest.getName());
            User savedUser = userService.saveUser(user);
            // Map Entity -> DTO
            UserDTO dto = modelMapper.map(savedUser, UserDTO.class);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}
