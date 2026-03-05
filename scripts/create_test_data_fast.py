#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""创建测试评分数据（多线程并发版，速度快10倍）"""

import requests
import json
import random
from concurrent.futures import ThreadPoolExecutor, as_completed

BASE_URL = "http://localhost:5031"
WORKERS = 10  # 并发线程数

def get_data():
    tasks = requests.get(f"{BASE_URL}/api/task/list").json()["data"]
    institutions = requests.get(f"{BASE_URL}/api/institution/list").json()["data"]
    judges = requests.get(f"{BASE_URL}/api/judge/list").json()["data"]
    templates = requests.get(f"{BASE_URL}/api/score-template/list").json()["data"]
    template = next((t for t in templates if t.get("isDefault") == 1), templates[0])
    categories = requests.get(f"{BASE_URL}/api/score-template/{template['id']}").json()["data"].get("categories", [])
    all_items = [
        {"id": item["id"], "maxScore": item["maxScore"]}
        for cat in categories for item in cat.get("items", [])
    ]
    return tasks[0], institutions, judges, all_items

def already_scored_institutions(task_id):
    """获取已有评分的机构ID集合"""
    r = requests.get(f"{BASE_URL}/api/score/submissions?taskId={task_id}")
    submissions = r.json().get("data", [])
    return {s["institutionId"] for s in submissions}

def submit_one(task_id, institution_id, judge_id, all_items, is_expert):
    """提交一次评分，返回 (success, message)"""
    low, high = (0.80, 1.00) if is_expert else (0.75, 0.95)
    item_scores = [
        {"itemId": item["id"], "score": round(item["maxScore"] * random.uniform(low, high), 2), "comment": ""}
        for item in all_items
    ]
    try:
        r = requests.post(f"{BASE_URL}/api/score/submit", json={
            "taskId": task_id,
            "institutionId": institution_id,
            "judgeId": judge_id,
            "scoreMode": "ITEM",
            "itemScores": item_scores
        }, timeout=15)
        return r.status_code == 200
    except Exception:
        return False

def score_institution(args):
    task_id, institution, expert_judges, public_judges, all_items = args
    inst_id = institution["id"]
    ok = 0
    fail = 0
    for j in expert_judges:
        if submit_one(task_id, inst_id, j["id"], all_items, True):
            ok += 1
        else:
            fail += 1
    for j in public_judges:
        if j.get("institutionId") == inst_id:
            continue  # 同机构回避
        if submit_one(task_id, inst_id, j["id"], all_items, False):
            ok += 1
        else:
            fail += 1
    return institution["name"], ok, fail

def main():
    print("=" * 60)
    print("快速并发创建测试评分数据")
    print("=" * 60)

    task, institutions, judges, all_items = get_data()
    task_id = task["id"]

    expert_judges = [j for j in judges if j.get("type") == "EXPERT"][:8]
    public_judges = [j for j in judges if j.get("type") == "PUBLIC"][:8]

    print(f"任务: {task['name']}")
    print(f"机构总数: {len(institutions)}  专家评委: {len(expert_judges)}  大众评委: {len(public_judges)}")
    print(f"评分条目: {len(all_items)}")

    # 跳过已评分机构
    scored = already_scored_institutions(task_id)
    pending = [inst for inst in institutions if inst["id"] not in scored]
    print(f"已有评分: {len(scored)} 家  待处理: {len(pending)} 家")
    print(f"并发线程数: {WORKERS}")
    print()

    if not pending:
        print("✅ 所有机构已有评分数据，无需重复创建！")
        return

    args_list = [(task_id, inst, expert_judges, public_judges, all_items) for inst in pending]

    total_ok = 0
    total_fail = 0
    done = 0

    with ThreadPoolExecutor(max_workers=WORKERS) as executor:
        futures = {executor.submit(score_institution, args): args[1]["name"] for args in args_list}
        for future in as_completed(futures):
            name, ok, fail = future.result()
            total_ok += ok
            total_fail += fail
            done += 1
            print(f"  [{done}/{len(pending)}] {name}: 成功{ok} 失败{fail}")

    print()
    print("=" * 60)
    print(f"完成！成功提交 {total_ok} 次，失败 {total_fail} 次")
    print("=" * 60)

    # 验证最终统计
    r = requests.get(f"{BASE_URL}/api/score/statistics?taskId={task_id}")
    d = r.json()["data"]
    print(f"\n最终统计:")
    print(f"  已完成机构数: {d.get('completedInstitutionCount', 0)} / {len(institutions)}")
    print(f"  总评分记录数: {d.get('totalScoreCount', 0)}")
    print(f"  专家评委人数: {d.get('expertJudgeCount', 0)}")
    print(f"  大众评委人数: {d.get('publicJudgeCount', 0)}")

if __name__ == "__main__":
    main()
