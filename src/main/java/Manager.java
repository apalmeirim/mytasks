import java.util.*;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.mindrot.jbcrypt.BCrypt;


public class Manager {
    
    private Map<String, User> users;
    private User curr;

    public Manager() {
        curr = null;
        loadData();
    }


    public boolean register(String user, String pass) {
        if(users.containsKey(user)) {
            return false;
        } 
        String hshdPass = BCrypt.hashpw(pass, BCrypt.gensalt());
        users.put(user, new User(user, hshdPass));
        saveData();
        return true;
    }


    public boolean login(String user, String pass) {
        User u = users.get(user);
        if(u != null && BCrypt.checkpw(pass, u.getHashedPass()) && !loggedIn()) {
            curr = u;
            return true;
        } return false;
    }


    public boolean logout() {
        if(loggedIn()) {
            curr = null;
            saveData();
            return true;
        } return false;
    }


    public boolean loggedIn() {
        return curr != null;
    }

    public void clearAllInformation() {
        users = new HashMap<>();
        saveData();
    }

    
    public boolean addTask(String name, String category, LocalDate deadline) {
        if(!loggedIn()) return false;
        Task t = new Task(name, deadline, category);
        curr.addTask(t);
        saveData();
        return true;
    }


    public boolean removeTask(int i) {
        if(!loggedIn()) return false;
        saveData();
        return curr.removeTask(i);
    }


    public boolean completeTask(int i) {
        if(!loggedIn()) return false;
        Task t = curr.getTask(i);
        if(t != null) {
            t.setComplete();
            saveData();
            return true;
        } return false;
    }


    public List<Task> searchTasks(String kw) {
        if(!loggedIn()) return Collections.emptyList();
        return curr.getList().stream().filter(t -> t.getName().toLowerCase().contains(kw.toLowerCase())
        || t.getCategory().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
    }


    public List<Task> getList() {
        return (loggedIn()) ? curr.getList() : Collections.emptyList();
    }


    public void saveData() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("users.json")) {
            String json = gson.toJson(users);
            String encryptedJson = CryptoUtil.encrypt(json);
            writer.write(encryptedJson);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }


    public void loadData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        try (FileReader reader = new FileReader("users.json")) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            String decryptedJson = CryptoUtil.decrypt(sb.toString());
            users = gson.fromJson(decryptedJson, new TypeToken<Map<String, User>>(){}.getType());
            if (users == null) users = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            users = new HashMap<>();
        }
    }

    

}
