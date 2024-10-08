package com.shuwei.dai;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public interface ObjectService {

    default Boolean eq(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    default Boolean eq(String o1, String o2) {
        return StringUtils.equals(o1, o2);
    }

    default Boolean nq(Object o1, Object o2) {
        return !Objects.equals(o1, o2);
    }

    default Boolean isNull(Object o) {
        return Objects.isNull(o);
    }

    default Boolean notNull(Object o) {
        return !Objects.isNull(o);
    }

    default Boolean isBlank(String o) {
        return StringUtils.isBlank(o);
    }

    default Boolean notBlank(String o) {
        return StringUtils.isNotBlank(o);
    }

    default Boolean isEmpty(Collection<?> o) {
        return CollectionUtils.isEmpty(o);
    }

    default Boolean notEmpty(Collection<?> o) {
        return CollectionUtils.isNotEmpty(o);
    }

}
