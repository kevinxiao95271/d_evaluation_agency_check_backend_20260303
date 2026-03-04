#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""测试 /api/score/records 接口"""

import requests
import json

BASE_URL = "http://localhost:5031"

def test_records_api():
    print("=" * 60)
    print("测试 /api/score/records 接口")
    print("=" * 60)
    print()
    
    # 测试1: 获取所有评分记录
    print("1. 获取所有评分记录")
    response = requests.get(f"{BASE_URL}/api/score/records?taskId=1")
    data = response.json()
    
    if response.status_code == 200:
        records = data["data"]
        print(f"   ✅ 状态码: {response.status_code}")
        print(f"   ✅ 返回记录数: {len(records)}")
        
        if len(records) > 0:
            first_record = records[0]
            print(f"\n   第一条记录结构:")
            print(f"   - id: {first_record.get('id')}")
            print(f"   - taskName: {first_record.get('taskName')}")
            print(f"   - institutionName: {first_record.get('institutionName')}")
            print(f"   - judgeName: {first_record.get('judgeName')}")
            print(f"   - judgeType: {first_record.get('judgeType')}")
            print(f"   - scoreMode: {first_record.get('scoreMode')} {'✅' if first_record.get('scoreMode') else '❌ 缺失'}")
            print(f"   - totalScore: {first_record.get('totalScore')} {'✅' if first_record.get('totalScore') is not None else '❌ 缺失'}")
            print(f"   - submitTime: {first_record.get('submitTime')} {'✅' if first_record.get('submitTime') else '❌ 缺失'}")
            
            item_scores = first_record.get('itemScores', [])
            print(f"   - itemScores: {len(item_scores)} 条明细 {'✅' if len(item_scores) > 0 else '❌ 缺失'}")
            
            if len(item_scores) > 0:
                print(f"\n   第一条明细:")
                print(f"     * itemId: {item_scores[0].get('itemId')}")
                print(f"     * itemName: {item_scores[0].get('itemName')}")
                print(f"     * categoryName: {item_scores[0].get('categoryName')}")
                print(f"     * score: {item_scores[0].get('score')}")
                print(f"     * maxScore: {item_scores[0].get('maxScore')}")
    else:
        print(f"   ❌ 状态码: {response.status_code}")
        print(f"   ❌ 错误: {data}")
    
    print()
    
    # 测试2: 按机构筛选
    print("2. 按机构筛选 (institutionId=1)")
    response = requests.get(f"{BASE_URL}/api/score/records?institutionId=1")
    data = response.json()
    
    if response.status_code == 200:
        records = data["data"]
        print(f"   ✅ 返回记录数: {len(records)}")
        
        # 统计评委类型
        expert_count = len([r for r in records if r.get('judgeType') == 'EXPERT'])
        public_count = len([r for r in records if r.get('judgeType') == 'PUBLIC'])
        print(f"   - 专家评委: {expert_count} 条")
        print(f"   - 大众评委: {public_count} 条")
    else:
        print(f"   ❌ 状态码: {response.status_code}")
    
    print()
    
    # 测试3: 按评委筛选
    print("3. 按评委筛选 (judgeId=1)")
    response = requests.get(f"{BASE_URL}/api/score/records?judgeId=1")
    data = response.json()
    
    if response.status_code == 200:
        records = data["data"]
        print(f"   ✅ 返回记录数: {len(records)}")
        
        if len(records) > 0:
            print(f"   - 评委: {records[0].get('judgeName')}")
            print(f"   - 评委类型: {records[0].get('judgeType')}")
    else:
        print(f"   ❌ 状态码: {response.status_code}")
    
    print()
    print("=" * 60)
    print("测试完成")
    print("=" * 60)

if __name__ == "__main__":
    test_records_api()
