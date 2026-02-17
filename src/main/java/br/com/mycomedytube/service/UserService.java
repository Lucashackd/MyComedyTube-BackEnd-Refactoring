// Responsabilidade: Validar regras e gerenciar a transação. Usamos EJB (@Stateless).

package br.com.mycomedytube.service;

import br.com.mycomedytube.model.User;
import br.com.mycomedytube.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UserService {

    @Inject
    private UserRepository repository;

    public void register(User user) {
        if (repository.readByEmail(user.getEmail()) != null)
            throw new RuntimeException("This email is already registered");

        if (repository.readByName(user.getName()) != null)
            throw new RuntimeException("This username is already in use");

        if (user.getAvatar() == null)
            user.setAvatar("TODO: INSERIR URL PADRÃO");

        repository.create(user);
    }

    public User searchByEmail(String email) {
        User user = repository.readByEmail(email);

        if (user == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("No user was found with this email");
        }

        return user;
    }
}
