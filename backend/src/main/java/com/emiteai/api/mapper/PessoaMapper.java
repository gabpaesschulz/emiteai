package com.emiteai.api.mapper;

import com.emiteai.api.model.dto.PessoaRequest;
import com.emiteai.api.model.dto.PessoaResponse;
import com.emiteai.api.model.entity.Pessoa;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    @Mapping(target = "bairro",  ignore = true)
    @Mapping(target = "cidade",  ignore = true)
    @Mapping(target = "estado",  ignore = true)
    Pessoa toEntity(PessoaRequest dto);

    PessoaResponse toResponse(Pessoa entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bairro",  ignore = true)
    @Mapping(target = "cidade",  ignore = true)
    @Mapping(target = "estado",  ignore = true)
    void updateEntityFromDto(PessoaRequest dto, @MappingTarget Pessoa entity);
}
