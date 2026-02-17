// Responsabilidade: Falar com o EntityManager. Ele é "burro", só sabe salvar, buscar e deletar. Usamos CDI (@RequestScoped).

package br.com.mycomedytube.repository;

import br.com.mycomedytube.model.Video;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@RequestScoped
public class VideoRepository {

    @PersistenceContext(unitName = "mycomedytube-pu")
    private EntityManager entityManager;

    public void create(Video video) {
        if (video.getId() == null) entityManager.persist(video);
        else entityManager.merge(video);
    }

    public List<Video> readAll() {
        return entityManager.createQuery("SELECT v FROM Video v", Video.class).getResultList();
    }

    public Video readOne(Long id) {
        return entityManager.find(Video.class, id);
    }

    public void delete(Long id) {
        Video video = readOne(id);
        if (video != null) entityManager.remove(video);
    }
}
