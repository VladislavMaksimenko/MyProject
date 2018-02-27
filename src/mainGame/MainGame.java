package mainGame;

import settings.GameResources;

import java.util.Random;

/** Абстрактный класс содержащий в себе базовые свойства объекта игры */
public abstract class MainGame {

    /** максимально возможное значение координат*/
    protected double max;
    /** минимально возможное значение координат*/
    protected double min;

    /** значение размера игрового объекта*/
    protected double size;
    /** энергия игрового объекта */
    protected double energy;
    /** координата Х объекта на карте */
    protected double x;
    /** координата Y объекта на карте */
    protected double y;
    /** вспомогательный объект для генирации случайного чисал */
    Random random;

    /** Базовый конструктор класса */
    protected MainGame() {
        random = new Random();
        x = random.nextInt(GameResources.mapSize);
        y = random.nextInt(GameResources.mapSize);
    }

    /** Корректировка координат */
    public abstract void fixPosition(double x, double y);
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getSize() {
        return size;
    }
    public double getEnergy() {
        return energy;
    }

    /** Задает новую позицию объекта */
    protected void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}