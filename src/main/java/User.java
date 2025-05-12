import java.util.ArrayList;
import java.util.List;

public class User {
    
    private String user;
    private String pass;
    private List<Task> list;


    public User(String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.list = new ArrayList<>();
    }


    public String getUser() {
        return user;
    }


    public boolean checkPass(String attempt) {
        return pass.equals(attempt);
    }

    public String getHashedPass() {
        return pass;
    }


    public List<Task> getList() {
        return list;
    }


    public Task getTask(int index) {
        return (index >= 0 && index < list.size()) ? list.get(index) : null;
    }


    public void addTask(Task task) {
        list.add(task);
    }


    public boolean removeTask(int index) {
        if(index >= 0 && index < list.size()) {
            list.remove(index);
            return true;
        } return false;
    }


    public void clearAll() {
        list.clear();
    }

}
