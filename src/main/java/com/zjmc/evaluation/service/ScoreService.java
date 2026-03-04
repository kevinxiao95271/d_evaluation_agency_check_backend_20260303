package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.ScoreRecordDTO;
import com.zjmc.evaluation.dto.ScoreResultDTO;
import com.zjmc.evaluation.dto.ScoreStatisticsDTO;
import com.zjmc.evaluation.dto.ScoreSubmitDTO;

import java.util.List;

public interface ScoreService {

    void submitScore(ScoreSubmitDTO dto);

    ScoreResultDTO calculateResult(Long taskId, Long institutionId);

    List<ScoreResultDTO> calculateAllResults(Long taskId);

    List<ScoreResultDTO> getResultsWithData(Long taskId);

    List<ScoreRecordDTO> findScoreRecords(Long taskId, Long institutionId, Long judgeId);

    ScoreRecordDTO findRecordById(Long recordId);

    void deleteRecord(Long recordId);

    void batchDeleteRecords(List<Long> recordIds);

    ScoreStatisticsDTO getScoreStatistics(Long taskId);

    ScoreStatisticsDTO getJudgeStatistics(Long judgeId);

    ScoreStatisticsDTO getInstitutionStatistics(Long institutionId);
}
