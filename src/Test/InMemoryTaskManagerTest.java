import Manager.*;
import Task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }

    @Test
    public void testTaskIdConflict(){ //Проверка, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;


        Task newTestTask = new Task(15, "Имя", TaskStatus.NEW);
        Task genId = new Task(TaskStatus.NEW, "Имя 1" );

        taskManager.addTask(newTestTask);
        taskManager.addTask(genId);

        assertEquals(2, taskManager.getAllTask().size(), "Размер списка меньше, чем кол-во задач.");
        // После отправки в менеджере задачам задается новый сгенерированный айди, даже если ранее айди уже был задан.

        assertNotNull(taskManager.getTaskById(0), "Не найдена задача с айди 1");
        assertNotNull(taskManager.getTaskById(1), "Не найдена задача с айди 2");
    }
    @Test
    public void  equalityOfTasks(){ // Проверка неизменности задачи после добавления.
        Task task1 = new Task(TaskStatus.NEW, "Имя");
        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskById(0);
        assertEquals(task1, task2, "Полученные объекты не равны");

    }

    @Test
    public void TasksAdd(){ // Проверка, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        Task task = new Task(TaskStatus.NEW, "Имя");
        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        SubTask subTask = new SubTask(TaskStatus.NEW, "СабТаск", 1);
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        assertEquals(task, taskManager.getTaskById(0), "Не найдена задача");
        assertEquals(epic, taskManager.getEpicById(1), "Не найден эпик");
        assertEquals(subTask, taskManager.getSubtaskById(2), "Не найдена подзадача");
        assertEquals(1, taskManager.getEpicById(1).getSubTaskCount(), "В эпике нет подзадач");
    }

    // Со следующим заданием не совсем понял реализацию. Если требуется передать эпик как объект в метод для добавления сабтаска,
    // то выходит, что теперь для методов требуется добавить обработку исключений? Через try-catch?
    // Оставил вариант с добавлением сабтаска c айди эпика.

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    public void addEpicAsSubTask(){
        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        assertNotEquals(taskManager.getEpicById(0), subTask);
        assertNotEquals(0, taskManager.getSubtaskById(1).getSubTaskId(), "Айди эпика и сабтаски равны");

    }
    //проверьте, что объект Subtask нельзя сделать своим же эпиком;
    @Test
    public void AddSubTaskAsEpic(){
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        assertNull(taskManager.getSubtaskById(0));

    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void equality(){
        TaskManager taskManager1 = Managers.getDefault();



        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        Task task = new Task(TaskStatus.NEW, "Имя");
        Task task1 = new Task(TaskStatus.NEW, "Имя");
        SubTask subTask = new SubTask(TaskStatus.NEW, "Имя", 0);

        taskManager.addEpic(epic);
        taskManager1.addEpic(epic);

        taskManager.addTask(task);
        taskManager1.addTask(task);

        taskManager.addSubTask(subTask);
        taskManager1.addSubTask(subTask);

        assertEquals(taskManager1.getTaskById(1), taskManager.getTaskById(1), "Таски не равны");
        assertEquals(taskManager1.getEpicById(0), taskManager.getEpicById(0), "Эпики не равны");
        assertEquals(taskManager1.getSubtaskById(3), taskManager.getSubtaskById(3), "Сабтаски не равны");

        taskManager.addTask(task1);
        assertNotEquals(taskManager.getTaskById(1), taskManager.getTaskById(4), "Таски с разными айди равны");
    }
}