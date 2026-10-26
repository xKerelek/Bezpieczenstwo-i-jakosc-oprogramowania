package pl.edu.atar.domain.mapper;

@FunctionalInterface
public interface Converter<T, F> {
    T convert(F from);
}
