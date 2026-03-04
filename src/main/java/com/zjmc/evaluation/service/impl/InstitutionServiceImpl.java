package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.InstitutionDTO;
import com.zjmc.evaluation.entity.Institution;
import com.zjmc.evaluation.repository.InstitutionRepository;
import com.zjmc.evaluation.service.InstitutionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    @Transactional
    public InstitutionDTO create(InstitutionDTO dto) {
        Institution institution = new Institution();
        BeanUtils.copyProperties(dto, institution);
        institution.setType(Institution.InstitutionType.valueOf(dto.getType()));
        institution.setStatus(1);
        institution = institutionRepository.save(institution);
        return convertToDTO(institution);
    }

    @Override
    @Transactional
    public InstitutionDTO update(Long id, InstitutionDTO dto) {
        Institution institution = institutionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
        BeanUtils.copyProperties(dto, institution, "id", "createTime");
        if (dto.getType() != null) {
            institution.setType(Institution.InstitutionType.valueOf(dto.getType()));
        }
        institution = institutionRepository.save(institution);
        return convertToDTO(institution);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Institution institution = institutionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
        institution.setStatus(0);
        institutionRepository.save(institution);
    }

    @Override
    public InstitutionDTO findById(Long id) {
        Institution institution = institutionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
        return convertToDTO(institution);
    }

    @Override
    public List<InstitutionDTO> findAll() {
        List<Institution> institutions = institutionRepository.findByStatus(1);
        return institutions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<InstitutionDTO> findByType(Institution.InstitutionType type) {
        List<Institution> institutions = institutionRepository.findByTypeAndStatus(type, 1);
        return institutions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<InstitutionDTO> findByTypeWithData(String type) {
        if (type == null || type.isEmpty()) {
            return findAll();
        }
        return findByType(Institution.InstitutionType.valueOf(type));
    }

    private InstitutionDTO convertToDTO(Institution institution) {
        InstitutionDTO dto = new InstitutionDTO();
        BeanUtils.copyProperties(institution, dto);
        dto.setType(institution.getType().name());
        return dto;
    }

    private List<InstitutionDTO> getDefaultInstitutions() {
        List<InstitutionDTO> all = getDefaultQualityControlInstitutions();
        all.addAll(getDefaultTechnicalServiceInstitutions());
        return all;
    }

    private List<InstitutionDTO> getDefaultInstitutionsByType(Institution.InstitutionType type) {
        if (type == Institution.InstitutionType.QUALITY_CONTROL) {
            return getDefaultQualityControlInstitutions();
        } else {
            return getDefaultTechnicalServiceInstitutions();
        }
    }

    private List<InstitutionDTO> getDefaultQualityControlInstitutions() {
        String[] names = {
            "临床检验中心", "医疗设备管理质量控制中心", "临床麻醉质控中心", "临床放射质控中心",
            "临床病理质控中心", "护理质控中心", "肿瘤诊治质控中心", "医院感染管理质控中心",
            "血液质量管理委员会", "医院药事管理质控中心", "病历管理质控中心", "口腔质控中心",
            "高压氧医疗质控中心", "急诊质控中心", "透析质控中心", "内镜（腔镜）质控中心",
            "ICU质控中心", "心血管疾病介入诊疗质控中心", "康复医学质控中心", "脑卒中医疗质控中心",
            "传染病诊治质控中心", "医院门诊管理质控中心", "产科医疗质控中心", "人类辅助生殖技术质控中心",
            "产前筛查质控中心", "儿童生长发育质控中心", "新生儿疾病筛查质控中心", "超声质控中心",
            "围绝经期保健质控中心", "妇科泌尿及盆底康复质控中心", "新生儿窒息复苏和管理质控中心",
            "儿童重症监护质控中心", "院前医疗急救质控中心", "微创技术质控中心", "性病诊治质控中心",
            "结核病诊治质控中心", "消化内镜质控中心", "整形美容质控中心"
        };
        return Arrays.stream(names).map(name -> {
            InstitutionDTO dto = new InstitutionDTO();
            dto.setId((long) (name.hashCode() & 0x7FFFFFFF));
            dto.setName(name);
            dto.setType(Institution.InstitutionType.QUALITY_CONTROL.name());
            dto.setStatus(1);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<InstitutionDTO> getDefaultTechnicalServiceInstitutions() {
        String[] names = {
            "省防盲指导中心", "省医院管理研究中心", "省核医学技术指导中心", "省器官移植技术指导中心",
            "省烧伤救治技术指导中心", "省中毒急救防治中心", "省临床营养中心", "省人工肝技术指导中心",
            "省病理、尸体解剖中心", "省皮肤病临床诊治技术指导中心", "省图书管理指导中心", "省细菌耐药监测中心",
            "省结直肠疾病诊疗中心", "省神经外科技术指导中心", "省骨科技术指导中心", "省口腔正畸中心",
            "分娩镇痛技术指导中心", "全科医学技术指导中心", "甲状腺病诊治技术指导中心", "老年病诊治指导中心",
            "日间手术技术指导中心", "口腔种植技术指导中心", "生殖微创技术指导中心", "角膜病诊治技术指导中心",
            "肿瘤靶向治疗技术指导中心", "胎儿心脏超声诊断技术指导中心"
        };
        return Arrays.stream(names).map(name -> {
            InstitutionDTO dto = new InstitutionDTO();
            dto.setId((long) (name.hashCode() & 0x7FFFFFFF));
            dto.setName(name);
            dto.setType(Institution.InstitutionType.TECHNICAL_SERVICE.name());
            dto.setStatus(1);
            return dto;
        }).collect(Collectors.toList());
    }
}
