package com.andersen.userservice.model.workspace;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

  private Long id;
  private String name;
  private LocalDate creationDate;
  private String description;
}
