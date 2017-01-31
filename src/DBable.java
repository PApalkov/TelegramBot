public interface DBable {

    public boolean isEmpty();
    public Quest readQuestTasks();
    public void writeQuest(Quest quest); //заносит в бд квест, созданный пользователем
    //пока не ебу, что еще мне нужно, но завтра точно узнаю))

}
