package logicGame;

import mainGame.Feed;
import mainGame.MainGame;
import settings.GameResources;

/** Класс Агента, реализующий поведение Interaction */
public class Agent extends MainGame implements Runnable {

    /** Состояние агента */
    private boolean isAlive;

    /** Значение скорости агента */
    private double speed;

    /** Номер группы к которой пренадлежит агент */
    private int group;

    /** Направление движения агента */
    private int direction;

    /** Урон наносимый агентом другим агентам */
    private double damage;

    /** Базовый конструктор класса */
    public Agent(int group, double speed, double size, double damage) {
        energy = 100;
        setDirection();
        this.group = group;
        this.speed = speed;
        this.size = size;
        this.damage = damage;
        min = size / 2;
        max = GameResources.mapSize - min;
    }

    /** Метод запускающий жизненный цикл Агента */
    public void start() {
        isAlive = true;
        /* Поток агента */
        Thread thread = new Thread(this);
        thread.start();
    }

    /** Метод останавливающий жизненный цикл Агента */
    public void stop() {
        isAlive = false;
    }


    /** Метод запускающий процесс перемещения объекта */
    @Override
    public void run() {
        while (isAlive) {
            oneStep();
            try {
                Thread.sleep(GameResources.timeToMove);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Метод задающий направление Агента в зависимости от начальных координат */
    private void setDirection() {
        if (x > 350)
            if (y < 350)
                direction = 2;
            else
                direction = 1;
        if (x < 350)
            if (y < 350)
                direction = 3;
            else
                direction = 0;
    }

    /** Метод, который делает один шаг в зависимости от направления Агента, и за этот шаг:
     * 1) проверяет не ушел ли Агнет за границы поля
     * 2) теряет заданное кол-во энергии Агента
     * 3) проверяет количество энергии Агента */
    private void oneStep() {
        int x = 0, y = 0;
        switch (direction) {
            case 0:
                x += speed;
                y -= speed; // rightup
                break;
            case 1:
                x -= speed;
                y -= speed; // leftup
                break;
            case 2:
                x -= speed;
                y += speed; // leftdown
                break;
            case 3:
                x += speed;
                y += speed; // rightdown
                break;
        }

        double newX = this.x + x;
        double newY = this.y + y;

        fixPosition(newX, newY);

        energy -= GameResources.energyLost;
        if (energy < 0)
            isAlive = false;
    }

    /** Меняет направление движения Агента */
    protected void changeDir() {
        direction++;
        if (direction > 3)
            direction = 0;
    }

    /** Метод отвечающий за питание Агента
     * Энергия Агента не может быть больше 100 */
    public void eat(Feed f) {
        this.energy += f.getEnergy();
        if (this.energy > 100) {
            this.energy = 100;
        }
    }

    /** Метод фиксирующий получение урона */
    public void giveDmg(double dmg) {
        energy -= dmg;
    }

    @Override
    public String toString() {
        return "group: " + group + " x: " + x + " y: " + y + " energy: " + energy + " alive: " + isAlive;
    }

    public boolean alive() {
        return isAlive;
    }
    public int getGroup() {
        return group;
    }
    public double getDamage() {
        return damage;
    }

    /** Коректирует данные координат, чтобы объект не выходил за пределы карты */
    @Override
    public void fixPosition(double x, double y) {
        if (x > max || y > max || x < min || y < min)
            changeDir();

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
}
