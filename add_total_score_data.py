#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
补充 TOTAL 模式评分数据（约占总量 15%）
使用 expert11-13 / public11-13 共 6 位新评委，
每人对 32 家机构直接打总分，合计约 192 条提交。
"""

import requests
import random

BASE_URL = "http://localhost:5031"

def get_tasks():
    r = requests.get(f"{BASE_URL}/api/task/list", timeout=15)
    return r.json()["data"]

def get_institutions():
    r = requests.get(f"{BASE_URL}/api/institution/list", timeout=15)
    return r.json()["data"]

def get_judges():
    r = requests.get(f"{BASE_URL}/api/judge/list", timeout=15)
    return r.json()["data"]

def submit_total_score(task_id, institution_id, judge_id, total_score):
    data = {
        "taskId": task_id,
        "institutionId": institution_id,
        "judgeId": judge_id,
        "scoreMode": "TOTAL",
        "totalScore": total_score,
        "itemScores": []
    }
    try:
        r = requests.post(
            f"{BASE_URL}/api/score/submit",
            json=data,
            headers={"Content-Type": "application/json"},
            timeout=15
        )
        return r.status_code == 200
    except Exception as e:
        print(f"  [ERR] {e}")
        return False

def main():
    print("=" * 60)
    print("补充 TOTAL 模式评分数据")
    print("=" * 60)

    tasks        = get_tasks()
    institutions = get_institutions()
    judges       = get_judges()

    task = tasks[0]
    print(f"任务: {task['name']} (ID: {task['id']})")
    print(f"机构总数: {len(institutions)}")
    print(f"评委总数: {len(judges)}")

    expert_judges = [j for j in judges if j.get("type") == "EXPERT"]
    public_judges = [j for j in judges if j.get("type") == "PUBLIC"]

    # 使用之前 ITEM 脚本未用过的评委（索引 10-12，即第 11-13 位）
    new_experts = expert_judges[10:13]   # 3 位专家评委
    new_publics = public_judges[10:13]   # 3 位大众评委

    print(f"\n本次使用专家评委: {[j['name'] for j in new_experts]}")
    print(f"本次使用大众评委: {[j['name'] for j in new_publics]}")

    # 每位评委随机选 32 家机构（打乱顺序，避免总是集中在前几家）
    INST_PER_JUDGE = 32

    success = 0
    skip    = 0
    fail    = 0

    for judge_list, label, score_range in [
        (new_experts, "专家", (78.0, 95.0)),
        (new_publics, "大众", (72.0, 90.0)),
    ]:
        for judge in judge_list:
            # 大众评委回避同机构
            avoid_id = (judge.get("institution") or {}).get("id")
            eligible = [i for i in institutions if i["id"] != avoid_id]

            random.seed(judge["id"])           # 固定种子保证可重复
            targets = random.sample(eligible, min(INST_PER_JUDGE, len(eligible)))

            print(f"\n  [{label}] {judge['name']} => {len(targets)} 家机构 (TOTAL 模式)")

            for inst in targets:
                total = round(random.uniform(*score_range), 1)
                ok = submit_total_score(task["id"], inst["id"], judge["id"], total)
                if ok:
                    success += 1
                    print(f"    [OK] {inst['name']}  总分={total}")
                else:
                    fail += 1
                    print(f"    [FAIL] {inst['name']}")

    print()
    print("=" * 60)
    print(f"完成: 成功 {success}  失败 {fail}  跳过(回避) {skip}")
    print(f"新增 TOTAL 提交占原有 ITEM 提交(1270)比例: {success/1270*100:.1f}%")
    print("=" * 60)

if __name__ == "__main__":
    main()
