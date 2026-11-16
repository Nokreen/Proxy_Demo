//  Общий интерфейс (Зеркало базы данных)
interface Database {
    void queryData(String username);
}

//  Реальный объект (Настоящая база данных)
class RealDatabase implements Database {
    public RealDatabase() {
        System.out.println("Инициализация реальной базы данных... (долгий процесс)");
    }

    @Override
    public void queryData(String username) {
        System.out.println("Выполнение секретного запроса из базы данных для пользователя: " + username);
    }
}

//  Прокси
class DatabaseProxy implements Database {
    private RealDatabase realDatabase;
    private String adminPassword = "12345"; // Пароль

    @Override
    public void queryData(String password) {
        // Проверяем права доступа (это контроль заместителя)
        if (adminPassword.equals(password)) {
            // Если пароль верный, создаем реальную БД и пускаем
            if (realDatabase == null) {
                realDatabase = new RealDatabase();
            }
            realDatabase.queryData("Admin");
        } else {
            System.out.println("ОШИБКА: Доступ запрещен! Неверный пароль.");
        }
    }
}

//  Тест
public class Main {
    public static void main(String[] args) {
        // Клиент работает через заместителя и думает, что это база данных
        Database db = new DatabaseProxy();

        System.out.println("--- Попытка 1: Пользователь с неверным паролем ---");
        db.queryData("qwerty"); // Вызовет ошибку доступа

        System.out.println("\n--- Попытка 2: Администратор с верным паролем ---");
        db.queryData("12345"); // Пропустит и инициализирует базу

        System.out.println("\n--- Попытка 3: Администратор делает второй запрос ---");
        db.queryData("12345"); // База уже создана, повторной инициализации не будет
    }
}