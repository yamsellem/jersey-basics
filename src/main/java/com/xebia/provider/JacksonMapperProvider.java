package com.xebia.provider;

import static org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY;
import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_DEFAULT;

import javax.ws.rs.ext.ContextResolver;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;

public class JacksonMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonMapperProvider() {
        mapper = createMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDeserializationConfig(mapper.getDeserializationConfig().without(FAIL_ON_UNKNOWN_PROPERTIES));
        mapper.setSerializationConfig(mapper.getSerializationConfig().withSerializationInclusion(NON_DEFAULT));
        mapper.setVisibilityChecker(Std.defaultInstance().withFieldVisibility(ANY));
        return mapper;
    }
}
