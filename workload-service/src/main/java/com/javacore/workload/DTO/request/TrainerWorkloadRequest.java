package com.javacore.workload.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerWorkloadRequest implements Serializable {
   @Serial
   private static final long serialVersionUID = 6407896819840030936L;
    private String username;
   private String firstName;
   private String lastName;
   private LocalDate trainingDate;
   private Number trainingDuration;
   private String actionType;
   private boolean isActive;

   public TrainerWorkloadRequest(String username, String firstName, String lastName, LocalDate trainingDate, Number trainingDuration, boolean isActive) {
      this.username = username;
      this.firstName = firstName;
      this.lastName = lastName;
      this.trainingDate = trainingDate;
      this.trainingDuration = trainingDuration;
      this.isActive = isActive;
   }
}
