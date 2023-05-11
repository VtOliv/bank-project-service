package com.study.bank.util;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@Component
public class Conditions<T> {

    public Specification<T> equals(String field, Object value, String... fieldsToJoin) {
        if (Optional.ofNullable(value).isEmpty() || !Optional.ofNullable(value).isPresent()) {
            return null;
        } else {
            return (root, query, criteria) -> {
				if (Optional.ofNullable(fieldsToJoin).isPresent() && fieldsToJoin.length > 0) {
                    return criteria.equal(join(root, fieldsToJoin).get(field), value);
                }

                return criteria.equal(root.get(field), value);
            };
        }
    }

    public Specification<T> equalsList(String field, Set<Long> value, String... fieldsToJoin) {
        if (Optional.ofNullable(value).isEmpty()) {
            return null;
        } else {
            return (root, query, criteria) -> {
                if (Optional.ofNullable(fieldsToJoin).isPresent() && fieldsToJoin.length > 0) {
                    return criteria.in(join(root, fieldsToJoin).get(field)).value(value);
                }
                return criteria.in(root.get(field)).value(value);
            };
        }
    }
    
    public Specification<T> likeIgnoreCase(String field, String value, String... fieldsToJoin) {
        if (Optional.ofNullable(value).isEmpty() || value.isEmpty()) {
            return null;
        } else {
            return (root, query, criteria) -> {
                if (Optional.ofNullable(fieldsToJoin).isPresent() && fieldsToJoin.length > 0) {
                    return criteria.like(criteria.lower(join(root, fieldsToJoin).get(field)),
                            "%" + value.toLowerCase() + "%");
                }

                return criteria.like(criteria.lower(root.get(field)), "%" + value.toLowerCase() + "%");
            };
        }
    }

    public Specification<T> notEquals(String field, Object value, String... fieldsToJoin) {
        if (Optional.ofNullable(value).isEmpty()) {
            return null;
        } else {
            return (root, query, criteria) -> {
                if (Optional.ofNullable(fieldsToJoin).isPresent() && fieldsToJoin.length > 0) {
                    return criteria.notEqual(join(root, fieldsToJoin).get(field), value);
                }

                return criteria.notEqual(root.get(field), value);
            };
        }
    }

    private Join<Object, Object> join(Root<T> root, String... fieldsToJoin) {
        var join = root.join(fieldsToJoin[0]);

        for (var i = 1; i < fieldsToJoin.length; i++) {
            join = join.join(fieldsToJoin[i]);
        }

        return join;
    }

}