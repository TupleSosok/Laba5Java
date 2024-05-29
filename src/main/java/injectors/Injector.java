package injectors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * {@code Injector} внедрение зависимостей в поля c аннотацией {@code AutoInjectable}.
 * @see AutoInjectable
 */
public class Injector {

    /**
     * Внедряет в поля передаваемого объекта
     *
     * @param object объект в которйо внедряются
     * @param <T>    тип объекта
     * @return объект с внедренными зависимостями
     */
    public <T> T inject(T object) throws IOException, ClassNotFoundException {
        Class<?> injectAble = object.getClass();
        Field[] fields = injectAble.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                Object instance = createInstance(fieldType);
                field.setAccessible(true);
                try {
                    field.set(object, instance);
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    /**
     * Создает экземпляр,используя имя класса из файла свойств
     * @param injectAble класс, для которого должен быть оздан экземпляр
     * @return экземпляр указанного класса
     * @throws IOException ошибка чтения файла свойств
     * @throws RuntimeException если экземпляр не может быть создан
     */
    private Object createInstance(Class<?> injectAble) throws IOException, ClassNotFoundException {
        String className = getClassNameFromProperties(injectAble.getName());
        try {
            Class<?> instance = Class.forName(className);
            Constructor<?> constructor = instance.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Невозможно создать объект класса : " + className);
        }
    }

    /**
     * Получает имя класса, соответствующий интерфейсу
     * @param interfaceName имя интерфейса
     * @return имя класса
     * @throws IOException ошибка чтения *.properties
     */
    private String getClassNameFromProperties(String interfaceName) throws IOException{
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("injector.properties")) {
            properties.load(input);
            return properties.getProperty(interfaceName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить свойства");
        }
    }
}