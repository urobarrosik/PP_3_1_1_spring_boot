package PP_3_1_1_spring_boot.CRUD_Boot.dao;


import PP_3_1_1_spring_boot.CRUD_Boot.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@Transactional
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    @Override
    public void dropUsersTable() {
        entityManager.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
    }

    @Override
    public User getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @Override
    public void update(User user) {
        User existingUser = getUserById(user.getId());
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            entityManager.merge(existingUser);
        }
    }

    @Override
    public void delete(Long id) {
        User user = getUserById(id);
        entityManager.remove(user);
    }

    @Override
    public void addUserIfNotExist(User user) {
        String result;

        try {
            User existingUser = entityManager.createQuery("SELECT u FROM User u WHERE u.firstName = :firstName", User.class)
                    .setParameter("firstName", user.getFirstName())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingUser != null) {
                result = "Пользователь с таким именем уже существует.";
            } else {
                entityManager.persist(user);
                result = "Пользователь успешно добавлен.";
            }
        } catch (PersistenceException e) {
            result = "Ошибка при добавлении пользователя: " + e.getMessage();
        }
        System.out.println(result);
    }
}

