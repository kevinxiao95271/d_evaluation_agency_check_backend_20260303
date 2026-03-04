#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""测试评分提交记录API"""

import requests
import json

BASE_URL = "http://localhost:5031"

def print_section(title):
    print("\n" + "=" * 70)
    print(title)
    print("=" * 70)

def test_old_api():
    """测试旧的records接口(条目明细级别)"""
    print_section("1. 旧接口: GET /api/score/records (条目明细级别)")
    
    r = requests.get(f"{BASE_URL}/api/score/records?taskId=1")
    data = r.json()["data"]
    
    print(f"\n返回记录数: {len(data)}")
    print(f"数据级别: 评分条目明细 (item级别)")
    
    if data:
        record = data[0]
        print(f"\n示例记录结构:")
        print(f"  - ID: {record['id']}")
        print(f"  - 任务: {record['taskName']}")
        print(f"  - 机构: {record['institutionName']}")
        print(f"  - 评委: {record['judgeName']}")
        print(f"  - 评分条目: {record['itemName']}")
        print(f"  - 得分: {record['score']}/{record['maxScore']}")
        print(f"  - 评分时间: {record['createTime']}")
        
        # 统计提交次数
        submissions = set()
        for r in data:
            key = f"{r['taskId']}_{r['institutionId']}_{r['judgeId']}"
            submissions.add(key)
        
        print(f"\n统计:")
        print(f"  - 总条目数: {len(data)}")
        print(f"  - 实际提交次数: {len(submissions)}")
        print(f"  - 平均每次提交条目数: {len(data) / len(submissions):.1f}")

def test_new_api():
    """测试新的submissions接口(提交记录级别)"""
    print_section("2. 新接口: GET /api/score/submissions (提交记录级别)")
    
    r = requests.get(f"{BASE_URL}/api/score/submissions?taskId=1")
    data = r.json()["data"]
    
    print(f"\n返回记录数: {len(data)}")
    print(f"数据级别: 评分提交记录 (submission级别)")
    
    if data:
        submission = data[0]
        print(f"\n示例记录结构:")
        print(f"  - ID: {submission['id']}")
        print(f"  - 任务: {submission['taskName']}")
        print(f"  - 机构: {submission['institutionName']}")
        print(f"  - 评委: {submission['judgeName']} ({submission['judgeType']})")
        print(f"  - 评分模式: {submission['scoreMode']}")
        print(f"  - 总分: {submission['totalScore']}")
        print(f"  - 提交时间: {submission['submitTime']}")
        print(f"  - 评分条目数: {len(submission['itemScores'])}")
        
        print(f"\n  评分明细 (前3条):")
        for item in submission['itemScores'][:3]:
            print(f"    • {item['categoryName']} - {item['itemName']}: {item['score']}/{item['maxScore']}")
        
        print(f"\n统计:")
        print(f"  - 总提交次数: {len(data)}")
        total_items = sum(len(s['itemScores']) for s in data)
        print(f"  - 总条目数: {total_items}")
        print(f"  - 平均每次提交条目数: {total_items / len(data):.1f}")

def test_filters():
    """测试筛选功能"""
    print_section("3. 测试筛选功能")
    
    # 按评委筛选
    print("\n3.1 按评委筛选 (judgeId=1)")
    r = requests.get(f"{BASE_URL}/api/score/submissions?judgeId=1")
    data = r.json()["data"]
    print(f"  返回提交记录数: {len(data)}")
    
    if data:
        institutions = set(s['institutionName'] for s in data)
        print(f"  评分机构数: {len(institutions)}")
        print(f"  机构列表: {', '.join(list(institutions)[:3])}...")
    
    # 按机构筛选
    print("\n3.2 按机构筛选 (institutionId=1)")
    r = requests.get(f"{BASE_URL}/api/score/submissions?institutionId=1")
    data = r.json()["data"]
    print(f"  返回提交记录数: {len(data)}")
    
    if data:
        judges = set(s['judgeName'] for s in data)
        print(f"  评委数: {len(judges)}")
        print(f"  评委列表: {', '.join(list(judges))}")

def compare_apis():
    """对比两个接口"""
    print_section("4. 接口对比")
    
    # 获取两个接口的数据
    r1 = requests.get(f"{BASE_URL}/api/score/records?taskId=1")
    records = r1.json()["data"]
    
    r2 = requests.get(f"{BASE_URL}/api/score/submissions?taskId=1")
    submissions = r2.json()["data"]
    
    print(f"\n对比结果:")
    print(f"  旧接口 (/api/score/records):")
    print(f"    - 返回数量: {len(records)} 条")
    print(f"    - 数据级别: 评分条目明细")
    print(f"    - 适用场景: 查看详细的逐条评分")
    
    print(f"\n  新接口 (/api/score/submissions):")
    print(f"    - 返回数量: {len(submissions)} 条")
    print(f"    - 数据级别: 评分提交记录")
    print(f"    - 包含字段: scoreMode, totalScore, submitTime, itemScores[]")
    print(f"    - 适用场景: 查看评分提交历史,前端列表展示")
    
    # 验证数据一致性
    total_items_in_submissions = sum(len(s['itemScores']) for s in submissions)
    print(f"\n数据一致性验证:")
    print(f"  旧接口条目总数: {len(records)}")
    print(f"  新接口条目总数: {total_items_in_submissions}")
    print(f"  一致性: {'✅ 通过' if len(records) == total_items_in_submissions else '❌ 不一致'}")

def main():
    print("\n" + "=" * 70)
    print("评分提交记录API测试")
    print("=" * 70)
    
    try:
        test_old_api()
        test_new_api()
        test_filters()
        compare_apis()
        
        print("\n" + "=" * 70)
        print("✅ 测试完成")
        print("=" * 70)
        
        print("""
📝 总结:

1. 旧接口 GET /api/score/records
   - 返回评分条目明细(item级别)
   - 一次评分提交被拆成多条记录
   - 适合查看详细的逐条评分

2. 新接口 GET /api/score/submissions ⭐推荐
   - 返回评分提交记录(submission级别)
   - 包含: scoreMode, totalScore, submitTime
   - 评分明细作为子数组 itemScores 返回
   - 适合前端列表展示

3. 前端建议:
   - 列表页使用: /api/score/submissions
   - 详情页使用: /api/score/submissions (已包含明细)
   - 导出功能: /api/score/records (如需逐条导出)
""")
        
    except Exception as e:
        print(f"\n❌ 测试失败: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
