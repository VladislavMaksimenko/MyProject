package mainGame;

import settings.GameResources;

public class Feed extends MainGame {

    /** В конструкторе случайным образом задается размер получаемой энергии агентом
     * Генерируется случаный размер объекта */
    public Feed() {
        energy = random.nextInt(GameResources.maxEnergyFeed) + GameResources.minEnergyFeed;
        size = random.nextInt(GameResources.maxSizeFeed) + GameResources.minSizeFeed;
        min = size / 2;
        max = GameResources.mapSize - min;
        fixPosition(x, y);
    }

    /** Коректирует данные координат, чтобы объект не выходил за пределы карты */
    @Override
    public void fixPosition(double x, double y) {
        if (x > max)
            x = max;
        else if (x < min)
            x = min;

        if (y > max)
            y = max;
        else if (y < min)
            y = min;

        setPosition(x, y);
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " size: " + size;
    }
}
