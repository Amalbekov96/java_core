package com.javacore.task.models.request;

import lombok.Builder;


@Builder
public record SignInRequest (
     String username,
     String password
){

}