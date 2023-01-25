package com.bobocode.severins.context;

import com.bobocode.severins.annotations.Bean;
import com.bobocode.severins.exceptions.NoSuchBeanException;
import com.bobocode.severins.exceptions.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContextImpl implements ApplicationContext {
    Map<String, Object> beansMap = new HashMap<>();
    public ApplicationContextImpl(String packageName) {
        var reflections = new Reflections(packageName);
        var beanClasses = reflections.getTypesAnnotatedWith(Bean.class);
        initContext(beanClasses);
    }

    private void initContext(Set<Class<?>> beanClasses) {
        for(var beanClass : beanClasses){
            try {
                var constructor = beanClass.getConstructor();
                var bean = constructor.newInstance();

                var beanName = getBeanName(beanClass);
                beansMap.put(beanName, bean);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Can't create bean instance.", e);
            }
        }
    }

    private String getBeanName(Class<?> beanClass){
        var annotationValue = beanClass.getAnnotation(Bean.class).value();
        if (! annotationValue.isEmpty()){
            return annotationValue;
        }
        var className = beanClass.getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        var matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1){
            throw new NoUniqueBeanException();
        }
        return matchingBeans.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(NoSuchBeanException::new);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) {
        return beansMap.entrySet().stream()
                .filter(entry -> name.equals(entry.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(NoSuchBeanException::new);
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beansMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
