package org.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

//@Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다. 
public class ReflectionTest {

    private static final Logger logger  = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan() {
        Set<Class<?>> beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));

        logger.debug("beans [{}]", beans);
    }

    @Test
    void showClass() {
        //클래스의 모든 정보를 출력하는 메서드
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        logger.debug("User all declared fields: [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared Constructors: [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared Methods: [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    @Test
    void load() throws ClassNotFoundException {
        //힙 영역에 로드되어 있는 클래스 객체를 가져오는 방법
        //1번째 방법
        Class<User> clazz = User.class;

        //2번째 방법
        User user = new User("serverwizard", "자바");
        Class<? extends User> clazz2 = user.getClass();

        //3번째 방법
        Class<?> clazz3 = Class.forName("org.example.model.User");

        logger.debug("clazz: [{}]", clazz);
        logger.debug("clazz2: [{}]", clazz2);
        logger.debug("clazz3: [{}]", clazz3);

        //위의 3가지 객체가 같은지 확인
        assertThat(clazz == clazz2).isTrue();
        assertThat(clazz2 == clazz3).isTrue();
        assertThat(clazz3 == clazz).isTrue();
        //테스트를 통과하면 모두 같은 객체이다.
    }

    private static Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections("org.example");//현재 해키지 기준으로 찾아 볼 것이다.

        Set<Class<?>> beans = new HashSet<>();
        //org.example 패키지 밑에 @Controller 라는 애노테이션을 찾아서 해당 HashSet에 담는 코드이다.
        /*
        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        beans.addAll(reflections.getTypesAnnotatedWith(Service.class));
         */
        //위의 두줄을 리펙토링 하였다.
        annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));
        return beans;
    }
}
