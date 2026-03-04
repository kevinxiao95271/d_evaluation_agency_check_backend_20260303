#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""测试统计API - 验证必需参数"""

import requests

BASE_URL = "http://localhost:5031"

def test_with_params():
    """测试带参数的正确调用"""
    print("=" * 60)
    print("✅ 正确调用 - 带必需参数")
    print("=" * 60)
    
    # 1. 任务统计 - 带taskId
    print("\n1. GET /api/score/statistics?taskId=1")
    try:
        r = requests.get(f"{BASE_URL}/api/score/statistics", params={"taskId": 1})
        print(f"   状态码: {r.status_code}")
        if r.status_code == 200:
            data = r.json()["data"]
            print(f"   ✅ 成功")
            print(f"   - 总评分数: {data.get('totalScoreCount', 0)}")
            print(f"   - 专家评委数: {data.get('expertJudgeCount', 0)}")
            print(f"   - 已完成机构数: {data.get('completedInstitutionCount', 0)}")
        else:
            print(f"   ❌ 失败: {r.text}")
    except Exception as e:
        print(f"   ❌ 异常: {e}")
    
    # 2. 评委统计 - 带judgeId
    print("\n2. GET /api/score/judge-stats?judgeId=1")
    try:
        r = requests.get(f"{BASE_URL}/api/score/judge-stats", params={"judgeId": 1})
        print(f"   状态码: {r.status_code}")
        if r.status_code == 200:
            data = r.json()["data"]
            print(f"   ✅ 成功")
            print(f"   - 评分记录数: {data.get('totalScoreCount', 0)}")
            if data.get('judgeProgress'):
                print(f"   - 已评分机构数: {data['judgeProgress'].get('scoredInstitutions', 0)}")
        else:
            print(f"   ❌ 失败: {r.text}")
    except Exception as e:
        print(f"   ❌ 异常: {e}")
    
    # 3. 机构统计 - 带institutionId
    print("\n3. GET /api/score/institution-stats?institutionId=1")
    try:
        r = requests.get(f"{BASE_URL}/api/score/institution-stats", params={"institutionId": 1})
        print(f"   状态码: {r.status_code}")
        if r.status_code == 200:
            data = r.json()["data"]
            print(f"   ✅ 成功")
            print(f"   - 收到评分数: {data.get('totalScoreCount', 0)}")
            if data.get('categoryAverages'):
                print(f"   - 分类数: {len(data['categoryAverages'])}")
        else:
            print(f"   ❌ 失败: {r.text}")
    except Exception as e:
        print(f"   ❌ 异常: {e}")

def test_without_params():
    """测试不带参数的错误调用"""
    print("\n" + "=" * 60)
    print("❌ 错误调用 - 缺少必需参数")
    print("=" * 60)
    
    # 1. 任务统计 - 不带taskId
    print("\n1. GET /api/score/statistics (缺少taskId)")
    try:
        r = requests.get(f"{BASE_URL}/api/score/statistics")
        print(f"   状态码: {r.status_code}")
        if r.status_code == 400:
            print(f"   ❌ 预期的400错误 - 缺少必需参数taskId")
        else:
            print(f"   响应: {r.text[:200]}")
    except Exception as e:
        print(f"   异常: {e}")
    
    # 2. 评委统计 - 不带judgeId
    print("\n2. GET /api/score/judge-stats (缺少judgeId)")
    try:
        r = requests.get(f"{BASE_URL}/api/score/judge-stats")
        print(f"   状态码: {r.status_code}")
        if r.status_code == 400:
            print(f"   ❌ 预期的400错误 - 缺少必需参数judgeId")
        else:
            print(f"   响应: {r.text[:200]}")
    except Exception as e:
        print(f"   异常: {e}")
    
    # 3. 机构统计 - 不带institutionId
    print("\n3. GET /api/score/institution-stats (缺少institutionId)")
    try:
        r = requests.get(f"{BASE_URL}/api/score/institution-stats")
        print(f"   状态码: {r.status_code}")
        if r.status_code == 400:
            print(f"   ❌ 预期的400错误 - 缺少必需参数institutionId")
        else:
            print(f"   响应: {r.text[:200]}")
    except Exception as e:
        print(f"   异常: {e}")

def main():
    print("\n" + "=" * 60)
    print("评分统计API参数测试")
    print("=" * 60)
    
    # 测试正确调用
    test_with_params()
    
    # 测试错误调用
    test_without_params()
    
    print("\n" + "=" * 60)
    print("📝 总结")
    print("=" * 60)
    print("""
✅ 正确用法:
   - GET /api/score/statistics?taskId=1
   - GET /api/score/judge-stats?judgeId=1
   - GET /api/score/institution-stats?institutionId=1

❌ 错误用法:
   - GET /api/score/statistics (缺少taskId,返回400)
   - GET /api/score/judge-stats (缺少judgeId,返回400)
   - GET /api/score/institution-stats (缺少institutionId,返回400)

💡 前端调用建议:
   // 使用axios
   axios.get('/api/score/statistics', { params: { taskId: 1 } })
   axios.get('/api/score/judge-stats', { params: { judgeId: 1 } })
   axios.get('/api/score/institution-stats', { params: { institutionId: 1 } })
   
   // 使用fetch
   fetch('/api/score/statistics?taskId=1')
   fetch('/api/score/judge-stats?judgeId=1')
   fetch('/api/score/institution-stats?institutionId=1')
""")

if __name__ == "__main__":
    main()
