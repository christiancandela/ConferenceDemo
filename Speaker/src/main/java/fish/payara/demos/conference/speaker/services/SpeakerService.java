package fish.payara.demos.conference.speaker.services;

import fish.payara.demos.conference.speaker.entitites.Speaker;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author fabio
 */
@ApplicationScoped
@CacheDefaults(cacheName = "speakers")
public class SpeakerService {
    
    private static final Logger LOG = Logger.getLogger(SpeakerService.class.getName());
    
    @PersistenceContext(unitName = "Speaker")
    EntityManager em;
    
    @Transactional
    public Speaker save(Speaker speaker){
        em.persist(speaker);
        return speaker;
    }
    
    @CacheResult
    public Speaker get(@CacheKey Integer id){
        LOG.info("Retrieving speaker from database");
        return em.find(Speaker.class, id);
    }
    
    public boolean allNamesExists(List<String> names){
        List<String> allNames = em.createNamedQuery("Speaker.all", Speaker.class).getResultStream()
                                  .map(Speaker::getName).collect(Collectors.toList());
        return names.stream().allMatch(allNames::contains);
    }
}
