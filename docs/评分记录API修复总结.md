# 评分记录API修复总结

## 🔴 问题描述

### 1. 数据结构问题
- **旧接口** `GET /api/score/records` 返回495条记录,都是评分条目明细(item级别)
- **缺少字段**: scoreMode、totalScore、submitTime
- **数据拆分**: 一次评分提交被拆成11条记录(每个评分条目一条)
- **前端需求**: 约45条评分记录汇总(submission级别),每条包含总分、评分模式、提交时间,逐条评分明细作为子数组返回

### 2. 测试数据问题
- 专家评委有机构信息(应该没有)
- 专家评委受同机构回避限制(不应该)
- 缺少大众评委的评分记录

---

## ✅ 解决方案

### 1. 新增评分提交记录DTO

创建 `ScoreSubmissionDTO.java`:
```java
@Data
public class ScoreSubmissionDTO {
    private Long id;                    // 提交ID
    private Long taskId;                // 任务ID
    private String taskName;            // 任务名称
    private Long institutionId;         // 机构ID
    private String institutionName;     // 机构名称
    private Long judgeId;               // 评委ID
    private String judgeName;           // 评委姓名
    private String judgeType;           // 评委类型
    private String scoreMode;           // 评分模式: ITEM/TOTAL
    private Double totalScore;          // 总分
    private LocalDateTime submitTime;   // 提交时间
    private List<ItemScoreDetail> itemScores;  // 评分条目明细
}
```

### 2. 新增API端点

**新接口**: `GET /api/score/submissions`
- 返回评分提交记录(submission级别)
- 支持按任务、机构、评委筛选
- 包含完整的评分明细作为子数组

**旧接口保留**: `GET /api/score/records`
- 继续返回评分条目明细(item级别)
- 用于需要逐条查看评分的场景

### 3. 修复评委数据初始化

修改 `DataInitializer.java`:
```java
// 专家评委不关联机构
judge.setInstitution(null);  // ✅ 修改后

// 大众评委关联机构
judge.setInstitution(institution);  // ✅ 保持不变
```

### 4. 修复测试数据生成

修改 `create_test_data.py`:
- 专家评委评审所有机构(不受同机构回避限制)
- 大众评委受同机构回避限制
- 同时生成专家和大众评委的评分记录

---

## 📊 测试结果

### 数据统计
- **总评分记录**: 1045条
  - 专家评委记录: 550条 (5个专家 × 10个机构 × 11个条目)
  - 大众评委记录: 495条 (平均4-5个大众评委 × 10个机构 × 11个条目)
- **评分提交次数**: 95次
  - 专家评委提交: 50次 (5个专家 × 10个机构)
  - 大众评委提交: 45次 (平均4-5个大众评委 × 10个机构)

### API对比

| 特性 | 旧接口 /api/score/records | 新接口 /api/score/submissions |
|------|---------------------------|-------------------------------|
| 返回数量 | 1045条 | 95条 |
| 数据级别 | 评分条目明细(item) | 评分提交记录(submission) |
| scoreMode | ❌ 无 | ✅ 有 |
| totalScore | ❌ 无 | ✅ 有 |
| submitTime | ❌ 无 | ✅ 有 |
| itemScores | ❌ 无 | ✅ 有(子数组) |
| 适用场景 | 逐条查看评分明细 | 前端列表展示 |

### 数据一致性验证
- 旧接口条目总数: 1045
- 新接口条目总数: 1045 (95次提交 × 11个条目)
- ✅ 数据一致性通过

---

## 🎯 前端使用建议

### 1. 列表页 - 使用新接口
```javascript
// ✅ 推荐: 使用submissions接口
axios.get('/api/score/submissions', {
  params: { taskId: 1 }
}).then(res => {
  const submissions = res.data.data;
  // 每条记录包含:
  // - scoreMode: 评分模式
  // - totalScore: 总分
  // - submitTime: 提交时间
  // - itemScores: 评分明细数组
});
```

### 2. 详情页 - 使用新接口
```javascript
// ✅ 推荐: submissions已包含明细
axios.get('/api/score/submissions', {
  params: { 
    taskId: 1,
    institutionId: 1,
    judgeId: 1
  }
}).then(res => {
  const submission = res.data.data[0];
  // submission.itemScores 包含所有评分明细
});
```

### 3. 导出功能 - 可选使用旧接口
```javascript
// 如需逐条导出,可使用records接口
axios.get('/api/score/export', {
  params: { taskId: 1 }
});
```

---

## 📝 API完整列表

### 评分记录查询

| API | 说明 | 返回级别 |
|-----|------|----------|
| `GET /api/score/records` | 获取评分条目明细 | item级别 |
| `GET /api/score/submissions` | 获取评分提交记录 ⭐推荐 | submission级别 |
| `GET /api/score/record/{id}` | 获取单条评分详情 | item级别 |

### 筛选参数(可选)
- `taskId`: 按任务筛选
- `institutionId`: 按机构筛选
- `judgeId`: 按评委筛选

### 示例
```bash
# 获取任务1的所有评分提交记录
GET /api/score/submissions?taskId=1

# 获取评委1对机构1的评分
GET /api/score/submissions?judgeId=1&institutionId=1

# 获取机构1收到的所有评分
GET /api/score/submissions?institutionId=1
```

---

## 🔧 评委规则说明

### 专家评委
- ✅ 无机构信息(`institutionId = null`)
- ✅ 可以评审所有机构
- ✅ 不受同机构回避限制
- ✅ 权重: 70%

### 大众评委
- ✅ 有机构信息(`institutionId != null`)
- ✅ 受同机构回避限制(不能评审自己的机构)
- ✅ 权重: 20%

---

## ✨ 总结

1. ✅ 新增 `GET /api/score/submissions` 接口,返回submission级别数据
2. ✅ 包含 scoreMode、totalScore、submitTime 字段
3. ✅ 评分明细作为 itemScores 子数组返回
4. ✅ 修复专家评委机构信息问题
5. ✅ 修复同机构回避逻辑
6. ✅ 添加大众评委评分记录
7. ✅ 数据一致性验证通过
8. ✅ 前端可直接使用新接口

**推荐前端使用**: `GET /api/score/submissions` 作为主要接口,数据结构更符合业务需求!
