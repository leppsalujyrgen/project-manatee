package ee.cyber.manatee.repository;

import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.model.Interview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Integer> {
}
