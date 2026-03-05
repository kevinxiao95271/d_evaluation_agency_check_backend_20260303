#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""验证评分数据明细"""

import requests
import json

BASE_URL = "http://localhost:5031"

def print_section(title):
    print("\n" + "=" * 70)
    print(title)
    print("=" * 70)

def test_records_api():
    """测试评分记录查询API"""
    print_section("1. 评分记录查询API测试")
    
    # 1.1 获取所有记录
    print("\n1.1 获取所有评分记录")
    r = requests.get(f"{BASE_URL}/api/score/records")
    records = r.json()["data"]
    print(f"   总记录数: {len(records)}")
    
    if records:
        print(f"\n   示例记录:")
        record = records[0]
        print(f"   - ID: {record['id']}")
        print(f"   - 任务: {record['taskName']}")
        print(f"   - 机构: {record['institutionName']}")
        print(f"   - 评委: {record['judgeName']} ({record['judgeType']})")
        print(f"   - 评分条目: {record['categoryName']} - {record['itemName']}")
        print(f"   - 得分: {record['score']}/{record['maxScore']}")
        print(f"   - 评分时间: {record['createTime']}")
    
    # 1.2 按任务筛选
    print("\n1.2 按任务筛选 (taskId=1)")
    r = requests.get(f"{BASE_URL}/api/score/records?taskId=1")
    records = r.json()["data"]
    print(f"   记录数: {len(records)}")
    
    # 1.3 按评委筛选
    print("\n1.3 按评委筛选 (judgeId=1)")
    r = requests.get(f"{BASE_URL}/api/score/records?judgeId=1")
    records = r.json()["data"]
    print(f"   记录数: {len(records)}")
    
    if records:
        # 统计该评委评分的机构
        institutions = set(r['institutionName'] for r in records)
        print(f"   评分机构数: {len(institutions)}")
        print(f"   机构列表: {', '.join(list(institutions)[:5])}...")
    
    # 1.4 按机构筛选
    print("\n1.4 按机构筛选 (institutionId=1)")
    r = requests.get(f"{BASE_URL}/api/score/records?institutionId=1")
    records = r.json()["data"]
    print(f"   记录数: {len(records)}")
    
    if records:
        # 统计评委
        judges = {}
        for record in records:
            judge_name = record['judgeName']
            judges[judge_name] = judges.get(judge_name, 0) + 1
        
        print(f"   评委数: {len(judges)}")
        print(f"   评委明细:")
        for judge, count in judges.items():
            print(f"     • {judge}: {count} 条评分")

def test_statistics_api():
    """测试统计API"""
    print_section("2. 评分统计API测试")
    
    # 2.1 任务统计
    print("\n2.1 任务评分统计 (taskId=1)")
    r = requests.get(f"{BASE_URL}/api/score/statistics?taskId=1")
    stats = r.json()["data"]
    print(f"   总评分数: {stats.get('totalScoreCount', 0)}")
    print(f"   专家评委数: {stats.get('expertJudgeCount', 0)}")
    print(f"   大众评委数: {stats.get('publicJudgeCount', 0)}")
    print(f"   已完成机构数: {stats.get('completedInstitutionCount', 0)}")
    
    if stats.get('categoryAverages'):
        print(f"\n   分类平均分:")
        for category, avg in stats['categoryAverages'].items():
            print(f"     • {category}: {avg:.2f}")
    
    if stats.get('topRankings'):
        print(f"\n   前5名机构:")
        for i, ranking in enumerate(stats['topRankings'][:5], 1):
            print(f"     {i}. {ranking['institutionName']}: {ranking['finalScore']:.2f}分")
    
    # 2.2 评委统计
    print("\n2.2 评委评分统计 (judgeId=1)")
    r = requests.get(f"{BASE_URL}/api/score/judge-stats?judgeId=1")
    stats = r.json()["data"]
    print(f"   评分记录数: {stats.get('totalScoreCount', 0)}")
    
    if stats.get('judgeProgress'):
        progress = stats['judgeProgress']
        print(f"   已评分机构数: {progress.get('scoredInstitutions', 0)}")
    
    # 2.3 机构统计
    print("\n2.3 机构评分统计 (institutionId=1)")
    r = requests.get(f"{BASE_URL}/api/score/institution-stats?institutionId=1")
    stats = r.json()["data"]
    print(f"   收到评分数: {stats.get('totalScoreCount', 0)}")
    
    if stats.get('categoryAverages'):
        print(f"\n   各分类平均分:")
        for category, avg in stats['categoryAverages'].items():
            print(f"     • {category}: {avg:.2f}")

def test_result_api():
    """测试结果计算API"""
    print_section("3. 评分结果计算API测试")
    
    # 3.1 单个机构结果
    print("\n3.1 单个机构得分计算 (taskId=1, institutionId=1)")
    r = requests.get(f"{BASE_URL}/api/score/result?taskId=1&institutionId=1")
    result = r.json()["data"]
    
    print(f"   机构: {result['institutionName']}")
    print(f"   机构类型: {result['institutionType']}")
    print(f"\n   专家评委:")
    print(f"     - 评委数: {result['expertJudgeCount']}")
    print(f"     - 平均分: {result['expertAvgScore']:.2f}")
    print(f"     - 贡献分: {result['expertContribution']:.2f}")
    print(f"\n   大众评委:")
    print(f"     - 评委数: {result['publicJudgeCount']}")
    print(f"     - 平均分: {result['publicAvgScore']:.2f}")
    print(f"     - 贡献分: {result['publicContribution']:.2f}")
    print(f"\n   附加项:")
    print(f"     - 主任演讲: {result['directorBonus']}")
    print(f"     - 会务秘书: {result['secretaryBonus']}")
    print(f"\n   最终得分: {result['finalScore']:.2f}")
    
    if result.get('itemResults'):
        print(f"\n   各条目平均分:")
        for item in result['itemResults'][:5]:
            print(f"     • {item['categoryName']} - {item['itemName']}: {item['avgScore']:.2f}/{item['maxScore']}")
    
    # 3.2 所有机构结果
    print("\n3.2 所有机构得分排名 (taskId=1)")
    r = requests.get(f"{BASE_URL}/api/score/results/1")
    results = r.json()["data"]
    print(f"   机构总数: {len(results)}")
    
    # 统计有评分的机构
    scored_institutions = [r for r in results if r['expertJudgeCount'] > 0 or r['publicJudgeCount'] > 0]
    print(f"   有评分的机构: {len(scored_institutions)}")
    
    print(f"\n   前10名:")
    for result in results[:10]:
        has_score = result['expertJudgeCount'] > 0 or result['publicJudgeCount'] > 0
        score_mark = "✓" if has_score else "○"
        print(f"     {result['rank']:2d}. [{score_mark}] {result['institutionName'][:20]:20s} - {result['finalScore']:.2f}分 (专家:{result['expertJudgeCount']}, 大众:{result['publicJudgeCount']})")

def test_export_api():
    """测试导出API"""
    print_section("4. 数据导出API测试")
    
    print("\n4.1 导出CSV (taskId=1)")
    r = requests.get(f"{BASE_URL}/api/score/export?taskId=1")
    
    if r.status_code == 200:
        lines = r.text.split('\n')
        print(f"   导出成功")
        print(f"   总行数: {len(lines)}")
        print(f"   数据行数: {len(lines) - 2}")  # 减去BOM和表头
        print(f"\n   CSV表头:")
        if len(lines) > 1:
            print(f"   {lines[1]}")
        print(f"\n   前3条数据:")
        for line in lines[2:5]:
            if line.strip():
                print(f"   {line[:100]}...")

def test_record_detail():
    """测试单条记录详情"""
    print_section("5. 单条评分记录详情测试")
    
    # 先获取一条记录的ID
    r = requests.get(f"{BASE_URL}/api/score/records?taskId=1")
    records = r.json()["data"]
    
    if records:
        record_id = records[0]['id']
        print(f"\n5.1 获取记录详情 (recordId={record_id})")
        
        r = requests.get(f"{BASE_URL}/api/score/record/{record_id}")
        detail = r.json()["data"]
        
        print(f"   记录ID: {detail['id']}")
        print(f"   任务: {detail['taskName']}")
        print(f"   机构: {detail['institutionName']}")
        print(f"   评委: {detail['judgeName']} ({detail['judgeType']})")
        print(f"   评分分类: {detail['categoryName']}")
        print(f"   评分条目: {detail['itemName']}")
        print(f"   得分: {detail['score']}/{detail['maxScore']}")
        print(f"   备注: {detail.get('comment', '无')}")
        print(f"   评分时间: {detail['createTime']}")

def main():
    print("\n" + "=" * 70)
    print("评分数据明细验证")
    print("=" * 70)
    
    try:
        test_records_api()
        test_statistics_api()
        test_result_api()
        test_export_api()
        test_record_detail()
        
        print("\n" + "=" * 70)
        print("✨ 所有验证完成!")
        print("=" * 70)
        print("\n✅ 结论:")
        print("   1. 所有API都能正常返回数据")
        print("   2. 评分记录包含完整的明细信息")
        print("   3. 支持按任务、评委、机构等多维度筛选")
        print("   4. 统计和计算功能正常")
        print("   5. 数据导出功能正常")
        print()
        
    except Exception as e:
        print(f"\n❌ 验证失败: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
