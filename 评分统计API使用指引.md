# 评分统计API使用指引

## ⚠️ 重要说明

以下三个接口的查询参数是**必需的**,不能省略,否则会返回 HTTP 400 错误。

---

## 1. 获取任务评分统计

### 接口信息
- **路径**: `GET /api/score/statistics`
- **说明**: 获取指定任务的总体评分统计信息

### 必需参数
| 参数名 | 类型 | 必需 | 说明 |
|--------|------|------|------|
| taskId | Long | ✅ 是 | 任务ID |

### 正确调用示例
```javascript
// ✅ 正确 - 带taskId参数
GET /api/score/statistics?taskId=1

// 前端调用示例
fetch('/api/score/statistics?taskId=1')
  .then(res => res.json())
  .then(data => console.log(data));

// axios示例
axios.get('/api/score/statistics', {
  params: { taskId: 1 }
})
```

### ❌ 错误调用示例
```javascript
// ❌ 错误 - 缺少taskId参数,会返回400
GET /api/score/statistics
```

### 返回数据结构
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalScoreCount": 495,
    "expertJudgeCount": 5,
    "publicJudgeCount": 0,
    "completedInstitutionCount": 10,
    "categoryAverages": {
      "服务质量": 7.48,
      "基础管理": 7.45,
      "业务开展": 11.95,
      "培训指导": 4.49
    },
    "topRankings": [
      {
        "rank": 1,
        "institutionName": "医院药事管理质控中心",
        "finalScore": 63.49
      }
    ]
  }
}
```

---

## 2. 获取评委评分统计

### 接口信息
- **路径**: `GET /api/score/judge-stats`
- **说明**: 获取指定评委的评分进度和统计

### 必需参数
| 参数名 | 类型 | 必需 | 说明 |
|--------|------|------|------|
| judgeId | Long | ✅ 是 | 评委ID |

### 正确调用示例
```javascript
// ✅ 正确 - 带judgeId参数
GET /api/score/judge-stats?judgeId=1

// 前端调用示例
fetch('/api/score/judge-stats?judgeId=1')
  .then(res => res.json())
  .then(data => console.log(data));

// axios示例
axios.get('/api/score/judge-stats', {
  params: { judgeId: 1 }
})
```

### ❌ 错误调用示例
```javascript
// ❌ 错误 - 缺少judgeId参数,会返回400
GET /api/score/judge-stats
```

### 返回数据结构
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalScoreCount": 99,
    "judgeProgress": {
      "scoredInstitutions": 9
    }
  }
}
```

---

## 3. 获取机构评分统计

### 接口信息
- **路径**: `GET /api/score/institution-stats`
- **说明**: 获取指定机构的得分统计

### 必需参数
| 参数名 | 类型 | 必需 | 说明 |
|--------|------|------|------|
| institutionId | Long | ✅ 是 | 机构ID |

### 正确调用示例
```javascript
// ✅ 正确 - 带institutionId参数
GET /api/score/institution-stats?institutionId=1

// 前端调用示例
fetch('/api/score/institution-stats?institutionId=1')
  .then(res => res.json())
  .then(data => console.log(data));

// axios示例
axios.get('/api/score/institution-stats', {
  params: { institutionId: 1 }
})
```

### ❌ 错误调用示例
```javascript
// ❌ 错误 - 缺少institutionId参数,会返回400
GET /api/score/institution-stats
```

### 返回数据结构
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalScoreCount": 44,
    "categoryAverages": {
      "服务质量": 7.50,
      "基础管理": 7.57,
      "业务开展": 11.57,
      "培训指导": 4.71
    }
  }
}
```

---

## 📋 完整的评分API列表

### 查询类接口

| 接口 | 必需参数 | 可选参数 | 说明 |
|------|----------|----------|------|
| `GET /api/score/records` | 无 | taskId, institutionId, judgeId | 获取评分记录列表 |
| `GET /api/score/record/{id}` | id(路径参数) | 无 | 获取单条评分详情 |
| `GET /api/score/statistics` | taskId | 无 | 获取任务评分统计 |
| `GET /api/score/judge-stats` | judgeId | 无 | 获取评委评分统计 |
| `GET /api/score/institution-stats` | institutionId | 无 | 获取机构评分统计 |
| `GET /api/score/result` | taskId, institutionId | 无 | 计算单个机构得分 |
| `GET /api/score/results/{taskId}` | taskId(路径参数) | 无 | 计算所有机构得分 |
| `GET /api/score/export` | taskId | 无 | 导出评分记录(CSV) |

### 操作类接口

| 接口 | 方法 | 必需参数 | 说明 |
|------|------|----------|------|
| `/api/score/submit` | POST | Body(JSON) | 提交评分 |
| `/api/score/record/{id}` | DELETE | id(路径参数) | 删除评分记录 |
| `/api/score/records/batch-delete` | POST | Body(ID数组) | 批量删除评分记录 |

---

## 🔧 前端集成建议

### 1. 创建API工具函数

```javascript
// api/score.js
import axios from 'axios';

const scoreAPI = {
  // 获取任务评分统计 - 必需taskId
  getTaskStatistics(taskId) {
    if (!taskId) {
      throw new Error('taskId is required');
    }
    return axios.get('/api/score/statistics', {
      params: { taskId }
    });
  },

  // 获取评委评分统计 - 必需judgeId
  getJudgeStatistics(judgeId) {
    if (!judgeId) {
      throw new Error('judgeId is required');
    }
    return axios.get('/api/score/judge-stats', {
      params: { judgeId }
    });
  },

  // 获取机构评分统计 - 必需institutionId
  getInstitutionStatistics(institutionId) {
    if (!institutionId) {
      throw new Error('institutionId is required');
    }
    return axios.get('/api/score/institution-stats', {
      params: { institutionId }
    });
  },

  // 获取评分记录列表 - 参数可选
  getScoreRecords(filters = {}) {
    return axios.get('/api/score/records', {
      params: filters // { taskId, institutionId, judgeId }
    });
  }
};

export default scoreAPI;
```

### 2. 使用示例

```javascript
// 在组件中使用
import scoreAPI from '@/api/score';

// 获取任务统计
async function loadTaskStats(taskId) {
  try {
    const { data } = await scoreAPI.getTaskStatistics(taskId);
    console.log('任务统计:', data.data);
  } catch (error) {
    console.error('获取失败:', error);
  }
}

// 获取评委统计
async function loadJudgeStats(judgeId) {
  try {
    const { data } = await scoreAPI.getJudgeStatistics(judgeId);
    console.log('评委统计:', data.data);
  } catch (error) {
    console.error('获取失败:', error);
  }
}

// 获取机构统计
async function loadInstitutionStats(institutionId) {
  try {
    const { data } = await scoreAPI.getInstitutionStatistics(institutionId);
    console.log('机构统计:', data.data);
  } catch (error) {
    console.error('获取失败:', error);
  }
}
```

---

## ⚠️ 常见错误及解决方案

### 错误1: HTTP 400 - Bad Request
**原因**: 缺少必需的查询参数

**解决方案**:
```javascript
// ❌ 错误
fetch('/api/score/statistics')

// ✅ 正确
fetch('/api/score/statistics?taskId=1')
```

### 错误2: 参数类型错误
**原因**: 参数应该是数字类型,但传了字符串或其他类型

**解决方案**:
```javascript
// ❌ 错误
const taskId = "abc"; // 非数字

// ✅ 正确
const taskId = 1; // 数字
const taskId = parseInt(route.params.id); // 从路由参数转换
```

### 错误3: 参数为null或undefined
**原因**: 参数值未正确获取

**解决方案**:
```javascript
// 添加参数验证
function getTaskStats(taskId) {
  if (!taskId || taskId <= 0) {
    console.error('Invalid taskId:', taskId);
    return;
  }
  
  return axios.get('/api/score/statistics', {
    params: { taskId }
  });
}
```

---

## 📝 测试命令

### 使用curl测试

```bash
# 测试任务统计
curl "http://localhost:5031/api/score/statistics?taskId=1"

# 测试评委统计
curl "http://localhost:5031/api/score/judge-stats?judgeId=1"

# 测试机构统计
curl "http://localhost:5031/api/score/institution-stats?institutionId=1"
```

### 使用Python测试

```python
import requests

# 测试任务统计
response = requests.get('http://localhost:5031/api/score/statistics', 
                       params={'taskId': 1})
print(response.json())

# 测试评委统计
response = requests.get('http://localhost:5031/api/score/judge-stats', 
                       params={'judgeId': 1})
print(response.json())

# 测试机构统计
response = requests.get('http://localhost:5031/api/score/institution-stats', 
                       params={'institutionId': 1})
print(response.json())
```

---

## 💡 总结

1. **这三个统计接口的查询参数都是必需的**,不能省略
2. **参数必须通过查询字符串传递**: `?taskId=1` 或 `?judgeId=1` 或 `?institutionId=1`
3. **参数类型必须是Long(数字)**,不能是字符串或其他类型
4. **前端调用时务必确保参数存在且有效**,建议添加参数验证
5. **使用axios时推荐使用params选项**,会自动处理URL编码

如有其他问题,请参考Swagger文档: `http://localhost:5031/swagger-ui.html`
