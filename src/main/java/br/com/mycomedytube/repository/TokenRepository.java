// Responsabilidade: Falar com o EntityManager. Ele é "burro", só sabe salvar, buscar e deletar. Usamos CDI (@RequestScoped).

package br.com.mycomedytube.repository;

import br.com.mycomedytube.model.UserToken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@RequestScoped
public class TokenRepository {

    @PersistenceContext(unitName = "mycomedytube-pu")
    private EntityManager entityManager;

    public void save(UserToken userToken) {
        entityManager.persist(userToken);
    }

    public Optional<UserToken> searchByString(String tokenString) {
        return entityManager.createQuery("SELECT t FROM UserToken t WHERE t.token = :token", UserToken.class)
                .setParameter("token", tokenString)
                .getResultStream()
                .findFirst();
    }

    public void deleteByString(String tokenString) {
        entityManager.createQuery("DELETE FROM UserToken t WHERE t.token = :token")
                .setParameter("token", tokenString)
                .executeUpdate();
    }
}
