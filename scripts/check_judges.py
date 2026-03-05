#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""检查评委数据"""

import requests

BASE_URL = "http://localhost:5031"

r = requests.get(f"{BASE_URL}/api/judge/list")
judges = r.json()["data"]

print(f"总评委数: {len(judges)}\n")

expert_judges = [j for j in judges if j.get("type") == "EXPERT"]
public_judges = [j for j in judges if j.get("type") == "PUBLIC"]

print(f"专家评委数: {len(expert_judges)}")
print(f"大众评委数: {len(public_judges)}\n")

print("前5个专家评委:")
for j in expert_judges[:5]:
    inst_info = f"机构ID: {j.get('institutionId')}" if j.get('institutionId') else "无机构"
    print(f"  - {j['name']} (ID: {j['id']}, {inst_info})")

print("\n前5个大众评委:")
for j in public_judges[:5]:
    inst_info = f"机构ID: {j.get('institutionId')}" if j.get('institutionId') else "无机构"
    print(f"  - {j['name']} (ID: {j['id']}, {inst_info})")
