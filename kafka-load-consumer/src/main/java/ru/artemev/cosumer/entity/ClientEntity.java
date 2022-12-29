package ru.artemev.cosumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientEntity {
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "birth_day")
  private String birthDay;

  @Column(name = "main_doc_type")
  private String mainDocType;

  @Column(name = "main_doc_nbr")
  private String mainDocNbr;

  @Column(name = "add_doc_type")
  private String addDocType;

  @Column(name = "add_doc_nbr")
  private String addDocNbr;

  @Column(name = "main_doc_issue_Date")
  private String mainDocIssueDate;

  @Column(name = "main_doc_issue_auth")
  private String mainDocIssueAuth;

  @Column(name = "add_doc_issue_date")
  private String addDocIssueDate;

  @Column(name = "add_doc_issue_auth")
  private String addDocIssueAuth;
}
