// ============================================
// 评分统计API前端调用示例
// ============================================

// ============================================
// 方式1: 使用axios (推荐)
// ============================================

import axios from 'axios';

// 1. 获取任务评分统计 - 必需参数: taskId
export function getTaskStatistics(taskId) {
  return axios.get('/api/score/statistics', {
    params: { taskId }  // ✅ 正确: 使用params选项
  });
}

// 2. 获取评委评分统计 - 必需参数: judgeId
export function getJudgeStatistics(judgeId) {
  return axios.get('/api/score/judge-stats', {
    params: { judgeId }  // ✅ 正确: 使用params选项
  });
}

// 3. 获取机构评分统计 - 必需参数: institutionId
export function getInstitutionStatistics(institutionId) {
  return axios.get('/api/score/institution-stats', {
    params: { institutionId }  // ✅ 正确: 使用params选项
  });
}

// 使用示例
async function example1() {
  try {
    // 获取任务1的统计
    const { data } = await getTaskStatistics(1);
    console.log('任务统计:', data.data);
    
    // 获取评委1的统计
    const judgeStats = await getJudgeStatistics(1);
    console.log('评委统计:', judgeStats.data.data);
    
    // 获取机构1的统计
    const instStats = await getInstitutionStatistics(1);
    console.log('机构统计:', instStats.data.data);
  } catch (error) {
    console.error('请求失败:', error);
  }
}

// ============================================
// 方式2: 使用fetch
// ============================================

// 1. 获取任务评分统计
export async function getTaskStatisticsFetch(taskId) {
  const response = await fetch(`/api/score/statistics?taskId=${taskId}`);
  return response.json();
}

// 2. 获取评委评分统计
export async function getJudgeStatisticsFetch(judgeId) {
  const response = await fetch(`/api/score/judge-stats?judgeId=${judgeId}`);
  return response.json();
}

// 3. 获取机构评分统计
export async function getInstitutionStatisticsFetch(institutionId) {
  const response = await fetch(`/api/score/institution-stats?institutionId=${institutionId}`);
  return response.json();
}

// 使用示例
async function example2() {
  try {
    const taskStats = await getTaskStatisticsFetch(1);
    console.log('任务统计:', taskStats.data);
  } catch (error) {
    console.error('请求失败:', error);
  }
}

// ============================================
// 方式3: Vue组件中使用
// ============================================

export default {
  data() {
    return {
      taskId: 1,
      taskStats: null,
      judgeStats: null,
      institutionStats: null
    };
  },
  
  methods: {
    // 加载任务统计
    async loadTaskStats() {
      try {
        const { data } = await this.$axios.get('/api/score/statistics', {
          params: { taskId: this.taskId }  // ✅ 必需参数
        });
        this.taskStats = data.data;
      } catch (error) {
        this.$message.error('加载失败');
      }
    },
    
    // 加载评委统计
    async loadJudgeStats(judgeId) {
      try {
        const { data } = await this.$axios.get('/api/score/judge-stats', {
          params: { judgeId }  // ✅ 必需参数
        });
        this.judgeStats = data.data;
      } catch (error) {
        this.$message.error('加载失败');
      }
    },
    
    // 加载机构统计
    async loadInstitutionStats(institutionId) {
      try {
        const { data } = await this.$axios.get('/api/score/institution-stats', {
          params: { institutionId }  // ✅ 必需参数
        });
        this.institutionStats = data.data;
      } catch (error) {
        this.$message.error('加载失败');
      }
    }
  },
  
  mounted() {
    // 组件挂载时加载数据
    this.loadTaskStats();
  }
};

// ============================================
// 方式4: React组件中使用
// ============================================

import { useState, useEffect } from 'react';
import axios from 'axios';

function ScoreStatistics({ taskId, judgeId, institutionId }) {
  const [taskStats, setTaskStats] = useState(null);
  const [judgeStats, setJudgeStats] = useState(null);
  const [institutionStats, setInstitutionStats] = useState(null);
  
  useEffect(() => {
    // 加载任务统计
    if (taskId) {
      axios.get('/api/score/statistics', {
        params: { taskId }  // ✅ 必需参数
      }).then(res => {
        setTaskStats(res.data.data);
      }).catch(err => {
        console.error('加载失败:', err);
      });
    }
  }, [taskId]);
  
  useEffect(() => {
    // 加载评委统计
    if (judgeId) {
      axios.get('/api/score/judge-stats', {
        params: { judgeId }  // ✅ 必需参数
      }).then(res => {
        setJudgeStats(res.data.data);
      });
    }
  }, [judgeId]);
  
  useEffect(() => {
    // 加载机构统计
    if (institutionId) {
      axios.get('/api/score/institution-stats', {
        params: { institutionId }  // ✅ 必需参数
      }).then(res => {
        setInstitutionStats(res.data.data);
      });
    }
  }, [institutionId]);
  
  return (
    <div>
      {taskStats && <div>总评分数: {taskStats.totalScoreCount}</div>}
      {judgeStats && <div>评分记录: {judgeStats.totalScoreCount}</div>}
      {institutionStats && <div>收到评分: {institutionStats.totalScoreCount}</div>}
    </div>
  );
}

// ============================================
// ❌ 常见错误示例 - 请避免
// ============================================

// ❌ 错误1: 不带参数调用
async function wrongExample1() {
  // 这会返回400错误
  const response = await axios.get('/api/score/statistics');
}

// ❌ 错误2: 参数拼接在URL中但使用了错误的方式
async function wrongExample2() {
  // 不推荐: 手动拼接URL容易出错
  const taskId = 1;
  const response = await axios.get('/api/score/statistics?taskId=' + taskId);
}

// ❌ 错误3: 参数为null或undefined
async function wrongExample3() {
  const taskId = null;  // 或 undefined
  // 这会导致参数缺失,返回400
  const response = await axios.get('/api/score/statistics', {
    params: { taskId }
  });
}

// ============================================
// ✅ 推荐做法: 添加参数验证
// ============================================

export async function getTaskStatisticsSafe(taskId) {
  // 参数验证
  if (!taskId || taskId <= 0) {
    throw new Error('taskId is required and must be positive');
  }
  
  return axios.get('/api/score/statistics', {
    params: { taskId }
  });
}

export async function getJudgeStatisticsSafe(judgeId) {
  // 参数验证
  if (!judgeId || judgeId <= 0) {
    throw new Error('judgeId is required and must be positive');
  }
  
  return axios.get('/api/score/judge-stats', {
    params: { judgeId }
  });
}

export async function getInstitutionStatisticsSafe(institutionId) {
  // 参数验证
  if (!institutionId || institutionId <= 0) {
    throw new Error('institutionId is required and must be positive');
  }
  
  return axios.get('/api/score/institution-stats', {
    params: { institutionId }
  });
}

// ============================================
// 📝 快速参考
// ============================================

/*
接口1: GET /api/score/statistics
必需参数: taskId (Long)
示例: /api/score/statistics?taskId=1

接口2: GET /api/score/judge-stats
必需参数: judgeId (Long)
示例: /api/score/judge-stats?judgeId=1

接口3: GET /api/score/institution-stats
必需参数: institutionId (Long)
示例: /api/score/institution-stats?institutionId=1

⚠️ 注意:
1. 这三个接口的参数都是必需的,不能省略
2. 参数必须是数字类型(Long)
3. 缺少参数会返回HTTP 400错误
4. 推荐使用axios的params选项传递参数
5. 建议添加参数验证,确保参数有效
*/
