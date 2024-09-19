package app.eventapi.eventapiservice.application.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<UUID, String> uuidToStringConverter = new Converter<UUID, String>() {
            @Override
            public String convert(MappingContext<UUID, String> context) {
                return context.getSource() != null ? context.getSource().toString() : null;
            }
        };

        Converter<String, UUID> stringToUuidConverter = new Converter<String, UUID>() {
            @Override
            public UUID convert(MappingContext<String, UUID> context) {
                return context.getSource() != null ? UUID.fromString(context.getSource()) : null;
            }
        };

        modelMapper.addConverter(uuidToStringConverter);
        modelMapper.addConverter(stringToUuidConverter);

        return modelMapper;

    }
}
