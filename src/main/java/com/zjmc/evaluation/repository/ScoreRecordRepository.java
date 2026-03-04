package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.ScoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRecordRepository extends JpaRepository<ScoreRecord, Long> {

    List<ScoreRecord> findByTaskIdAndInstitutionId(Long taskId, Long institutionId);

    List<ScoreRecord> findByTaskIdAndInstitutionIdAndJudgeId(Long taskId, Long institutionId, Long judgeId);

    Optional<ScoreRecord> findByTaskIdAndInstitutionIdAndJudgeIdAndItemId(Long taskId, Long institutionId, Long judgeId, Long itemId);

    @Query("SELECT sr FROM ScoreRecord sr WHERE sr.task.id = :taskId AND sr.institution.id = :institutionId AND sr.judge.type = :judgeType")
    List<ScoreRecord> findByTaskIdAndInstitutionIdAndJudgeType(@Param("taskId") Long taskId, @Param("institutionId") Long institutionId, @Param("judgeType") String judgeType);

    boolean existsByTaskIdAndInstitutionIdAndJudgeId(Long taskId, Long institutionId, Long judgeId);

    @Query("SELECT COUNT(DISTINCT sr.judge.id) FROM ScoreRecord sr WHERE sr.task.id = :taskId AND sr.institution.id = :institutionId AND sr.judge.type = 'EXPERT'")
    Long countExpertJudgesByTaskIdAndInstitutionId(@Param("taskId") Long taskId, @Param("institutionId") Long institutionId);

    @Query("SELECT COUNT(DISTINCT sr.judge.id) FROM ScoreRecord sr WHERE sr.task.id = :taskId AND sr.institution.id = :institutionId AND sr.judge.type = 'PUBLIC'")
    Long countPublicJudgesByTaskIdAndInstitutionId(@Param("taskId") Long taskId, @Param("institutionId") Long institutionId);

    @Query("SELECT sr FROM ScoreRecord sr WHERE " +
           "(:taskId IS NULL OR sr.task.id = :taskId) AND " +
           "(:institutionId IS NULL OR sr.institution.id = :institutionId) AND " +
           "(:judgeId IS NULL OR sr.judge.id = :judgeId) " +
           "ORDER BY sr.createTime DESC")
    List<ScoreRecord> findByFilters(@Param("taskId") Long taskId, 
                                   @Param("institutionId") Long institutionId, 
                                   @Param("judgeId") Long judgeId);

    List<ScoreRecord> findByTaskId(Long taskId);

    List<ScoreRecord> findByJudgeId(Long judgeId);

    List<ScoreRecord> findByInstitutionId(Long institutionId);

    @Query("SELECT COUNT(DISTINCT sr.judge.id) FROM ScoreRecord sr WHERE sr.task.id = :taskId AND sr.judge.type = 'EXPERT'")
    Long countExpertJudgesByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT COUNT(DISTINCT sr.judge.id) FROM ScoreRecord sr WHERE sr.task.id = :taskId AND sr.judge.type = 'PUBLIC'")
    Long countPublicJudgesByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT COUNT(DISTINCT sr.institution.id) FROM ScoreRecord sr WHERE sr.task.id = :taskId")
    Long countCompletedInstitutionsByTaskId(@Param("taskId") Long taskId);
}
