package com.javacore.task.mappers;

import com.javacore.task.entities.User;
import com.javacore.task.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    UserModel userToUserModel(User user);
    User userModelToUser(UserModel userModel);
}