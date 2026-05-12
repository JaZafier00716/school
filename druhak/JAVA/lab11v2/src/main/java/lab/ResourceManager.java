package lab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.image.Image;

public class ResourceManager {

    private static final Random RANDOM = new Random();

    private static final Map<String, Image> images = new HashMap<>();

    private ResourceManager() {
        /*hide public one*/
    }

    public static Image getImage(Class<?> clazz, String name) {
        if(images.containsKey(name)) {
            return images.get(name);
        } else {
            System.out.println(name);
            Image image = new Image(clazz.getResourceAsStream(name));
            images.put(name, image);
            return image;
        }
    }

    public static <T> T getRandomElement(List<T> elements) {
        if(elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("List elements cannot be null or empty.");
        }
        return elements.get(RANDOM.nextInt(elements.size()));
    }
}
