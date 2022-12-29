package ru.artemev.producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = ClientModel.ClientModelBuilder.class)
public class ClientModel {
  @JsonProperty("LAST_NAME")
  private String lastName;

  @JsonProperty("FIRST_NAME")
  private String firstName;

  @JsonProperty("MIDDLE_NAME")
  private String middleName;

  @JsonProperty("BIRTH_DATE")
  private String birthDay;

  @JsonProperty("MAIN_DOC_TYPE")
  private String mainDocType;

  @JsonProperty("MAIN_DOC_NBR")
  private String mainDocNbr;

  @JsonProperty("ADD_DOC_TYPE")
  private String addDocType;

  @JsonProperty("ADD_DOC_NBR")
  private String addDocNbr;

  @JsonProperty("MAIN_DOC_ISSUE_DATE")
  private String mainDocIssueDate;

  @JsonProperty("MAIN_DOC_ISSUE_AUTH")
  private String mainDocIssueAuth;

  @JsonProperty("ADD_DOC_ISSUE_DATE")
  private String addDocIssueDate;

  @JsonProperty("ADD_DOC_ISSUE_AUTH")
  private String addDocIssueAuth;
}
