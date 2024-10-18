package org.example.exercise_shop.entity;


import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Service.redis.UserRedisService;

@AllArgsConstructor
@Slf4j
public class UserListener {

    private final UserRedisService userRedisService;

    @PrePersist
    public void prePersist(User user){
        log.info("UserListener prePersist");
    }

    @PostPersist
    public void postPersist(User user){
        log.info("UserListener postPersist");
        userRedisService.clear(user.getId());
    }

    @PreUpdate
    public void preUpdate(User user){
        log.info("UserListener preUpdate")  ;
    }

    @PostUpdate
    public void postUpdate(User user){
        log.info("UserListener postUpdate");
        userRedisService.clear(user.getId());
    }


}
