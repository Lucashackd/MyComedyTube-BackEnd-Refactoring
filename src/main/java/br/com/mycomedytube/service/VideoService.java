// Responsabilidade: Validar regras e gerenciar a transação. Usamos EJB (@Stateless).

package br.com.mycomedytube.service;

import br.com.mycomedytube.model.Video;
import br.com.mycomedytube.repository.VideoRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class VideoService {

    @Inject
    private VideoRepository repository;

    public void upload(Video video) {
        if (video.getTitle() == null || video.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Video title should not be empty");
        }

        if (video.getThumbnail() == null) {
            video.setThumbnail("TODO: INSERIR URL PADRÃO");
        }

        repository.save(video);
    }

    public List<Video> listAll() {
        return repository.readAll();
    }

    public Video watch(Long id) {
        return null; // TODO
    }
}
