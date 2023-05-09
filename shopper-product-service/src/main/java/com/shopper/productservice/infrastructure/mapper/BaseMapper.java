package com.shopper.productservice.infrastructure.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseMapper<E, D> {

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

    public Collection<E> toEntity(Collection<D> dto) {
        return dto.stream().map(d -> toEntity(d)).collect(Collectors.toList());
    }

    public Collection<D> toDto(Collection<E> entity) {
        return entity.stream().map(e -> toDto(e)).collect(Collectors.toList());
    }

    public List<E> toEntityList(Collection<D> dto) {
        return toEntity(dto).stream().collect(Collectors.toList());
    }

    public List<D> toDtoList(Collection<E> entity) {
        return toDto(entity).stream().collect(Collectors.toList());
    }

    public Set<E> toEntitySet(Collection<D> dto) {
        return toEntity(dto).stream().collect(Collectors.toSet());
    }

    public Set<D> toDtoSet(Collection<E> entity) {
        return toDto(entity).stream().collect(Collectors.toSet());
    }
}
