package PP_3_1_1_spring_boot.CRUD_Boot.service;



import PP_3_1_1_spring_boot.CRUD_Boot.model.User;

import java.util.List;

public interface UserService {
    void add(User user);

    List<User> listUsers();

    void dropUsersTable();

    User getUserById(Long id);

    void update(User user);

    void delete(Long id);

    void addUserIfNotExist(User user);
}
