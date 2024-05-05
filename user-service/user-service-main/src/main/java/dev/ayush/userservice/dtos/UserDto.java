package dev.ayush.userservice.dtos;

import dev.ayush.userservice.models.Role;
import dev.ayush.userservice.models.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
