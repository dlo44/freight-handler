package org.example.freighthandler.trailer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TrailerService {

    @Autowired
    private TrailerRepository trailerRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public Trailer saveTrailer(Trailer trailer) {
        trailer = trailerRepository.saveAndFlush(trailer);
        entityManager.refresh(trailer);
        return trailer;
    }

    public Trailer getTrailerByNumber(String trailerNumber) {
        return trailerRepository.findByTrailerNumber(trailerNumber).orElse(null);

    }

    public Trailer findByDoorNumber(int doorNumber) {
        return trailerRepository.findByDoor_DoorNumber(doorNumber).orElse(null);
    }


}
