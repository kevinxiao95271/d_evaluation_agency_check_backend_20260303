package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.JudgeDTO;
import com.zjmc.evaluation.entity.Institution;
import com.zjmc.evaluation.entity.Judge;
import com.zjmc.evaluation.repository.InstitutionRepository;
import com.zjmc.evaluation.repository.JudgeRepository;
import com.zjmc.evaluation.service.JudgeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    @Transactional
    public JudgeDTO create(JudgeDTO dto) {
        Judge judge = new Judge();
        BeanUtils.copyProperties(dto, judge);
        judge.setType(Judge.JudgeType.valueOf(dto.getType()));
        judge.setStatus(1);

        if (dto.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(dto.getInstitutionId())
                .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
            judge.setInstitution(institution);
        } else {
            judge.setInstitution(null);
        }

        judge = judgeRepository.save(judge);
        return convertToDTO(judge);
    }

    @Override
    @Transactional
    public JudgeDTO update(Long id, JudgeDTO dto) {
        Judge judge = judgeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("评委不存在"));
        BeanUtils.copyProperties(dto, judge, "id", "createTime", "password");
        if (dto.getType() != null) {
            judge.setType(Judge.JudgeType.valueOf(dto.getType()));
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            judge.setPassword(dto.getPassword());
        }
        if (dto.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(dto.getInstitutionId())
                .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
            judge.setInstitution(institution);
        } else {
            judge.setInstitution(null);
        }
        judge = judgeRepository.save(judge);
        return convertToDTO(judge);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Judge judge = judgeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("评委不存在"));
        judge.setStatus(0);
        judgeRepository.save(judge);
    }

    @Override
    public JudgeDTO findById(Long id) {
        Judge judge = judgeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("评委不存在"));
        return convertToDTO(judge);
    }

    @Override
    public List<JudgeDTO> findAll() {
        List<Judge> judges = judgeRepository.findByStatus(1);
        return judges.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<JudgeDTO> findAllWithData() {
        return findAll();
    }

    @Override
    public JudgeDTO login(String username, String password) {
        Judge judge = judgeRepository.findByUsernameAndStatus(username, 1)
            .orElseThrow(() -> new RuntimeException("用户不存在或已禁用"));
        if (!judge.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return convertToDTO(judge);
    }

    @Override
    public List<JudgeDTO> findByType(String type) {
        List<Judge> judges = judgeRepository.findByTypeAndStatus(Judge.JudgeType.valueOf(type), 1);
        if (judges.isEmpty()) {
            return getDefaultJudgesByType(type);
        }
        return judges.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private JudgeDTO convertToDTO(Judge judge) {
        JudgeDTO dto = new JudgeDTO();
        BeanUtils.copyProperties(judge, dto);
        dto.setType(judge.getType().name());
        if (judge.getInstitution() != null) {
            dto.setInstitutionId(judge.getInstitution().getId());
            dto.setInstitutionName(judge.getInstitution().getName());
        }
        dto.setPassword(null);
        return dto;
    }

    private List<JudgeDTO> getDefaultJudges() {
        List<JudgeDTO> list = new ArrayList<>();
        String[][] experts = {
            {"1", "张专家", "expert1", "EXPERT", "主任医师", "1", "临床检验中心"},
            {"2", "李专家", "expert2", "EXPERT", "副主任医师", "2", "护理质控中心"},
            {"3", "王专家", "expert3", "EXPERT", "主任医师", null, null},
        };
        String[][] publics = {
            {"4", "赵评委", "public1", "PUBLIC", null, "3", "省防盲指导中心"},
            {"5", "钱评委", "public2", "PUBLIC", null, "4", "省骨科技术指导中心"},
        };

        for (String[] data : experts) {
            list.add(createDefaultJudge(data));
        }
        for (String[] data : publics) {
            list.add(createDefaultJudge(data));
        }
        return list;
    }

    private List<JudgeDTO> getDefaultJudgesByType(String type) {
        return getDefaultJudges().stream()
            .filter(j -> j.getType().equals(type))
            .collect(Collectors.toList());
    }

    private JudgeDTO createDefaultJudge(String[] data) {
        JudgeDTO dto = new JudgeDTO();
        dto.setId(Long.parseLong(data[0]));
        dto.setName(data[1]);
        dto.setUsername(data[2]);
        dto.setType(data[3]);
        dto.setTitle(data[4]);
        if (data[5] != null) {
            dto.setInstitutionId(Long.parseLong(data[5]));
        }
        dto.setInstitutionName(data[6]);
        dto.setStatus(1);
        return dto;
    }
}
