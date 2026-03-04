package com.zjmc.evaluation.config;

import com.zjmc.evaluation.entity.*;
import com.zjmc.evaluation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskInstitutionRepository taskInstitutionRepository;

    @Autowired
    private ScoreTemplateRepository scoreTemplateRepository;

    @Autowired
    private ScoreCategoryRepository scoreCategoryRepository;

    @Autowired
    private ScoreItemRepository scoreItemRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 检查是否已有数据
        if (institutionRepository.count() > 0) {
            System.out.println("数据已存在，跳过初始化");
            return;
        }

        System.out.println("开始初始化基础数据...");

        // 1. 初始化机构
        initInstitutions();

        // 2. 初始化评委
        initJudges();

        // 3. 初始化评分表模板
        initScoreTemplate();

        // 4. 初始化任务
        initTask();

        System.out.println("基础数据初始化完成！");
    }

    private void initInstitutions() {
        System.out.println("初始化机构数据...");

        // 专业质控中心 - 38个
        String[] qualityControlCenters = {
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

        for (String name : qualityControlCenters) {
            Institution institution = new Institution();
            institution.setName(name);
            institution.setType(Institution.InstitutionType.QUALITY_CONTROL);
            institution.setDescription("浙江省" + name);
            institution.setStatus(1);
            institutionRepository.save(institution);
        }

        // 技术指导中心 - 26个
        String[] technicalServiceCenters = {
            "省防盲指导中心", "省医院管理研究中心", "省核医学技术指导中心", "省器官移植技术指导中心",
            "省烧伤救治技术指导中心", "省中毒急救防治中心", "省临床营养中心", "省人工肝技术指导中心",
            "省病理、尸体解剖中心", "省皮肤病临床诊治技术指导中心", "省图书管理指导中心", "省细菌耐药监测中心",
            "省结直肠疾病诊疗中心", "省神经外科技术指导中心", "省骨科技术指导中心", "省口腔正畸中心",
            "分娩镇痛技术指导中心", "全科医学技术指导中心", "甲状腺病诊治技术指导中心", "老年病诊治指导中心",
            "日间手术技术指导中心", "口腔种植技术指导中心", "生殖微创技术指导中心", "角膜病诊治技术指导中心",
            "肿瘤靶向治疗技术指导中心", "胎儿心脏超声诊断技术指导中心"
        };

        for (String name : technicalServiceCenters) {
            Institution institution = new Institution();
            institution.setName(name);
            institution.setType(Institution.InstitutionType.TECHNICAL_SERVICE);
            institution.setDescription("浙江省" + name);
            institution.setStatus(1);
            institutionRepository.save(institution);
        }

        System.out.println("机构数据初始化完成，共 " + (qualityControlCenters.length + technicalServiceCenters.length) + " 个机构");
    }

    private void initJudges() {
        System.out.println("初始化评委数据...");

        // 获取所有质控中心（专家评委和大众评委都对应质控中心）
        List<Institution> qualityControlInstitutions = institutionRepository.findByTypeAndStatus(
            Institution.InstitutionType.QUALITY_CONTROL, 1);

        // 专家评委 - 每个质控中心一个（38个）
        // 注意:专家评委不关联机构,可以评审所有机构
        String[] expertSurnames = {"张", "李", "王", "赵", "陈", "刘", "杨", "黄", "周", "吴", 
                                   "郑", "孙", "钱", "冯", "褚", "卫", "蒋", "沈", "韩", "杨",
                                   "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹",
                                   "严", "华", "金", "魏", "陶", "姜", "戚", "谢"};
        String[] expertTitles = {"教授", "研究员", "院长", "主任", "专家", "教授", "研究员", "主任医师"};

        for (int i = 0; i < qualityControlInstitutions.size(); i++) {
            Institution institution = qualityControlInstitutions.get(i);
            Judge judge = new Judge();
            // 评委名称：姓+职称（如"张教授"、"李研究员"、"王院长"）
            String surname = expertSurnames[i % expertSurnames.length];
            String title = expertTitles[i % expertTitles.length];
            judge.setName(surname + title);
            judge.setUsername("expert" + (i + 1));
            judge.setPassword("123456");
            judge.setType(Judge.JudgeType.EXPERT);
            judge.setTitle(title);
            // 专家评委不关联机构
            judge.setInstitution(null);
            judge.setStatus(1);
            judgeRepository.save(judge);
        }

        // 大众评委 - 每个质控中心一个（38个）
        String[] publicSurnames = {"钱", "孙", "周", "郑", "王", "冯", "陈", "褚", "卫", "蒋",
                                   "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施",
                                   "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚",
                                   "谢", "邹", "喻", "柏", "水", "窦", "章", "云"};

        for (int i = 0; i < qualityControlInstitutions.size(); i++) {
            Institution institution = qualityControlInstitutions.get(i);
            Judge judge = new Judge();
            // 评委名称：机构名+代表（如"临床检验中心代表"）
            judge.setName(institution.getName() + "代表");
            judge.setUsername("public" + (i + 1));
            judge.setPassword("123456");
            judge.setType(Judge.JudgeType.PUBLIC);
            // 关联对应的质控中心
            judge.setInstitution(institution);
            judge.setStatus(1);
            judgeRepository.save(judge);
        }

        int totalJudges = qualityControlInstitutions.size() * 2;
        System.out.println("评委数据初始化完成，共 " + totalJudges + " 个评委（" + 
                          qualityControlInstitutions.size() + "个专家评委 + " + 
                          qualityControlInstitutions.size() + "个大众评委）");
    }

    private void initScoreTemplate() {
        System.out.println("初始化评分表模板...");

        // 创建模板
        ScoreTemplate template = new ScoreTemplate();
        template.setName("默认评分表（100分制）");
        template.setTemplateMaxScore(100.0);
        template.setSystemTotalScore(45.0);
        template.setIsDefault(1);
        template.setStatus(1);
        template = scoreTemplateRepository.save(template);

        // 创建分类和条目
        Long templateId = template.getId();

        // 分类1：基础管理
        ScoreCategory category1 = new ScoreCategory();
        category1.setTemplate(template);
        category1.setName("基础管理");
        category1.setSortOrder(1);
        category1 = scoreCategoryRepository.save(category1);

        createScoreItem(template, category1, "组织架构与人员配备情况", "机构设置完善，人员配备合理，岗位职责明确", 10.0, 0.10, 1);
        createScoreItem(template, category1, "规章制度建设与执行", "各项规章制度健全，执行到位，定期修订完善", 10.0, 0.10, 2);
        createScoreItem(template, category1, "档案资料管理规范性", "档案资料完整，分类清晰，管理规范，查阅便捷", 5.0, 0.05, 3);

        // 分类2：业务开展
        ScoreCategory category2 = new ScoreCategory();
        category2.setTemplate(template);
        category2.setName("业务开展");
        category2.setSortOrder(2);
        category2 = scoreCategoryRepository.save(category2);

        createScoreItem(template, category2, "年度业务指标完成情况", "完成年度业务指标，工作量饱满，覆盖面广", 15.0, 0.15, 1);
        createScoreItem(template, category2, "医疗质量控制与持续改进", "业务质量达标，质控措施有效，持续改进明显", 15.0, 0.15, 2);
        createScoreItem(template, category2, "新技术新项目推广应用", "积极开展新技术新项目，推广应用效果显著", 10.0, 0.10, 3);

        // 分类3：服务质量
        ScoreCategory category3 = new ScoreCategory();
        category3.setTemplate(template);
        category3.setName("服务质量");
        category3.setSortOrder(3);
        category3 = scoreCategoryRepository.save(category3);

        createScoreItem(template, category3, "服务态度与患者满意度", "服务态度良好，沟通有效，患者满意度高", 10.0, 0.10, 1);
        createScoreItem(template, category3, "服务流程优化与效率提升", "服务流程优化，等候时间短，工作效率高", 10.0, 0.10, 2);
        createScoreItem(template, category3, "投诉处理与持续改进机制", "投诉处理及时，原因分析到位，整改措施有效", 5.0, 0.05, 3);

        // 分类4：培训指导
        ScoreCategory category4 = new ScoreCategory();
        category4.setTemplate(template);
        category4.setName("培训指导");
        category4.setSortOrder(4);
        category4 = scoreCategoryRepository.save(category4);

        createScoreItem(template, category4, "年度培训计划制定与执行", "培训计划科学合理，执行到位，覆盖面广", 5.0, 0.05, 1);
        createScoreItem(template, category4, "培训效果评估与考核", "培训效果显著，考核合格率高，学员反馈良好", 5.0, 0.05, 2);

        System.out.println("评分表模板初始化完成");
    }

    private void createScoreItem(ScoreTemplate template, ScoreCategory category, String name, 
                                  String description, Double maxScore, Double weight, Integer sortOrder) {
        ScoreItem item = new ScoreItem();
        item.setTemplate(template);
        item.setCategory(category);
        item.setName(name);
        item.setDescription(description);
        item.setMaxScore(maxScore);
        item.setWeight(weight);
        item.setSortOrder(sortOrder);
        item.setStatus(1);
        scoreItemRepository.save(item);
    }

    private void initTask() {
        System.out.println("初始化任务数据...");

        // 创建2026Q1任务
        Task task = new Task();
        task.setName("2026年第一季度考核");
        task.setPeriod("2026Q1");
        task.setStartDate(LocalDate.of(2026, 1, 1));
        task.setEndDate(LocalDate.of(2026, 3, 31));
        task.setDescription("2026年第一季度机构考核评估任务");
        task.setIsCurrent(1);
        task.setStatus(1);
        task = taskRepository.save(task);

        // 关联所有机构到任务
        List<Institution> institutions = institutionRepository.findByStatus(1);
        for (Institution institution : institutions) {
            TaskInstitution ti = new TaskInstitution();
            ti.setTask(task);
            ti.setInstitution(institution);
            ti.setMaterialDescription("请查看线下派发的考核材料");
            ti.setStatus(0);
            taskInstitutionRepository.save(ti);
        }

        System.out.println("任务数据初始化完成，关联了 " + institutions.size() + " 个机构");
    }
}
