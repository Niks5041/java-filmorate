package ru.yandex.practicum.filmorate.model.films.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {

    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDateConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        // Проверяем, что дата не раньше 28 декабря 1895 года
        return releaseDate == null || !releaseDate.isBefore(EARLIEST_DATE);
    }
}
