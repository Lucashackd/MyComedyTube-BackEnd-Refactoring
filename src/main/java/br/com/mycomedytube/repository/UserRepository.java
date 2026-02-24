// Responsabilidade: Falar com o EntityManager. Ele é "burro", só sabe salvar, buscar e deletar. Usamos CDI (@RequestScoped).

package br.com.mycomedytube.repository;

import br.com.mycomedytube.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class UserRepository {

    @PersistenceContext(unitName = "mycomedytube-pu")
    private EntityManager entityManager;

    public void save(User user) {
        if (user.getId() == null) entityManager.persist(user);
        else entityManager.merge(user);
    }

    public User readByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User readByName(String name) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
