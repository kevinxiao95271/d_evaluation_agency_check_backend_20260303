#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""创建测试评分数据"""

import requests
import json
import random

BASE_URL = "http://localhost:5031"

def get_tasks():
    """获取任务列表"""
    response = requests.get(f"{BASE_URL}/api/task/list")
    return response.json()["data"]

def get_institutions():
    """获取机构列表"""
    response = requests.get(f"{BASE_URL}/api/institution/list")
    return response.json()["data"]

def get_judges():
    """获取评委列表"""
    response = requests.get(f"{BASE_URL}/api/judge/list")
    return response.json()["data"]

def get_score_template():
    """获取评分模板"""
    response = requests.get(f"{BASE_URL}/api/score-template/list")
    templates = response.json()["data"]
    # 找到默认模板
    for t in templates:
        if t.get("isDefault") == 1:
            return t
    return templates[0] if templates else None

def get_score_items(template_id):
    """获取评分条目"""
    response = requests.get(f"{BASE_URL}/api/score-template/{template_id}")
    template = response.json()["data"]
    return template.get("categories", [])

def submit_score(task_id, institution_id, judge_id, item_scores):
    """提交评分"""
    data = {
        "taskId": task_id,
        "institutionId": institution_id,
        "judgeId": judge_id,
        "scoreMode": "ITEM",
        "itemScores": item_scores
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/api/score/submit",
            json=data,
            headers={"Content-Type": "application/json"}
        )
        return response.status_code == 200
    except Exception as e:
        print(f"提交失败: {e}")
        return False

def main():
    print("=" * 60)
    print("开始创建测试评分数据")
    print("=" * 60)
    print()
    
    # 1. 获取基础数据
    print("1. 获取基础数据...")
    tasks = get_tasks()
    institutions = get_institutions()
    judges = get_judges()
    template = get_score_template()
    
    if not tasks:
        print("❌ 没有找到任务")
        return
    if not institutions:
        print("❌ 没有找到机构")
        return
    if not judges:
        print("❌ 没有找到评委")
        return
    if not template:
        print("❌ 没有找到评分模板")
        return
    
    print(f"   - 任务数量: {len(tasks)}")
    print(f"   - 机构数量: {len(institutions)}")
    print(f"   - 评委数量: {len(judges)}")
    print(f"   - 评分模板: {template['name']}")
    print()
    
    # 2. 获取评分条目
    print("2. 获取评分条目...")
    categories = get_score_items(template["id"])
    
    all_items = []
    for category in categories:
        for item in category.get("items", []):
            all_items.append({
                "id": item["id"],
                "name": item["name"],
                "maxScore": item["maxScore"],
                "categoryName": category["name"]
            })
    
    print(f"   - 评分条目数量: {len(all_items)}")
    for item in all_items:
        print(f"     • {item['categoryName']} - {item['name']} (满分: {item['maxScore']})")
    print()
    
    # 3. 选择第一个任务
    task = tasks[0]
    print(f"3. 使用任务: {task['name']} (ID: {task['id']})")
    print()
    
    # 4. 为前10个机构创建评分数据
    print("4. 创建评分数据...")
    target_institutions = institutions[:10]  # 只为前10个机构创建数据
    target_judges = judges[:5]  # 使用前5个评委
    
    success_count = 0
    total_count = 0
    
    for institution in target_institutions:
        print(f"\n   机构: {institution['name']} (ID: {institution['id']})")
        
        for judge in target_judges:
            # 检查是否是同机构回避
            if judge.get("institutionId") == institution["id"]:
                print(f"     ⊘ 评委 {judge['name']} - 同机构回避")
                continue
            
            # 为每个评分条目生成随机分数
            item_scores = []
            for item in all_items:
                # 生成80%-100%之间的随机分数
                score = round(item["maxScore"] * random.uniform(0.8, 1.0), 2)
                item_scores.append({
                    "itemId": item["id"],
                    "score": score,
                    "comment": f"评分说明-{random.randint(1, 100)}"
                })
            
            # 提交评分
            total_count += 1
            if submit_score(task["id"], institution["id"], judge["id"], item_scores):
                success_count += 1
                judge_type = "专家" if judge.get("type") == "EXPERT" else "大众"
                print(f"     ✅ 评委 {judge['name']} ({judge_type}) - 提交成功")
            else:
                print(f"     ❌ 评委 {judge['name']} - 提交失败")
    
    print()
    print("=" * 60)
    print(f"数据创建完成: 成功 {success_count}/{total_count}")
    print("=" * 60)
    print()
    
    # 5. 验证数据
    print("5. 验证评分数据...")
    response = requests.get(f"{BASE_URL}/api/score/records?taskId={task['id']}")
    records = response.json()["data"]
    print(f"   - 评分记录总数: {len(records)}")
    
    # 按机构统计
    institution_counts = {}
    for record in records:
        inst_name = record["institutionName"]
        institution_counts[inst_name] = institution_counts.get(inst_name, 0) + 1
    
    print(f"   - 已评分机构数: {len(institution_counts)}")
    for inst_name, count in list(institution_counts.items())[:5]:
        print(f"     • {inst_name}: {count} 条记录")
    
    print()
    print("✨ 测试数据创建完成!")
    print()
    print("现在可以测试以下API:")
    print(f"  - GET /api/score/records?taskId={task['id']}")
    print(f"  - GET /api/score/records?institutionId={target_institutions[0]['id']}")
    print(f"  - GET /api/score/records?judgeId={target_judges[0]['id']}")
    print(f"  - GET /api/score/statistics?taskId={task['id']}")
    print(f"  - GET /api/score/results/{task['id']}")

if __name__ == "__main__":
    main()
