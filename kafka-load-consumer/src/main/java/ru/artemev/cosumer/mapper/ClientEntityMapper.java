package ru.artemev.cosumer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.artemev.cosumer.entity.ClientEntity;
import ru.artemev.cosumer.model.ClientModel;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper {
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "addDocType", source = "addDocType"),
    @Mapping(target = "addDocNbr", source = "addDocNbr"),
    @Mapping(target = "addDocIssueDate", source = "addDocIssueDate"),
    @Mapping(target = "addDocIssueAuth", source = "addDocIssueAuth"),
  })
  ClientEntity toClientEntity(ClientModel clientModel);
}
