package ru.yandex.practicum.filmorate.model.films.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
//@Target – указывает, что именно мы можем пометить этой аннотацией.
@Retention(RetentionPolicy.RUNTIME) //@Retention – позволяет указать жизненный цикл аннотации: будет она присутствовать только в исходном коде,
// в скомпилированном файле, или она будет также видна и в процессе выполнения.
@Constraint(validatedBy = ReleaseDateValidator.class) //список реализаций данного интерфейса
public @interface ReleaseDateConstraint {
    String message() default "Дата релиза не может быть раньше 28 декабря 1895 года";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
