package com.smuniov.sarafan.repo;

import com.smuniov.sarafan.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
