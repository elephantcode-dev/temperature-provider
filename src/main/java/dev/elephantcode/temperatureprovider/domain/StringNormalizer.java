package dev.elephantcode.temperatureprovider.domain;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
class StringNormalizer {

    String normalize(String value) {
        return StringUtils.normalizeSpace(value)
                .toLowerCase();
    }

}
