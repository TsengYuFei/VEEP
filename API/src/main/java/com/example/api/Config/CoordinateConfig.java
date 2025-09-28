package com.example.api.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "coordinate")
public class CoordinateConfig {
    private X x;
    private Y y;

    @Getter
    @Setter
    public static class X{
        private Integer min;
        private Integer max;
    }

    @Getter
    @Setter
    public static class Y{
        private Integer min;
        private Integer max;
    }
}
