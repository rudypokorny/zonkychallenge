package cz.rudypokorny.zonkychallenge.common;

import cz.rudypokorny.zonkychallenge.exceptions.ConverterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Factory class taking care of searching for registered DTO to Domain object converters
 */
@Component
public class ConverterFactory {

    private final Collection<DataConverter> registeredConverters;

    @Autowired
    public ConverterFactory(final ApplicationContext applicationContext) {
        registeredConverters = applicationContext.getBeansOfType(DataConverter.class).values();
    }

    /**
     * Searches in application context for classes of type {@link DataConverter} -> for the first one which supports (converts) given object
     *
     * @param convertFrom the object which needs to be converted
     * @return found instance of {@link DataConverter}
     * @throws ConverterNotFoundException if the {@link DataConverter} instance is not present in the application context
     */
    public DataConverter<Object, ExternalData> findConverter(final Object convertFrom) throws ConverterNotFoundException {
        return registeredConverters.stream()
                .filter(converter -> converter.supportsConversion(convertFrom))
                .findFirst()
                .orElseThrow(() -> new ConverterNotFoundException(convertFrom));
    }


}
