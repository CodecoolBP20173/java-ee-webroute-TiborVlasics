import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface WebRoute {
    String value() default "";
    String method() default  "GET";
    String path() default "";
}

