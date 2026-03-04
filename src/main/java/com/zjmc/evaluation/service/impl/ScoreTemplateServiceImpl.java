package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.ScoreCategoryDTO;
import com.zjmc.evaluation.dto.ScoreItemDTO;
import com.zjmc.evaluation.dto.ScoreTemplateDTO;
import com.zjmc.evaluation.entity.ScoreCategory;
import com.zjmc.evaluation.entity.ScoreItem;
import com.zjmc.evaluation.entity.ScoreTemplate;
import com.zjmc.evaluation.repository.ScoreCategoryRepository;
import com.zjmc.evaluation.repository.ScoreItemRepository;
import com.zjmc.evaluation.repository.ScoreTemplateRepository;
import com.zjmc.evaluation.service.ScoreTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreTemplateServiceImpl implements ScoreTemplateService {

    @Autowired
    private ScoreTemplateRepository templateRepository;

    @Autowired
    private ScoreCategoryRepository categoryRepository;

    @Autowired
    private ScoreItemRepository itemRepository;

    @Override
    @Transactional
    public ScoreTemplateDTO create(ScoreTemplateDTO dto) {
        ScoreTemplate template = new ScoreTemplate();
        BeanUtils.copyProperties(dto, template);
        template.setStatus(1);
        template = templateRepository.save(template);
        return convertToDTO(template);
    }

    @Override
    @Transactional
    public ScoreTemplateDTO update(Long id, ScoreTemplateDTO dto) {
        ScoreTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));
        BeanUtils.copyProperties(dto, template, "id", "createTime");
        template = templateRepository.save(template);
        return convertToDTO(template);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ScoreTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));
        template.setStatus(0);
        templateRepository.save(template);
    }

    @Override
    public ScoreTemplateDTO findById(Long id) {
        ScoreTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));
        return convertToFullDTO(template);
    }

    @Override
    public List<ScoreTemplateDTO> findAll() {
        List<ScoreTemplate> templates = templateRepository.findByStatus(1);
        return templates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ScoreTemplateDTO> findAllWithData() {
        return findAll();
    }

    @Override
    public ScoreTemplateDTO getDefaultTemplate() {
        return templateRepository.findByIsDefaultAndStatus(1, 1)
            .map(this::convertToFullDTO)
            .orElse(null);
    }

    @Override
    @Transactional
    public void setDefaultTemplate(Long id) {
        List<ScoreTemplate> all = templateRepository.findByStatus(1);
        for (ScoreTemplate t : all) {
            t.setIsDefault(0);
        }
        templateRepository.saveAll(all);

        ScoreTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));
        template.setIsDefault(1);
        templateRepository.save(template);
    }

    @Override
    @Transactional
    public ScoreCategoryDTO addCategory(Long templateId, ScoreCategoryDTO dto) {
        ScoreTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));

        ScoreCategory category = new ScoreCategory();
        category.setTemplate(template);
        category.setName(dto.getName());
        category.setSortOrder(dto.getSortOrder());
        category = categoryRepository.save(category);

        ScoreCategoryDTO result = new ScoreCategoryDTO();
        result.setId(category.getId());
        result.setName(category.getName());
        result.setSortOrder(category.getSortOrder());
        return result;
    }

    @Override
    @Transactional
    public ScoreItemDTO addItem(Long templateId, Long categoryId, ScoreItemDTO dto) {
        ScoreTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -> new EntityNotFoundException("模板不存在"));

        ScoreItem item = new ScoreItem();
        item.setTemplate(template);
        if (categoryId != null) {
            ScoreCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));
            item.setCategory(category);
        }
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setMaxScore(dto.getMaxScore());
        item.setWeight(dto.getWeight());
        item.setSortOrder(dto.getSortOrder());
        item.setStatus(1);
        item = itemRepository.save(item);

        ScoreItemDTO result = new ScoreItemDTO();
        BeanUtils.copyProperties(item, result);
        return result;
    }

    private ScoreTemplateDTO convertToDTO(ScoreTemplate template) {
        ScoreTemplateDTO dto = new ScoreTemplateDTO();
        BeanUtils.copyProperties(template, dto);
        return dto;
    }

    private ScoreTemplateDTO convertToFullDTO(ScoreTemplate template) {
        ScoreTemplateDTO dto = convertToDTO(template);
        List<ScoreCategory> categories = categoryRepository.findByTemplateIdOrderBySortOrderAsc(template.getId());
        List<ScoreCategoryDTO> categoryDTOs = categories.stream().map(cat -> {
            ScoreCategoryDTO catDTO = new ScoreCategoryDTO();
            catDTO.setId(cat.getId());
            catDTO.setName(cat.getName());
            catDTO.setSortOrder(cat.getSortOrder());

            List<ScoreItem> items = itemRepository.findByCategoryIdAndStatusOrderBySortOrderAsc(cat.getId(), 1);
            List<ScoreItemDTO> itemDTOs = items.stream().map(item -> {
                ScoreItemDTO itemDTO = new ScoreItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());
            catDTO.setItems(itemDTOs);

            return catDTO;
        }).collect(Collectors.toList());
        dto.setCategories(categoryDTOs);
        return dto;
    }

    private List<ScoreTemplateDTO> getDefaultTemplates() {
        List<ScoreTemplateDTO> list = new ArrayList<>();

        ScoreTemplateDTO dto = new ScoreTemplateDTO();
        dto.setId(1L);
        dto.setName("默认评分表");
        dto.setTemplateMaxScore(100.0);
        dto.setSystemTotalScore(45.0);
        dto.setIsDefault(1);
        dto.setStatus(1);

        List<ScoreCategoryDTO> categories = new ArrayList<>();

        // 分类1：基础管理
        ScoreCategoryDTO cat1 = new ScoreCategoryDTO();
        cat1.setId(1L);
        cat1.setName("基础管理");
        cat1.setSortOrder(1);
        List<ScoreItemDTO> items1 = new ArrayList<>();
        items1.add(createItem(1L, "组织建设", "机构设置完善，人员配备合理", 10.0, 0.1, 1));
        items1.add(createItem(2L, "制度建设", "各项规章制度健全，执行到位", 10.0, 0.1, 2));
        items1.add(createItem(3L, "档案管理", "档案资料完整，管理规范", 5.0, 0.05, 3));
        cat1.setItems(items1);
        categories.add(cat1);

        // 分类2：业务开展
        ScoreCategoryDTO cat2 = new ScoreCategoryDTO();
        cat2.setId(2L);
        cat2.setName("业务开展");
        cat2.setSortOrder(2);
        List<ScoreItemDTO> items2 = new ArrayList<>();
        items2.add(createItem(4L, "业务数量", "完成业务指标情况", 15.0, 0.15, 1));
        items2.add(createItem(5L, "业务质量", "业务质量达标情况", 15.0, 0.15, 2));
        items2.add(createItem(6L, "技术创新", "新技术新项目开展情况", 10.0, 0.1, 3));
        cat2.setItems(items2);
        categories.add(cat2);

        // 分类3：服务质量
        ScoreCategoryDTO cat3 = new ScoreCategoryDTO();
        cat3.setId(3L);
        cat3.setName("服务质量");
        cat3.setSortOrder(3);
        List<ScoreItemDTO> items3 = new ArrayList<>();
        items3.add(createItem(7L, "服务态度", "服务态度良好，患者满意度高", 10.0, 0.1, 1));
        items3.add(createItem(8L, "服务效率", "服务流程优化，效率高", 10.0, 0.1, 2));
        items3.add(createItem(9L, "投诉处理", "投诉处理及时，整改到位", 5.0, 0.05, 3));
        cat3.setItems(items3);
        categories.add(cat3);

        // 分类4：培训指导
        ScoreCategoryDTO cat4 = new ScoreCategoryDTO();
        cat4.setId(4L);
        cat4.setName("培训指导");
        cat4.setSortOrder(4);
        List<ScoreItemDTO> items4 = new ArrayList<>();
        items4.add(createItem(10L, "培训计划", "培训计划制定及执行情况", 5.0, 0.05, 1));
        items4.add(createItem(11L, "培训效果", "培训效果显著，考核合格率高", 5.0, 0.05, 2));
        cat4.setItems(items4);
        categories.add(cat4);

        dto.setCategories(categories);
        list.add(dto);

        return list;
    }

    private ScoreItemDTO createItem(Long id, String name, String desc, Double maxScore, Double weight, Integer sortOrder) {
        ScoreItemDTO item = new ScoreItemDTO();
        item.setId(id);
        item.setName(name);
        item.setDescription(desc);
        item.setMaxScore(maxScore);
        item.setWeight(weight);
        item.setSortOrder(sortOrder);
        item.setStatus(1);
        return item;
    }
}
