package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.ScoreRecordDTO;
import com.zjmc.evaluation.dto.ScoreResultDTO;
import com.zjmc.evaluation.dto.ScoreStatisticsDTO;
import com.zjmc.evaluation.dto.ScoreSubmitDTO;
import com.zjmc.evaluation.entity.*;
import com.zjmc.evaluation.repository.*;
import com.zjmc.evaluation.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ScoreServiceImpl implements ScoreService {

    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Autowired
    private ScoreRecordRepository scoreRecordRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private ScoreItemRepository scoreItemRepository;

    @Autowired
    private ScoreTemplateRepository templateRepository;

    @Autowired
    private InstitutionBonusRepository bonusRepository;

    @Autowired
    private ScoreCategoryRepository categoryRepository;

    @Autowired
    private TaskInstitutionRepository taskInstitutionRepository;

    @Value("${evaluation.expert-weight:0.7}")
    private Double expertWeight;

    @Value("${evaluation.public-weight:0.2}")
    private Double publicWeight;

    @Value("${evaluation.director-bonus:8}")
    private Integer directorBonus;

    @Value("${evaluation.secretary-bonus:2}")
    private Integer secretaryBonus;

    @Override
    @Transactional
    public void submitScore(ScoreSubmitDTO dto) {
        Task task = taskRepository.findById(dto.getTaskId())
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));

        Institution institution = institutionRepository.findById(dto.getInstitutionId())
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));

        Judge judge = judgeRepository.findById(dto.getJudgeId())
            .orElseThrow(() -> new EntityNotFoundException("评委不存在"));

        // 同机构回避检查
        if (judge.getInstitution() != null && 
            judge.getInstitution().getId().equals(institution.getId())) {
            throw new RuntimeException("不能给自己的机构评分");
        }

        String scoreMode = dto.getScoreMode() != null ? dto.getScoreMode() : "ITEM";

        if ("TOTAL".equals(scoreMode) && dto.getTotalScore() != null) {
            distributeTotalScore(task, institution, judge, dto.getTotalScore());
        } else if (dto.getItemScores() != null) {
            for (ScoreSubmitDTO.ItemScoreDTO itemScore : dto.getItemScores()) {
                ScoreItem item = scoreItemRepository.findById(itemScore.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("评分条目不存在"));

                if (itemScore.getScore() != null && itemScore.getScore() > item.getMaxScore()) {
                    throw new RuntimeException("分数 " + itemScore.getScore() + " 不能超过条目 " + item.getName() + " 的最大分 " + item.getMaxScore());
                }

                ScoreRecord record = scoreRecordRepository
                    .findByTaskIdAndInstitutionIdAndJudgeIdAndItemId(
                        dto.getTaskId(), dto.getInstitutionId(), dto.getJudgeId(), itemScore.getItemId())
                    .orElse(new ScoreRecord());

                record.setTask(task);
                record.setInstitution(institution);
                record.setJudge(judge);
                record.setItem(item);
                record.setScore(itemScore.getScore());
                record.setComment(itemScore.getComment());
                scoreRecordRepository.save(record);
            }
        }
        logger.info("Score submission successful for judge {} and institution {}", judge.getName(), institution.getName());
    }

    private void distributeTotalScore(Task task, Institution institution, Judge judge, Double totalScore) {
        // 获取默认模板
        ScoreTemplate template = templateRepository.findByIsDefaultAndStatus(1, 1)
            .orElseThrow(() -> new RuntimeException("未找到默认评分模板"));

        if (totalScore > template.getTemplateMaxScore()) {
            throw new RuntimeException("总分 " + totalScore + " 不能超过模板最大分 " + template.getTemplateMaxScore());
        }

        List<ScoreItem> items = scoreItemRepository.findByTemplateIdAndStatusOrderBySortOrderAsc(
            template.getId(), 1);

        for (ScoreItem item : items) {
            // 根据权重分配分数
            Double distributedScore = totalScore * item.getWeight();
            // 不超过条目满分
            distributedScore = Math.min(distributedScore, item.getMaxScore());

            ScoreRecord record = scoreRecordRepository
                .findByTaskIdAndInstitutionIdAndJudgeIdAndItemId(
                    task.getId(), institution.getId(), judge.getId(), item.getId())
                .orElse(new ScoreRecord());

            record.setTask(task);
            record.setInstitution(institution);
            record.setJudge(judge);
            record.setItem(item);
            record.setScore(distributedScore);
            scoreRecordRepository.save(record);
        }
    }

    @Override
    public ScoreResultDTO calculateResult(Long taskId, Long institutionId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        Institution institution = institutionRepository.findById(institutionId)
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));

        ScoreResultDTO result = new ScoreResultDTO();
        result.setTaskId(taskId);
        result.setTaskName(task.getName());
        result.setInstitutionId(institutionId);
        result.setInstitutionName(institution.getName());
        result.setInstitutionType(institution.getType().name());

        // 获取所有评分记录
        List<ScoreRecord> records = scoreRecordRepository.findByTaskIdAndInstitutionId(taskId, institutionId);

        // 获取模板
        ScoreTemplate template = templateRepository.findByIsDefaultAndStatus(1, 1)
            .orElseThrow(() -> new RuntimeException("未找到默认评分模板"));

        // 计算专家评委平均分
        List<ScoreRecord> expertRecords = records.stream()
            .filter(r -> r.getJudge().getType() == Judge.JudgeType.EXPERT)
            .collect(Collectors.toList());

        Double expertAvg = calculateAverageByJudges(expertRecords, template);
        Long expertCount = scoreRecordRepository.countExpertJudgesByTaskIdAndInstitutionId(taskId, institutionId);

        // 计算大众评委平均分
        List<ScoreRecord> publicRecords = records.stream()
            .filter(r -> r.getJudge().getType() == Judge.JudgeType.PUBLIC)
            .collect(Collectors.toList());

        Double publicAvg = calculateAverageByJudges(publicRecords, template);
        Long publicCount = scoreRecordRepository.countPublicJudgesByTaskIdAndInstitutionId(taskId, institutionId);

        // 换算到100分制
        Double conversionRate = 100.0 / template.getTemplateMaxScore();
        Double expertConverted = expertAvg * conversionRate;
        Double publicConverted = publicAvg * conversionRate;

        // 计算各项贡献
        Double expertContribution = expertConverted * expertWeight;
        Double publicContribution = publicConverted * publicWeight;

        // 获取附加项
        InstitutionBonus bonus = bonusRepository.findByTaskIdAndInstitutionId(taskId, institutionId)
            .orElse(new InstitutionBonus());

        Integer directorBonusScore = bonus.getDirectorPresentation() != null && bonus.getDirectorPresentation() == 1 
            ? directorBonus : 0;
        Integer secretaryBonusScore = bonus.getSecretaryParticipation() != null && bonus.getSecretaryParticipation() == 1 
            ? secretaryBonus : 0;

        // 计算最终总分
        Double finalScore = expertContribution + publicContribution + directorBonusScore + secretaryBonusScore;

        result.setExpertAvgScore(expertAvg);
        result.setExpertJudgeCount(expertCount);
        result.setExpertContribution(expertContribution);
        result.setPublicAvgScore(publicAvg);
        result.setPublicJudgeCount(publicCount);
        result.setPublicContribution(publicContribution);
        result.setDirectorBonus(directorBonusScore);
        result.setSecretaryBonus(secretaryBonusScore);
        result.setFinalScore(finalScore);

        // 计算各条目平均分
        List<ScoreResultDTO.ItemResultDTO> itemResults = calculateItemResults(records, template);
        result.setItemResults(itemResults);

        return result;
    }

    @Override
    public List<ScoreResultDTO> calculateAllResults(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));

        // 获取该任务下的所有机构
        List<TaskInstitution> taskInstitutions = taskInstitutionRepository.findByTaskId(taskId);

        List<ScoreResultDTO> results = new ArrayList<>();
        for (TaskInstitution ti : taskInstitutions) {
            ScoreResultDTO result = calculateResult(taskId, ti.getInstitution().getId());
            results.add(result);
        }

        // 排序并设置排名
        results.sort((a, b) -> Double.compare(b.getFinalScore(), a.getFinalScore()));
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }

        return results;
    }

    @Override
    public List<ScoreResultDTO> getResultsWithData(Long taskId) {
        return calculateAllResults(taskId);
    }

    private Double calculateAverageByJudges(List<ScoreRecord> records, ScoreTemplate template) {
        if (records.isEmpty()) {
            return 0.0;
        }

        // 按评委分组
        Map<Long, List<ScoreRecord>> judgeRecords = records.stream()
            .collect(Collectors.groupingBy(r -> r.getJudge().getId()));

        List<Double> judgeTotals = new ArrayList<>();
        for (List<ScoreRecord> judgeRecs : judgeRecords.values()) {
            Double total = judgeRecs.stream().mapToDouble(ScoreRecord::getScore).sum();
            judgeTotals.add(total);
        }

        if (judgeTotals.size() <= 2) {
            // 评委数量不足，直接取平均
            return judgeTotals.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        // 去掉最高分和最低分
        Collections.sort(judgeTotals);
        List<Double> trimmed = judgeTotals.subList(1, judgeTotals.size() - 1);
        return trimmed.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private List<ScoreResultDTO.ItemResultDTO> calculateItemResults(List<ScoreRecord> records, ScoreTemplate template) {
        Map<Long, List<ScoreRecord>> itemRecords = records.stream()
            .collect(Collectors.groupingBy(r -> r.getItem().getId()));

        List<ScoreResultDTO.ItemResultDTO> results = new ArrayList<>();
        for (Map.Entry<Long, List<ScoreRecord>> entry : itemRecords.entrySet()) {
            ScoreItem item = entry.getValue().get(0).getItem();
            List<ScoreRecord> itemRecs = entry.getValue();

            // 按评委分组计算平均分
            Map<Long, List<ScoreRecord>> judgeRecs = itemRecs.stream()
                .collect(Collectors.groupingBy(r -> r.getJudge().getId()));

            List<Double> judgeScores = new ArrayList<>();
            for (List<ScoreRecord> recs : judgeRecs.values()) {
                Double avg = recs.stream().mapToDouble(ScoreRecord::getScore).average().orElse(0.0);
                judgeScores.add(avg);
            }

            Double finalAvg;
            if (judgeScores.size() <= 2) {
                finalAvg = judgeScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            } else {
                Collections.sort(judgeScores);
                List<Double> trimmed = judgeScores.subList(1, judgeScores.size() - 1);
                finalAvg = trimmed.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            }

            ScoreResultDTO.ItemResultDTO dto = new ScoreResultDTO.ItemResultDTO();
            dto.setItemId(item.getId());
            dto.setItemName(item.getName());
            dto.setCategoryName(item.getCategory() != null ? item.getCategory().getName() : "");
            dto.setMaxScore(item.getMaxScore());
            dto.setAvgScore(finalAvg);
            results.add(dto);
        }

        return results;
    }

    @Override
    public List<ScoreRecordDTO> findScoreRecords(Long taskId, Long institutionId, Long judgeId) {
        List<ScoreRecord> records = scoreRecordRepository.findByFilters(taskId, institutionId, judgeId);
        return records.stream().map(this::convertToRecordDTO).collect(Collectors.toList());
    }

    @Override
    public ScoreRecordDTO findRecordById(Long recordId) {
        ScoreRecord record = scoreRecordRepository.findById(recordId)
            .orElseThrow(() -> new EntityNotFoundException("评分记录不存在"));
        return convertToRecordDTO(record);
    }

    @Override
    @Transactional
    public void deleteRecord(Long recordId) {
        if (!scoreRecordRepository.existsById(recordId)) {
            throw new EntityNotFoundException("评分记录不存在");
        }
        scoreRecordRepository.deleteById(recordId);
    }

    @Override
    @Transactional
    public void batchDeleteRecords(List<Long> recordIds) {
        for (Long id : recordIds) {
            if (scoreRecordRepository.existsById(id)) {
                scoreRecordRepository.deleteById(id);
            }
        }
    }

    @Override
    public ScoreStatisticsDTO getScoreStatistics(Long taskId) {
        ScoreStatisticsDTO stats = new ScoreStatisticsDTO();
        stats.setTotalScoreCount(scoreRecordRepository.count());
        stats.setExpertJudgeCount(scoreRecordRepository.countExpertJudgesByTaskId(taskId));
        stats.setPublicJudgeCount(scoreRecordRepository.countPublicJudgesByTaskId(taskId));
        stats.setCompletedInstitutionCount(scoreRecordRepository.countCompletedInstitutionsByTaskId(taskId));
        
        List<ScoreResultDTO> results = calculateAllResults(taskId);
        stats.setTopRankings(results.stream().limit(10).collect(Collectors.toList()));
        
        // 分类平均分
        List<ScoreRecord> records = scoreRecordRepository.findByTaskId(taskId);
        Map<String, Double> categoryAvgs = records.stream()
            .filter(r -> r.getItem().getCategory() != null)
            .collect(Collectors.groupingBy(
                r -> r.getItem().getCategory().getName(),
                Collectors.averagingDouble(ScoreRecord::getScore)
            ));
        stats.setCategoryAverages(categoryAvgs);

        return stats;
    }

    @Override
    public ScoreStatisticsDTO getJudgeStatistics(Long judgeId) {
        ScoreStatisticsDTO stats = new ScoreStatisticsDTO();
        List<ScoreRecord> records = scoreRecordRepository.findByJudgeId(judgeId);
        stats.setTotalScoreCount((long) records.size());
        
        Map<String, Long> progress = new HashMap<>();
        progress.put("scoredInstitutions", records.stream().map(r -> r.getInstitution().getId()).distinct().count());
        stats.setJudgeProgress(progress);
        
        return stats;
    }

    @Override
    public ScoreStatisticsDTO getInstitutionStatistics(Long institutionId) {
        ScoreStatisticsDTO stats = new ScoreStatisticsDTO();
        List<ScoreRecord> records = scoreRecordRepository.findByInstitutionId(institutionId);
        stats.setTotalScoreCount((long) records.size());
        
        Map<String, Double> categoryAvgs = records.stream()
            .filter(r -> r.getItem().getCategory() != null)
            .collect(Collectors.groupingBy(
                r -> r.getItem().getCategory().getName(),
                Collectors.averagingDouble(ScoreRecord::getScore)
            ));
        stats.setCategoryAverages(categoryAvgs);
        
        return stats;
    }

    private ScoreRecordDTO convertToRecordDTO(ScoreRecord record) {
        ScoreRecordDTO dto = new ScoreRecordDTO();
        BeanUtils.copyProperties(record, dto);
        dto.setTaskId(record.getTask().getId());
        dto.setTaskName(record.getTask().getName());
        dto.setInstitutionId(record.getInstitution().getId());
        dto.setInstitutionName(record.getInstitution().getName());
        dto.setJudgeId(record.getJudge().getId());
        dto.setJudgeName(record.getJudge().getName());
        dto.setJudgeType(record.getJudge().getType().name());
        dto.setItemId(record.getItem().getId());
        dto.setItemName(record.getItem().getName());
        dto.setMaxScore(record.getItem().getMaxScore());
        if (record.getItem().getCategory() != null) {
            dto.setCategoryName(record.getItem().getCategory().getName());
        }
        return dto;
    }
}
