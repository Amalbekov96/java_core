package com.javacore.task.models.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineesListResponse {
    private String userName;
    private String firstName;
    private String lastName;
}
